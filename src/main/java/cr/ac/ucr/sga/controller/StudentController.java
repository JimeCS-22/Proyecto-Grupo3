package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.data.CareerData;
import cr.ac.ucr.sga.model.data.StudentData;
import cr.ac.ucr.sga.model.data.UserData;
import cr.ac.ucr.sga.model.entities.*;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentController implements Initializable {

    @FXML private TableColumn<Student, Integer> colAge;
    @FXML private TableColumn<Student, String> colCarnet;
    @FXML private TableColumn<Student, String> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colUsername;
    @FXML private TableColumn<Student, String> colCareer;
    @FXML private TableView<Student> tblStudents;
    @FXML private TextField txtAge;
    @FXML private TextField txtCarnet;
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private Label lblCount;
    @FXML private ComboBox<Career> cmbCareer;

    private final StudentData studentData = new StudentData();
    private final UserData userData = new UserData();
    private final CareerData careerData = new CareerData();
    private final ObservableList<Student> studentList = FXCollections.observableArrayList();
    private MainController mainController;
    private boolean isLoaded = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupStudentController();
        initializeTable();
        loadStudents();
        tableListener();

        if (!isLoaded) {
            cmbCareer.getItems().clear();
            cmbCareer.getItems().addAll(careerData.getAllCareers().toList());
            cmbCareer.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(Career career, boolean empty) {
                    super.updateItem(career, empty);
                    setText(empty || career == null ? "" : career.getName());
                }
            });
            cmbCareer.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Career career, boolean empty) {
                    super.updateItem(career, empty);
                    setText(empty || career == null ? "" : career.getName());
                }
            });
            isLoaded = true;
        }
    }

    private void setupStudentController() {
        btnAdd.setOnAction(e -> addStudent());
        btnUpdate.setOnAction(e -> updateStudent());
        btnDelete.setOnAction(e -> {
            try { deleteStudent(); } catch (ListException ex) { throw new RuntimeException(ex); }
        });
        btnClear.setOnAction(e -> clearFields());
    }

    public void setMainController(MainController mainController) { this.mainController = mainController; }

    private void initializeTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colCarnet.setCellValueFactory(new PropertyValueFactory<>("carnet"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colCareer.setCellValueFactory(cell -> {
            Career career = careerData.findCareerById(cell.getValue().getCareerId());
            return new SimpleStringProperty(career != null ? career.getName() : "");
        });
        tblStudents.setItems(studentList);
    }

    private void loadStudents() {
        studentList.clear();
        studentList.addAll(studentData.getAllStudents().toList());
        updateCount();
    }

    @FXML
    private void addStudent() {
        try {
            String id = txtId.getText();
            String name = txtName.getText();
            int age = Integer.parseInt(txtAge.getText());
            String carnet = txtCarnet.getText();
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            Career career = cmbCareer.getValue();

            if (career == null) {
                showAlert(Alert.AlertType.WARNING, "Carrera", "Seleccione una carrera.");
                return;
            }
            Student student = new Student.Builder().setId(id).setName(name).setAge(age).setCarnet(carnet).setcareerId(career.getId()).setUsername(username).setPassword(password).build();
            Student added = studentData.addStudent(student);

            if (mainController != null && mainController.getRecordController() != null) {
                mainController.getRecordController().reloadStudents();
            }

            if (added != null) {
                AcademicRecordData recordData = new AcademicRecordData();
                if (recordData.findByStudentId(added.getId()) == null) {
                    recordData.addRecord(new AcademicRecord(added));
                }
                userData.addUser(new User(username, password, Role.STUDENT));
                studentList.add(added);
                clearFields();
                updateCount();
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Estudiante agregado correctamente");
            } else {
                showAlert(Alert.AlertType.WARNING, "Duplicado", "Ya existe un estudiante con ese ID");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "La edad debe ser numérica");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void updateStudent() {
        Student selected = tblStudents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Sin selección", "Seleccione un estudiante");
            return;
        }
        Career career = cmbCareer.getValue();
        if (career == null) {
            showAlert(Alert.AlertType.WARNING, "Carrera", "Seleccione una carrera.");
            return;
        }
        try {
            Student updatedStudent = new Student.Builder().setId(selected.getId()).setName(txtName.getText()).setAge(Integer.parseInt(txtAge.getText())).setcareerId(career.getId()).setCarnet(txtCarnet.getText()).setUsername(txtUsername.getText()).setPassword(txtPassword.getText()).build();
            studentData.updateStudent(updatedStudent);
            loadStudents();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Actualizado", "Estudiante actualizado");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void deleteStudent() throws ListException {

        Student selected =
                tblStudents.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione un estudiante"
            );
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar eliminación");
        confirm.setHeaderText("Eliminar estudiante");
        confirm.setContentText(
                "¿Está seguro de que desea eliminar al estudiante "
                        + selected.getName() + "?"
        );

        if (confirm.showAndWait().orElse(ButtonType.CANCEL)
                != ButtonType.OK) {
            return;
        }

        if (studentData.deleteStudent(selected.getId())) {

            studentList.remove(selected);
            clearFields();
            updateCount();

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Eliminado",
                    "Estudiante eliminado correctamente"
            );
        }
    }

    @FXML
    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtAge.clear();
        txtCarnet.clear();
        txtUsername.clear();
        txtPassword.clear();
        cmbCareer.setValue(null);
        tblStudents.getSelectionModel().clearSelection();
    }

    private void tableListener() {
        tblStudents.getSelectionModel().selectedItemProperty().addListener((obs, old, student) -> {
            if (student != null) {
                txtId.setText(student.getId());
                txtName.setText(student.getName());
                txtAge.setText(String.valueOf(student.getAge()));
                txtCarnet.setText(student.getCarnet());
                txtUsername.setText(student.getUsername());
                txtPassword.setText(student.getPassword());
                Career career = careerData.findCareerById(student.getCareerId());
                if (career != null) cmbCareer.setValue(career);
            }
        });
    }

    private void updateCount() { lblCount.setText(studentList.size() + " registros"); }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}