package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.entities.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class CourseController implements Initializable {

    @FXML
    private TableView<Course> tblCourses;

    @FXML
    private TableColumn<Course, String> colId;

    @FXML
    private TableColumn<Course, String> colName;

    @FXML
    private TableColumn<Course, Integer> colCredits;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtCredits;

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

    // DATA
    private final CourseData courseData = new CourseData();

    // TABLE DATA
    private final ObservableList<Course> courseList =
            FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeTable();

        loadCourses();

        tableListener();
    }

    // =========================
    // TABLE
    // =========================

    private void initializeTable() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        colName.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        colCredits.setCellValueFactory(
                new PropertyValueFactory<>("credits"));

        tblCourses.setItems(courseList);
    }

    // =========================
    // LOAD
    // =========================

    private void loadCourses() {

        courseList.clear();

        courseList.addAll(courseData.getAllCourses());

        updateCount();
    }

    // =========================
    // ADD
    // =========================

    @FXML
    private void addCourse() {

        try {

            String id = txtId.getText();

            String name = txtName.getText();

            int credits = Integer.parseInt(
                    txtCredits.getText());

            Course course = new Course.Builder()
                    .setId(id)
                    .setName(name)
                    .setCredits(credits)
                    .build();

            Course added = courseData.addCourse(course);

            if (added != null) {

                courseList.add(added);

                clearFields();

                updateCount();

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Éxito",
                        "Curso agregado correctamente"
                );

            } else {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Duplicado",
                        "Ya existe un curso con ese código"
                );
            }

        } catch (NumberFormatException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Los créditos deben ser numéricos"
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
    private void updateCourse() {

        Course selected = tblCourses
                .getSelectionModel()
                .getSelectedItem();

        if (selected == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione un curso"
            );

            return;
        }

        try {

            Course updatedCourse = new Course.Builder()
                    .setId(selected.getId())
                    .setName(txtName.getText())
                    .setCredits(
                            Integer.parseInt(txtCredits.getText())
                    )
                    .setGrade(selected.getGrade())
                    .setStatus(selected.getStatus())
                    .build();

            courseData.updateCourse(updatedCourse);

            loadCourses();

            clearFields();

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Actualizado",
                    "Curso actualizado correctamente"
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
    private void deleteCourse() {

        Course selected = tblCourses
                .getSelectionModel()
                .getSelectedItem();

        if (selected == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione un curso"
            );

            return;
        }

        boolean removed = courseData.removeCourse(
                selected.getId());

        if (removed) {

            courseList.remove(selected);

            clearFields();

            updateCount();

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Eliminado",
                    "Curso eliminado correctamente"
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

        txtCredits.clear();

        tblCourses.getSelectionModel()
                .clearSelection();
    }

    // =========================
    // TABLE LISTENER
    // =========================

    private void tableListener() {

        tblCourses.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, course) -> {

                    if (course != null) {

                        txtId.setText(course.getId());

                        txtName.setText(course.getName());

                        txtCredits.setText(
                                String.valueOf(
                                        course.getCredits()
                                )
                        );
                    }
                });
    }

    // =========================
    // COUNT
    // =========================

    private void updateCount() {

        lblCount.setText(
                courseList.size() + " registros"
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

        Alert alert = new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}