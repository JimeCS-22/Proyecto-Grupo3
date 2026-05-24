package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.data.StudentData;
import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.queues.PriorityLinkedQueue;
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

    private final PriorityLinkedQueue<Student> enrollmentQueue = new PriorityLinkedQueue<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        //loadStudents();
        loadPriorityQueue();
        loadStudentsByPriority();
        loadCourses();
        loadEnrollments();
        cmbStudents.setCellFactory(lv -> new ListCell<Student>() {
            @Override
            protected void updateItem(Student student, boolean empty) {
                super.updateItem(student, empty);
                if (empty || student == null) {
                    setText(null);
                } else {
                    int credits = calculateTotalCredits(student);
                    setText(student.getName() + " -> Créditos: " + credits);
                }
            }
        });

        cmbStudents.setButtonCell(cmbStudents.getCellFactory().call(null));

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
            for (int i = 0; i < size; i++) { // <= para incluir al último
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
                recordData.addRecord(new AcademicRecord(student));
                ok = recordData.addCourseToStudent(student.getId(), course);
            }
            if (ok) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Curso asignado correctamente");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo asignar el curso");
            }
            loadEnrollments();
            cleanFields();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void cleanFields() {
        cmbStudents.getSelectionModel().clearSelection();
        cmbCourses.getSelectionModel().clearSelection();
    }

    private void loadEnrollments() {
        tblEnrollments.getItems().clear();
        try {
            int size = recordData.getAll().size();
            for (int i = 1; i < size; i++) { // <= para incluir al último
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
    //cálculo de prioridad por cant de créditos
    private int calculateTotalCredits(Student student) {

        int totalCredits = 0;

        try {

            int size = recordData.getAll().size();

            for (int i = 0; i < size; i++) {

                var record = recordData.getAll().get(i);

                if (record.getStudent().getId().equals(student.getId())) {

                    int coursesSize = record.getCourses().size();

                    for (int j = 1; j <= coursesSize; j++) {

                        totalCredits += record.getCourses().get(j).getCredits();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalCredits;
    }
    private void loadPriorityQueue() {

        try {

            enrollmentQueue.clear();

            int size = studentData.getAllStudents().size();

            for (int i = 0; i < size; i++) {

                Student student = studentData.getAllStudents().get(i);

                int totalCredits = calculateTotalCredits(student);

            /*
             PRIORIDAD:
             Más créditos = prioridad más alta
            */

                int priority;

                if (totalCredits >= 30) {
                    priority = 1; // máxima prioridad
                } else if (totalCredits >= 20) {
                    priority = 2;
                } else if (totalCredits >= 10) {
                    priority = 3;
                } else {
                    priority = 4;
                }

                enrollmentQueue.enQueue(student, priority);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadStudentsByPriority() {

        cmbStudents.getItems().clear();

        try {

            while (!enrollmentQueue.isEmpty()) {

                Student student = enrollmentQueue.deQueue();

                int credits = calculateTotalCredits(student);

                System.out.println(
                        student.getName() +
                                " -> Créditos: " + credits
                );

                cmbStudents.getItems().add(student);
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