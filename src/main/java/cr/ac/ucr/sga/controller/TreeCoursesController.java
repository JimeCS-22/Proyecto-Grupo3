package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.entities.Career;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import cr.ac.ucr.sga.model.structures.trees.AVL;
import cr.ac.ucr.sga.model.structures.trees.TreeException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static cr.ac.ucr.sga.model.structures.trees.TreePainter.drawTreeNode;

public class TreeCoursesController  implements Initializable {

    @javafx.fxml.FXML
    private Canvas treeCoursesCanvas;
    @javafx.fxml.FXML
    private ComboBox<Career> cbCarrera;
    @javafx.fxml.FXML
    private Button btShowTree;
    @javafx.fxml.FXML
    private ComboBox<String> cbTours;
    private final CourseData courseData = new CourseData();

    private AVL<Course> avl;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupAVL();
    }
    /// Methods Controller for AVL TREE COURSES
    private void setupAVL() {
        avl = new AVL<>();

        //config boton
        btShowTree.setOnAction(e -> runSearchAVL());
        loadCoursesInCareer();
        cbTours.getSelectionModel().selectFirst();

        cbTours.setItems(FXCollections.observableArrayList("PreOrder",
                "InOrder", "PostOrder"
        ));
        cbTours.getSelectionModel().selectFirst();

    }

    // CARGA DE CURSOS

    private void loadCoursesInCareer() {
        try {
            Career career = new Career();
            career.setName("Informática Empresarial");
            career.setCourses(courseData.getAllCourses());
            if (cbCarrera == null) return;
            cbCarrera.getItems().clear();
            int size = courseData.getAllCourses().size();
            for (int i = 1; i < size; i++) {
                cbCarrera.getItems().add(career);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void runSearchAVL() {
        try {
            Career selectedCareer = cbCarrera.getSelectionModel().getSelectedItem();
            if (selectedCareer != null) {
                buildAVLFromCareer(selectedCareer);
            } else {
                showAlert("Error", "Debe seleccionar una carrera");
            }
        } catch (Exception ex) {
            showAlert("Error", "No se pudo construir el árbol");
        }
    }
    private void buildAVLFromCareer(Career career) throws ListException {

        for (Course course : career.getCourses().toList()) {
            avl.add(course); // insertar cada curso en el árbol
        }
        drawAVL(avl); // dibujar en el canvas
    }

    private void clearAVL() {
        avl.root = null;//vaciar árbol

        GraphicsContext gc = treeCoursesCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, treeCoursesCanvas.getWidth(), treeCoursesCanvas.getHeight());
    }

    private void playTours() throws ListException, TreeException {
        String tour = cbTours.getSelectionModel().getSelectedItem();
        List<Course> recorrido = null;

        switch (tour) {
            case "PreOrder":
                recorrido = avl.preOrder();
                break;
            case "InOrder":
                recorrido = avl.inOrder();
                break;
            case "PostOrder":
                recorrido = avl.postOrder();
                break;
        }

        if (recorrido != null) {
            animateTraversal(recorrido);
        }
    }



    private void drawAVL(AVL<Course> tree) {
        // graphicContext:forma de "dibujar" es como un objeto
        GraphicsContext treeGraphic = treeCoursesCanvas.getGraphicsContext2D();
        // limpiar cada vez antes de entrar al if
        treeGraphic.clearRect(0, 0, treeCoursesCanvas.getWidth(), treeCoursesCanvas.getHeight());

        // avl tree
        if (avl.root != null) {// si hay raiz entonces permite llamar metodo drawBTreeNodes
            // getWidth()/2 para que se centrara el arbol
            if (tree.root.equals(avl.root)) {
                drawTreeNode(treeGraphic, avl.root, treeCoursesCanvas.getWidth() / 2, 40, treeCoursesCanvas.getWidth() / 4,tree, true);
            }else{
                drawTreeNode(treeGraphic, avl.root, treeCoursesCanvas.getWidth() / 2, 40, treeCoursesCanvas.getWidth() / 4,tree, false);
            }

        }

    }
/*
    private void registrarOperacion(int nodos, int altura, boolean isBalanced) {
        String texto = "Nodos: " +nodos + "| Altura: " + altura + "| Balanceado:  " + isBalanced;
        lblAvlInfo.setText(texto);
    }*/
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void animateTraversal(List<Course> recorrido) {
        Timeline timeline = new Timeline();
        int delay = 800; // milisegundos entre cada paso
        GraphicsContext gc = treeCoursesCanvas.getGraphicsContext2D();

        for (int i = 0; i < recorrido.size(); i++) {
            Course course = recorrido.get(i);
            KeyFrame frame = new KeyFrame(
                    Duration.millis(i * delay),
                    e -> {
                        // Limpia y dibuja el árbol
                        drawAVL(avl);
                        // Resalta el nodo actual
                        gc.setFill(javafx.scene.paint.Color.RED);
                        gc.fillText(">> " + course.getId(), 20, 20);
                    }
            );
            timeline.getKeyFrames().add(frame);
        }
        timeline.play();
    }

}
