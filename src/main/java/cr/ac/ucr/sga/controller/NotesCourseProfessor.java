package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.data.EnrollmentApprovedData;
import cr.ac.ucr.sga.model.data.EnrollmentData;
import cr.ac.ucr.sga.model.entities.*;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class NotesCourseProfessor implements Initializable {

    @FXML private Label lblProfessorInfo;
    @FXML private TableView<EnrollmentRow> tblEnrollments;
    @FXML private TableColumn<EnrollmentRow, String> colStudent;
    @FXML private TableColumn<EnrollmentRow, String> colCourse;
    @FXML private TableColumn<EnrollmentRow, Double> colGrade;
    @FXML private TableColumn<EnrollmentRow, String> colStatus;

    private User currentUser;
    private final EnrollmentApprovedData enrollmentApprovedData = new EnrollmentApprovedData();
    private final EnrollmentData enrollmentData = new EnrollmentData();
    private final ObservableList<EnrollmentRow> enrollmentRows = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupTableEditing();
    }

    public void setUser(User user) {
        this.currentUser = user;
        lblProfessorInfo.setText("📚 Profesor: " + user.getUsername());
        loadEnrollments();
    }

    private void setupTableColumns() {
        colStudent.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupTableEditing() {
        tblEnrollments.setEditable(true);
        colGrade.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colGrade.setOnEditCommit(event -> {
            EnrollmentRow row = event.getRowValue();
            Double newGrade = event.getNewValue();
            if (newGrade == null || newGrade < 0 || newGrade > 100) {
                showAlert(Alert.AlertType.WARNING, "Nota inválida", "La nota debe estar entre 0 y 100");
                return;
            }
            row.setGrade(newGrade);
            tblEnrollments.refresh();
        });
        colStatus.setEditable(false);
    }

    private void loadEnrollments() {
        try {
            syncFromApprovedToEnrollments();
            if (currentUser == null) {
                tblEnrollments.setItems(FXCollections.observableArrayList());
                return;
            }
            enrollmentRows.clear();
            String professorUsername = currentUser.getUsername();
            LinkedList<MatriculaAprobada> matriculas = enrollmentApprovedData.getMatriculasByProfessor(professorUsername);
            for (MatriculaAprobada mat : matriculas.toList()) {
                Student student = mat.getStudent();
                if (student == null) continue;
                for (Enrollment enrollment : mat.getEnrollments().toList()) {
                    String enrollmentId = enrollment.getId();
                    if (enrollmentId == null || enrollmentId.isEmpty()) {
                        enrollmentId = UUID.randomUUID().toString();
                        enrollment.setId(enrollmentId);
                    }
                    String studentName = student.getName() + " (" + student.getCarnet() + ")";
                    String courseName = getCourseName(enrollment.getCourseId());
                    String status = enrollment.getStatus();
                    if (status == null || status.isEmpty()) {
                        status = determineStatus(enrollment.getGrade());
                    }
                    EnrollmentRow row = new EnrollmentRow(
                            enrollmentId,
                            studentName,
                            courseName,
                            enrollment.getGrade(),
                            status
                    );
                    enrollmentRows.add(row);
                }
            }
            tblEnrollments.setItems(null);
            tblEnrollments.setItems(enrollmentRows);
            tblEnrollments.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error al cargar las matrículas: " + e.getMessage());
        }
    }

    private String determineStatus(double grade) {
        if (grade >= 70) return "Aprobado";
        if (grade >= 60) return "Condicionado";
        return "Reprobado";
    }

    private String getCourseName(String courseId) {
        Course course = new CourseData().findCourseById(courseId);
        return course != null ? course.getName() : courseId;
    }

    @FXML
    private void onSaveGrades() {
        try {
            int updated = 0;
            for (EnrollmentRow row : enrollmentRows) {
                Enrollment enrollment = enrollmentData.findById(row.getEnrollmentId());
                if (enrollment == null) continue;
                double currentGrade = enrollment.getGrade();
                String currentStatus = enrollment.getStatus();
                if (Math.abs(row.getGrade() - currentGrade) > 0.001 ||
                        !row.getStatus().equals(currentStatus)) {
                    enrollment.setGrade(row.getGrade());
                    enrollment.setStatus(row.getStatus());
                    if (enrollmentData.updateEnrollment(enrollment)) {
                        updated++;
                    }
                }
            }
            syncEnrollmentsToApproved();
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "✅ " + updated + " calificaciones actualizadas correctamente");
            loadEnrollments();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error al guardar las calificaciones: " + e.getMessage());
        }
    }

    @FXML
    private void onReload() {
        if (currentUser == null) return;
        loadEnrollments();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class EnrollmentRow {
        private final SimpleStringProperty enrollmentId;
        private final SimpleStringProperty studentName;
        private final SimpleStringProperty courseName;
        private final SimpleDoubleProperty grade;
        private final SimpleStringProperty status;

        public EnrollmentRow(String enrollmentId, String studentName, String courseName, double grade, String status) {
            this.enrollmentId = new SimpleStringProperty(enrollmentId);
            this.studentName = new SimpleStringProperty(studentName);
            this.courseName = new SimpleStringProperty(courseName);
            this.grade = new SimpleDoubleProperty(grade);
            this.status = new SimpleStringProperty(status);
        }

        public String getEnrollmentId() { return enrollmentId.get(); }
        public String getStudentName() { return studentName.get(); }
        public String getCourseName() { return courseName.get(); }
        public double getGrade() { return grade.get(); }
        public String getStatus() { return status.get(); }

        public void setGrade(double value) {
            grade.set(value);
            if (value >= 70) {
                setStatus("Aprobado");
            } else if (value >= 60) {
                setStatus("Condicionado");
            } else {
                setStatus("Reprobado");
            }
        }
        public void setStatus(String value) { status.set(value); }
    }

    private void syncEnrollmentsToApproved() {
        try {
            LinkedList<MatriculaAprobada> matriculas = enrollmentApprovedData.loadAll();
            boolean updated = false;
            for (MatriculaAprobada mat : matriculas.toList()) {
                LinkedList<Enrollment> nuevosEnrollments = new LinkedList<>();
                for (Enrollment e : mat.getEnrollments().toList()) {
                    Enrollment updatedEnrollment = enrollmentData.findById(e.getId());
                    if (updatedEnrollment != null) {
                        nuevosEnrollments.add(updatedEnrollment);
                        if (updatedEnrollment.getGrade() != e.getGrade()) {
                            updated = true;
                        }
                    } else {
                        nuevosEnrollments.add(e);
                    }
                }
                mat.setEnrollments(nuevosEnrollments);
            }
            if (updated) {
                enrollmentApprovedData.saveAll(matriculas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncFromApprovedToEnrollments() {
        try {
            LinkedList<MatriculaAprobada> matriculas = enrollmentApprovedData.loadAll();
            LinkedList<Enrollment> allEnrollments = new LinkedList<>();
            for (MatriculaAprobada mat : matriculas.toList()) {
                for (Enrollment e : mat.getEnrollments().toList()) {
                    if (e.getId() == null || e.getId().isEmpty()) {
                        e.setId(UUID.randomUUID().toString());
                    }
                    allEnrollments.add(e);
                }
            }
            for (Enrollment e : allEnrollments.toList()) {
                Enrollment existing = enrollmentData.findById(e.getId());
                if (existing == null) {
                    enrollmentData.addEnrollment(e);
                } else {
                    existing.setGrade(e.getGrade());
                    existing.setStatus(e.getStatus());
                    enrollmentData.updateEnrollment(existing);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}