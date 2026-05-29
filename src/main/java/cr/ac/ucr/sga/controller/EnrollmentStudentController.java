package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.data.EnrollmentRequestData;
import cr.ac.ucr.sga.model.data.StudentData;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.EnrollmentRequest;
import cr.ac.ucr.sga.model.entities.Student;

import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class EnrollmentStudentController
        implements Initializable {

    // =========================
    // FXML
    // =========================

    @FXML
    private ComboBox<Course> cmbCourses;

    @FXML
    private TableView<EnrollmentRow> tblEnrollments;

    @FXML
    private TableColumn<EnrollmentRow, String> colCourse;

    @FXML
    private TableColumn<EnrollmentRow, Integer> colCredits;

    @FXML
    private Label lblPriority;

    @FXML
    private Button btnSolicitudMatricula;

    // =========================
    // DATA
    // =========================

    private final CourseData courseData = new CourseData();

    private final StudentData studentData = new StudentData();

    private final EnrollmentRequestData enrollmentRequestData = new EnrollmentRequestData();

    // CURRENT STUDENT
    private Student currentStudent;

    // SELECTED COURSES
    private final ObservableList<EnrollmentRow>
            selectedCourses =
            FXCollections.observableArrayList();
    @FXML
    private Button btnAddCourse;

    // =========================
    // INITIALIZE
    // =========================

    @Override
    public void initialize(
            URL url,
            ResourceBundle resourceBundle
    ) {
        setupEnrollmentStudent();
        initializeTable();

        loadCourses();

        tblEnrollments.setItems(selectedCourses);

        initializeDeleteEvent();
    }

    private void setupEnrollmentStudent() {
        btnSolicitudMatricula.setOnAction(e->sendEnrollmentRequest());
    }

    // =========================
    // SET STUDENT
    // =========================

    public void setStudent(Student student) {

        this.currentStudent = student;

        loadStudentPriority();
    }

    // =========================
    // TABLE
    // =========================

    private void initializeTable() {

        colCourse.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue()
                                .getCourseName()
                )
        );

        colCredits.setCellValueFactory(data ->
                new SimpleIntegerProperty(
                        data.getValue()
                                .getCredits()
                ).asObject()
        );
    }

    // =========================
    // LOAD COURSES
    // =========================

    private void loadCourses() {

        try {

            cmbCourses.getItems().clear();

            int size =
                    courseData.getAllCourses().size();

            for (int i = 0; i < size; i++) {

                Course course =
                        courseData
                                .getAllCourses()
                                .get(i);

                cmbCourses.getItems().add(course);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // PRIORITY
    // =========================

    private void loadStudentPriority() {
        String result = "";
        int totalCredits =
                calculateTotalCredits(currentStudent);

        int priority;

        if (totalCredits >= 30) { //30 o más creditos alta prioridad

            priority = 1;
            result = "Alta";
        } else if (totalCredits >= 20) { //20 o más créditos prioridad media

            priority = 2;
            result = "Media";
        } else if (totalCredits >= 10) {//20 o más créditos prioridad media-baja

            priority = 3;
            result = "Media-baja";
        } else {

            priority = 4; ////20 o más créditos prioridad baja
            result = "Baja";
        }

        lblPriority.setText(
                "Tu prioridad de matrícula es: "
                        + priority +" ( "+result+" ) "
        );
    }

    // =========================
    // TOTAL CREDITS
    // =========================

    private int calculateTotalCredits(
            Student student
    ) {

        int totalCredits = 0;

        try {

            int size =
                    student.getAcademicRecord()
                            .getCourses()
                            .size();

            for (int i = 1; i <= size; i++) {

                Course course =
                        student.getAcademicRecord()
                                .getCourses()
                                .get(i);

                totalCredits += course.getCredits();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return totalCredits;
    }

    // =========================
    // CALCULATE PRIORITY
    // =========================

    private int calculatePriority() {

        int totalCredits =
                calculateTotalCredits(currentStudent);

        if (totalCredits >= 30) {

            return 1;

        } else if (totalCredits >= 20) {

            return 2;

        } else if (totalCredits >= 10) {

            return 3;
        }

        return 4;
    }

    // =========================
    // ADD COURSE
    // =========================

    @FXML
    private void assignCourse() {

        try {

            Course course =
                    cmbCourses.getValue();

            if (course == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Curso vacío",
                        "Seleccione un curso"
                );

                return;
            }

            // VALIDAR DUPLICADOS

            for (EnrollmentRow row
                    : selectedCourses) {

                if (
                        row.getCourseName()
                                .equals(
                                        course.getName()
                                )
                ) {

                    showAlert(
                            Alert.AlertType.WARNING,
                            "Duplicado",
                            "Ese curso ya fue agregado"
                    );

                    return;
                }
            }

            // AGREGAR A TABLA

            selectedCourses.add(

                    new EnrollmentRow(
                            course,
                            course.getName(),
                            course.getCredits()
                    )
            );

            cmbCourses.getSelectionModel()
                    .clearSelection();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // DELETE EVENT
    // =========================

    private void initializeDeleteEvent() {

        tblEnrollments.setOnKeyPressed(event -> {

            switch (event.getCode()) {

                case DELETE -> {

                    EnrollmentRow selected =
                            tblEnrollments
                                    .getSelectionModel()
                                    .getSelectedItem();

                    if (selected != null) {

                        selectedCourses.remove(selected);
                    }
                }
            }
        });
    }

    // =========================
    // SEND REQUEST
    // =========================

    @FXML
    private void sendEnrollmentRequest() {

        for (EnrollmentRequest req : enrollmentRequestData.getRequests()) {

            if (Objects.equals(req.getStudentId(), currentStudent.getId())
                    && req.getStatus().equalsIgnoreCase("PENDING")) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Solicitud existente",
                        "Ya tienes una solicitud en espera. No puedes enviar otra."
                );
                return;
            }
        }

        try {

            if (selectedCourses.isEmpty()) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Sin cursos",
                        "Debe seleccionar cursos"
                );

                return;
            }
//(Student student, int priority, String status, LinkedList<Course> courses)
            EnrollmentRequest request = new EnrollmentRequest(currentStudent,calculatePriority(),"PENDING",new LinkedList<>());

          //  request.setStudent(currentStudent);

          //  request.setPriority(calculatePriority());

          //  request.setStatus("PENDING");

            // AGREGAR CURSOS

            for (EnrollmentRow row : selectedCourses) {
                request.getCourses().add(row.getCourse());
            }

            // GUARDAR
            enrollmentRequestData.addRequest(request);

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Solicitud enviada",
                    "La solicitud fue enviada correctamente"
            );

            // LIMPIAR TABLA
            selectedCourses.clear();

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

    // =========================
    // ROW CLASS
    // =========================

    public static class EnrollmentRow {

        private final Course course;

        private final String courseName;

        private final int credits;

        public EnrollmentRow(
                Course course,
                String courseName,
                int credits
        ) {

            this.course = course;
            this.courseName = courseName;
            this.credits = credits;
        }

        public Course getCourse() {
            return course;
        }

        public String getCourseName() {
            return courseName;
        }

        public int getCredits() {
            return credits;
        }
    }
}