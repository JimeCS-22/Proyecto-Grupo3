package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CourseProfessorController implements Initializable {

    @FXML
    private FlowPane coursesContainer;

    private final CourseData courseData = new CourseData();
    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización
    }

    public void setUser(User user) {
        this.currentUser = user;
        System.out.println("👤 Profesor logueado: " + user.getUsername());
        loadCourses();
    }

    private void loadCourses() {
        try {
            coursesContainer.getChildren().clear();

            String username = currentUser.getUsername();
            System.out.println("🔍 Buscando cursos para profesor: " + username);

            // Obtener solo los cursos asignados a este profesor
            DoublyLinkedList<Course> courses =
                    courseData.getCoursesByProfessor(username);

            if (courses == null || courses.isEmpty()) {
                System.out.println("⚠️ No hay cursos asignados para: " + username);
                // Mostrar mensaje en la UI
                showNoCoursesMessage();
                return;
            }

            System.out.println("✅ Cursos encontrados: " + courses.size());

            for (int i = 1; i <= courses.size(); i++) {
                Course course = courses.get(i);
                System.out.println("  - " + course.getId() + ": " + course.getName());

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/views/components/course-card.fxml")
                );

                Parent card = loader.load();

                CourseCardController controller = loader.getController();
                controller.setCourse(course);

                coursesContainer.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Error al cargar los cursos");
        }
    }

    private void showNoCoursesMessage() {
        // Mostrar mensaje de que no tiene cursos asignados
        Label label = new Label("No tiene cursos asignados");
        label.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
        coursesContainer.getChildren().add(label);
    }

    private void showErrorMessage(String message) {
        Label label = new Label(message);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #ff4444;");
        coursesContainer.getChildren().add(label);
    }
}