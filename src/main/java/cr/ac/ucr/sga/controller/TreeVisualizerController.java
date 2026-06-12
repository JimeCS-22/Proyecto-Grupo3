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

public class TreeVisualizerController {

    @FXML
    private Canvas treeCanvas;

    @FXML
    private Label courseId;

    @FXML
    private Label courseName;

    @FXML
    private Label courseCredits;

    @FXML
    private Label courseStatus;

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
    private Label lblNodes;
    @FXML
    private Label lblHeight;

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

        renderer.render(
                currentTree.getRoot()
        );

        gc().setFill(Color.WHITE);

        gc().fillText(
                "Árbol Actual: " +
                        treeTypeCombo.getValue(),
                20,
                20
        );

        updateStats();
    }

    private GraphicsContext gc(){
        return treeCanvas.getGraphicsContext2D();
    }

    private void initializeMouseEvents(){

        treeCanvas.setOnMouseClicked(event -> {

            Course course =
                    renderer.findCourseAt(
                            event.getX(),
                            event.getY()
                    );

            if(course != null){

                showCourseInfo(course);
            }
        });
    }

    private void showCourseInfo(
            Course course
    ){

        courseId.setText(
                course.getId()
        );

        courseName.setText(
                course.getName()
        );

        courseCredits.setText(
                String.valueOf(
                        course.getCredits()
                )
        );

        courseStatus.setText(
                course.getStatus()
        );
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

            AVL<Course> avl =
                    (AVL<Course>) currentTree;

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

    private void updateStats(){

        try{

            lblNodes.setText(
                    "Nodos: " +
                            currentTree.size()
            );

            lblHeight.setText(
                    "Altura: " +
                            currentTree.height()
            );

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
