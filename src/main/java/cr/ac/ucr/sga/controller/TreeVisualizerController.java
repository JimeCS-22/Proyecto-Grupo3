package cr.ac.ucr.sga.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.graphics.TreeAnimationManager;
import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.data.StudentData;
import cr.ac.ucr.sga.model.entities.Career;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.graphics.TreeRenderer;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.trees.AVL;
import cr.ac.ucr.sga.model.structures.trees.BST;
import cr.ac.ucr.sga.model.structures.trees.BTree;
import cr.ac.ucr.sga.model.structures.trees.BTreeNode;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

public class TreeVisualizerController {

    @FXML private Canvas treeCanvas;
    private TreeRenderer renderer;
    private BTree<Course> currentTree;
    private BTree<Course> binaryTree;
    private BST<Course> bstTree;
    private AVL<Course> avlTree;
    private TreeAnimationManager animator;

    @FXML private TextField searchField;
    @FXML private TextArea resultArea;
    @FXML private ComboBox<String> treeTypeCombo;
    @FXML private ComboBox<String> rotationCombo;
    @FXML private ComboBox<String> cmbCarreras;
    @FXML private ScrollPane scrollPane;
    @FXML private Group canvasGroup;
    private double scaleValue = 1.0;
    private Map<String, List<Course>> cursosPorCarrera;
    private Map<String, Career> mapaCarreras;
    private User currentUser;

    @FXML
    public void initialize() {
        GraphicsContext gc = treeCanvas.getGraphicsContext2D();
        renderer = new TreeRenderer(gc);
        loadCareerData();
        treeTypeCombo.getItems().addAll("Binary Tree", "BST", "AVL");
        rotationCombo.getItems().addAll("LL - Derecha", "RR - Izquierda", "LR - Izquierda-Derecha", "RL - Derecha-Izquierda");
        rotationCombo.getSelectionModel().selectFirst();
        treeTypeCombo.getSelectionModel().select("AVL");

        if (!mapaCarreras.isEmpty()) {
            List<Career> carrerasOrdenadas = new ArrayList<>(mapaCarreras.values());
            carrerasOrdenadas.sort(Comparator.comparing(Career::getName));
            for (Career c : carrerasOrdenadas) cmbCarreras.getItems().add(c.getName());
            cmbCarreras.getSelectionModel().selectFirst();
        }

        updateCurrentTree();
        animator = new TreeAnimationManager(renderer, treeCanvas, this::drawTree);
        drawTree();
        initializeMouseEvents();

        cmbCarreras.setOnAction(e -> {
            loadSelectedCareer();
            drawTree();
            String selected = cmbCarreras.getValue();
            resultArea.setText("Mostrando carrera: " + (selected != null ? selected : "N/A"));
        });

        scrollPane.setOnScroll(event -> {
            double zoomFactor = 1.05;
            scaleValue = (event.getDeltaY() < 0) ? scaleValue / zoomFactor : scaleValue * zoomFactor;
            scaleValue = Math.min(Math.max(scaleValue, 0.2), 3.0);
            canvasGroup.setScaleX(scaleValue);
            canvasGroup.setScaleY(scaleValue);
            canvasGroup.layout();
            event.consume();
        });
    }

    private void drawTree() {
        gc().clearRect(0, 0, treeCanvas.getWidth(), treeCanvas.getHeight());
        if (currentTree == null || currentTree.getRoot() == null) return;
        renderer.render(avlTree, currentTree.getRoot());
        gc().setFill(Color.WHITE);
        String nombreCarrera = cmbCarreras.getValue() != null ? cmbCarreras.getValue() : "N/A";
        gc().fillText("Árbol: " + treeTypeCombo.getValue() + " | Carrera: " + nombreCarrera, 20, 20);
    }

    private GraphicsContext gc() { return treeCanvas.getGraphicsContext2D(); }

    private void initializeMouseEvents() {
        treeCanvas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Course course = renderer.findCourseAt(event.getX(), event.getY());
                if (course != null) showCourseDialog(course);
            }
        });
    }

    private void showCourseDialog(Course course) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información del Curso");
        alert.setHeaderText(course.getId());
        alert.setContentText("Nombre: " + course.getName() + "\n\nCréditos: " + course.getCredits() + "\n\nEstado: " + course.getStatus());
        alert.showAndWait();
    }

    @FXML
    private void onInOrder() {
        try {
            resultArea.setText(currentTree.inOrder());
            animator.animateNodes(currentTree.getInOrderNodes());
        } catch (Exception e) { resultArea.setText(e.getMessage()); }
    }

    @FXML
    private void onPreOrder() {
        try {
            resultArea.setText(currentTree.preOrder());
            animator.animateNodes(currentTree.getPreOrderNodes());
        } catch (Exception e) { resultArea.setText(e.getMessage()); }
    }

    @FXML
    private void onPostOrder() {
        try {
            resultArea.setText(currentTree.postOrder());
            animator.animateNodes(currentTree.getPostOrderNodes());
        } catch (Exception e) { resultArea.setText(e.getMessage()); }
    }

    @FXML
    private void onAnimatedSearch() {
        try {
            String code = searchField.getText().trim();
            if (code.isEmpty()) {
                resultArea.setText("Ingrese un código de curso.");
                return;
            }
            Course target = new Course.Builder().setId(code).setName("TEMP").setCredits(0).build();
            List<BTreeNode<Course>> path = currentTree.getSearchPath(target);
            if (path == null || path.isEmpty()) {
                resultArea.setText("✗ Curso no encontrado");
                return;
            }
            animator.animateSearchPath(path);
        } catch (Exception ex) { resultArea.setText(ex.getMessage()); }
    }

    @FXML
    private void onTreeTypeChanged() {
        updateCurrentTree();
        drawTree();
        resultArea.setText("Mostrando árbol: " + treeTypeCombo.getValue());
    }

    @FXML
    private void onShowAVLInfo() {
        if (!(currentTree instanceof AVL)) {
            resultArea.setText("Seleccione el árbol AVL.");
            return;
        }
        try {
            AVL<Course> avl = (AVL<Course>) currentTree;
            resultArea.setText(avl.getRebalancingInfo());
        } catch (Exception e) { resultArea.setText(e.getMessage()); }
    }

    @FXML
    private void onSearchCourse() {
        try {
            String code = searchField.getText().trim();
            if (code.isEmpty()) {
                resultArea.setText("Ingrese un código de curso.");
                return;
            }
            Course target = new Course.Builder().setId(code).setName("TEMP").setCredits(0).build();
            boolean found = currentTree.contains(target);
            if (found) {
                BTreeNode<Course> node = renderer.findNodeByCourse(currentTree.getRoot(), target);
                renderer.setFoundNode(node);
                drawTree();
                resultArea.setText("✓ Curso encontrado: " + code);
            } else {
                resultArea.setText("✗ Curso no encontrado: " + code);
            }
        } catch (Exception e) { resultArea.setText(e.getMessage()); }
    }

    private void loadCareerData() {
        CourseData courseData = new CourseData();
        DoublyLinkedList<Course> cursos = courseData.getAllCourses();
        cursosPorCarrera = new HashMap<>();
        mapaCarreras = new HashMap<>();

        try {
            Gson gson = new Gson();
            InputStream is = TreeVisualizerController.class.getResourceAsStream("/data/careers.json");
            if (is != null) {
                InputStreamReader reader = new InputStreamReader(is);
                Type listType = new TypeToken<ArrayList<Career>>(){}.getType();
                List<Career> listaCarreras = gson.fromJson(reader, listType);
                for (Career c : listaCarreras) mapaCarreras.put(c.getId(), c);
            }
        } catch (Exception e) { e.printStackTrace(); }

        for (Course c : cursos.toList()) {
            String idCarrera = c.getCareerId();
            if (idCarrera == null || idCarrera.isEmpty()) continue;
            cursosPorCarrera.computeIfAbsent(idCarrera, k -> new ArrayList<>()).add(c);
        }
    }

    private void loadSelectedCareer() {
        String nombreCarreraSeleccionada = cmbCarreras.getValue();
        if (nombreCarreraSeleccionada == null) return;
        String selectedCareerId = mapaCarreras.entrySet().stream()
                .filter(e -> e.getValue().getName().equals(nombreCarreraSeleccionada))
                .map(Map.Entry::getKey).findFirst().orElse(null);

        if (selectedCareerId == null) return;
        List<Course> cursosDeEstaCarrera = cursosPorCarrera.getOrDefault(selectedCareerId, new ArrayList<>());
        binaryTree = new BTree<>();
        bstTree = new BST<>();
        avlTree = new AVL<>();

        try {
            for (Course curso : cursosDeEstaCarrera) {
                binaryTree.addBFS(curso);
                bstTree.add(curso);
                avlTree.add(curso);
            }
        } catch (Exception e) { e.printStackTrace(); }
        updateCurrentTree();
    }

    private void updateCurrentTree() {
        String selected = treeTypeCombo.getValue();
        currentTree = switch (selected) {
            case "Binary Tree" -> binaryTree;
            case "BST" -> bstTree;
            default -> avlTree;
        };
    }

    public void setUser(User user) {
        this.currentUser = user;
        if (user.getRole().equals("STUDENT")) {
            StudentData studentData = new StudentData();
            Student student = studentData.findByUsername(user.getUsername());
            String careerId = (student != null) ? student.getCareerId() : null;
            if (careerId != null && mapaCarreras.containsKey(careerId)) {
                String nombreCarrera = mapaCarreras.get(careerId).getName();
                cmbCarreras.getItems().clear();
                cmbCarreras.getItems().add(nombreCarrera);
                cmbCarreras.setValue(nombreCarrera);
                cmbCarreras.setDisable(true);
                loadSelectedCareer();
                drawTree();
                resultArea.setText("Mostrando tu carrera: " + nombreCarrera);
            } else {
                resultArea.setText("⚠️ No se encontró tu carrera asignada.");
            }
        } else {
            cmbCarreras.setDisable(false);
            cmbCarreras.getItems().clear();
            mapaCarreras.values().stream().sorted(Comparator.comparing(Career::getName))
                    .forEach(c -> cmbCarreras.getItems().add(c.getName()));
            if (!cmbCarreras.getItems().isEmpty()) {
                cmbCarreras.getSelectionModel().selectFirst();
                loadSelectedCareer();
                drawTree();
                resultArea.setText("Seleccione una carrera para visualizar.");
            }
        }
    }

    @FXML
    private void onRotationDemo() {
        try {
            avlTree = new AVL<>();
            currentTree = avlTree;
            String nombreCarreraSeleccionada = cmbCarreras.getValue();
            String selectedCareerId = mapaCarreras.entrySet().stream()
                    .filter(e -> e.getValue().getName().equals(nombreCarreraSeleccionada))
                    .map(Map.Entry::getKey).findFirst().orElse(null);
            List<Course> cursosCarrera = cursosPorCarrera.getOrDefault(selectedCareerId, new ArrayList<>());
            animateRotation(rotationCombo.getValue(), cursosCarrera);
            resultArea.setText("Demostrando " + rotationCombo.getValue());
        } catch (Exception ex) { resultArea.setText(ex.getMessage()); }
    }

    private void animateRotation(String tipo, List<Course> cursos) {
        Timeline timeline = new Timeline();
        String[] ids = switch (tipo) {
            case "LL - Derecha" -> new String[]{"IF0003", "IF0002", "IF0001"};
            case "RR - Izquierda" -> new String[]{"IF0001", "IF0002", "IF0003"};
            case "LR - Izquierda-Derecha" -> new String[]{"IF0003", "IF0001", "IF0002"};
            case "RL - Derecha-Izquierda" -> new String[]{"IF0001", "IF0003", "IF0002"};
            default -> new String[]{};
        };

        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(i + 1), e -> {
                try {
                    insertCourse(id, cursos);
                    currentTree = avlTree;
                    drawTree();
                } catch (Exception ex) { ex.printStackTrace(); }
            }));
        }
        timeline.play();
    }

    private void insertCourse(String code, List<Course> cursos) throws Exception {
        for (Course c : cursos) {
            if (c.getId().equals(code)) {
                avlTree.add(c);
                return;
            }
        }
    }
}