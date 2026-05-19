package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.entities.Role;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.services.SessionHistoryService;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button btnStudents;

    @FXML
    private Button btnCourses;

    @FXML
    private Button btnAcademic;

    @FXML
    private Button btnUsers;

    private User currentUser;

    @FXML
    private TabPane mainTabs;

    private final SessionHistoryService historyService = SessionHistoryService.getInstance();
    private boolean ignoreTabChange = false;

    @Override
    public void initialize(
            URL url,
            ResourceBundle resourceBundle
    ) {

        mainTabs.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            if (!ignoreTabChange && newIndex != null) {
                try {
                    historyService.addTabIndex(newIndex.intValue());
                } catch (ListException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        System.out.println(
                "Sistema iniciado correctamente"
        );
        try {
            historyService.addTabIndex(mainTabs.getSelectionModel().getSelectedIndex());
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
    }

    // =====================================================
    // SET USER
    // =====================================================

    public void setUser(User user) {

        this.currentUser = user;

        configurePermissions();
    }

    // =====================================================
    // CONFIGURAR PERMISOS
    // =====================================================

    private void configurePermissions() {

        if (currentUser == null) {
            return;
        }

        // ADMIN
        if (currentUser.getRole() == Role.ADMIN) {

            btnStudents.setVisible(true);
            btnCourses.setVisible(true);
            btnAcademic.setVisible(true);
            btnUsers.setVisible(true);
        }

        // PROFESSOR
        else if (
                currentUser.getRole()
                        == Role.PROFESSOR
        ) {

            btnStudents.setVisible(true);
            btnCourses.setVisible(true);

            btnAcademic.setVisible(true);

            btnUsers.setVisible(false);
        }

        // STUDENT
        else if (
                currentUser.getRole()
                        == Role.STUDENT
        ) {

            btnStudents.setVisible(false);

            btnCourses.setVisible(true);

            btnAcademic.setVisible(true);

            btnUsers.setVisible(false);
        }
    }


    // =====================================================
    // LOAD ACADEMIC VIEW
    // =====================================================

    @FXML
    public void loadAcademicView() {

        System.out.println(
                "Vista académica próximamente..."
        );
    }

    // =====================================================
    // LOAD USERS VIEW
    // =====================================================

    @FXML
    public void loadUsersView() {

        System.out.println(
                "Vista de usuarios próximamente..."
        );
    }

    // =====================================================
    // SESSION HISTORY SERVICES
    // =====================================================

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

}