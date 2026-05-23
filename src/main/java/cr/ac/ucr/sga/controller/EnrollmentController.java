package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.data.StudentData;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.Student;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class EnrollmentController implements Initializable {

    @FXML
    private ComboBox<Student> cmbStudents;
    @FXML
    private ComboBox<Course> cmbCourses;
    @FXML
    private TableView<EnrollmentRow> tblEnrollments;
    @FXML
    private TableColumn<EnrollmentRow, String> colStudent;
    @FXML
    private TableColumn<EnrollmentRow, String> colCourse;
    @FXML
    private TableColumn<EnrollmentRow, Integer> colCredits;

    private final StudentData studentData = new StudentData();
    private final CourseData courseData = new CourseData();
    private final AcademicRecordData recordData = new AcademicRecordData();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        loadStudents();
        loadCourses();
        loadEnrollments();
        System.out.println("EnrollmentController iniciado");
    }

    private void initializeTable() {
        colStudent.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudentName()));
        colCourse.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourseName()));
        colCredits.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCredits()).asObject());
    }

    private void loadStudents() {
        try {
            int size = studentData.getAllStudents().size();
            for (int i = 1; i <= size; i++) { // <= para incluir al último
                Student student = studentData.getAllStudents().get(i);
                cmbStudents.getItems().add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCourses() {
        try {
            int size = courseData.getAllCourses().size();
            for (int i = 1; i <= size; i++) { // <= para incluir al último
                Course course = courseData.getAllCourses().get(i);
                cmbCourses.getItems().add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void assignCourse() {
        try {
            Student student = cmbStudents.getValue();
            Course course = cmbCourses.getValue();
            if (student == null || course == null) {
                showAlert(Alert.AlertType.WARNING, "Campos vacíos", "Seleccione estudiante y curso");
                return;
            }
            // Usa el método centralizado para agregar curso y guardar
            boolean ok = recordData.addCourseToStudent(student.getId(), course);
            if (!ok) {
                // Si no existe, crea expediente y vuelve a intentar
                recordData.addRecord(new cr.ac.ucr.sga.model.entities.AcademicRecord(student));
                ok = recordData.addCourseToStudent(student.getId(), course);
            }
            if (ok) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Curso asignado correctamente");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo asignar el curso");
            }
            loadEnrollments();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void loadEnrollments() {
        tblEnrollments.getItems().clear();
        try {
            int size = recordData.getAll().size();
            for (int i = 1; i <= size; i++) { // <= para incluir al último
                var record = recordData.getAll().get(i);
                int coursesSize = record.getCourses().size();
                for (int j = 1; j <= coursesSize; j++) { // <= para todos los cursos
                    var course = record.getCourses().get(j);
                    tblEnrollments.getItems().add(
                            new EnrollmentRow(
                                    record.getStudent().getName(),
                                    course.getName(),
                                    course.getCredits()
                            )
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class EnrollmentRow {
        private final String studentName;
        private final String courseName;
        private final int credits;

        public EnrollmentRow(String studentName, String courseName, int credits) {
            this.studentName = studentName;
            this.courseName = courseName;
            this.credits = credits;
        }
        public String getStudentName() { return studentName; }
        public String getCourseName() { return courseName; }
        public int getCredits() { return credits; }
    }
}