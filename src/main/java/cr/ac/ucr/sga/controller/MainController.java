package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.entities.Role;
import cr.ac.ucr.sga.model.entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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

    @Override
    public void initialize(
            URL url,
            ResourceBundle resourceBundle
    ) {

        System.out.println(
                "Sistema iniciado correctamente"
        );

        loadStudentView();
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
    // LOAD STUDENT VIEW
    // =====================================================

    @FXML
    public void loadStudentView() {

        try {

            Parent view =
                    FXMLLoader.load(
                            getClass().getResource(
                                    "/views/student-view.fxml"
                            )
                    );

            rootPane.setCenter(view);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // LOAD COURSE VIEW
    // =====================================================

    @FXML
    public void loadCourseView() {

        try {

            Parent view =
                    FXMLLoader.load(
                            getClass().getResource(
                                    "/views/course-view.fxml"
                            )
                    );

            rootPane.setCenter(view);

        } catch (Exception e) {

            e.printStackTrace();
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
}