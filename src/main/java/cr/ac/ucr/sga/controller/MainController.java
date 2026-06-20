package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.StudentData;
import cr.ac.ucr.sga.model.entities.Role;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.services.NotificationObserver;
import cr.ac.ucr.sga.model.services.NotificationService;
import cr.ac.ucr.sga.model.services.SessionHistoryService;
import cr.ac.ucr.sga.model.services.NotificationObserver;
import cr.ac.ucr.sga.model.services.NotificationRepository;
import cr.ac.ucr.sga.model.entities.Notification;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, NotificationObserver {
    @FXML private BorderPane rootPane;
    @FXML private TabPane mainTabs;
    @FXML private Label lblStudent;
    @FXML
    private ListView<String> lstNotifications;


    @FXML private Tab studentTab;
    @FXML private Tab preMatriculaTab;
    @FXML private Tab matriculaEstudianteTab;
    @FXML private Tab reviewStudentTab;
    @FXML private Tab tramiteTab;
    @FXML
    private Tab notificationTab;

    @FXML private AnchorPane studentContent;
    @FXML private AnchorPane coursesContent;
    @FXML private AnchorPane preMatriculaContent;
    @FXML private AnchorPane matriculaEstudianteContent;
    @FXML private AnchorPane expedienteContent;

    // =========================
    // CONTROLLERS / MODELS
    // =========================
    private User currentUser;
    private RecordController recordController;
    private CourseController courseController;
    private StudentController studentController;
    private TramiteReviewController tramiteReviewController;
    @FXML private Tab reportsTab;
    @FXML private AnchorPane reportsContent;

    private ReportsController reportsController;

    // =========================
    // SERVICES / STATE
    // =========================
    private final SessionHistoryService historyService = SessionHistoryService.getInstance();
    private boolean ignoreTabChange = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mainTabs.getSelectionModel()
                .selectedIndexProperty()
                .addListener((obs, oldIndex, newIndex) -> {

                    if (!ignoreTabChange && newIndex != null) {

                        try {
                            historyService.addTabIndex(newIndex.intValue());

                        } catch (ListException e) {
                            e.printStackTrace();
                        }
                    }
                });

        mainTabs.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldTab, newTab) -> {

                    if (newTab != null
                            && "Expediente".equals(newTab.getText())
                            && currentUser != null) {

                        loadRecordView();
                    }
                });

        NotificationService.getInstance()
                .addObserver(this);


    }

    // =========================
    // GETTERS DE CONTROLADORES
    // =========================
    public RecordController getRecordController() {
        return recordController;
    }

    public CourseController getCourseController() {
        return courseController;
    }

    public StudentController getStudentController() {
        return studentController;
    }

    public TramiteReviewController getTramiteReviewController() {
        return tramiteReviewController;
    }


    // =========================
    // CONFIGURACIÓN DE USUARIO
    // =========================
    public void setUser(User user) {
        this.currentUser = user;

        applyAccessByRole();
        loadCourseView();
        loadStudentView();
        loadEnrollmentStudentView();
        loadEnrollmentView();
        loadTramiteView();
        loadNotifications();
        mostrarNombreEstudianteSiCorresponde();
        loadReportsView();
    }

    private void loadReportsView() {
        if (reportsContent == null || currentUser == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/reports-view.fxml"));
            Parent view = loader.load();

            reportsController = loader.getController();
            reportsController.setMainController(this);

            setFullAnchor(view, reportsContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // CONTROL DE ACCESO POR ROL
    // =========================
    private void applyAccessByRole() {
        if (mainTabs == null || currentUser == null) {
            return;
        }

        if (currentUser.getRole() == Role.STUDENT) {
            removeTab(studentTab);
            removeTab(reviewStudentTab);
            removeTab(reportsTab);
            mainTabs.getSelectionModel().select(0);
            return;
        }

        removeTab(preMatriculaTab);
        removeTab(matriculaEstudianteTab);

        if (currentUser.getRole() == Role.ADMIN) {
            removeTab(reviewStudentTab);
            removeTab(reportsTab); //
        }
        if (currentUser.getRole() == Role.PROFESSOR) {
            removeTab(reviewStudentTab);

        }
    }

    // =========================
    // UTILIDADES DE UI
    // =========================
    private void removeTab(Tab tab) {
        if (tab != null) {
            mainTabs.getTabs().remove(tab);
        }
    }

    private void setFullAnchor(Parent view, AnchorPane container) {
        if (container == null) {
            return;
        }

        container.getChildren().clear();
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        container.getChildren().add(view);
    }

    // =========================
    // CARGA DE VISTAS
    // =========================
    private void loadCourseView() {
        try {
            FXMLLoader loader;

            if (currentUser.getRole() == Role.STUDENT) {
                loader = new FXMLLoader(getClass().getResource("/views/student-course-view.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/views/course-view.fxml"));
            }

            Parent view = loader.load();

            if (currentUser.getRole() == Role.STUDENT) {
                StudentCourseController controller = loader.getController();
                controller.setUser(currentUser);
            } else {
                courseController = loader.getController();
                courseController.setUser(currentUser);
                courseController.setMainController(this);
            }

            setFullAnchor(view, coursesContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadStudentView() {
        if (studentContent == null || currentUser.getRole() == Role.STUDENT) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/student-view.fxml"));
            Parent view = loader.load();

            studentController = loader.getController();
            studentController.setMainController(this);

            setFullAnchor(view, studentContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTramiteView() {
        try {
            FXMLLoader tramiteLoader;
            Parent tramiteView;

            if (currentUser.getRole() == Role.STUDENT) {
                // Vista para estudiante: enviar trámites
                tramiteLoader = new FXMLLoader(getClass().getResource("/views/tramite-student-view.fxml"));
                tramiteView = tramiteLoader.load();

                TramiteStudentController controller = tramiteLoader.getController();
                Student student = new StudentData().findByUsername(currentUser.getUsername());
                if (student != null) {
                    controller.setEstudiante(student);
                    controller.setMainController(this);
                }
            } else {
                // Vista para admin: gestionar/procesar trámites
                tramiteLoader = new FXMLLoader(getClass().getResource("/views/tramite-view.fxml"));
                tramiteView = tramiteLoader.load();

                tramiteReviewController = tramiteLoader.getController();
            }

            // Agregar vista al tab
            if (tramiteTab != null && mainTabs.getTabs().contains(tramiteTab)) {
                tramiteTab.setContent(tramiteView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   //======================
    // CARGAR VISTAS DE PRE-MATRICULA Y MATRICULA ESTUDIANTE
    // =========================
    private void loadEnrollmentStudentView() {
        if (preMatriculaContent == null || currentUser.getRole() != Role.STUDENT) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/enrollment-student-view.fxml"));
            Parent view = loader.load();

            EnrollmentStudentController controller = loader.getController();
            Student student = new StudentData().findByUsername(currentUser.getUsername());
            if (student != null) {
                controller.setStudent(student);
            }

            setFullAnchor(view, preMatriculaContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadEnrollmentView() {
        if (matriculaEstudianteContent == null || currentUser.getRole() != Role.STUDENT) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/enrollment-view.fxml"));
            Parent view = loader.load();

            EnrollmentController controller = loader.getController();
            Student student = new StudentData().findByUsername(currentUser.getUsername());
            if (student != null) {
                controller.setStudent(student);
                controller.setMainController(this);
            }

            setFullAnchor(view, matriculaEstudianteContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRecordView() {
        if (expedienteContent == null || !expedienteContent.getChildren().isEmpty()) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/academic-record-view.fxml"));
            Parent view = loader.load();

            recordController = loader.getController();
            recordController.setUser(currentUser);

            setFullAnchor(view, expedienteContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // =========================
    // NAVEGACIÓN ENTRE PESTAÑAS (Historial)
    // =========================
    @FXML
    public void goBack() {
        try {
            Integer prevIndex = historyService.backTab();
            if (prevIndex != null) {
                ignoreTabChange = true;
                mainTabs.getSelectionModel().select(prevIndex);
                ignoreTabChange = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goForward() {
        try {
            Integer nextIndex = historyService.forwardTab();
            if (nextIndex != null) {
                ignoreTabChange = true;
                mainTabs.getSelectionModel().select(nextIndex);
                ignoreTabChange = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // LOGOUT
    // =========================
    @FXML
    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) rootPane.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // ESTUDIANTE
    // =========================
    private void mostrarNombreEstudianteSiCorresponde() {
        if (lblStudent != null && currentUser != null && currentUser.getRole() == Role.STUDENT) {
            Student estudiante = new StudentData().findByUsername(currentUser.getUsername());
            if (estudiante != null) {
                lblStudent.setText("Estudiante: " + estudiante.getName() + " (" + estudiante.getCarnet() + ")");
            } else {
                lblStudent.setText("Estudiante en sesion");
            }
            lblStudent.setVisible(true);
        } else if (lblStudent != null) {
            lblStudent.setVisible(false);
        }
    }

    @Override
    public void onNotification(String studentId, String message) {

        Student student =
                new StudentData()
                        .findByUsername(currentUser.getUsername());

        if(student == null){
            return;
        }

        if(!student.getId().equals(studentId)){
            return;
        }

        NotificationRepository repo =
                NotificationRepository.getInstance();


        if(repo.exists(studentId, message)) {
            return;
        }

        Notification nueva = new Notification(studentId, message);
        repo.addNotification(nueva);

        Platform.runLater(() -> {
            String texto = nueva.getFecha() + " - " + nueva.getMensaje();
            if (!lstNotifications.getItems().contains(texto)) {
                lstNotifications.getItems().add(texto);
            }
        });
    }

    private void loadNotifications() {

        if (lstNotifications == null || currentUser == null) {
            return;
        }

        lstNotifications.getItems().clear();

        System.out.println("USERNAME = " + currentUser.getUsername());


        Student student =
                new StudentData()
                        .findByUsername(currentUser.getUsername());

        if (student == null) {
            return;
        }

        System.out.println("ID = " + student.getId());


        String studentId = student.getId();

        for (Notification n : NotificationRepository.getInstance().getNotifications()) {

            if (n.getStudentId().equals(studentId)) {

                lstNotifications.getItems().add(
                        n.getFecha() + " - " + n.getMensaje()
                );
            }
        }
    }
}
