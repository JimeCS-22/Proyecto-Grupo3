package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.EnrollmentApprovedData;
import cr.ac.ucr.sga.model.data.EnrollmentData;
import cr.ac.ucr.sga.model.data.StudentData;
import cr.ac.ucr.sga.model.entities.Enrollment;
import cr.ac.ucr.sga.model.entities.MatriculaAprobada;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;
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

public class NotesCourseProfessor implements Initializable {

    @FXML
    private Label lblProfessorInfo;

    @FXML
    private TableView<EnrollmentRow> tblEnrollments;

    @FXML
    private TableColumn<EnrollmentRow, String> colStudent;

    @FXML
    private TableColumn<EnrollmentRow, String> colCourse;

    @FXML
    private TableColumn<EnrollmentRow, Double> colGrade;

    @FXML
    private TableColumn<EnrollmentRow, String> colStatus;

    private User currentUser;
    private final EnrollmentApprovedData enrollmentApprovedData = new EnrollmentApprovedData();
    private final EnrollmentData enrollmentData = new EnrollmentData();
    private final StudentData studentData = new StudentData();
    private ObservableList<EnrollmentRow> enrollmentRows = FXCollections.observableArrayList();

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
        // Habilitar edición en la tabla
        tblEnrollments.setEditable(true);

        // Columna de nota editable
        colGrade.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colGrade.setOnEditCommit(event -> {
            EnrollmentRow row = event.getRowValue();
            double newGrade = event.getNewValue();

            // Validar nota entre 0 y 100
            if (newGrade < 0 || newGrade > 100) {
                showAlert(Alert.AlertType.WARNING, "Nota inválida",
                        "La nota debe estar entre 0 y 100");
                return;
            }

            row.setGrade(newGrade);

            // Actualizar automáticamente el estado según la nota
            String status = determineStatus(newGrade);
            row.setStatus(status);

            System.out.println("✅ Nota actualizada: " + row.getStudentName() +
                    " - " + row.getCourseName() +
                    " = " + newGrade + " (" + status + ")");
        });

        // Columna de estado (solo lectura para el profesor, se actualiza automáticamente)
        colStatus.setEditable(false);

        // Agregar tooltip para explicar la escala
        colGrade.setCellFactory(column -> new TextFieldTableCell<EnrollmentRow, Double>(new DoubleStringConverter()) {
            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
    }

    private String determineStatus(double grade) {
        if (grade >= 70) {
            return "Aprobado";
        } else if (grade >= 60) {
            return "Condicionado";
        } else {
            return "Reprobado";
        }
    }

    private void loadEnrollments() {
        try {
            enrollmentRows.clear();

            String professorUsername = currentUser.getUsername();
            System.out.println("🔍 Cargando matrículas para profesor: " + professorUsername);

            // Obtener todas las matrículas aprobadas del profesor
            LinkedList<MatriculaAprobada> matriculas =
                    enrollmentApprovedData.getMatriculasByProfessor(professorUsername);

            System.out.println("📚 Matrículas encontradas: " + matriculas.size());

            int totalEnrollments = 0;

            for (MatriculaAprobada mat : matriculas.toList()) {
                Student student = mat.getStudent();
                if (student == null) continue;

                // Obtener los enrollments del estudiante en esta matrícula
                for (Enrollment enrollment : mat.getEnrollments().toList()) {
                    // Solo mostrar los que son de este profesor
                    if (enrollment.getProfessorId() != null &&
                            enrollment.getProfessorId().equalsIgnoreCase(professorUsername)) {

                        // Obtener nombre del estudiante
                        String studentName = student.getName() + " (" + student.getCarnet() + ")";
                        String courseName = getCourseName(enrollment.getCourseId());

                        EnrollmentRow row = new EnrollmentRow(
                                enrollment.getId(),
                                studentName,
                                courseName,
                                enrollment.getGrade(),
                                enrollment.getStatus() != null ? enrollment.getStatus() : "Sin calificar"
                        );

                        enrollmentRows.add(row);
                        totalEnrollments++;
                    }
                }
            }

            tblEnrollments.setItems(enrollmentRows);
            System.out.println("✅ Total inscripciones cargadas: " + totalEnrollments);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error al cargar las matrículas: " + e.getMessage());
        }
    }

    private String getCourseName(String courseId) {
        // Aquí puedes obtener el nombre del curso desde CourseData
        // Por ahora retornamos el ID
        return courseId;
    }

    // =========================
    // SAVE GRADES
    // =========================
    @FXML
    private void onSaveGrades() {
        try {
            int updated = 0;

            for (EnrollmentRow row : enrollmentRows) {
                // Buscar el enrollment original
                Enrollment enrollment = enrollmentData.findById(row.getEnrollmentId());
                if (enrollment == null) continue;

                // Actualizar nota y estado
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

            showAlert(Alert.AlertType.INFORMATION, "Éxito",
                    "✅ " + updated + " calificaciones actualizadas correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error al guardar las calificaciones: " + e.getMessage());
        }
    }

    // =========================
    // RELOAD
    // =========================
    @FXML
    private void onReload() {
        loadEnrollments();
        showAlert(Alert.AlertType.INFORMATION, "Recargado",
                "Datos recargados correctamente");
    }

    // =========================
    // UTILITY METHODS
    // =========================
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // =========================
    // ENROLLMENT ROW CLASS
    // =========================
    public static class EnrollmentRow {
        private final SimpleStringProperty enrollmentId;
        private final SimpleStringProperty studentName;
        private final SimpleStringProperty courseName;
        private final SimpleDoubleProperty grade;
        private final SimpleStringProperty status;

        public EnrollmentRow(String enrollmentId, String studentName,
                             String courseName, double grade, String status) {
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

        public void setStudentName(String value) { studentName.set(value); }
        public void setCourseName(String value) { courseName.set(value); }
        public void setGrade(double value) {
            grade.set(value);
            // Actualizar automáticamente el estado
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
}