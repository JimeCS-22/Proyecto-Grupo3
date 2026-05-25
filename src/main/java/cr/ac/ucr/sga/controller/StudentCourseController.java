package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentCourseController
        implements Initializable {

    @FXML
    private FlowPane coursesContainer;

    private final AcademicRecordData recordData =
            new AcademicRecordData();

    private User currentUser;

    @Override
    public void initialize(
            URL url,
            ResourceBundle resourceBundle
    ) {

    }

    public void setUser(User user) {

        this.currentUser = user;

        loadCourses();
    }

    private void loadCourses() {

        try {

            AcademicRecord record =
                    recordData.findByUsername(
                            currentUser.getUsername()
                    );

            if (record != null) {

                DoublyLinkedList<Course> courses =
                        record.getCourses();

                for (
                        int i = 1;
                        i <= courses.size();
                        i++
                ) {

                    Course course = courses.get(i);

                    FXMLLoader loader =
                            new FXMLLoader(
                                    getClass().getResource(
                                            "/views/components/course-card.fxml"
                                    )
                            );

                    Parent card = loader.load();

                    CourseCardController controller =
                            loader.getController();

                    controller.setCourse(course);

                    coursesContainer.getChildren().add(card);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
