package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.entities.Course;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CourseController implements Initializable {

    @FXML
    private TableView<Course> tblCourses;

    @FXML
    private TableColumn<Course, String> colId;

    @FXML
    private TableColumn<Course, String> colName;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;

    private final CourseData courseData = new CourseData();
    private final ObservableList<Course> courseList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        loadCourses();

        tblCourses.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillForm(newSelection);
            }
        });
    }

    private void loadCourses() {
        courseList.setAll(courseData.getAllCourses());
        tblCourses.setItems(courseList);
    }

    private void fillForm(Course course) {
        txtId.setText(course.getId());
        txtName.setText(course.getName());
    }

    private Course buildCourseFromForm() {
        String id = txtId.getText() != null ? txtId.getText().trim() : "";
        String name = txtName.getText() != null ? txtName.getText().trim() : "";

        if (id.isEmpty() || name.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Datos incompletos", "Debes llenar el ID y el nombre del curso.");
            return null;
        }

        Course course = new Course();
        course.setId(id);
        course.setName(name);

        return course;
    }

    @FXML
    private void onSave() {
        Course course = buildCourseFromForm();
        if (course == null) return;

        Course added = courseData.addCourse(course);

        if (added != null) {
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Curso agregado correctamente.");
            loadCourses();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Ya existe un curso con ese ID.");
        }
    }

    @FXML
    private void onUpdate() {
        Course course = buildCourseFromForm();
        if (course == null) return;

        boolean updated = courseData.updateCourse(course);

        if (updated) {
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Curso actualizado correctamente.");
            loadCourses();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No se encontró un curso con ese ID.");
        }
    }

    @FXML
    private void onDelete() {
        String id = txtId.getText() != null ? txtId.getText().trim() : "";

        if (id.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Dato faltante", "Debes escribir el ID del curso.");
            return;
        }

        boolean removed = courseData.removeCourse(id);

        if (removed) {
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Curso eliminado correctamente.");
            loadCourses();
            clearForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No se encontró un curso con ese ID.");
        }
    }

    @FXML
    private void onClear() {
        clearForm();
    }

    private void clearForm() {
        txtId.clear();
        txtName.clear();
        tblCourses.getSelectionModel().clearSelection();
        txtId.requestFocus();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}