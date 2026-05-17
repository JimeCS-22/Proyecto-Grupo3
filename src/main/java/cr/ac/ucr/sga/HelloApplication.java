package cr.ac.ucr.sga;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        loadLogin(stage);
    }

    private void loadLogin(Stage stage)
            throws IOException {

        FXMLLoader loader =
                new FXMLLoader(
                        HelloApplication.class.getResource(
                                "/views/login-view.fxml"
                        )
                );

        Scene scene =
                new Scene(
                        loader.load(),
                        900,
                        600
                );

        scene.getStylesheets().add(
                HelloApplication.class
                        .getResource("/styles.css")
                        .toExternalForm()
        );

        stage.setTitle(
                "Sistema de Gestión Académica"
        );

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}