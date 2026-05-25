package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.entities.Role;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.services.SessionHistoryService;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;

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

    private User currentUser;

    private final SessionHistoryService historyService =
            SessionHistoryService.getInstance();

    private boolean ignoreTabChange = false;

    @Override
    public void initialize(
            URL url,
            ResourceBundle resourceBundle
    ) {

        // =========================
        // HISTORIAL TABS
        // =========================

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

        // =========================
        // TAB EXPEDIENTE
        // =========================

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
    // SET USER
    // =========================

    public void setUser(User user) {

        this.currentUser = user;

        applyAccessByRole();

        loadCourseView();
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

                // Eliminar Matrícula
                mainTabs.getTabs().remove(2);

                // Eliminar Estudiantes
                mainTabs.getTabs().remove(0);

                // Seleccionar Cursos
                mainTabs.getSelectionModel().select(0);
            }
        }
    }
    // =========================
// LOAD COURSES VIEW
// =========================

    private void loadCourseView() {

        try {

            FXMLLoader loader;

            // =========================
            // STUDENT
            // =========================

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

            }

            // =========================
            // ADMIN / OTROS
            // =========================

            else {

                loader =
                        new FXMLLoader(
                                getClass().getResource(
                                        "/views/course-view.fxml"
                                )
                        );
            }

            Parent view = loader.load();

            // =========================
            // CONTROLLER STUDENT
            // =========================

            if (
                    currentUser.getRole()
                            == Role.STUDENT
            ) {

                StudentCourseController controller =
                        loader.getController();

                controller.setUser(currentUser);

            }

            // =========================
            // CONTROLLER ADMIN
            // =========================

            else {

                CourseController controller =
                        loader.getController();

                controller.setUser(currentUser);
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

                RecordController controller =
                        loader.getController();

                controller.setUser(currentUser);

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