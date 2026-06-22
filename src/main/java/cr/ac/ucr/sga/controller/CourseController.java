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
    @FXML private FlowPane coursesContainer;
    @FXML private TextField txtSearch;
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtCredits;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private Spinner<Integer> spnSemestre;
    @FXML private ComboBox<Course> cmbPrerequisitos;
    @FXML private ComboBox<Career> cmbCareer;
    @FXML private Button btnAgregarPrerequisito;
    @FXML private ListView<String> lstPrerequisitos;
    @FXML private ComboBox<Course> cmbCorequisitos;
    @FXML private Button btnAgregarCorequisito;
    @FXML private ListView<String> lstCorequisitos;
    @FXML private ComboBox<Professor> cmbProfessor;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;

    private final CourseData courseData = new CourseData();
    private final AcademicRecordData recordData = new AcademicRecordData();
    private final CareerData careerData = new CareerData();
    private final ProfessorData professorData = new ProfessorData();
    private Course selectedCourse;
    private MainController mainController;

    private final ObservableList<String> prerequisitosSeleccionados = FXCollections.observableArrayList();
    private final ObservableList<String> corequisitosSeleccionados = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (cmbStatus != null) {
            cmbStatus.getItems().addAll("Activo", "Inactivo");
        }

        if (spnSemestre != null) {
            spnSemestre.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 1));
        }

        cmbCareer.getItems().addAll(careerData.getAllCareers().toList());
        cmbCareer.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Career career, boolean empty) {
                super.updateItem(career, empty);
                setText(empty || career == null ? "" : career.getName());
            }
        });
        cmbCareer.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Career career, boolean empty) {
                super.updateItem(career, empty);
                setText(empty || career == null ? "" : career.getName());
            }
        });

        loadProfessors();

        if (lstPrerequisitos != null) lstPrerequisitos.setItems(prerequisitosSeleccionados);
        if (lstCorequisitos != null) lstCorequisitos.setItems(corequisitosSeleccionados);

        if (btnAgregarPrerequisito != null) btnAgregarPrerequisito.setOnAction(e -> agregarPrerequisito());
        if (btnAgregarCorequisito != null) btnAgregarCorequisito.setOnAction(e -> agregarCorequisito());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setUser(User user) {
        this.currentUser = user;
        if (user.getRole() == Role.STUDENT) {
            ocultarCRUD();
            loadStudentCourses();
        } else {
            loadCourses();
        }
    }

    private void loadCourses() {
        coursesContainer.getChildren().clear();
        try {
            for (Course course : courseData.getAllCourses().toList()) {
                addCourseCard(course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        actualizarCombosRequisitos();
    }

    private void loadStudentCourses() {
        coursesContainer.getChildren().clear();
        try {
            AcademicRecord record = recordData.findByUsername(currentUser.getUsername());
            if (record != null) {
                DoublyLinkedList<Course> courses = record.getCourses();
                for (int i = 1; i <= courses.size(); i++) {
                    addCourseCard(courses.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCourseCard(Course course) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/components/course-card.fxml"));
        Parent card = loader.load();
        CourseCardController controller = loader.getController();
        controller.setCourse(course);

        if (currentUser != null && currentUser.getRole() != Role.STUDENT) {
            card.setOnMouseClicked(event -> selectCourse(course));
        }
        coursesContainer.getChildren().add(card);
    }

    private void ocultarCRUD() {
        txtId.setVisible(false);
        txtName.setVisible(false);
        txtCredits.setVisible(false);
        cmbStatus.setVisible(false);
        btnAdd.setVisible(false);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
        btnClear.setVisible(false);
        if (spnSemestre != null) spnSemestre.setVisible(false);
        if (cmbPrerequisitos != null) cmbPrerequisitos.setVisible(false);
        if (btnAgregarPrerequisito != null) btnAgregarPrerequisito.setVisible(false);
        if (lstPrerequisitos != null) lstPrerequisitos.setVisible(false);
        if (cmbCorequisitos != null) cmbCorequisitos.setVisible(false);
        if (btnAgregarCorequisito != null) btnAgregarCorequisito.setVisible(false);
        if (lstCorequisitos != null) lstCorequisitos.setVisible(false);
    }

    private void actualizarCombosRequisitos() {
        try {
            List<Course> todos = courseData.getAllCourses().toList();
            ObservableList<Course> cursos = FXCollections.observableArrayList(todos);

            if (cmbPrerequisitos != null) {
                ObservableList<Course> cursosConVacio = FXCollections.observableArrayList();
                cursosConVacio.add(null);
                cursosConVacio.addAll(cursos);
                cmbPrerequisitos.setItems(cursosConVacio);
                cmbPrerequisitos.setCellFactory(lv -> new ListCell<Course>() {
                    @Override
                    protected void updateItem(Course c, boolean empty) {
                        super.updateItem(c, empty);
                        setText(empty || c == null ? "(Sin pre-requisito)" : c.getId() + " - " + c.getName());
                    }
                });
                cmbPrerequisitos.setButtonCell(new ListCell<Course>() {
                    @Override
                    protected void updateItem(Course c, boolean empty) {
                        super.updateItem(c, empty);
                        setText(empty || c == null ? "(Sin pre-requisito)" : c.getId() + " - " + c.getName());
                    }
                });
            }

            if (cmbCorequisitos != null) {
                ObservableList<Course> cursosConVacio = FXCollections.observableArrayList();
                cursosConVacio.add(null);
                cursosConVacio.addAll(cursos);
                cmbCorequisitos.setItems(cursosConVacio);
                cmbCorequisitos.setCellFactory(lv -> new ListCell<Course>() {
                    @Override
                    protected void updateItem(Course c, boolean empty) {
                        super.updateItem(c, empty);
                        setText(empty || c == null ? "(Sin co-requisito)" : c.getId() + " - " + c.getName());
                    }
                });
                cmbCorequisitos.setButtonCell(new ListCell<Course>() {
                    @Override
                    protected void updateItem(Course c, boolean empty) {
                        super.updateItem(c, empty);
                        setText(empty || c == null ? "(Sin co-requisito)" : c.getId() + " - " + c.getName());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void agregarPrerequisito() {
        Course curso = cmbPrerequisitos.getValue();
        if (curso == null) return;
        if (!prerequisitosSeleccionados.contains(curso.getId())) {
            prerequisitosSeleccionados.add(curso.getId() + " - " + curso.getName());
        }
        cmbPrerequisitos.setValue(null);
    }

    @FXML
    private void agregarCorequisito() {
        Course curso = cmbCorequisitos.getValue();
        if (curso == null) return;
        if (!corequisitosSeleccionados.contains(curso.getId())) {
            corequisitosSeleccionados.add(curso.getId() + " - " + curso.getName());
        }
        cmbCorequisitos.setValue(null);
    }

    @FXML
    private void addCourse() {
        try {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            if (id.isEmpty() || name.isEmpty() || txtCredits.getText().isEmpty() || cmbStatus.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Campos vacíos", "Complete todos los campos");
                return;
            }
            int credits = Integer.parseInt(txtCredits.getText());
            String status = cmbStatus.getValue();
            int semestre = spnSemestre != null ? spnSemestre.getValue() : 1;
            List<String> prerequisitos = new ArrayList<>();
            if (cmbPrerequisitos != null && cmbPrerequisitos.getValue() != null) prerequisitos.add(cmbPrerequisitos.getValue().getId());
            List<String> corequisitos = new ArrayList<>();
            if (cmbCorequisitos != null && cmbCorequisitos.getValue() != null) corequisitos.add(cmbCorequisitos.getValue().getId());
            Career career = cmbCareer.getValue();
            if (career == null) {
                showAlert(Alert.AlertType.WARNING, "Carrera", "Seleccione una carrera");
                return;
            }
            Professor professor = cmbProfessor.getValue();
            Course course = new Course.Builder().setId(id).setName(name).setCredits(credits).setStatus(status).setSemestre(semestre).setCareerId(career.getId()).setProfessorId(professor != null ? professor.getUsername() : null).setPrerequisitosIds(prerequisitos).setCorequisitosIds(corequisitos).build();
            Course added = courseData.addCourse(course);
            if (added != null) {
                loadCourses();
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Curso agregado correctamente");
            } else {
                showAlert(Alert.AlertType.WARNING, "Duplicado", "Ya existe un curso con ese ID");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Los créditos deben ser numéricos");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void updateCourse() {
        if (selectedCourse == null) {
            showAlert(Alert.AlertType.WARNING, "Sin selección", "Seleccione un curso");
            return;
        }
        try {
            List<String> prerequisitos = new ArrayList<>();
            if (cmbPrerequisitos != null && cmbPrerequisitos.getValue() != null) prerequisitos.add(cmbPrerequisitos.getValue().getId());
            List<String> corequisitos = new ArrayList<>();
            if (cmbCorequisitos != null && cmbCorequisitos.getValue() != null) corequisitos.add(cmbCorequisitos.getValue().getId());
            Career career = cmbCareer.getValue();
            if (career == null) {
                showAlert(Alert.AlertType.WARNING, "Carrera", "Seleccione una carrera");
                return;
            }
            Professor professor = cmbProfessor.getValue();
            Course updatedCourse = new Course.Builder().setId(selectedCourse.getId()).setName(txtName.getText()).setCredits(Integer.parseInt(txtCredits.getText())).setStatus(cmbStatus.getValue()).setSemestre(spnSemestre != null ? spnSemestre.getValue() : 1).setCareerId(career.getId()).setProfessorId(professor != null ? professor.getUsername() : null).setPrerequisitosIds(prerequisitos).setCorequisitosIds(corequisitos).build();
            if (courseData.updateCourse(updatedCourse)) {
                loadCourses();
                if (mainController != null) mainController.refreshCourseProfessorList();
                clearFields();
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Curso actualizado correctamente");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo actualizar");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteCourse() throws ListException {
        if (selectedCourse == null) {
            showAlert(Alert.AlertType.WARNING, "Sin selección", "Seleccione un curso");
            return;
        }
        if (courseData.removeCourse(selectedCourse.getId())) {
            loadCourses();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Eliminado", "Curso eliminado correctamente");
        }
    }

    private void selectCourse(Course course) {
        selectedCourse = course;
        txtId.setText(course.getId());
        txtName.setText(course.getName());
        txtCredits.setText(String.valueOf(course.getCredits()));
        cmbStatus.setValue(course.getStatus());
        Career career = careerData.findCareerById(course.getCareerId());
        cmbCareer.setValue(career);
        if (course.getProfessorId() != null && !course.getProfessorId().isEmpty()) {
            cmbProfessor.setValue(professorData.findProfessorByUsername(course.getProfessorId()));
        } else {
            cmbProfessor.setValue(null);
        }
        if (spnSemestre != null) spnSemestre.getValueFactory().setValue(course.getSemestre());
        if (!course.getPrerequisitosIds().isEmpty()) {
            try {
                cmbPrerequisitos.setValue(buscarCursoById(course.getPrerequisitosIds().get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            cmbPrerequisitos.setValue(null);
        }
        if (!course.getCorequisitosIds().isEmpty()) {
            try {
                cmbCorequisitos.setValue(buscarCursoById(course.getCorequisitosIds().get(0)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            cmbCorequisitos.setValue(null);
        }
    }

    private Course buscarCursoById(String id) throws Exception {
        for (Course c : courseData.getAllCourses().toList()) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    @FXML
    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtCredits.clear();
        cmbStatus.setValue(null);
        if (spnSemestre != null) spnSemestre.getValueFactory().setValue(1);
        cmbCareer.setValue(null);
        cmbProfessor.setValue(null);
        if (cmbPrerequisitos != null) cmbPrerequisitos.setValue(null);
        if (cmbCorequisitos != null) cmbCorequisitos.setValue(null);
        selectedCourse = null;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void loadProfessors() {
        if (cmbProfessor == null) return;
        try {
            List<Professor> professors = professorData.getAllProfessors().toList();
            cmbProfessor.setItems(FXCollections.observableArrayList(professors));
            cmbProfessor.setCellFactory(lv -> new ListCell<Professor>() {
                @Override
                protected void updateItem(Professor item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getId() + " - " + item.getName());
                }
            });
            cmbProfessor.setButtonCell(new ListCell<Professor>() {
                @Override
                protected void updateItem(Professor item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getId() + " - " + item.getName());
                }
            });
            cmbProfessor.setPlaceholder(new Label(professors.isEmpty() ? "No hay profesores disponibles" : "Seleccione un profesor"));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error al cargar profesores", "No se pudieron cargar: " + e.getMessage());
        }
    }

    public void refreshProfessors() {
        loadProfessors();
    }
}