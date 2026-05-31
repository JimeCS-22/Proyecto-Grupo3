package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.data.EnrollmentRequestData;
import cr.ac.ucr.sga.model.data.StudentData;
import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.EnrollmentRequest;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.queues.PriorityLinkedQueue;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controlador para Pre-Matrícula (estudiante solicita cursos) y utilidades administrativas mínimas.
 */
public class EnrollmentStudentController implements Initializable {

    @FXML private ComboBox<Student> cmbStudents; // opcional para admin
    @FXML private ComboBox<Course> cmbCourses;
    @FXML private TableView<EnrollmentRow> tblEnrollments;
    @FXML private TableColumn<EnrollmentRow, String> colCode;
    @FXML private TableColumn<EnrollmentRow, String> colCourse;
    @FXML private TableColumn<EnrollmentRow, Integer> colCredits;
    @FXML private Label lblPriority;
    @FXML private Button btnSolicitudPreMatricula;
    @FXML private Button btnAddCourse;

    // =========================
    // DATA / STATE
    // =========================
    private final CourseData courseData = new CourseData();
    private final StudentData studentData = new StudentData();
    private final EnrollmentRequestData enrollmentRequestData = new EnrollmentRequestData();
    private final AcademicRecordData recordData = new AcademicRecordData();
    private final PriorityLinkedQueue<Student> enrollmentQueue = new PriorityLinkedQueue<>();
    private final ObservableList<EnrollmentRow> selectedCourses = FXCollections.observableArrayList();
    private final ObservableList<EnrollmentRow> enrollmentRows = FXCollections.observableArrayList();

    private Student currentStudent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupPreMatricula();
        initializeTable();
        loadCourses();
        tblEnrollments.setItems(selectedCourses);
        initializeDeleteEvent();
        loadPriorityQueue();
        loadStudentsByPriority();
        loadEnrollments();
    }

    // =========================
    // SETUP / EVENTOS
    // =========================
    private void setupPreMatricula() {
        if (btnSolicitudPreMatricula != null) btnSolicitudPreMatricula.setOnAction(e -> sendPreMatriculaRequest());
        if (btnAddCourse != null) btnAddCourse.setOnAction(e -> assignCourseStudent());
    }

    // =========================
    // CONFIGURACIÓN EXTERNA
    // =========================
    public void setStudent(Student student) {
        this.currentStudent = student;
        loadStudentPriority();
    }

    // =========================
    // TABLA
    // =========================
    private void initializeTable() {
        if (colCode != null) {
            colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourseCode()));
        }
        if (colCourse != null) {
            colCourse.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourseName()));
        }
        if (colCredits != null) {
            colCredits.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCredits()).asObject());
        }
    }

    // =========================
    // CARGA DE CURSOS
    // =========================
    private void loadCourses() {
        try {
            if (cmbCourses == null) return;
            cmbCourses.getItems().clear();
            int size = courseData.getAllCourses().size();
            for (int i = 0; i < size; i++) {
                cmbCourses.getItems().add(courseData.getAllCourses().get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // PRIORIDAD DEL ESTUDIANTE
    // =========================
    private void loadStudentPriority() {
        if (lblPriority == null || currentStudent == null) return;
        String result = "";
        int totalCredits = calculateTotalCredits(currentStudent);
        int priority;

        if (totalCredits >= 30) {
            priority = 1;
            result = "Alta";
        } else if (totalCredits >= 20) {
            priority = 2;
            result = "Media";
        } else if (totalCredits >= 10) {
            priority = 3;
            result = "Media-baja";
        } else {
            priority = 4;
            result = "Baja";
        }

        lblPriority.setText("Tu prioridad de pre-matrícula es: " + priority + " (" + result + ")");
    }

    // =========================
    // CÁLCULO CRÉDITOS (USADO POR PRIORIDAD)
    // =========================
    private int calculateTotalCredits(Student student) {
        if (student == null) return 0;
        try {
            AcademicRecord record = recordData.findByStudentId(student.getId());
            if (record == null || record.getCourses() == null) return 0;
            int total = 0;
            int size = record.getCourses().size();
            for (int i = 1; i <= size; i++) {
                Course c = record.getCourses().get(i);
                if (c != null) total += c.getCredits();
            }
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int calculatePriority() {
        int totalCredits = calculateTotalCredits(currentStudent);
        if (totalCredits >= 30) return 1;
        else if (totalCredits >= 20) return 2;
        else if (totalCredits >= 10) return 3;
        return 4;
    }

    // =========================
    // ASIGNAR CURSO AL ESTUDIANTE (PRE-MATRÍCULA)
    // =========================
    @FXML
    private void assignCourseStudent() {
        try {
            if (cmbCourses == null) return;
            Course course = cmbCourses.getValue();
            if (course == null) {
                showAlert(Alert.AlertType.WARNING, "Curso vacío", "Seleccione un curso");
                return;
            }
            for (EnrollmentRow row : selectedCourses) {
                if (row.getCourseName().equals(course.getName())) {
                    showAlert(Alert.AlertType.WARNING, "Duplicado", "Ese curso ya fue agregado");
                    return;
                }
            }
            selectedCourses.add(new EnrollmentRow(course, course.getName(), course.getCredits()));
            cmbCourses.getSelectionModel().clearSelection();
            tblEnrollments.setItems(selectedCourses);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // DELETE EVENT
    // =========================
    private void initializeDeleteEvent() {
        if (tblEnrollments == null) return;
        tblEnrollments.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DELETE -> {
                    EnrollmentRow sel = tblEnrollments.getSelectionModel().getSelectedItem();
                    if (sel != null) {
                        selectedCourses.remove(sel);
                        enrollmentRows.remove(sel);
                    }
                }
            }
        });
    }

    // =========================
    // ENVIAR SOLICITUD PRE-MATRÍCULA
    // =========================
    @FXML
    private void sendPreMatriculaRequest() {
        if (currentStudent == null) {
            showAlert(Alert.AlertType.WARNING, "Sin estudiante", "No hay estudiante en sesión");
            return;
        }

        for (EnrollmentRequest req : enrollmentRequestData.getRequests()) {
            if (Objects.equals(req.getStudentId(), currentStudent.getId())
                    && req.getStatus() != null
                    && req.getStatus().equalsIgnoreCase("PENDING")) {
                showAlert(Alert.AlertType.WARNING, "Solicitud existente",
                        "Ya tienes una solicitud en espera. No puedes enviar otra.");
                return;
            }
        }

        try {
            if (selectedCourses.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Sin cursos", "Debe seleccionar cursos");
                return;
            }

            EnrollmentRequest request = new EnrollmentRequest(
                    currentStudent,
                    calculatePriority(),
                    "PENDING",
                    new LinkedList<>()
            );

            for (EnrollmentRow row : selectedCourses) {
                request.getCourses().add(row.getCourse());
            }

            enrollmentRequestData.addRequest(request);

            showAlert(Alert.AlertType.INFORMATION, "Solicitud enviada",
                    "La solicitud de pre-matrícula fue enviada correctamente");

            selectedCourses.clear();
            tblEnrollments.setItems(selectedCourses);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    // =========================
    // ADMIN: cargar expedientes en tabla (opcional)
    // =========================
    private void loadEnrollments() {
        enrollmentRows.clear();
        try {
            int size = recordData.getAll().size();
            for (int i = 1; i <= size; i++) {
                var record = recordData.getAll().get(i);
                int coursesSize = record.getCourses().size();
                for (int j = 1; j <= coursesSize; j++) {
                    var course = record.getCourses().get(j);
                    enrollmentRows.add(new EnrollmentRow(
                            record.getStudent().getName(),
                            course.getId(),
                            course.getName(),
                            course.getCredits()
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // PRIORITY QUEUE (ADMIN)
    // =========================
    private void loadPriorityQueue() {
        try {
            enrollmentQueue.clear();
            int size = studentData.getAllStudents().size();
            for (int i = 0; i < size; i++) {
                Student student = studentData.getAllStudents().get(i);
                int totalCredits = calculateTotalCredits(student);
                System.out.println("Alumno " + student.getName() + " créditos = " + totalCredits); // debug
                int priority;
                if (totalCredits >= 30) priority = 1;
                else if (totalCredits >= 20) priority = 2;
                else if (totalCredits >= 10) priority = 3;
                else priority = 4;
                enrollmentQueue.enQueue(student, priority);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStudentsByPriority() {
        if (cmbStudents == null) return;
        cmbStudents.getItems().clear();
        try {
            while (!enrollmentQueue.isEmpty()) {
                Student student = enrollmentQueue.deQueue();
                cmbStudents.getItems().add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // ALERT UTIL
    // =========================
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void assignCourse(ActionEvent actionEvent) {
    }

    // =========================
    // ROW DTO
    // =========================
    public static class EnrollmentRow {
        private final Course course;
        private final String studentName;
        private final String courseCode;
        private final String courseName;
        private final int credits;

        public EnrollmentRow(Course course, String courseName, int credits) {
            this.course = course;
            this.studentName = null;
            this.courseCode = course != null ? course.getId() : null;
            this.courseName = courseName;
            this.credits = credits;
        }

        public EnrollmentRow(String studentName, String courseCode, String courseName, int credits) {
            this.course = null;
            this.studentName = studentName;
            this.courseCode = courseCode;
            this.courseName = courseName;
            this.credits = credits;
        }

        public Course getCourse() { return course; }
        public String getStudentName() { return studentName; }
        public String getCourseCode() { return courseCode; }
        public String getCourseName() { return courseName; }
        public int getCredits() { return credits; }
    }

    // =========================
    // RELOAD HELPER
    // =========================
    public void reloadStudentsAndCourses() {
        if (cmbStudents != null) cmbStudents.getItems().clear();
        if (cmbCourses != null) cmbCourses.getItems().clear();
        loadPriorityQueue();
        loadStudentsByPriority();
        loadCourses();
        loadEnrollments();
    }
}
