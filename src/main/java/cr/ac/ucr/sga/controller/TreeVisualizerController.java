package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.graphics.TreeAnimationManager;
import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.graphics.TreeRenderer;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.trees.AVL;
import cr.ac.ucr.sga.model.structures.trees.BST;
import cr.ac.ucr.sga.model.structures.trees.BTree;
import cr.ac.ucr.sga.model.structures.trees.BTreeNode;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TreeVisualizerController {

    @FXML
    private Canvas treeCanvas;

    private TreeRenderer renderer;

    private BTree<Course> currentTree;

    private BTree<Course> binaryTree;

    private BST<Course> bstTree;

    private AVL<Course> avlTree;

    private TreeAnimationManager animator;
    @FXML
    private TextField searchField;
    @FXML
    private TextArea resultArea;
    @FXML
    private ComboBox<String> treeTypeCombo;
    @FXML
    private ComboBox<String> rotationCombo;
    @FXML
    public void initialize() {

        GraphicsContext gc =
                treeCanvas.getGraphicsContext2D();

        renderer =
                new TreeRenderer(gc);

        loadTrees();

        treeTypeCombo.getItems().addAll(
                "Binary Tree",
                "BST",
                "AVL"
        );

        rotationCombo.getItems().addAll(
                "LL - Derecha",
                "RR - Izquierda",
                "LR - Izquierda-Derecha",
                "RL - Derecha-Izquierda"
        );

        rotationCombo.getSelectionModel().selectFirst();

        treeTypeCombo.getSelectionModel()
                .select("AVL");

        updateCurrentTree();

        animator =
                new TreeAnimationManager(
                        renderer,
                        treeCanvas,
                        this::drawTree
                );

        drawTree();

        initializeMouseEvents();
    }
    private void drawTree(){

        gc().clearRect(
                0,
                0,
                treeCanvas.getWidth(),
                treeCanvas.getHeight()
        );

        if(currentTree == null ||
                currentTree.getRoot() == null){
            return;
        }

        renderer.render(avlTree, currentTree.getRoot());

        gc().setFill(Color.WHITE);

        gc().fillText(
                "Árbol Actual: " +
                        treeTypeCombo.getValue(),
                20,
                20
        );

    }

    private GraphicsContext gc(){
        return treeCanvas.getGraphicsContext2D();
    }

    private void initializeMouseEvents(){

        treeCanvas.setOnMouseClicked(event -> {

            if(event.getClickCount() == 2){

                Course course =
                        renderer.findCourseAt(
                                event.getX(),
                                event.getY()
                        );

                if(course != null){
                    showCourseDialog(course);
                }
            }

        });
    }

    private void showCourseDialog(Course course){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Información del Curso");
        alert.setHeaderText(course.getId());

        StringBuilder info = new StringBuilder();

        info.append("Nombre: ")
                .append(course.getName())
                .append("\n\n");

        info.append("Créditos: ")
                .append(course.getCredits())
                .append("\n\n");

        info.append("Estado: ")
                .append(course.getStatus());

        alert.setContentText(info.toString());

        alert.showAndWait();
    }



    @FXML
    private void onInOrder() {

        try {

            resultArea.setText(
                    currentTree.inOrder()
            );

            animator.animateNodes(
                    currentTree.getInOrderNodes()
            );

        } catch (Exception e) {

            resultArea.setText(
                    e.getMessage()
            );
        }
    }

    @FXML
    private void onPreOrder() {

        try {

            resultArea.setText(
                    currentTree.preOrder()
            );

            animator.animateNodes(
                    currentTree.getPreOrderNodes()
            );

        } catch (Exception e) {

            resultArea.setText(
                    e.getMessage()
            );
        }
    }
    @FXML
    private void onPostOrder() {

        try {

            resultArea.setText(
                    currentTree.postOrder()
            );

            animator.animateNodes(
                    currentTree.getPostOrderNodes()
            );

        } catch (Exception e) {

            resultArea.setText(
                    e.getMessage()
            );
        }
    }


    @FXML
    private void onAnimatedSearch() {

        try{

            if(!(currentTree instanceof BST)){

                resultArea.setText(
                        "Seleccione BST para la búsqueda animada."
                );

                return;
            }

            String code =
                    searchField
                            .getText()
                            .trim();

            Course target =
                    new Course.Builder()
                            .setId(code)
                            .setName("TEMP")
                            .setCredits(0)
                            .build();

            BST<Course> bst =
                    (BST<Course>) currentTree;

            animator.animateSearchPath(
                    bst.getSearchPath(target)
            );

        }catch(Exception ex){

            resultArea.setText(
                    ex.getMessage()
            );
        }
    }
    @FXML
    private void onTreeTypeChanged() {

        updateCurrentTree();

        drawTree();

        resultArea.setText(
                "Mostrando árbol: "
                        + treeTypeCombo.getValue()
        );
    }
    @FXML
    private void onShowAVLInfo() {

        if(!(currentTree instanceof AVL)){

            resultArea.setText(
                    "Seleccione el árbol AVL."
            );

            return;
        }

        try {

            AVL<Course> avl = (AVL<Course>) currentTree;

            resultArea.setText(
                    avl.getRebalancingInfo()
            );


        } catch (Exception e) {

            resultArea.setText(
                    e.getMessage()
            );
        }
    }
    @FXML
    private void onSearchCourse() {

        try {

            String code =
                    searchField.getText().trim();

            if(code.isEmpty()){
                resultArea.setText(
                        "Ingrese un código de curso."
                );
                return;
            }

            Course target =
                    new Course.Builder()
                            .setId(code)
                            .setName("TEMP")
                            .setCredits(0)
                            .build();

            boolean found =
                    currentTree.contains(target);

            if(found){

                BTreeNode<Course> node =
                        renderer.findNodeByCourse(
                                currentTree.getRoot(),
                                target
                        );

                renderer.setFoundNode(node);

                drawTree();

                resultArea.setText(
                        "✓ Curso encontrado: " + code
                );

            }else{

                resultArea.setText(
                        "✗ Curso no encontrado: " + code
                );
            }

        } catch (Exception e) {

            resultArea.setText(
                    e.getMessage()
            );
        }
    }

    private void loadTrees() {
        CourseData courseData = new CourseData();
        DoublyLinkedList<Course> cursos = courseData.getAllCourses();

        binaryTree = new BTree<>();
        bstTree = new BST<>();
        avlTree = new AVL<>();

        try {
            for (Course curso : cursos.toList()) {
                binaryTree.addBFS(curso);
                bstTree.add(curso);
                avlTree.add(curso);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateCurrentTree() {

        String selected =
                (String) treeTypeCombo.getValue();

        switch (selected) {

            case "Binary Tree":

                currentTree =
                        binaryTree;
                break;

            case "BST":

                currentTree =
                        bstTree;
                break;

            case "AVL":

                currentTree =
                        avlTree;
                break;

            default:

                currentTree =
                        avlTree;
        }
    }


    @FXML
    private void onRotationDemo() {

        try {

            avlTree = new AVL<>();

            currentTree = avlTree;

            CourseData data = new CourseData();

            DoublyLinkedList<Course> cursos =
                    data.getAllCourses();

            animateRotation(
                    rotationCombo.getValue(),
                    cursos
            );

            resultArea.setText(
                    "Demostrando " +
                            rotationCombo.getValue()
            );

        } catch (Exception ex) {

            resultArea.setText(
                    ex.getMessage()
            );
        }
    }

    private void insertCourse(String code,
                              DoublyLinkedList<Course> cursos)
            throws Exception {

        for (Course c : cursos.toList()) {

            if (c.getId().equals(code)) {

                avlTree.add(c);

                return;
            }
        }
    }

    private void animateRotation(String tipo,
                                 DoublyLinkedList<Course> cursos) {

        Timeline timeline = new Timeline();

        String[] ids;

        switch (tipo) {

            case "LL - Derecha":
                ids = new String[]{
                        "IF0003",
                        "IF0002",
                        "IF0001"
                };
                break;

            case "RR - Izquierda":
                ids = new String[]{
                        "IF0001",
                        "IF0002",
                        "IF0003"
                };
                break;

            case "LR - Izquierda-Derecha":
                ids = new String[]{
                        "IF0003",
                        "IF0001",
                        "IF0002"
                };
                break;

            case "RL - Derecha-Izquierda":
                ids = new String[]{
                        "IF0001",
                        "IF0003",
                        "IF0002"
                };
                break;

            default:
                return;
        }

        for (int i = 0; i < ids.length; i++) {

            String id = ids[i];

            timeline.getKeyFrames().add(

                    new KeyFrame(

                            Duration.seconds(i + 1),

                            e -> {

                                try {

                                    insertCourse(id, cursos);

                                    currentTree = avlTree;

                                    drawTree();

                                } catch (Exception ex) {

                                    ex.printStackTrace();
                                }

                            })
            );
        }

        timeline.play();
    }
}
