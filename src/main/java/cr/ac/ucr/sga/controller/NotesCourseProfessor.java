package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.data.EnrollmentApprovedData;
import cr.ac.ucr.sga.model.data.EnrollmentData;
import cr.ac.ucr.sga.model.data.StudentData;
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
    private final CourseData courseData = new CourseData();
    private ObservableList<EnrollmentRow> enrollmentRows = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupTableEditing();
    }

    public void setUser(User user) {
        this.currentUser = user;
        lblProfessorInfo.setText("📚 Profesor: " + user.getUsername());
        System.out.println("Profesor logueado: " + currentUser.getUsername());
        loadEnrollments();
    }

    private void setupTableColumns() {
        // Los nombres deben coincidir con los getters de EnrollmentRow
        colStudent.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colCourse.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupTableEditing() {
        // Habilitar edición en la tabla
        tblEnrollments.setEditable(true);

        // ✅ Configurar la columna de nota para que sea editable
        colGrade.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colGrade.setOnEditCommit(event -> {
            // Obtener la fila y el nuevo valor
            EnrollmentRow row = event.getRowValue();
            Double newGrade = event.getNewValue();

            System.out.println("📝 Editando nota para: " + row.getStudentName());
            System.out.println("  - Valor anterior: " + row.getGrade());
            System.out.println("  - Nuevo valor: " + newGrade);

            // Validar nota entre 0 y 100
            if (newGrade == null || newGrade < 0 || newGrade > 100) {
                showAlert(Alert.AlertType.WARNING, "Nota inválida",
                        "La nota debe estar entre 0 y 100");
                return;
            }

            // ✅ Actualizar la nota en la fila
            row.setGrade(newGrade);

            // El estado se actualiza automáticamente en setGrade()
            String status = row.getStatus();

            System.out.println("  ✅ Nota actualizada a: " + newGrade);
            System.out.println("  ✅ Estado actualizado a: " + status);

            // Forzar refresco de la tabla
            tblEnrollments.refresh();
        });

        // La columna de estado NO es editable
        colStatus.setEditable(false);
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
            if (currentUser == null) {
                System.out.println("⚠️ currentUser es null, no se pueden cargar matrículas");
                tblEnrollments.setItems(FXCollections.observableArrayList());
                return;
            }

            enrollmentRows.clear();

            String professorUsername = currentUser.getUsername();
            System.out.println("🔍 Cargando matrículas para profesor: " + professorUsername);

            LinkedList<MatriculaAprobada> matriculas =
                    enrollmentApprovedData.getMatriculasByProfessor(professorUsername);

            System.out.println("📚 Matrículas encontradas: " + matriculas.size());

            int totalEnrollments = 0;

            for (MatriculaAprobada mat : matriculas.toList()) {
                Student student = mat.getStudent();
                if (student == null) continue;

                for (Enrollment enrollment : mat.getEnrollments().toList()) {
                    String studentName = student.getName() + " (" + student.getCarnet() + ")";
                    String courseName = getCourseName(enrollment.getCourseId());

                    String status = enrollment.getStatus();
                    if (status == null || status.isEmpty()) {
                        status = determineStatus(enrollment.getGrade());
                    }

                    EnrollmentRow row = new EnrollmentRow(
                            enrollment.getId(),
                            studentName,
                            courseName,
                            enrollment.getGrade(),
                            status
                    );

                    enrollmentRows.add(row);
                    totalEnrollments++;

                    // Debug: imprimir cada fila agregada
                    System.out.println("  ➕ Agregando fila: " + studentName + " - " + courseName + " - Nota: " + enrollment.getGrade());
                }
            }

            // ✅ FORZAR ACTUALIZACIÓN DE LA TABLA
            tblEnrollments.setItems(null);
            tblEnrollments.setItems(enrollmentRows);
            tblEnrollments.refresh();

            System.out.println("✅ Total inscripciones cargadas: " + totalEnrollments);
            System.out.println("📊 Filas en la tabla: " + enrollmentRows.size());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error al cargar las matrículas: " + e.getMessage());
        }
    }
    private String getCourseName(String courseId) {
        try {
            CourseData courseData = new CourseData();
            Course course = courseData.findCourseById(courseId);
            return course != null ? course.getName() : courseId;
        } catch (Exception e) {
            return courseId;
        }
    }
    // =========================
    // SAVE GRADES
    // =========================
    @FXML
    private void onSaveGrades() {
        try {
            System.out.println("💾 Iniciando guardado de calificaciones...");
            int updated = 0;

            for (EnrollmentRow row : enrollmentRows) {
                System.out.println("🔍 Buscando enrollment con ID: " + row.getEnrollmentId());

                Enrollment enrollment = enrollmentData.findById(row.getEnrollmentId());

                if (enrollment == null) {
                    System.out.println("⚠️ Enrollment no encontrado: " + row.getEnrollmentId());
                    continue;
                }

                double currentGrade = enrollment.getGrade();
                String currentStatus = enrollment.getStatus();

                System.out.println("  - Nota actual: " + currentGrade + " -> Nueva: " + row.getGrade());
                System.out.println("  - Estado actual: " + currentStatus + " -> Nuevo: " + row.getStatus());

                if (Math.abs(row.getGrade() - currentGrade) > 0.001 ||
                        !row.getStatus().equals(currentStatus)) {

                    enrollment.setGrade(row.getGrade());
                    enrollment.setStatus(row.getStatus());

                    boolean result = enrollmentData.updateEnrollment(enrollment);
                    if (result) {
                        updated++;
                        System.out.println("✅ Actualizado: " + row.getStudentName() +
                                " - Nota: " + row.getGrade());
                    } else {
                        System.out.println("❌ Falló al actualizar: " + row.getStudentName());
                    }
                } else {
                    System.out.println("  ℹ️ Sin cambios para: " + row.getStudentName());
                }
            }

            showAlert(Alert.AlertType.INFORMATION, "Éxito",
                    "✅ " + updated + " calificaciones actualizadas correctamente");

            // Recargar para mostrar los cambios
            loadEnrollments();

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
        if (currentUser == null) {
            showAlert(Alert.AlertType.WARNING, "Sin sesión",
                    "No hay un profesor logueado. Por favor, inicie sesión.");
            return;
        }
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

        // ✅ GETTERS (necesarios para PropertyValueFactory)
        public String getEnrollmentId() { return enrollmentId.get(); }
        public String getStudentName() { return studentName.get(); }
        public String getCourseName() { return courseName.get(); }
        public double getGrade() { return grade.get(); }
        public String getStatus() { return status.get(); }

        // ✅ SETTERS
        public void setStudentName(String value) { studentName.set(value); }
        public void setCourseName(String value) { courseName.set(value); }
        public void setGrade(double value) {
            System.out.println("📝 Setting grade to: " + value + " for: " + getStudentName());
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