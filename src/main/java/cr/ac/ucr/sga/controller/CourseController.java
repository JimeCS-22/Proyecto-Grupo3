package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.entities.Course;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CourseController implements Initializable {

    // =========================
    // FXML
    // =========================

    @FXML
    private FlowPane coursesContainer;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtCredits;

    @FXML
    private ComboBox<String> cmbStatus;

    // =========================
    // DATA
    // =========================

    private final CourseData courseData = new CourseData();

    private Course selectedCourse;

    // =========================
    // INITIALIZE
    // =========================

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (cmbStatus != null) {

            cmbStatus.getItems().addAll(
                    "Activo",
                    "Inactivo"
            );
        }

        loadCourses();
    }

    // =========================
    // LOAD COURSES
    // =========================

    private void loadCourses() {

        coursesContainer.getChildren().clear();

        try {

            for (Course course : courseData.getAllCourses()) {

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/views/components/course-card.fxml"
                        )
                );

                Parent card = loader.load();

                CourseCardController controller =
                        loader.getController();

                controller.setCourse(course);

                // CLICK SOBRE LA CARD
                card.setOnMouseClicked((MouseEvent event) -> {
                    selectCourse(course);
                });

                coursesContainer.getChildren().add(card);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // ADD COURSE
    // =========================

    @FXML
    private void addCourse() {

        try {

            String id = txtId.getText().trim();
            String name = txtName.getText().trim();

            if (id.isEmpty()
                    || name.isEmpty()
                    || txtCredits.getText().isEmpty()
                    || cmbStatus.getValue() == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Campos vacíos",
                        "Complete todos los campos"
                );

                return;
            }

            int credits =
                    Integer.parseInt(txtCredits.getText());

            String status = cmbStatus.getValue();

            Course course = new Course.Builder()
                    .setId(id)
                    .setName(name)
                    .setCredits(credits)
                    .setStatus(status)
                    .build();

            Course added =
                    courseData.addCourse(course);

            if (added != null) {

                loadCourses();

                clearFields();

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Éxito",
                        "Curso agregado correctamente"
                );

            } else {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Duplicado",
                        "Ya existe un curso con ese ID"
                );
            }

        } catch (NumberFormatException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Los créditos deben ser numéricos"
            );

        } catch (Exception e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage()
            );
        }
    }

    // =========================
    // UPDATE COURSE
    // =========================

    @FXML
    private void updateCourse() {

        if (selectedCourse == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione un curso"
            );

            return;
        }

        try {

            Course updatedCourse = new Course.Builder()
                    .setId(selectedCourse.getId())
                    .setName(txtName.getText())
                    .setCredits(
                            Integer.parseInt(txtCredits.getText())
                    )
                    .setStatus(cmbStatus.getValue())
                    .build();

            boolean updated =
                    courseData.updateCourse(updatedCourse);

            if (updated) {

                loadCourses();

                clearFields();

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Actualizado",
                        "Curso actualizado correctamente"
                );

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "No se pudo actualizar"
                );
            }

        } catch (NumberFormatException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Los créditos deben ser numéricos"
            );

        } catch (Exception e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage()
            );
        }
    }

    // =========================
    // DELETE COURSE
    // =========================

    @FXML
    private void deleteCourse() {

        if (selectedCourse == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Sin selección",
                    "Seleccione un curso"
            );

            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirm.setTitle("Confirmar");

        confirm.setHeaderText(null);

        confirm.setContentText(
                "¿Desea eliminar el curso "
                        + selectedCourse.getName()
                        + "?"
        );

        if (confirm.showAndWait().get()
                == ButtonType.OK) {

            boolean removed =
                    courseData.removeCourse(
                            selectedCourse.getId()
                    );

            if (removed) {

                loadCourses();

                clearFields();

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Eliminado",
                        "Curso eliminado correctamente"
                );

            } else {

                showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "No se pudo eliminar"
                );
            }
        }
    }

    // =========================
    // SELECT COURSE
    // =========================

    private void selectCourse(Course course) {

        selectedCourse = course;

        txtId.setText(course.getId());

        txtName.setText(course.getName());

        txtCredits.setText(
                String.valueOf(course.getCredits())
        );

        cmbStatus.setValue(course.getStatus());
    }

    // =========================
    // CLEAR FIELDS
    // =========================

    @FXML
    private void clearFields() {

        txtId.clear();

        txtName.clear();

        txtCredits.clear();

        cmbStatus.setValue(null);

        selectedCourse = null;
    }

    // =========================
    // ALERTS
    // =========================

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {

        Alert alert = new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}