package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.data.EnrollmentApprovedData;
import cr.ac.ucr.sga.model.data.StudentData;
import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.MatriculaAprobada;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import cr.ac.ucr.sga.model.structures.queues.PriorityLinkedQueue;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para Matrícula Final (estudiante selecciona de los cursos aprobados).
 */
public class EnrollmentController implements Initializable {

    // =========================
    // FXML NODES
    // =========================
    @FXML private ComboBox<Student> cmbStudents;
    @FXML private ComboBox<Course> cmbCourses;

    // Tabla de expedientes (opcional administrativa)
    @FXML private TableView<EnrollmentRow> tblEnrollments;
    @FXML private TableColumn<EnrollmentRow, String> colStudent;
    @FXML private TableColumn<EnrollmentRow, String> colCourse;
    @FXML private TableColumn<EnrollmentRow, Integer> colCredits;

    // Tablas para aprobación/matriculación final
    @FXML private TableView<Course> tblCursosAprobados;
    @FXML private TableColumn<Course, String> colAprobadoCodigo;
    @FXML private TableColumn<Course, String> colAprobadoNombre;
    @FXML private TableColumn<Course, Integer> colAprobadoCreditos;

    @FXML private TableView<Course> tblCursosAMatricular;
    @FXML private TableColumn<Course, String> colMatriculaCodigo;
    @FXML private TableColumn<Course, String> colMatriculaNombre;
    @FXML private TableColumn<Course, Integer> colMatriculaCreditos;

    @FXML private Button btnAgregarCurso;
    @FXML private Button btnQuitarCurso;
    @FXML private Button btnMatricular;
    @FXML private Button btnNegar;

    // =========================
    // DATA / SERVICES
    // =========================
    private final StudentData studentData = new StudentData();
    private final CourseData courseData = new CourseData();
    private final AcademicRecordData recordData = new AcademicRecordData();
    private final EnrollmentApprovedData matriculaData = new EnrollmentApprovedData();

    private final PriorityLinkedQueue<Student> enrollmentQueue = new PriorityLinkedQueue<>();
    private final ObservableList<EnrollmentRow> enrollmentRows = FXCollections.observableArrayList();

    private Student currentStudent;
    private MatriculaAprobada matriculaActual;
    private MainController mainController;

    private final ObservableList<Course> cursosAprobados = FXCollections.observableArrayList();
    private final ObservableList<Course> cursosAMatricular = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();                 // tabla expedientes
        initializeTableColumns();          // tablas aprobados / a matricular
        setupButtonActions();              // botones aprobados
        loadPriorityQueue();
        loadStudentsByPriority();
        loadCourses();
        loadEnrollments();
        configureStudentsComboCellFactory();
    }

    // =========================
    // TABLA: COLUMNAS (expedientes)
    // =========================
    private void initializeTable() {
        if (colStudent != null)
            colStudent.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudentName()));
        if (colCourse != null)
            colCourse.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourseName()));
        if (colCredits != null)
            colCredits.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCredits()).asObject());

        if (tblEnrollments != null) tblEnrollments.setItems(enrollmentRows);
    }

    // =========================
    // TABLAS: COLUMNAS para cursos aprobados/matricular
    // =========================
    private void initializeTableColumns() {
        // Cursos Aprobados
        if (colAprobadoCodigo != null)
            colAprobadoCodigo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        if (colAprobadoNombre != null)
            colAprobadoNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        if (colAprobadoCreditos != null)
            colAprobadoCreditos.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCredits()).asObject());

        // Cursos a Matricular
        if (colMatriculaCodigo != null)
            colMatriculaCodigo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        if (colMatriculaNombre != null)
            colMatriculaNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        if (colMatriculaCreditos != null)
            colMatriculaCreditos.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCredits()).asObject());

        if (tblCursosAprobados != null) tblCursosAprobados.setItems(cursosAprobados);
        if (tblCursosAMatricular != null) tblCursosAMatricular.setItems(cursosAMatricular);
    }

    // =========================
    // BOTONES: SETUP
    // =========================
    private void setupButtonActions() {
        if (btnAgregarCurso != null) btnAgregarCurso.setOnAction(e -> agregarCursoAMatricular());
        if (btnQuitarCurso != null) btnQuitarCurso.setOnAction(e -> quitarCursoDeMatricula());
        if (btnMatricular != null) btnMatricular.setOnAction(e -> matricular());
        if (btnNegar != null) btnNegar.setOnAction(e -> {
            try {
                negar();
            } catch (ListException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    // =========================
    // SET STUDENT
    // =========================
    public void setStudent(Student student) {
        this.currentStudent = student;
        loadCursosAprobados();
    }


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // =========================
    // LOAD: estudiantes (combo), cursos (combo)
    // =========================
    private void loadStudents() {
        if (cmbStudents == null) return;
        try {
            cmbStudents.getItems().clear();
            int size = studentData.getAllStudents().size();
            for (int i = 0; i < size; i++) {
                Student student = studentData.getAllStudents().get(i);
                cmbStudents.getItems().add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCourses() {
        if (cmbCourses == null) return;
        try {
            cmbCourses.getItems().clear();
            int size = courseData.getAllCourses().size();
            for (int i = 0; i < size; i++) {
                Course course = courseData.getAllCourses().get(i);
                cmbCourses.getItems().add(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // ASIGNAR CURSO (ADMIN) - mantiene nombre original
    // =========================
    @FXML
    private void assignCourse() {
        try {
            if (cmbStudents == null || cmbCourses == null) return;
            Student student = cmbStudents.getValue();
            Course course = cmbCourses.getValue();
            if (student == null || course == null) {
                showAlert(Alert.AlertType.WARNING, "Campos vacíos", "Seleccione estudiante y curso");
                return;
            }
            boolean ok = recordData.addCourseToStudent(student.getId(), course);
            if (!ok) {
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
        if (cmbStudents != null) cmbStudents.getSelectionModel().clearSelection();
        if (cmbCourses != null) cmbCourses.getSelectionModel().clearSelection();
    }

    // =========================
    // CARGA EXPEDIENTES (tabla admin)
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
                            course.getName(),
                            course.getCredits()
                    ));
                }
            }
            if (tblEnrollments != null) tblEnrollments.setItems(enrollmentRows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // CARGA CURSOS APROBADOS para currentStudent
    // =========================
    private void loadCursosAprobados() {
        if (currentStudent == null || tblCursosAprobados == null) return;
        cursosAprobados.clear();
        matriculaActual = matriculaData.findByStudentId(currentStudent.getId());
        if (matriculaActual != null && "APPROVED".equalsIgnoreCase(matriculaActual.getStatus())) {
            try {
                for (int i = 1; i <= matriculaActual.getCoursesApproved().size(); i++) {
                    cursosAprobados.add(matriculaActual.getCoursesApproved().get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // =========================
    // AGREGAR / QUITAR CURSOS entre aprobados <-> a matricular
    // =========================
    @FXML
    private void agregarCursoAMatricular() {
        if (tblCursosAprobados == null || tblCursosAMatricular == null) return;
        Course selected = tblCursosAprobados.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cursosAMatricular.add(selected);
            cursosAprobados.remove(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "Sin selección", "Seleccione un curso primero");
        }
    }

    @FXML
    private void quitarCursoDeMatricula() {
        if (tblCursosAMatricular == null || tblCursosAprobados == null) return;
        Course selected = tblCursosAMatricular.getSelectionModel().getSelectedItem();
        if (selected != null) {
            cursosAprobados.add(selected);
            cursosAMatricular.remove(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "Sin selección", "Seleccione un curso primero");
        }
    }

    // =========================
    // MATRICULAR cursos seleccionados (añadir al expediente)
    // =========================
    @FXML
    private void matricular() {
        if (cursosAMatricular.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Sin cursos", "Debe seleccionar al menos un curso");
            return;
        }
        try {
            if (currentStudent == null) {
                showAlert(Alert.AlertType.WARNING, "Sin estudiante", "No hay estudiante en sesión");
                return;
            }

            AcademicRecord record = recordData.findByStudentId(currentStudent.getId());

            if (record == null) {
                record = new AcademicRecord(currentStudent);
                recordData.addRecord(record);
            }

            // Añadir cursos al expediente del estudiante
            for (Course c : cursosAMatricular) {
                recordData.addCourseToStudent(currentStudent.getId(), c);
            }

            // Buscar matrícula aprobada existente para ese estudiante
            MatriculaAprobada matriculaActualExistente =
                    matriculaData.findByStudentId(currentStudent.getId());

            if (matriculaActualExistente != null) {
                // Actualizar cursos y estado sin cambiar ID
                LinkedList<Course> cursosFinales = new LinkedList<>();
                for (Course c : cursosAMatricular) {
                    cursosFinales.add(c);
                }
                matriculaActualExistente.setCoursesApproved(cursosFinales);
                matriculaActualExistente.setStatus("MATRICULATED");
                matriculaData.addOrUpdate(matriculaActualExistente);
            } else {
                // Crear nueva matrícula con nuevo ID si no existe aún
                LinkedList<Course> cursosFinales = new LinkedList<>();
                for (Course c : cursosAMatricular) {
                    cursosFinales.add(c);
                }

                // Generar id único sin usar UUID
                String id = currentStudent.getId() + "-" + System.currentTimeMillis();

                MatriculaAprobada nuevaMatricula = new MatriculaAprobada(
                        id,
                        currentStudent,
                        cursosFinales
                );
                nuevaMatricula.setStatus("MATRICULATED");
                matriculaData.addOrUpdate(nuevaMatricula);
            }

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Cursos matriculados correctamente");

            cursosAprobados.clear();
            cursosAMatricular.clear();
            loadEnrollments();
            loadCursosAprobados();

            tblCursosAprobados.refresh();
            tblCursosAMatricular.refresh();

            if (mainController != null) {
                if (mainController.getRecordController() != null) {
                    mainController.getRecordController().refreshForStudent(currentStudent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }



    // =========================
    // NEGAR matrícula aprobada
    // =========================
    @FXML
    private void negar() throws ListException {
        if (matriculaActual == null) {
            showAlert(Alert.AlertType.WARNING, "Sin matrícula", "No hay matrícula aprobada para negar");
            return;
        }
        matriculaData.delete(matriculaActual.getId());
        cursosAprobados.clear();
        cursosAMatricular.clear();
        showAlert(Alert.AlertType.INFORMATION, "Matrícula Negada", "Debes hacer pre-matrícula de nuevo para continuar");
    }

    // =========================
    // CÁLCULO PRIORIDAD (reutilizado)
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

    private void loadPriorityQueue() {
        try {
            enrollmentQueue.clear();
            int size = studentData.getAllStudents().size();
            for (int i = 1; i < size; i++) {
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

    // =========================
    // DTO LOCAL: EnrollmentRow
    // =========================
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

    private void configureStudentsComboCellFactory() {
        if (cmbStudents == null) return;
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
}
