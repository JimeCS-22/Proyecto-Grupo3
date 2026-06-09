package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.graphics.TreeAnimationManager;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.graphics.TreeRenderer;
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

        binaryTree = new BTree<>();

        bstTree = new BST<>();

        avlTree = new AVL<>();

        Course c1 =
                new Course.Builder()
                        .setId("CI0110")
                        .setName("Introducción")
                        .setCredits(4)
                        .setStatus("Aprobado")
                        .build();

        Course c2 =
                new Course.Builder()
                        .setId("CI0120")
                        .setName("Programación I")
                        .setCredits(4)
                        .setStatus("Aprobado")
                        .build();

        Course c3 =
                new Course.Builder()
                        .setId("CI0130")
                        .setName("Estructuras")
                        .setCredits(4)
                        .setStatus("Cursando")
                        .build();

        Course c4 =
                new Course.Builder()
                        .setId("CI0140")
                        .setName("Algoritmos")
                        .setCredits(4)
                        .setStatus("Pendiente")
                        .build();

        Course c5 =
                new Course.Builder()
                        .setId("CI0150")
                        .setName("Bases de Datos")
                        .setCredits(4)
                        .setStatus("Pendiente")
                        .build();

        // Binary Tree

        binaryTree.addBFS(c1);
        binaryTree.addBFS(c2);
        binaryTree.addBFS(c3);
        binaryTree.addBFS(c4);
        binaryTree.addBFS(c5);

        // BST

        bstTree.add(c1);
        bstTree.add(c2);
        bstTree.add(c3);
        bstTree.add(c4);
        bstTree.add(c5);

        // AVL

        avlTree.add(c1);
        avlTree.add(c2);
        avlTree.add(c3);
        avlTree.add(c4);
        avlTree.add(c5);
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
