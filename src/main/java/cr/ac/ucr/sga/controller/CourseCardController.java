package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.entities.Course;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class CourseCardController {

    @FXML
    private ImageView imgCourse;

    @FXML
    private Label lblName;

    @FXML
    private Label lblCode;

    @FXML
    private Label lblCredits;

    @FXML
    private Label lblStatus;

    private Course course;

    private final String[] images = {
            "/images/course-card1.png",
            "/images/course-card2.png",
            "/images/course-card3.png",
            "/images/course-card4.png"
    };

    public void setCourse(Course course) {

        this.course = course;

        lblName.setText(course.getName());

        lblCode.setText(course.getId());

        lblCredits.setText(
                course.getCredits() + " créditos"
        );

        lblStatus.setText(course.getStatus());

        Random random = new Random();

        int index = random.nextInt(images.length);

        Image image = new Image(
                getClass().getResourceAsStream(images[index])
        );

        imgCourse.setImage(image);
    }

    @FXML
    private void showDetails() {

        String prereqs = "Ninguno";
        if (course.getPrerequisitosIds() != null && !course.getPrerequisitosIds().isEmpty()) {
            prereqs = String.join(", ", course.getPrerequisitosIds());
        }

        String coreqs = "Ninguno";
        if (course.getCorequisitosIds() != null && !course.getCorequisitosIds().isEmpty()) {
            coreqs = String.join(", ", course.getCorequisitosIds());
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles del Curso");
        alert.setHeaderText(course.getName());
        alert.setContentText(
                "Código: " + course.getId() + "\n" +
                        "Créditos: " + course.getCredits() + "\n" +
                        "Estado: " + course.getStatus() + "\n" +
                        "Pre-requisitos: " + prereqs + "\n" +
                        "Co-requisitos: " + coreqs
        );

        alert.showAndWait();
    }

}