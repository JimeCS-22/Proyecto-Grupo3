package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.data.StudentData;
import cr.ac.ucr.sga.model.entities.AcademicRecord;
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

    // =========================
    // COMBOBOX
    // =========================

    @FXML
    private ComboBox<Student> cmbStudents;

    @FXML
    private ComboBox<Course> cmbCourses;

    // =========================
    // TABLE
    // =========================

    @FXML
    private TableView<EnrollmentRow> tblEnrollments;

    @FXML
    private TableColumn<EnrollmentRow, String> colStudent;

    @FXML
    private TableColumn<EnrollmentRow, String> colCourse;

    @FXML
    private TableColumn<EnrollmentRow, Integer> colCredits;

    // =========================
    // DATA
    // =========================

    private final StudentData studentData =
            new StudentData();

    private final CourseData courseData =
            new CourseData();

    private final AcademicRecordData recordData =
            new AcademicRecordData();

    // =========================
    // INITIALIZE
    // =========================

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {

        initializeTable();

        loadStudents();

        loadCourses();

        loadEnrollments();

        System.out.println(
                "EnrollmentController iniciado"
        );
    }

    // =========================
    // TABLE CONFIG
    // =========================

    private void initializeTable() {

        colStudent.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getStudentName()
                )
        );

        colCourse.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getCourseName()
                )
        );

        colCredits.setCellValueFactory(
                data -> new SimpleIntegerProperty(
                        data.getValue().getCredits()
                ).asObject()
        );
    }

    // =========================
    // LOAD STUDENTS
    // =========================

    private void loadStudents() {

        try {

            int size = studentData.getAllStudents().size();

            for (int i = 1; i < size; i++) {

                Student student =
                        studentData.getAllStudents().get(i);

                cmbStudents.getItems().add(student);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // LOAD COURSES
    // =========================

    private void loadCourses() {

        try {

            int size = courseData.getAllCourses().size();

            for (int i = 1; i < size; i++) {

                Course course =
                        courseData.getAllCourses().get(i);

                cmbCourses.getItems().add(course);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // ASSIGN COURSE
    // =========================

    @FXML
    private void assignCourse() {

        try {

            Student student =
                    cmbStudents.getValue();

            Course course =
                    cmbCourses.getValue();

            if (student == null || course == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Campos vacíos",
                        "Seleccione estudiante y curso"
                );

                return;
            }

            AcademicRecord record =
                    recordData.findByStudentId(
                            student.getId()
                    );

            // SI NO EXISTE EXPEDIENTE

            if (record == null) {

                record = new AcademicRecord(student);

                recordData.addRecord(record);
            }

            // AGREGAR CURSO

            record.addCourse(course);

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Curso asignado correctamente"
            );

            loadEnrollments();

        } catch (Exception e) {

            e.printStackTrace();

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage()
            );
        }
    }

    // =========================
    // LOAD ENROLLMENTS
    // =========================

    private void loadEnrollments() {

        tblEnrollments.getItems().clear();

        try {

            int size = recordData.getAll().size();

            for (int i = 1; i < size; i++) {

                AcademicRecord record =
                        recordData.getAll().get(i);

                int coursesSize =
                        record.getCourses().size();

                for (int j = 0; j < coursesSize; j++) {

                    Course course =
                            record.getCourses().get(j);

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

    // =========================
    // ALERT
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

    // =========================
    // INNER CLASS
    // =========================

    public static class EnrollmentRow {

        private final String studentName;
        private final String courseName;
        private final int credits;

        public EnrollmentRow(
                String studentName,
                String courseName,
                int credits
        ) {

            this.studentName = studentName;
            this.courseName = courseName;
            this.credits = credits;
        }

        public String getStudentName() {
            return studentName;
        }

        public String getCourseName() {
            return courseName;
        }

        public int getCredits() {
            return credits;
        }
    }
}