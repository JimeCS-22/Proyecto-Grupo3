package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.data.StudentData;
import cr.ac.ucr.sga.model.data.UserData;
import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Role;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentController implements Initializable {

    @FXML
    private TableColumn<Student, Integer> colAge;

    @FXML
    private TableColumn<Student, String> colCarnet;

    @FXML
    private TableColumn<Student, String> colId;

    @FXML
    private TableColumn<Student, String> colName;

    @FXML
    private TableColumn<Student, String> colUsername;

    @FXML
    private TableView<Student> tblStudents;

    @FXML
    private TextField txtAge;

    @FXML
    private TextField txtCarnet;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;

    @FXML
    private Label lblCount;

    private final StudentData studentData =
            new StudentData();

    private final UserData userData =
            new UserData();

    private final ObservableList<Student> studentList =
            FXCollections.observableArrayList();

    private MainController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupStudentController();
        initializeTable();

        loadStudents();

        tableListener();

        System.out.println("StudentController iniciado");
    }

    private void setupStudentController() {
        btnAdd.setOnAction(e->addStudent());
        btnUpdate.setOnAction(e->updateStudent());
        btnDelete.setOnAction(e->deleteStudent());
        btnClear.setOnAction(e->clearFields());
    }

    public void setMainController(
            MainController mainController
    ) {
        this.mainController = mainController;
    }

    // =========================
    // TABLE
    // =========================

    private void initializeTable() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        colName.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        colAge.setCellValueFactory(
                new PropertyValueFactory<>("age"));

        colCarnet.setCellValueFactory(
                new PropertyValueFactory<>("carnet"));

        colUsername.setCellValueFactory(
                new PropertyValueFactory<>("username"));

        tblStudents.setItems(studentList);
    }

    // =========================
    // LOAD
    // =========================

    private void loadStudents() {

        studentList.clear();

        studentList.addAll(
                studentData.getAllStudents()
        );

        updateCount();
    }

    // =========================
    // ADD
    // =========================

    @FXML
    private void addStudent() {

        try {

            String id =
                    txtId.getText();

            String name =
                    txtName.getText();

            int age =
                    Integer.parseInt(
                            txtAge.getText()
                    );

            String carnet =
                    txtCarnet.getText();

            String username =
                    txtUsername.getText();

            String password =
                    txtPassword.getText();

            Student student =
                    new Student.Builder()
                            .setId(id)
                            .setName(name)
                            .setAge(age)
                            .setCarnet(carnet)
                            .setUsername(username)
                            .setPassword(password)
                            .build();

            Student added =
                    studentData.addStudent(student);

            if (
                    mainController != null
                            &&
                            mainController.getEnrollmentController()
                                    != null
            ) {

                mainController
                        .getEnrollmentController()
                        .reloadStudentsAndCourses();
            }

            if (
                    mainController != null
                            &&
                            mainController.getRecordController()
                                    != null
            ) {

                mainController
                        .getRecordController()
                        .reloadStudents();
            }

            if (added != null) {
                AcademicRecordData recordData = new AcademicRecordData();
                if (recordData.findByStudentId(added.getId()) == null) {
                    recordData.addRecord(new AcademicRecord(added));
                }

                User user =
                        new User(
                                username,
                                password,
                                Role.STUDENT
                        );

                userData.addUser(user);

                studentList.add(added);

                clearFields();

                updateCount();

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Éxito",
                        "Estudiante agregado correctamente"
                );

            } else {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Duplicado",
                        "Ya existe un estudiante con ese ID"
                );
            }

        } catch (NumberFormatException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "La edad debe ser numérica"
            );

        } catch (Exception e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage()
            );
        }
    }

    // =========================
    // UPDATE
    // =========================

    @FXML
    private void updateStudent() {

        Student selected =
                tblStudents.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione un estudiante"
            );

            return;
        }

        try {

            Student updatedStudent =
                    new Student.Builder()
                            .setId(selected.getId())
                            .setName(txtName.getText())
                            .setAge(
                                    Integer.parseInt(
                                            txtAge.getText()
                                    )
                            )
                            .setCarnet(txtCarnet.getText())
                            .setUsername(txtUsername.getText())
                            .setPassword(txtPassword.getText())
                            .build();

            studentData.updateStudent(updatedStudent);

            loadStudents();

            clearFields();

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Actualizado",
                    "Estudiante actualizado"
            );

        } catch (Exception e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage()
            );
        }
    }

    // =========================
    // DELETE
    // =========================

    @FXML
    private void deleteStudent() {

        Student selected =
                tblStudents.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione un estudiante"
            );

            return;
        }

        boolean removed =
                studentData.deleteStudent(
                        selected.getId()
                );

        if (removed) {

            studentList.remove(selected);

            clearFields();

            updateCount();

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Eliminado",
                    "Estudiante eliminado"
            );
        }
    }

    // =========================
    // CLEAR
    // =========================

    @FXML
    private void clearFields() {

        txtId.clear();
        txtName.clear();
        txtAge.clear();
        txtCarnet.clear();
        txtUsername.clear();
        txtPassword.clear();

        tblStudents.getSelectionModel()
                .clearSelection();
    }

    // =========================
    // LISTENER
    // =========================

    private void tableListener() {

        tblStudents.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, student) -> {

                            if (student != null) {

                                txtId.setText(
                                        student.getId());

                                txtName.setText(
                                        student.getName());

                                txtAge.setText(
                                        String.valueOf(
                                                student.getAge()
                                        )
                                );

                                txtCarnet.setText(
                                        student.getCarnet()
                                );

                                txtUsername.setText(
                                        student.getUsername()
                                );

                                txtPassword.setText(
                                        student.getPassword()
                                );
                            }
                        });
    }

    // =========================
    // COUNT
    // =========================

    private void updateCount() {

        lblCount.setText(
                studentList.size()
                        + " registros"
        );
    }

    // =========================
    // ALERTS
    // =========================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {

        Alert alert =
                new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}