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
    private ComboBox<String> cmbCarreras;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Group canvasGroup;
    private double scaleValue = 1.0;  // escala inicial

    // Mapas para filtrar los cursos por carrera
    private Map<String, List<Course>> cursosPorCarrera;
    private Map<String, Career> mapaCarreras;

    private User currentUser;

    @FXML
    public void initialize() {

        GraphicsContext gc =
                treeCanvas.getGraphicsContext2D();

        renderer =
                new TreeRenderer(gc);

        // 1. Cargar datos de carreras y cursos UNA SOLA VEZ al iniciar
        loadCareerData();

        // 2. Configurar ComboBoxes de tipo de árbol y rotaciones
        treeTypeCombo.getItems().addAll("Binary Tree", "BST", "AVL");
        rotationCombo.getItems().addAll("LL - Derecha", "RR - Izquierda", "LR - Izquierda-Derecha", "RL - Derecha-Izquierda");
        rotationCombo.getSelectionModel().selectFirst();
        treeTypeCombo.getSelectionModel().select("AVL");

        // 3. Poblar el ComboBox de Carreras si hay datos
        if (!mapaCarreras.isEmpty()) {
            List<Career> carrerasOrdenadas = new ArrayList<>(mapaCarreras.values());
            carrerasOrdenadas.sort(Comparator.comparing(Career::getName));

            for (Career c : carrerasOrdenadas) {
                cmbCarreras.getItems().add(c.getName());
            }
            // Seleccionar la primera por defecto (esto se sobrescribirá en setUser si es necesario)
            cmbCarreras.getSelectionModel().selectFirst();
        }

        // 4. Inicializar el árbol con la primera carrera y dibujar
        updateCurrentTree();

        animator =
                new TreeAnimationManager(
                        renderer,
                        treeCanvas,
                        this::drawTree
                );

        drawTree();

        initializeMouseEvents();

        // Listener para cuando se cambia la carrera (solo aplica si está habilitado)
        cmbCarreras.setOnAction(e -> {
            loadSelectedCareer();
            drawTree();
            String selected = cmbCarreras.getValue();
            resultArea.setText("Mostrando carrera: " + (selected != null ? selected : "N/A"));
        });

        // Listener para zoom con rueda del ratón
        scrollPane.setOnScroll(event -> {
            double zoomFactor = 1.05;
            if (event.getDeltaY() < 0) {
                scaleValue /= zoomFactor;
            } else {
                scaleValue *= zoomFactor;
            }
            scaleValue = Math.min(Math.max(scaleValue, 0.2), 3.0);

            canvasGroup.setScaleX(scaleValue);
            canvasGroup.setScaleY(scaleValue);

            canvasGroup.layout();

            event.consume();
        });
    }

    private void drawTree() {
        gc().clearRect(0, 0, treeCanvas.getWidth(), treeCanvas.getHeight());

        if (currentTree == null || currentTree.getRoot() == null) {
            return;
        }

        renderer.render(avlTree, currentTree.getRoot());

        gc().setFill(Color.WHITE);

        String nombreCarrera = cmbCarreras.getValue() != null ? cmbCarreras.getValue() : "N/A";
        gc().fillText(
                "Árbol: " + treeTypeCombo.getValue() + " | Carrera: " + nombreCarrera,
                20,
                20
        );
    }

    private GraphicsContext gc() {
        return treeCanvas.getGraphicsContext2D();
    }

    private void initializeMouseEvents() {
        treeCanvas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Course course = renderer.findCourseAt(event.getX(), event.getY());
                if (course != null) showCourseDialog(course);
            }
        });

        scrollPane.addEventFilter(javafx.scene.input.ScrollEvent.SCROLL, event -> {
            double zoomFactor = 1.05;
            double delta = event.getDeltaY();

            if (delta < 0) {
                scaleValue /= zoomFactor;
            } else if (delta > 0) {
                scaleValue *= zoomFactor;
            }

            scaleValue = Math.max(0.2, Math.min(scaleValue, 3.0));

            canvasGroup.setScaleX(scaleValue);
            canvasGroup.setScaleY(scaleValue);

            event.consume();
        });
    }

    private void showCourseDialog(Course course) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información del Curso");
        alert.setHeaderText(course.getId());

        StringBuilder info = new StringBuilder();
        info.append("Nombre: ").append(course.getName()).append("\n\n");
        info.append("Créditos: ").append(course.getCredits()).append("\n\n");
        info.append("Estado: ").append(course.getStatus());

        alert.setContentText(info.toString());
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

            Course target = new Course.Builder()
                    .setId(code)
                    .setName("TEMP")
                    .setCredits(0)
                    .build();

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

            Course target = new Course.Builder()
                    .setId(code)
                    .setName("TEMP")
                    .setCredits(0)
                    .build();

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

    // ==========================================
    // LÓGICA DE CARRERAS Y CURSOS
    // ==========================================

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

                for (Career c : listaCarreras) {
                    mapaCarreras.put(c.getId(), c);
                }
                System.out.println("✅ Carreras cargadas correctamente. Total: " + listaCarreras.size());
            } else {
                System.out.println("⚠️ No se encontró el archivo careers.json en target/classes/data/");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Agrupar los cursos por ID de carrera
        for (Course c : cursos.toList()) {
            String idCarrera = c.getCareerId();
            if (idCarrera == null || idCarrera.isEmpty()) {
                continue;
            }
            if (!cursosPorCarrera.containsKey(idCarrera)) {
                cursosPorCarrera.put(idCarrera, new ArrayList<>());
            }
            cursosPorCarrera.get(idCarrera).add(c);
        }
    }

    private void loadSelectedCareer() {
        String nombreCarreraSeleccionada = cmbCarreras.getValue();

        if (nombreCarreraSeleccionada == null) {
            return;
        }

        String selectedCareerId = null;
        for (Map.Entry<String, Career> entry : mapaCarreras.entrySet()) {
            if (entry.getValue().getName().equals(nombreCarreraSeleccionada)) {
                selectedCareerId = entry.getKey();
                break;
            }
        }

        if (selectedCareerId == null) {
            return;
        }

        List<Course> cursosDeEstaCarrera = cursosPorCarrera.get(selectedCareerId);
        if (cursosDeEstaCarrera == null) {
            cursosDeEstaCarrera = new ArrayList<>();
        }

        binaryTree = new BTree<>();
        bstTree = new BST<>();
        avlTree = new AVL<>();

        try {
            for (Course curso : cursosDeEstaCarrera) {
                binaryTree.addBFS(curso);
                bstTree.add(curso);
                avlTree.add(curso);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateCurrentTree();
    }

    private void updateCurrentTree() {
        String selected = treeTypeCombo.getValue();
        switch (selected) {
            case "Binary Tree": currentTree = binaryTree; break;
            case "BST": currentTree = bstTree; break;
            case "AVL": currentTree = avlTree; break;
            default: currentTree = avlTree;
        }
    }

    // ==========================================
    // NUEVO: SET USER Y RESTRICCIÓN DE ROLES
    // ==========================================

    public void setUser(User user) {
        this.currentUser = user;

        if (user.getRole().equals("STUDENT")) {

            String careerId = null;
            StudentData studentData = new StudentData();
            Student student = studentData.findByUsername(user.getUsername());
            if (student != null) {
                careerId = student.getCareerId();
            }

            if (careerId != null && mapaCarreras.containsKey(careerId)) {
                String nombreCarrera = mapaCarreras.get(careerId).getName();


                cmbCarreras.getItems().clear();
                cmbCarreras.getItems().add(nombreCarrera);

                cmbCarreras.setValue(nombreCarrera);
                cmbCarreras.setDisable(true); // Bloquear para que no pueda cambiar

                // Cargar el árbol de su carrera
                loadSelectedCareer();
                drawTree();
                resultArea.setText("Mostrando tu carrera: " + nombreCarrera);
            } else {
                resultArea.setText("⚠️ No se encontró tu carrera asignada.");
            }

        } else {

            cmbCarreras.setDisable(false);

            cmbCarreras.getItems().clear();
            List<Career> carrerasOrdenadas = new ArrayList<>(mapaCarreras.values());
            carrerasOrdenadas.sort(Comparator.comparing(Career::getName));
            for (Career c : carrerasOrdenadas) {
                cmbCarreras.getItems().add(c.getName());
            }

            if (!cmbCarreras.getItems().isEmpty()) {
                cmbCarreras.getSelectionModel().selectFirst();
                loadSelectedCareer();
                drawTree();
                resultArea.setText("Seleccione una carrera para visualizar.");
            }
        }
    }

    // ==========================================
    // DEMOSTRACIÓN DE ROTACIONES
    // ==========================================

    @FXML
    private void onRotationDemo() {
        try {
            avlTree = new AVL<>();
            currentTree = avlTree;

            String nombreCarreraSeleccionada = cmbCarreras.getValue();

            String selectedCareerId = null;
            for (Map.Entry<String, Career> entry : mapaCarreras.entrySet()) {
                if (entry.getValue().getName().equals(nombreCarreraSeleccionada)) {
                    selectedCareerId = entry.getKey();
                    break;
                }
            }

            List<Course> cursosCarrera = cursosPorCarrera.get(selectedCareerId);
            if (cursosCarrera == null) {
                cursosCarrera = new ArrayList<>();
            }

            animateRotation(rotationCombo.getValue(), cursosCarrera);
            resultArea.setText("Demostrando " + rotationCombo.getValue());

        } catch (Exception ex) {
            resultArea.setText(ex.getMessage());
        }
    }

    private void animateRotation(String tipo, List<Course> cursos) {
        Timeline timeline = new Timeline();
        String[] ids;

        switch (tipo) {
            case "LL - Derecha":
                ids = new String[]{"IF0003", "IF0002", "IF0001"};
                break;
            case "RR - Izquierda":
                ids = new String[]{"IF0001", "IF0002", "IF0003"};
                break;
            case "LR - Izquierda-Derecha":
                ids = new String[]{"IF0003", "IF0001", "IF0002"};
                break;
            case "RL - Derecha-Izquierda":
                ids = new String[]{"IF0001", "IF0003", "IF0002"};
                break;
            default:
                return;
        }

        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(i + 1), e -> {
                try {
                    insertCourse(id, cursos);
                    currentTree = avlTree;
                    drawTree();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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