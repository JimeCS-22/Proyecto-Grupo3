package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.CareerData;
import cr.ac.ucr.sga.model.data.ProfessorData;
import cr.ac.ucr.sga.model.data.UserData;
import cr.ac.ucr.sga.model.entities.Career;
import cr.ac.ucr.sga.model.entities.Professor;
import cr.ac.ucr.sga.model.entities.Role;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ProfessorController {

    @FXML private TableColumn<Professor, String> colId;
    @FXML private TableColumn<Professor, String> colName;
    @FXML private TableColumn<Professor, String> colCareer;
    @FXML private TableColumn<Professor, String> colUsername;

    @FXML private TableView<Professor> tblProfessors;

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;

    @FXML private ComboBox<Career> cmbCareer;

    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;

    // =========================
    // DATA
    // =========================
    private final ProfessorData professorData = new ProfessorData();
    private final CareerData careerData = new CareerData();
    private final UserData userData = new UserData();

    private final ObservableList<Professor> professorList =
            FXCollections.observableArrayList();

    private MainController mainController;
    // =========================
    // INIT (llamar desde FXML si quieres o Initializable)
    // =========================
    @FXML
    public void initialize() {

        initializeTable();
        loadProfessors();
        loadCareers();
        setupListeners();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    // =========================
    // TABLE
    // =========================
    private void initializeTable() {

        colId.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getId()));

        colName.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName()));

        colUsername.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getUsername()));

        colCareer.setCellValueFactory(data -> {
            Career c = careerData.findCareerById(
                    data.getValue().getCareerId()
            );

            return new SimpleStringProperty(
                    c != null ? c.getName() : ""
            );
        });

        tblProfessors.setItems(professorList);
    }

    // =========================
    // LOAD
    // =========================
    private void loadProfessors() {
        professorList.clear();
        professorList.addAll(
                professorData.getAllProfessors().toList()
        );
    }

    private void loadCareers() {
        cmbCareer.getItems().clear();
        cmbCareer.getItems().addAll(
                careerData.getAllCareers().toList()
        );
    }

    // =========================
    // ADD
    // =========================
    @FXML
    public void addProfessor(ActionEvent actionEvent) {
        try {
            Career career = cmbCareer.getValue();

            if (career == null) {
                showAlert(Alert.AlertType.WARNING,
                        "Carrera",
                        "Seleccione una carrera");
                return;
            }

            Professor professor = new Professor.Builder()
                    .setId(txtId.getText())
                    .setName(txtName.getText())
                    .setCareerId(career.getId())
                    .setUsername(txtUsername.getText())
                    .setPassword(txtPassword.getText())
                    .build();

            Professor added = professorData.addProfessor(professor);

            if (added == null) {
                showAlert(Alert.AlertType.WARNING,
                        "Duplicado",
                        "Ya existe un profesor con ese ID");
                return;
            }

            User user = new User(
                    professor.getUsername(),
                    professor.getPassword(),
                    Role.PROFESSOR
            );
            userData.addUser(user);

            professorList.add(added);
            clearFields();

            if (mainController != null) {
                mainController.refreshCourseProfessorList();
            }

            showAlert(Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Profesor agregado correctamente y habilitado para login");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage());
        }
    }

    // =========================
    // UPDATE
    // =========================
    @FXML
    public void updateProfessor(ActionEvent actionEvent) {

        Professor selected =
                tblProfessors.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione un profesor");
            return;
        }

        try {
            Career career = cmbCareer.getValue();

            if (career == null) {
                showAlert(Alert.AlertType.WARNING,
                        "Carrera",
                        "Seleccione una carrera");
                return;
            }

            Professor updated = new Professor.Builder()
                    .setId(selected.getId())
                    .setName(txtName.getText())
                    .setCareerId(career.getId())
                    .setUsername(txtUsername.getText())
                    .setPassword(txtPassword.getText())
                    .build();

            professorData.updateProfessor(updated);

            loadProfessors();
            clearFields();

            if (mainController != null) {
                mainController.refreshCourseProfessorList();
            }

            showAlert(Alert.AlertType.INFORMATION,
                    "Actualizado",
                    "Profesor actualizado");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage());
        }
    }

    // =========================
    // DELETE
    // =========================
    @FXML
    public void deleteProfessor(ActionEvent actionEvent) {

        Professor selected =
                tblProfessors.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione un profesor");
            return;
        }

        boolean removed =
                false;
        try {
            removed = professorData.deleteProfessor(selected.getId());
        } catch (ListException e) {
            throw new RuntimeException(e);
        }

        if (mainController != null) {
            mainController.refreshCourseProfessorList();
        }

        if (removed) {
            professorList.remove(selected);
            clearFields();

            showAlert(Alert.AlertType.INFORMATION,
                    "Eliminado",
                    "Profesor eliminado");
        }
    }

    // =========================
    // CLEAR
    // =========================
    @FXML
    public void clearFields(ActionEvent actionEvent) {
        clearFields();
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtUsername.clear();
        txtPassword.clear();
        cmbCareer.setValue(null);

        tblProfessors.getSelectionModel().clearSelection();
    }

    // =========================
    // LISTENER TABLE
    // =========================
    private void setupListeners() {

        tblProfessors.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, professor) -> {

                    if (professor != null) {

                        txtId.setText(professor.getId());
                        txtName.setText(professor.getName());
                        txtUsername.setText(professor.getUsername());
                        txtPassword.setText(professor.getPassword());

                        Career c = careerData.findCareerById(
                                professor.getCareerId()
                        );

                        cmbCareer.setValue(c);
                    }
                });
    }


    public void refreshProfessors() {
        loadProfessors();
    }

    // =========================
    // ALERT
    // =========================
    private void showAlert(Alert.AlertType type,
                           String title,
                           String msg) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}