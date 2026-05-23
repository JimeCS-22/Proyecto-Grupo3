package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.HelloApplication;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.services.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblStatus;

    private final AuthService authService =
            new AuthService();

    @FXML
    public void onLogin() {

        String username =
                txtUsername.getText();

        String password =
                txtPassword.getText();

        User user =
                authService.login(
                        username,
                        password
                );

        if (user != null) {

            try {

                FXMLLoader loader =
                        new FXMLLoader(
                                HelloApplication.class.getResource(
                                        "/views/main-view.fxml"
                                )
                        );

                Scene scene =
                        new Scene(
                                loader.load(),
                                1100,
                                720
                        );
                MainController mainController = loader.getController();
                mainController.setUser(user);

                scene.getStylesheets().add(
                        HelloApplication.class
                                .getResource("/styles.css")
                                .toExternalForm()
                );

                Stage stage =
                        (Stage)
                                txtUsername
                                        .getScene()
                                        .getWindow();

                stage.setScene(scene);

                stage.show();

            } catch (Exception e) {

                lblStatus.setText(
                        "Error al abrir el sistema"
                );

                e.printStackTrace();
            }

        } else {

            lblStatus.setText(
                    "Usuario o contraseña incorrectos"
            );
        }
    }
}