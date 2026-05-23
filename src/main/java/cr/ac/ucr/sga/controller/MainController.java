package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.entities.Role;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.services.SessionHistoryService;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private BorderPane rootPane;

    private User currentUser;

    @FXML
    private TabPane mainTabs;

    private final SessionHistoryService historyService = SessionHistoryService.getInstance();
    private boolean ignoreTabChange = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Registrar historial de pestañas
        mainTabs.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            if (!ignoreTabChange && newIndex != null) {
                try {
                    historyService.addTabIndex(newIndex.intValue());
                } catch (ListException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        System.out.println("Sistema iniciado correctamente");
        try {
            historyService.addTabIndex(mainTabs.getSelectionModel().getSelectedIndex());
        } catch (ListException e) {
            throw new RuntimeException(e);
        }

        mainTabs.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null && "Expediente".equals(newTab.getText()) && currentUser != null) {
                try {
                    AnchorPane contenido = (AnchorPane) newTab.getContent();
                    if (contenido.getChildren().isEmpty()) {
                        FXMLLoader expedienteLoader = new FXMLLoader(getClass().getResource("/views/academic-record-view.fxml"));
                        Parent expediente = expedienteLoader.load();
                        RecordController recordController = expedienteLoader.getController();
                        recordController.setUser(currentUser);
                        contenido.getChildren().add(expediente);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // ========== SET USER ==========
    public void setUser(User user) {
        this.currentUser = user;
        applyAccessByRole();
    }

    // ========== CONTROL DE ACCESO POR ROL ==========

    private void applyAccessByRole() {
        if (mainTabs != null && currentUser != null) {
            if (currentUser.getRole() == Role.STUDENT) {
                mainTabs.getTabs().get(0).setDisable(true); // Ocultar Tab: Estudiantes para estudiante
            }
        }
    }

    // ========== NAVEGACIÓN HISTORIAL ==========
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

    @FXML
    private void logout() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/views/login-view.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage =
                    (Stage) rootPane.getScene().getWindow();

            Scene scene = new Scene(root);

            stage.setScene(scene);

            stage.setTitle("Login");

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}