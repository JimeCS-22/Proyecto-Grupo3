package cr.ac.ucr.sga;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        star2(stage);
    }

    private void star2(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/fxml/course.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 720);
        scene.getStylesheets().add(HelloApplication.class.getResource("/styles.css").toExternalForm());
        stage.setTitle("Proyecto Final IF-3001 Algoritmos y Estructuras de Datos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
