package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.StudentData;
import cr.ac.ucr.sga.model.entities.Role;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.services.SessionHistoryService;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private BorderPane rootPane;

    @FXML
    private TabPane mainTabs;

    @FXML
    private AnchorPane expedienteContent;

    @FXML
    private AnchorPane coursesContent;

    @FXML
    private AnchorPane enrollmentContent;

    @FXML
    private Tab enrollmentStudentTab;

    @FXML
    private AnchorPane studentContent;

    @FXML
    private AnchorPane enrollmentStudentContent;

    @FXML
    private Tab reviewStudentTab;

    private User currentUser;

    private final SessionHistoryService historyService =
            SessionHistoryService.getInstance();

    private boolean ignoreTabChange = false;

    // =========================
    // CONTROLLERS
    // =========================

    private EnrollmentController enrollmentController;

    private RecordController recordController;

    private CourseController courseController;

    private StudentController studentController;

    private EnrollmentStudentController enrollmentStudentController;

    @Override
    public void initialize(
            URL url,
            ResourceBundle resourceBundle
    ) {

        mainTabs.getSelectionModel()
                .selectedIndexProperty()
                .addListener((obs, oldIndex, newIndex) -> {

                    if (
                            !ignoreTabChange
                                    && newIndex != null
                    ) {

                        try {

                            historyService.addTabIndex(
                                    newIndex.intValue()
                            );

                        } catch (ListException e) {

                            e.printStackTrace();
                        }
                    }
                });

        mainTabs.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldTab, newTab) -> {

                    if (
                            newTab != null
                                    &&
                                    "Expediente".equals(
                                            newTab.getText()
                                    )
                                    &&
                                    currentUser != null
                    ) {

                        loadRecordView();
                    }
                });
    }

    // =========================
    // GETTERS
    // =========================

    public EnrollmentController getEnrollmentController() {
        return enrollmentController;
    }

    public RecordController getRecordController() {
        return recordController;
    }

    public CourseController getCourseController() {
        return courseController;
    }

    public StudentController getStudentController() {
        return studentController;
    }

    // =========================
    // SET USER
    // =========================

    public void setUser(User user) {

        this.currentUser = user;

        applyAccessByRole();

        loadCourseView();

        loadEnrollmentView();

        loadStudentView();

        loadEnrollmentStudent();

    }

    // =========================
    // CONTROL ACCESO
    // =========================

    private void applyAccessByRole() {

        if (
                mainTabs != null
                        &&
                        currentUser != null
        ) {

            if (
                    currentUser.getRole()
                            == Role.STUDENT
            ) {

                mainTabs.getTabs().remove(2);

                mainTabs.getTabs().remove(0);

                mainTabs.getSelectionModel().select(0);
            }
            if (currentUser.getRole() != Role.ADMIN) {
                mainTabs.getTabs().remove(reviewStudentTab);
            }
        }
    }

    // =========================
    // LOAD COURSE VIEW
    // =========================

    private void loadCourseView() {

        try {

            FXMLLoader loader;

            if (
                    currentUser.getRole()
                            == Role.STUDENT
            ) {

                loader =
                        new FXMLLoader(
                                getClass().getResource(
                                        "/views/student-course-view.fxml"
                                )
                        );

            } else {

                loader =
                        new FXMLLoader(
                                getClass().getResource(
                                        "/views/course-view.fxml"
                                )
                        );
            }

            Parent view = loader.load();

            if (
                    currentUser.getRole()
                            == Role.STUDENT
            ) {

                StudentCourseController controller =
                        loader.getController();

                controller.setUser(currentUser);

            } else {

                courseController =
                        loader.getController();

                courseController.setUser(currentUser);

                courseController.setMainController(this);
            }

            coursesContent.getChildren().clear();

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

            coursesContent.getChildren().add(view);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // LOAD RECORD VIEW
    // =========================

    private void loadRecordView() {

        try {

            if (
                    expedienteContent.getChildren()
                            .isEmpty()
            ) {

                FXMLLoader loader =
                        new FXMLLoader(
                                getClass().getResource(
                                        "/views/academic-record-view.fxml"
                                )
                        );

                Parent view = loader.load();

                recordController =
                        loader.getController();

                recordController.setUser(currentUser);

                AnchorPane.setTopAnchor(view, 0.0);
                AnchorPane.setBottomAnchor(view, 0.0);
                AnchorPane.setLeftAnchor(view, 0.0);
                AnchorPane.setRightAnchor(view, 0.0);

                expedienteContent
                        .getChildren()
                        .add(view);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // LOAD ENROLLMENT VIEW
    // =========================

    private void loadEnrollmentView() {

        try {

            if (enrollmentContent != null) {

                FXMLLoader loader =
                        new FXMLLoader(
                                getClass().getResource(
                                        "/views/enrollment-view.fxml"
                                )
                        );

                Parent view = loader.load();

                enrollmentController =
                        loader.getController();

                AnchorPane.setTopAnchor(view, 0.0);
                AnchorPane.setBottomAnchor(view, 0.0);
                AnchorPane.setLeftAnchor(view, 0.0);
                AnchorPane.setRightAnchor(view, 0.0);

                enrollmentContent.getChildren().clear();

                enrollmentContent
                        .getChildren()
                        .add(view);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // LOAD STUDENT VIEW
    // =========================

    private void loadStudentView() {

        try {

            if (studentContent != null) {

                FXMLLoader loader =
                        new FXMLLoader(
                                getClass().getResource(
                                        "/views/student-view.fxml"
                                )
                        );

                Parent view = loader.load();

                studentController =
                        loader.getController();

                studentController.setMainController(this);

                AnchorPane.setTopAnchor(view, 0.0);
                AnchorPane.setBottomAnchor(view, 0.0);
                AnchorPane.setLeftAnchor(view, 0.0);
                AnchorPane.setRightAnchor(view, 0.0);

                studentContent.getChildren().clear();

                studentContent
                        .getChildren()
                        .add(view);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // LOAD ENROLLMENT-STUDENT VIEW
    // =========================

    private void loadEnrollmentStudent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/enrollment-student-view.fxml"));
            Parent view = loader.load();
            EnrollmentStudentController controller = loader.getController();

            if (currentUser != null && currentUser.getRole() == Role.STUDENT) {
                Student student = new StudentData().findByUsername(currentUser.getUsername());
                if (student != null) {
                    controller.setStudent(student);
                    System.out.println("Estableciendo el estudiante en EnrollmentStudentController: " + student);
                }
            }

            enrollmentStudentContent.getChildren().clear();
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

            enrollmentStudentContent.getChildren().add(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // BACK
    // =========================

    @FXML
    public void goBack() {

        try {

            Integer prevIndex =
                    historyService.backTab();

            if (prevIndex != null) {

                ignoreTabChange = true;

                mainTabs.getSelectionModel()
                        .select(prevIndex);

                ignoreTabChange = false;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // FORWARD
    // =========================

    @FXML
    public void goForward() {

        try {

            Integer nextIndex =
                    historyService.forwardTab();

            if (nextIndex != null) {

                ignoreTabChange = true;

                mainTabs.getSelectionModel()
                        .select(nextIndex);

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

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/views/login-view.fxml"
                            )
                    );

            Parent root = loader.load();

            Stage stage =
                    (Stage)
                            rootPane.getScene()
                                    .getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.setTitle("Login");

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}