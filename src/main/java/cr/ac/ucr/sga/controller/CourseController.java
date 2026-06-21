package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.data.CareerData;
import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.data.ProfessorData;
import cr.ac.ucr.sga.model.entities.*;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CourseController implements Initializable {

    private User currentUser;

    @FXML
    private FlowPane coursesContainer;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtCredits;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private Spinner<Integer> spnSemestre;

    @FXML
    private ComboBox<Course> cmbPrerequisitos;

    @FXML
    private ComboBox<Career> cmbCareer;

    @FXML
    private Button btnAgregarPrerequisito;

    @FXML
    private ListView<String> lstPrerequisitos;

    @FXML
    private ComboBox<Course> cmbCorequisitos;

    @FXML
    private Button btnAgregarCorequisito;

    @FXML
    private ListView<String> lstCorequisitos;

    @FXML
    private ComboBox<Professor> cmbProfessor;

    // BOTONES
    @FXML
    private Button btnAdd;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnClear;

    // =========================
    // DATA
    // =========================

    private final CourseData courseData = new CourseData();

    private final AcademicRecordData recordData = new AcademicRecordData();

    private final CareerData careerData = new CareerData();

    private final ProfessorData professorData = new ProfessorData();

    private Course selectedCourse;

    private MainController mainController;

    // =========================
    // LISTAS OBSERVABLES PARA REQUISITOS
    // =========================
    private final ObservableList<String> prerequisitosSeleccionados = FXCollections.observableArrayList();
    private final ObservableList<String> corequisitosSeleccionados = FXCollections.observableArrayList();

    // =========================
    // INITIALIZE
    // =========================

    @Override
    public void initialize(
            URL url,
            ResourceBundle resourceBundle
    ) {

        // Configurar ComboBox de estado
        if (cmbStatus != null) {
            cmbStatus.getItems().addAll(
                    "Activo",
                    "Inactivo"
            );
        }

        if (spnSemestre != null) {
            spnSemestre.setValueFactory(
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1)
            );
        }

        cmbCareer.getItems().addAll(
                careerData.getAllCareers().toList()
        );

        cmbCareer.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Career career, boolean empty) {
                super.updateItem(career, empty);

                if (empty || career == null) {
                    setText("");
                } else {
                    setText(career.getName());
                }
            }
        });

        cmbCareer.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Career career, boolean empty) {
                super.updateItem(career, empty);

                if (empty || career == null) {
                    setText("");
                } else {
                    setText(career.getName());
                }
            }
        });

        System.out.println("Inicializando CourseController...");
        loadProfessors();
        System.out.println("Profesores cargados en initialize");
        // Configurar ListViews para requisitos
        if (lstPrerequisitos != null) {
            lstPrerequisitos.setItems(prerequisitosSeleccionados);
        }

        if (lstCorequisitos != null) {
            lstCorequisitos.setItems(corequisitosSeleccionados);
        }

        // Botones de agregar requisitos
        if (btnAgregarPrerequisito != null) {
            btnAgregarPrerequisito.setOnAction(e -> agregarPrerequisito());
        }

        if (btnAgregarCorequisito != null) {
            btnAgregarCorequisito.setOnAction(e -> agregarCorequisito());
        }
    }

    public void setMainController(
            MainController mainController
    ) {
        this.mainController = mainController;
    }

    // =========================
    // SET USER
    // =========================

    public void setUser(User user) {

        this.currentUser = user;

        // =========================
        // STUDENT
        // =========================

        if (user.getRole() == Role.STUDENT) {

            ocultarCRUD();

            loadStudentCourses();

        } else {

            loadCourses();
        }
    }

    // =========================
    // LOAD ALL COURSES
    // =========================

    private void loadCourses() {

        coursesContainer.getChildren().clear();

        try {

            for (Course course : courseData.getAllCourses().toList()) {

                addCourseCard(course);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        // Actualizar combos de requisitos
        actualizarCombosRequisitos();
    }

    // =========================
    // LOAD STUDENT COURSES
    // =========================

    private void loadStudentCourses() {

        coursesContainer.getChildren().clear();

        try {

            AcademicRecord record =
                    recordData.findByUsername(
                            currentUser.getUsername()
                    );

            if (record != null) {

                DoublyLinkedList<Course> courses =
                        record.getCourses();

                for (
                        int i = 1;
                        i <= courses.size();
                        i++
                ) {

                    Course course = courses.get(i);

                    addCourseCard(course);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // ADD COURSE CARD
    // =========================

    private void addCourseCard(Course course)
            throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/views/components/course-card.fxml"
                        )
                );

        Parent card = loader.load();

        CourseCardController controller =
                loader.getController();

        controller.setCourse(course);

        // SOLO ADMIN/PROFESOR
        if (
                currentUser != null
                        &&
                        currentUser.getRole()
                                != Role.STUDENT
        ) {

            card.setOnMouseClicked(
                    (MouseEvent event) -> {
                        selectCourse(course);
                    }
            );
        }

        coursesContainer.getChildren().add(card);
    }

    // =========================
    // HIDE CRUD
    // =========================

    private void ocultarCRUD() {

        txtId.setVisible(false);
        txtName.setVisible(false);
        txtCredits.setVisible(false);
        cmbStatus.setVisible(false);

        btnAdd.setVisible(false);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
        btnClear.setVisible(false);

        // Ocultar también los campos de requisitos
        if (spnSemestre != null) spnSemestre.setVisible(false);
        if (cmbPrerequisitos != null) cmbPrerequisitos.setVisible(false);
        if (btnAgregarPrerequisito != null) btnAgregarPrerequisito.setVisible(false);
        if (lstPrerequisitos != null) lstPrerequisitos.setVisible(false);
        if (cmbCorequisitos != null) cmbCorequisitos.setVisible(false);
        if (btnAgregarCorequisito != null) btnAgregarCorequisito.setVisible(false);
        if (lstCorequisitos != null) lstCorequisitos.setVisible(false);
    }

    // =========================
    // ACTUALIZAR COMBOS DE REQUISITOS
    // =========================
    private void actualizarCombosRequisitos() {
        try {
            List<Course> todos = courseData.getAllCourses().toList();
            ObservableList<Course> cursos = FXCollections.observableArrayList(todos);

            // =========================
            // CONFIGURAR COMBOBOX PRE-REQUISITOS
            // =========================
            if (cmbPrerequisitos != null) {
                // Agregar opción vacía al inicio
                ObservableList<Course> cursosConVacio = FXCollections.observableArrayList();
                cursosConVacio.add(null); // Opción vacía
                cursosConVacio.addAll(cursos);

                cmbPrerequisitos.setItems(cursosConVacio);
                cmbPrerequisitos.setCellFactory(lv -> new ListCell<Course>() {
                    @Override
                    protected void updateItem(Course c, boolean empty) {
                        super.updateItem(c, empty);
                        if (empty || c == null) {
                            setText("(Sin pre-requisito)"); // Texto para opción vacía
                        } else {
                            setText(c.getId() + " - " + c.getName());
                        }
                    }
                });

                // También configurar el botón de selección (para mostrar bien el null)
                cmbPrerequisitos.setButtonCell(new ListCell<Course>() {
                    @Override
                    protected void updateItem(Course c, boolean empty) {
                        super.updateItem(c, empty);
                        if (empty || c == null) {
                            setText("(Sin pre-requisito)");
                        } else {
                            setText(c.getId() + " - " + c.getName());
                        }
                    }
                });
            }

            // =========================
            // CONFIGURAR COMBOBOX CO-REQUISITOS
            // =========================
            if (cmbCorequisitos != null) {
                // Agregar opción vacía al inicio
                ObservableList<Course> cursosConVacio = FXCollections.observableArrayList();
                cursosConVacio.add(null); // Opción vacía
                cursosConVacio.addAll(cursos);

                cmbCorequisitos.setItems(cursosConVacio);
                cmbCorequisitos.setCellFactory(lv -> new ListCell<Course>() {
                    @Override
                    protected void updateItem(Course c, boolean empty) {
                        super.updateItem(c, empty);
                        if (empty || c == null) {
                            setText("(Sin co-requisito)"); // Texto para opción vacía
                        } else {
                            setText(c.getId() + " - " + c.getName());
                        }
                    }
                });

                // También configurar el botón de selección (para mostrar bien el null)
                cmbCorequisitos.setButtonCell(new ListCell<Course>() {
                    @Override
                    protected void updateItem(Course c, boolean empty) {
                        super.updateItem(c, empty);
                        if (empty || c == null) {
                            setText("(Sin co-requisito)");
                        } else {
                            setText(c.getId() + " - " + c.getName());
                        }
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // AGREGAR PRE-REQUISITO
    // =========================
    @FXML
    private void agregarPrerequisito() {
        Course curso = cmbPrerequisitos.getValue();

        // Si es null (Sin pre-requisito), no hacer nada
        if (curso == null) {
            return;
        }

        // Verificar que no esté duplicado
        if (!prerequisitosSeleccionados.contains(curso.getId())) {
            prerequisitosSeleccionados.add(curso.getId() + " - " + curso.getName());
        }

        cmbPrerequisitos.setValue(null);
    }

    // =========================
    // AGREGAR CO-REQUISITO
    // =========================
    @FXML
    private void agregarCorequisito() {
        Course curso = cmbCorequisitos.getValue();

        // Si es null (Sin co-requisito), no hacer nada
        if (curso == null) {
            return;
        }

        // Verificar que no esté duplicado
        if (!corequisitosSeleccionados.contains(curso.getId())) {
            corequisitosSeleccionados.add(curso.getId() + " - " + curso.getName());
        }

        cmbCorequisitos.setValue(null);
    }

    // =========================
    // ADD COURSE (AGREGAR)
    // =========================
    @FXML
    private void addCourse() {
        try {

            String id = txtId.getText().trim();
            String name = txtName.getText().trim();

            if (id.isEmpty() || name.isEmpty()
                    || txtCredits.getText().isEmpty()
                    || cmbStatus.getValue() == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Campos vacíos",
                        "Complete todos los campos"
                );
                return;
            }

            int credits = Integer.parseInt(txtCredits.getText());
            String status = cmbStatus.getValue();
            int semestre = spnSemestre != null
                    ? spnSemestre.getValue()
                    : 1;

            List<String> prerequisitos = new ArrayList<>();

            if (cmbPrerequisitos != null
                    && cmbPrerequisitos.getValue() != null) {

                prerequisitos.add(
                        cmbPrerequisitos.getValue().getId()
                );
            }

            List<String> corequisitos = new ArrayList<>();

            if (cmbCorequisitos != null
                    && cmbCorequisitos.getValue() != null) {

                corequisitos.add(
                        cmbCorequisitos.getValue().getId()
                );
            }

            Career career = cmbCareer.getValue();

            if (career == null) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Carrera",
                        "Seleccione una carrera"
                );
                return;
            }

            Professor professor = cmbProfessor.getValue();

            Course course = new Course.Builder()
                    .setId(id)
                    .setName(name)
                    .setCredits(credits)
                    .setStatus(status)
                    .setSemestre(semestre)
                    .setCareerId(career.getId())
                    .setProfessorId(professor != null ? professor.getUsername() : null)
                    .setPrerequisitosIds(prerequisitos)
                    .setCorequisitosIds(corequisitos)
                    .build();

            Course added = courseData.addCourse(course);

            if (added != null) {

                loadCourses();
                clearFields();

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Éxito",
                        "Curso agregado correctamente"
                );

            } else {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Duplicado",
                        "Ya existe un curso con ese ID"
                );
            }

        } catch (NumberFormatException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Los créditos deben ser numéricos"
            );

        } catch (Exception e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage()
            );
        }
    }

    // =========================
    // UPDATE COURSE (ACTUALIZAR)
    // =========================
    @FXML
    private void updateCourse() {

        if (selectedCourse == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione un curso"
            );
            return;
        }

        try {

            List<String> prerequisitos = new ArrayList<>();

            if (cmbPrerequisitos != null
                    && cmbPrerequisitos.getValue() != null) {

                prerequisitos.add(
                        cmbPrerequisitos.getValue().getId()
                );
            }

            List<String> corequisitos = new ArrayList<>();

            if (cmbCorequisitos != null
                    && cmbCorequisitos.getValue() != null) {

                corequisitos.add(
                        cmbCorequisitos.getValue().getId()
                );
            }

            Career career = cmbCareer.getValue();

            if (career == null) {
                showAlert(
                        Alert.AlertType.WARNING,
                        "Carrera",
                        "Seleccione una carrera"
                );
                return;
            }

            Professor professor = cmbProfessor.getValue();

            Course updatedCourse = new Course.Builder()
                    .setId(selectedCourse.getId())
                    .setName(txtName.getText())
                    .setCredits(Integer.parseInt(txtCredits.getText()))
                    .setStatus(cmbStatus.getValue())
                    .setSemestre(spnSemestre != null ? spnSemestre.getValue() : 1)
                    .setCareerId(career.getId())
                    .setProfessorId(professor != null ? professor.getUsername() : null) // ← CAMBIO AQUÍ
                    .setPrerequisitosIds(prerequisitos)
                    .setCorequisitosIds(corequisitos)
                    .build();

            boolean updated =
                    courseData.updateCourse(
                            updatedCourse
                    );

            if (updated) {

                loadCourses();

                if (mainController != null) {
                    mainController.refreshCourseProfessorList();
                }

                clearFields();

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Éxito",
                        "Curso actualizado correctamente"
                );

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "No se pudo actualizar"
                );
            }

        } catch (Exception e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage()
            );

            e.printStackTrace();
        }
    }

    // =========================
    // DELETE COURSE
    // =========================

    @FXML
    private void deleteCourse() throws ListException {

        if (selectedCourse == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione un curso"
            );

            return;
        }

        boolean removed =
                courseData.removeCourse(
                        selectedCourse.getId()
                );

        if (removed) {

            loadCourses();

            clearFields();

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Eliminado",
                    "Curso eliminado correctamente"
            );
        }
    }

    // =========================
    // SELECT COURSE
    // =========================

    private void selectCourse(Course course) {

        selectedCourse = course;

        txtId.setText(course.getId());

        txtName.setText(course.getName());

        txtCredits.setText(
                String.valueOf(course.getCredits())
        );

        cmbStatus.setValue(course.getStatus());

        Career career =
                careerData.findCareerById(
                        course.getCareerId()
                );

        cmbCareer.setValue(career);
        // Buscar profesor por username en lugar de ID
        if (course.getProfessorId() != null && !course.getProfessorId().isEmpty()) {
            Professor professor = professorData.findProfessorByUsername(course.getProfessorId()); // ← NUEVO MÉTODO
            cmbProfessor.setValue(professor);
        } else {
            cmbProfessor.setValue(null);
        }

        // Cargar nuevos campos
        if (spnSemestre != null) {
            spnSemestre.getValueFactory().setValue(course.getSemestre());
        }

        // Cargar prerequisito

        if (!course.getPrerequisitosIds().isEmpty()) {

            try {

                Course pre =
                        buscarCursoById(
                                course.getPrerequisitosIds().get(0)
                        );

                cmbPrerequisitos.setValue(pre);

            } catch (Exception e) {

                e.printStackTrace();
            }

        } else {

            cmbPrerequisitos.setValue(null);
        }


        // Cargar corequisito

        if (!course.getCorequisitosIds().isEmpty()) {

            try {

                Course co =
                        buscarCursoById(
                                course.getCorequisitosIds().get(0)
                        );

                cmbCorequisitos.setValue(co);

            } catch (Exception e) {

                e.printStackTrace();
            }

        } else {

            cmbCorequisitos.setValue(null);
        }
    }

    // =========================
    // BUSCAR CURSO POR ID
    // =========================
    private Course buscarCursoById(String id) throws Exception {
        List<Course> todos = courseData.getAllCourses().toList();
        for (Course c : todos) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    // =========================
    // CLEAR
    // =========================

    @FXML
    private void clearFields() {

        txtId.clear();
        txtName.clear();
        txtCredits.clear();

        cmbStatus.setValue(null);

        if (spnSemestre != null) {
            spnSemestre.getValueFactory().setValue(1);
        }

        cmbCareer.setValue(null);
        cmbProfessor.setValue(null);

        if (cmbPrerequisitos != null) {
            cmbPrerequisitos.setValue(null);
        }

        if (cmbCorequisitos != null) {
            cmbCorequisitos.setValue(null);
        }

        selectedCourse = null;
    }

    // =========================
    // ALERT
    // =========================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {

        Alert alert =
                new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    public void loadProfessors() {
        if (cmbProfessor == null) {
            System.err.println("cmbProfessor es null, no se puede cargar profesores");
            return;
        }

        try {
            List<Professor> professors = professorData.getAllProfessors().toList();

            ObservableList<Professor> professorObservableList = FXCollections.observableArrayList(professors);

            cmbProfessor.setItems(professorObservableList);

            if (cmbProfessor.getCellFactory() == null ||
                    cmbProfessor.getCellFactory().toString().contains("ListView$")) {
                cmbProfessor.setCellFactory(lv -> new ListCell<Professor>() {
                    @Override
                    protected void updateItem(Professor item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("");
                        } else {
                            setText(item.getId() + " - " + item.getName());
                        }
                    }
                });

                cmbProfessor.setButtonCell(new ListCell<Professor>() {
                    @Override
                    protected void updateItem(Professor item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("");
                        } else {
                            setText(item.getId() + " - " + item.getName());
                        }
                    }
                });
            }

            if (professors.isEmpty()) {
                cmbProfessor.setPlaceholder(new Label("No hay profesores disponibles"));
            } else {
                cmbProfessor.setPlaceholder(new Label("Seleccione un profesor"));
            }

            System.out.println("Profesores cargados: " + professors.size());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,
                    "Error al cargar profesores",
                    "No se pudieron cargar los profesores: " + e.getMessage());
        }
    }

    public void refreshProfessors() {
        loadProfessors();
    }


}