package ucr.algoritmos.proyecto.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import ucr.algoritmos.proyecto.model.*;
import ucr.algoritmos.proyecto.model.Node.*;
import ucr.algoritmos.proyecto.model.linkedList.DoublyLinkedList;
import ucr.algoritmos.proyecto.model.linkedList.LinkedList;
import ucr.algoritmos.proyecto.model.linkedList.ListException;
import ucr.algoritmos.proyecto.util.BigIntegerSpinnerValueFactory;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainController {

    // TAB: Miller-Rabin
    @FXML private TabPane mainTabs;
    @FXML private Spinner<BigInteger> spParams;
    @FXML private Canvas canvasMiller;
    @FXML private Button btnMillerRabin;
    @FXML private TextField txfBigInteger;
    @FXML private Button btnClean;
    @FXML private Button btnCleanField;
    @FXML private ListView<String> listViewOperations;

    @FXML private TableView<MillerRabinResult> tableResults;
    @FXML private TableColumn<MillerRabinResult, String> colNumber;
    @FXML private TableColumn<MillerRabinResult, String> colResult;

    //TAB: Random Search
    @FXML private Button btnCleanSearch;
    @FXML private Slider sliderParaSearch;
    @FXML private TableColumn<RandomSearchResult, Integer> colValueSearch;
    @FXML private TableColumn<RandomSearchResult, Integer> colIndexSearch;
    @FXML private TableColumn<RandomSearchResult, Integer> colAttemptsSearch;
    @FXML private TableColumn<RandomSearchResult, Integer> colMaxAttemptsSearch;
    @FXML private TextField txtValueSearch;
    @FXML private Button btnRandomSearchSearch;
    @FXML private TextField arrayTextSearch;
    @FXML private Canvas canvasSearch;
    @FXML private Button btnGenerateSearch;
    @FXML private TextField txtMaxAttemptsSearch;
    @FXML private ListView<String> listViewOperationsSearch;
    @FXML private TableView<RandomSearchResult> tableResultsSearch;

    // Random Search: estado
    private int[] randomArraySearch = new int[0];
    private final ObservableList<RandomSearchResult> randomSearchResults = FXCollections.observableArrayList();

    // TAB: Linked List
    @FXML private TextField textFieldValue;
    @FXML private Button btnAgregarInicio;
    @FXML private Button btnAgregarFinal;
    @FXML private Button btnSearch;
    @FXML private Button btnDelete;
    @FXML private Button btnClearList;
    @FXML private Canvas canvasListDraw;
    @FXML private TableView<NodeInfo> tableLinkedList;
    @FXML private TableColumn<NodeInfo, String> colPosition;
    @FXML private TableColumn<NodeInfo, String> colValue;
    @FXML private TableColumn<NodeInfo, String> colInsert;
    @FXML private ListView<String> listViewOperationsList;
    @FXML private TextArea txAreaNodeStructure;
    @FXML private Label txFieldNodeRepre;
    @FXML private Label txtInsertadoIn;

    // Linked List: estado del modelo
    private LinkedList<String> list;
    private ObservableList<NodeInfo> dataTable;
    private int contadorPosicion;


    // Variables
    BigInteger min = new BigInteger("1");
    BigInteger max = new BigInteger("999999999999999999");
    BigInteger initial = new BigInteger("1000000000000");
    BigInteger step = new BigInteger("1");
    private final SecureRandom random = new SecureRandom();

    //Lista Doble enlazada
    @FXML
    private Button btnClearListDoble;
    @FXML
    private Label txtInsertadoIn1;
    @FXML
    private Button btnSearchDoble;
    @FXML
    private Canvas canvasListDoubly;
    @FXML
    private TableColumn colPositionDoubly;
    @FXML
    private Button btnEliminarInicio;
    @FXML
    private TextField txtName;
    @FXML
    private TextArea txAreaNodeStructureD;
    @FXML
    private Label txtInsertar;
    @FXML
    private TableColumn colPuesto;
    @FXML
    private Button btnAgregarDoble;
    @FXML
    private ListView listViewOperationsListDoubly;
    @FXML
    private TextField txtId;
    @FXML
    private Button btnUltimo;
    @FXML
    private Button btnAnterior;
    @FXML
    private TableView colID;
    @FXML
    private Button btnEliminarDoble;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colFechaIngreso;
    @FXML
    private ChoiceBox bxJobPosition;
    @FXML
    private Button btnEliminarFinal;
    @FXML
    private Button btnPrimero;
    @FXML
    private DatePicker dpHireDate;
    @FXML
    private Button btnSiguiente;

    // DoublyLinkedList: estado
    private DoublyLinkedList<Employee> doublyList = new DoublyLinkedList<>();
    private final ObservableList<Employee> doublyData = FXCollections.observableArrayList();
    private Node<Employee> currentEmployeeNode = null;
    private final DateTimeFormatter hireFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @FXML
    private TextArea txAreaNodeRepreDoubly;


    @FXML
    public void initialize() {
        setupMillerRabin();
        setupLinkedListTab();
        setupRandomSearch();
        setupDoublyLinkedListTab();
    }

    private void setupMillerRabin() {
        spParams.setValueFactory(
                new BigIntegerSpinnerValueFactory(min, max, initial, step)
        );

        spParams.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txfBigInteger.setText(newVal.toString());
            }
        });

        txfBigInteger.textProperty().addListener((obs, oldVal, newVal) -> {
            clearCanvasMiller();
        });

        colNumber.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNumber()));

        colResult.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getResult()));

        colResult.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);

                    if (item.contains("Probablemente Primo")) {
                        setStyle("-fx-background-color: #b6ffb3; -fx-text-fill: black;");
                    } else {
                        setStyle("-fx-background-color: #ffb3b3; -fx-text-fill: black;");
                    }
                }
            }
        });

        colNumber.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-font-weight: bold;");
                }
            }
        });

        btnMillerRabin.setOnAction(e -> runMillerRabin());
        btnClean.setOnAction(e -> reset());
        btnCleanField.setOnAction(e -> txfBigInteger.setText(""));
    }

    private void runMillerRabin() {

        String input = txfBigInteger.getText().trim();

        if (input.isEmpty()) {
            showAlert("Error", "Debe ingresar un número");
            return;
        }

        try {
            Probabilistic p = new Probabilistic();
            String result = p.millerRabin(input);

            boolean isPrime = result.contains("probably prime");

            ObservableList<String> items = FXCollections.observableArrayList(listViewOperations.getItems());
            items.add(input + " → " + (isPrime ? "✔ Primo" : "✘ No primo"));
            listViewOperations.setItems(items);

            tableResults.getItems().add(
                    new MillerRabinResult(
                            input,
                            isPrime ? "Probablemente Primo" : "No Primo"
                    )
            );

            drawCircle(input, isPrime);

        } catch (Exception e) {
            showAlert("Error", "Número inválido");
        }
    }

    private void drawCircle(String number, boolean isPrime) {

        GraphicsContext gc = canvasMiller.getGraphicsContext2D();

        clearCanvasMiller();

        double size = 120;

        double x = (canvasMiller.getWidth() - size) / 2;
        double y = (canvasMiller.getHeight() - size) / 2;

        gc.setFill(isPrime ? Color.LIMEGREEN : Color.RED);
        gc.fillOval(x, y, size, size);

        gc.setStroke(Color.BLACK);
        gc.strokeOval(x, y, size, size);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font(14));

        String text = number.length() > 10 ? number.substring(0, 10) + "..." : number;

        double textWidth = text.length() * 7;
        double textX = x + (size - textWidth) / 2;
        double textY = y + size / 2;

        gc.fillText(text, textX, textY);
    }

    private void clearCanvasMiller() {
        GraphicsContext gc = canvasMiller.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasMiller.getWidth(), canvasMiller.getHeight());
    }

    @FXML
    private void generarAleatorio() {
        BigInteger rand = new BigInteger(50, random);
        txfBigInteger.setText(rand.toString());
    }

    private void reset() {
        listViewOperations.getItems().clear();
        tableResults.getItems().clear();
        clearCanvasMiller();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Methods Controller for Linked List - Camila

    private void setupLinkedListTab() {
        //atributos para Linked List
         list = new LinkedList<>();
         dataTable = FXCollections.observableArrayList();
         contadorPosicion = 0; // atributo en tu Controller

        colPosition.setCellValueFactory(new PropertyValueFactory<>("posicion"));
        colValue.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colInsert.setCellValueFactory(new PropertyValueFactory<>("referencia"));
        tableLinkedList.setItems(dataTable);
        //config botones de operaciones
        btnAgregarInicio.setOnAction(e -> addFirst());
        btnAgregarFinal.setOnAction(e -> addLast());
        btnSearch.setOnAction(e -> runSearchInLinkedList());
        btnDelete.setOnAction(e -> remove());
        btnClearList.setOnAction(e -> cleanListTab());

    }


    private void addFirst() {

        String input = textFieldValue.getText().trim();

        if (input.isEmpty()) {
            showAlert("Error", "Debe ingresar un valor");
            return;
        }

        try {
            list.addFirst(input);
            String result = list.toString();
            //mostrar la Representación de la lista
            txFieldNodeRepre.setText(result);
            txtInsertadoIn.setText("al inicio: " + input);

            // colocar el registro de operaciones
            registrarOperacion("addFirst", input, "HEAD → [" + input + "] → ...");
            //llenado tabla
            desplazarPosiciones();
            // agregar fila a la tabla
            dataTable.add(0, new NodeInfo("1", input, "Inicio"));
            drawLinkedList(input);//dibujar la acción de addFirst en el Canvas

        } catch (Exception e) {
            showAlert("Error", "Valor inválido");
        }

    }

    private void addLast() {

            String input = textFieldValue.getText().trim();

            if (input.isEmpty()) {
                showAlert("Error", "Debe ingresar un valor");
                return;
            }

            try {
                list.addLast(input);
                txFieldNodeRepre.setText(list.toString());
                txtInsertadoIn.setText("al final: "+input );

                registrarOperacion("addLast", input, "... → [" + input + "] → NULL");

                contadorPosicion++;
                int posicion = list.indexOf(input); //String.valueOf(posicion)
                dataTable.add(new NodeInfo(
                        String.valueOf(posicion),
                        input,
                        "Final"
                ));

                drawLinkedList(input);

            } catch (Exception e) {
                showAlert("Error", "Valor inválido");
            }

    }

    private void runSearchInLinkedList() {

        String input = textFieldValue.getText().trim();

        // Validación
        if (input.isEmpty()) {
            showAlert("Error", "Debe ingresar un valor a buscar");
            return;
        }

        try {
            // Buscar en la lista enlazada
            int posicion = list.indexOf(input);

            if (posicion == -1) {
                // Valor NO encontrado
                txtInsertadoIn.setText("El valor \"" + input + "\" no se encuentra en la lista");
                showAlert("Resultado de búsqueda",
                        "El valor \"" + input + "\" no se encuentra en la lista");

                // registrar operación
                listViewOperationsList.getItems().add(
                        "search(" + input + ") → NO ENCONTRADO"
                );
            } else {
                // Valor encontrado
                txtInsertadoIn.setText("El valor \"" + input + "\" fue encontrado en la posición: " + posicion);
                showAlert("Resultado de búsqueda",
                        "El valor \"" + input + "\" fue encontrado en la posición: " + posicion);

                // registrar operación
                listViewOperationsList.getItems().add(
                        "search(" + input + ") → encontrado en posición " + posicion
                );

                // redibujar lista (TO DO  resaltar el nodo)
                drawLinkedList(input);
            }

        } catch (Exception e) {
            showAlert("Error", "Error al buscar el valor");
        }

    }

    private void remove() {
        String input = textFieldValue.getText().trim();

        if (input.isEmpty()) {
            showAlert("Error", "Debe ingresar un valor");
            return;
        }

        try {
            list.remove(input);
            String result = list.toString();
            //mostrar la Representación de la lista
            txFieldNodeRepre.setText(result);

            // colocar el registro de operaciones
            registrarOperacion("remove",input, "Nodo eliminado");

            //llenado tabla
            // agregar fila a la tabla
            contadorPosicion++;
            dataTable.removeIf(n -> n.getValor().equals(input));

            drawLinkedList(input);//dibujar la acción de addFirst en el Canvas

        } catch (Exception e) {
            showAlert("Error", "Valor inválido");
        }
    }
    private void cleanListTab() {
        list.clear();
        txFieldNodeRepre.setText("");
        txtInsertadoIn.setText("");
        textFieldValue.setText("");
        listViewOperationsList.getItems().clear();
        tableLinkedList.getItems().clear();
        clearCanvasList();
    }

    private void clearCanvasList() {
        GraphicsContext gc = canvasListDraw.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasListDraw.getWidth(), canvasListDraw.getHeight());
    }

    private void drawLinkedList(String input) {

        GraphicsContext gc = canvasListDraw.getGraphicsContext2D();
        clearCanvasList();

        // Configuración visual
        double startX = 50;
        double startY = canvasListDraw.getHeight() / 2;
        double nodeWidth = 60;
        double nodeHeight = 40;
        double spacing = 30;

        // Colores
        Color nodeColor = Color.web("#1f2a44");
        Color arrowColor = Color.web("#f5a623");

        gc.setFont(Font.font(14));

        // Dibujar HEAD
        gc.setFill(Color.BLACK);
        gc.fillText("HEAD", startX - 35, startY + 5);

        double currentX = startX;
        Node<String> current = list.getHead();

        // Recorrer la lista enlazada
        while (current != null) {

            // Nodo (rectángulo)
            gc.setFill(nodeColor);
            gc.fillRect(currentX, startY - nodeHeight / 2, nodeWidth, nodeHeight);

            // Borde
            gc.setStroke(Color.BLACK);
            gc.strokeRect(currentX, startY - nodeHeight / 2, nodeWidth, nodeHeight);

            // Valor del nodo
            gc.setFill(Color.WHITE);
            gc.fillText(
                    current.getData(),
                    currentX + 15,
                    startY + 5
            );

            // Flecha al siguiente nodo
            if (current.getNext() != null) {
                double arrowStartX = currentX + nodeWidth;
                double arrowEndX = currentX + nodeWidth + spacing;

                gc.setStroke(arrowColor);
                gc.setLineWidth(2);
                gc.strokeLine(arrowStartX, startY, arrowEndX, startY);

                // Punta de la flecha
                gc.strokeLine(arrowEndX - 5, startY - 5, arrowEndX, startY);
                gc.strokeLine(arrowEndX - 5, startY + 5, arrowEndX, startY);
            }

            currentX += nodeWidth + spacing;
            current = current.getNext();
        }

        // Dibujar NULL
        gc.setFill(Color.BLACK);
        gc.fillText("NULL", currentX + 10, startY + 5);
    }


    private void registrarOperacion(String operacion, String valor, String representacion) {
        String texto = operacion + "(" + valor + ")  " + representacion;
        listViewOperationsList.getItems().add(texto);
    }
    private void desplazarPosiciones() {
        for (NodeInfo info : dataTable) {
            int pos = Integer.parseInt(info.getPosicion());
            info.setPosicion(String.valueOf(pos + 1));
        }
    }



    // TAB: Random Search
    private static final int VISIBLE = 12;
    private void setupRandomSearch() {

        sliderParaSearch.setMin(0);
        sliderParaSearch.setMax(99);
        sliderParaSearch.setValue(20);
        sliderParaSearch.setMajorTickUnit(5);
        sliderParaSearch.setMinorTickCount(0);
        sliderParaSearch.setSnapToTicks(true);

        sliderParaSearch.valueProperty().addListener((obs, ov, nv) -> {
            if (nv != null) txtValueSearch.setText(String.valueOf(nv.intValue()));
        });
        txtValueSearch.textProperty().addListener((obs, o, n) -> {
            if (n == null || n.isBlank()) return;
            if (!n.matches("\\d+")) return;

            int v = Integer.parseInt(n);
            int min = (int) sliderParaSearch.getMin();
            int max = (int) sliderParaSearch.getMax();
            if (v < min) v = min;
            if (v > max) v = max;

            if ((int) sliderParaSearch.getValue() != v) sliderParaSearch.setValue(v);
        });

        // tabla
        colValueSearch.setCellValueFactory(new PropertyValueFactory<>("value"));
        colIndexSearch.setCellValueFactory(new PropertyValueFactory<>("index"));
        colAttemptsSearch.setCellValueFactory(new PropertyValueFactory<>("attempts"));
        colMaxAttemptsSearch.setCellValueFactory(new PropertyValueFactory<>("maxAttempts"));
        tableResultsSearch.setItems(randomSearchResults);

        // botones
        btnGenerateSearch.setOnAction(e -> genArray());
        btnRandomSearchSearch.setOnAction(e -> doSearch());
        btnCleanSearch.setOnAction(e -> clearSearch());

        // NO generar al abrir el tab
        clearCanvasSearch();
        arrayTextSearch.setText("");
        txtMaxAttemptsSearch.setText("");
    }

    private void genArray() {
        int n = 64;
        int min = 0, max = 99;

        randomArraySearch = new int[n];
        for (int i = 0; i < n; i++) {
            randomArraySearch[i] = min + random.nextInt(max - min + 1);
        }

        arrayTextSearch.setText(previewArray(randomArraySearch) + "   (n=" + randomArraySearch.length + ")");

        // muestra primeros 12
        draw(0, -1, "", 0, 0);
    }

    private void doSearch() {
        if (randomArraySearch == null || randomArraySearch.length == 0) {
            showAlert("Error", "Primero presione: Generar Aleatorio.");
            return;
        }

        int value, maxAtt;
        try {
            value = Integer.parseInt(txtValueSearch.getText().trim());
            maxAtt = Integer.parseInt(txtMaxAttemptsSearch.getText().trim());
            if (maxAtt <= 0) {
                showAlert("Error", "Max Attempts debe ser mayor a 0.");
                return;
            }
        } catch (Exception e) {
            showAlert("Error", "Valor y Max Attempts deben ser números enteros.");
            return;
        }

        Probabilistic p = new Probabilistic();
        int[] r = p.randomSearch(randomArraySearch, value, maxAtt);

        int idx = r[0];
        int att = r[1];
        boolean ok = idx != -1;

        // log
        String log = "Item[" + value + "] " +
                (ok
                        ? "found in index: " + idx + ". Attempts: " + att + "  ✓"
                        : "not found. Max attempts: " + maxAtt + "  ✗");
        listViewOperationsSearch.getItems().add(log);

        // tabla
        randomSearchResults.add(new RandomSearchResult(value, idx, att, maxAtt));

        // mensaje
        String msg = ok
                ? "¡VALOR ENCONTRADO EN EL ÍNDICE " + idx + "!"
                : "VALOR NO ENCONTRADO (Intentos agotados)";

        // dibujar 12 cerca del encontrado
        int start = ok ? startOf(idx) : 0;
        draw(start, idx, msg, att, maxAtt);
    }

    private void clearSearch() {
        txtValueSearch.clear();
        txtMaxAttemptsSearch.clear();
        listViewOperationsSearch.getItems().clear();
        randomSearchResults.clear();
        clearCanvasSearch();
        arrayTextSearch.setText("");
        randomArraySearch = new int[0];
    }

    private int startOf(int idx) {
        int n = randomArraySearch.length;
        if (n <= VISIBLE) return 0;

        int s = idx - (VISIBLE / 2);
        if (s < 0) s = 0;

        int maxS = n - VISIBLE;
        if (s > maxS) s = maxS;

        return s;
    }

    private void draw(int start, int highlight, String msg, int att, int maxAtt) {
        GraphicsContext gc = canvasSearch.getGraphicsContext2D();

        // fondo
        gc.setFill(Color.web("#f2f2f2"));
        gc.fillRect(0, 0, canvasSearch.getWidth(), canvasSearch.getHeight());

        if (randomArraySearch == null || randomArraySearch.length == 0) return;

        // textos
        gc.setFont(Font.font(14));
        gc.setFill(Color.BLACK);

        String vtxt = (txtValueSearch.getText() == null) ? "" : txtValueSearch.getText().trim();
        if (!vtxt.isBlank()) {
            gc.fillText("Buscando valor: " + vtxt, 60, 35);
            if (maxAtt > 0) gc.fillText("Intentos realizados: " + att + " / " + maxAtt, 60, 55);
        }

        if (msg != null && !msg.isBlank()) {
            gc.setFill(highlight >= 0 ? Color.web("#0a8f08") : Color.web("#d10000"));
            gc.fillText(msg, 60, 80);
        }

        // cajas
        double x0 = 30, y0 = 110, w = 55, h = 45, gap = 6;

        int win = Math.min(VISIBLE, randomArraySearch.length);
        int s = Math.max(0, Math.min(start, Math.max(0, randomArraySearch.length - win)));

        gc.setFont(Font.font(13));

        for (int k = 0; k < win; k++) {
            int i = s + k;
            double x = x0 + k * (w + gap);

            boolean hi = (i == highlight);

            gc.setFill(hi ? Color.web("#8ef28e") : Color.WHITE);
            gc.fillRect(x, y0, w, h);

            gc.setStroke(Color.web("#444"));
            gc.setLineWidth(1.2);
            gc.strokeRect(x, y0, w, h);

            gc.setFill(Color.BLACK);
            gc.fillText(String.valueOf(randomArraySearch[i]), x + 18, y0 + 28);

            gc.setFill(Color.web("#777"));
            gc.fillText("[" + i + "]", x + 14, y0 + 70);
        }
    }

    private void clearCanvasSearch() {
        GraphicsContext gc = canvasSearch.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasSearch.getWidth(), canvasSearch.getHeight());
    }

    private String previewArray(int[] arr) {
        if (arr == null) return "[]";
        if (arr.length <= 20) return java.util.Arrays.toString(arr);

        StringBuilder sb = new StringBuilder("[");
        int left = Math.min(12, arr.length);
        for (int i = 0; i < left; i++) {
            sb.append(arr[i]);
            if (i < left - 1) sb.append(", ");
        }
        sb.append(", ...]");
        return sb.toString();
    }



    // TAB: Doubly Linked List

    private void setupDoublyLinkedListTab() {

        // ChoiceBox puestos (como en imagen 2)
        bxJobPosition.setItems(FXCollections.observableArrayList(
                "Informático/a", "Doctor/a", "Docente", "Administrador/a",
                "Periodista", "Arquitecto/a"
        ));
        bxJobPosition.getSelectionModel().selectFirst();

        dpHireDate.setValue(LocalDate.now());


        colPositionDoubly.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPuesto.setCellValueFactory(new PropertyValueFactory<>("jobPosition"));
        colFechaIngreso.setCellValueFactory(new PropertyValueFactory<>("hireDate"));

        colID.setItems(doublyData);

        // Botones
        btnAgregarDoble.setOnAction(e -> addEmployeeDoubly());
        btnSearchDoble.setOnAction(e -> searchEmployeeDoubly());
        btnClearListDoble.setOnAction(e -> clearDoublyTab());

        btnPrimero.setOnAction(e -> goFirstEmployee());
        btnUltimo.setOnAction(e -> goLastEmployee());
        btnAnterior.setOnAction(e -> goPrevEmployee());
        btnSiguiente.setOnAction(e -> goNextEmployee());

        btnEliminarInicio.setOnAction(e -> removeFirstEmployee());
        btnEliminarFinal.setOnAction(e -> removeLastEmployee());
        btnEliminarDoble.setOnAction(e -> removeEmployeeById());

    }

    private void addEmployeeDoubly() {
        String id = txtId.getText() == null ? "" : txtId.getText().trim();
        String name = txtName.getText() == null ? "" : txtName.getText().trim();
        String job = (String) bxJobPosition.getValue();
        LocalDate hire = dpHireDate.getValue();

        if (id.isBlank() || name.isBlank() || job == null || hire == null) {
            showAlert("Error", "Debe completar: Id, Name, Job Position y Hire date.");
            return;
        }

        // Solo letras y espacios
        if (!name.matches("[\\p{L} ]+")) {
            showAlert("Error", "El nombre solo puede contener letras y espacios.");
            txtName.requestFocus();
            return;
        }

        // El ID solo puede tener números
        if (!id.matches("\\d+")) {
            showAlert("Error", "El ID solo puede contener números.");
            txtName.requestFocus();
            return;
        }

        // Que el ID no exista ya en la lista
        if (existsEmployeeId(id)) {
            showAlert("Error", "Ya existe un empleado con Id: " + id);
            return;
        }

        Employee emp = new Employee(name, id, 0, 0.0, 0.0, job, hire);
        doublyList.add(emp);
        doublyData.add(emp);

        currentEmployeeNode = doublyList.getTail();

        listViewOperationsListDoubly.getItems().add("add(" + id + ") HEAD ↔ [Empleado ID: " + id + "] ↔ ...");

        refreshDoublyUI("Insertado item: " + id);

    }

    private boolean existsEmployeeId(String id) {
        try {
            if (doublyList.isEmpty()) return false;
            Node<Employee> aux = doublyList.getHead();
            while (aux != null) {
                if (aux.data != null && id.equals(aux.data.getId())) return true;
                aux = aux.next;
            }
        } catch (Exception ignored) {}
        return false;
    }

    private void searchEmployeeDoubly() {
        String id = txtId.getText() == null ? "" : txtId.getText().trim();
        if (id.isBlank()) {
            showAlert("Error", "Digite el Id a buscar.");
            return;
        }

        Node<Employee> found = findById(id);
        if (found == null) {
            listViewOperationsListDoubly.getItems().add("search(" + id + ") → NO ENCONTRADO");
            showAlert("Resultado", "No se encontró el empleado con Id: " + id);
            return;
        }

        currentEmployeeNode = found;
        listViewOperationsListDoubly.getItems().add("search(" + id + ") → ENCONTRADO");
        refreshDoublyUI("Encontrado: " + id);
        selectEmployeeInTable(found.data);
    }

    private Node<Employee> findById(String id) {
        try {
            if (doublyList.isEmpty()) return null;
            Node<Employee> aux = doublyList.getHead();
            while (aux != null) {
                if (aux.data != null && id.equals(aux.data.getId())) return aux;
                aux = aux.next;
            }
        } catch (Exception ignored) {}
        return null;
    }

    private void goFirstEmployee() {
        if (doublyList.isEmpty()) { showAlert("Info", "Lista vacía"); return; }
        currentEmployeeNode = doublyList.getHead();
        refreshDoublyUI("Primero");
        selectEmployeeInTable(currentEmployeeNode.data);
    }

    private void goLastEmployee() {
        if (doublyList.isEmpty()) { showAlert("Info", "Lista vacía"); return; }
        currentEmployeeNode = doublyList.getTail();
        refreshDoublyUI("Último");
        selectEmployeeInTable(currentEmployeeNode.data);
    }

    private void goPrevEmployee() {
        if (currentEmployeeNode == null) { showAlert("Info", "No hay selección"); return; }
        if (currentEmployeeNode.prev == null) { showAlert("Info", "Ya está en el primero"); return; }
        currentEmployeeNode = currentEmployeeNode.prev;
        refreshDoublyUI("Anterior");
        selectEmployeeInTable(currentEmployeeNode.data);
    }

    private void goNextEmployee() {
        if (currentEmployeeNode == null) { showAlert("Info", "No hay selección"); return; }
        if (currentEmployeeNode.next == null) { showAlert("Info", "Ya está en el último"); return; }
        currentEmployeeNode = currentEmployeeNode.next;
        refreshDoublyUI("Siguiente");
        selectEmployeeInTable(currentEmployeeNode.data);
    }

    private void removeFirstEmployee() {
        try {
            Employee removed = doublyList.removeFirst();
            doublyData.remove(removed);
            listViewOperationsListDoubly.getItems().add("removeFirst() → eliminado ID: " + removed.getId());

            currentEmployeeNode = doublyList.isEmpty() ? null : doublyList.getHead();
            refreshDoublyUI("Eliminado inicio");
        } catch (ListException e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void removeLastEmployee() {
        try {
            Employee removed = doublyList.removeLast();
            doublyData.remove(removed);
            listViewOperationsListDoubly.getItems().add("removeLast() → eliminado ID: " + removed.getId());

            currentEmployeeNode = doublyList.isEmpty() ? null : doublyList.getTail();
            refreshDoublyUI("Eliminado final");
        } catch (ListException e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void removeEmployeeById() {
        String id = txtId.getText() == null ? "" : txtId.getText().trim();
        if (id.isBlank()) {
            showAlert("Error", "Digite el Id a eliminar.");
            return;
        }

        Node<Employee> found = findById(id);
        if (found == null) {
            showAlert("Resultado", "No existe el Id: " + id);
            return;
        }

        try {
            doublyList.remove(found.data);
            doublyData.remove(found.data);
            listViewOperationsListDoubly.getItems().add("remove(" + id + ") → eliminado");

            currentEmployeeNode = doublyList.isEmpty() ? null : doublyList.getHead();
            refreshDoublyUI("Eliminado: " + id);
        } catch (ListException e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void clearDoublyTab() {
        doublyList.clear();
        doublyData.clear();
        currentEmployeeNode = null;

        txtId.clear();
        txtName.clear();
        dpHireDate.setValue(LocalDate.now());
        bxJobPosition.getSelectionModel().selectFirst();

        listViewOperationsListDoubly.getItems().clear();
        txAreaNodeRepreDoubly.setText("");
        txtInsertadoIn1.setText("");

        clearCanvasDoubly();
    }

    private void refreshDoublyUI(String status) {
        // Representación
        txAreaNodeRepreDoubly.setText(doublyList.toString());

        // Insertad
        txtInsertadoIn1.setText(status);

        // dibujar
        drawDoublyList();
    }

    private void selectEmployeeInTable(Employee emp) {
        if (emp == null) return;
        colID.getSelectionModel().select(emp);
        colID.scrollTo(emp);
    }

    private void clearCanvasDoubly() {
        GraphicsContext gc = canvasListDoubly.getGraphicsContext2D();
        gc.clearRect(0, 0, canvasListDoubly.getWidth(), canvasListDoubly.getHeight());
    }

    private void drawDoublyList() {
        GraphicsContext gc = canvasListDoubly.getGraphicsContext2D();
        gc.setFill(Color.web("#f2f2f2"));
        gc.fillRect(0, 0, canvasListDoubly.getWidth(), canvasListDoubly.getHeight());

        if (doublyList.isEmpty()) return;

        double startX = 60;
        double y = canvasListDoubly.getHeight() / 2.0;
        double nodeW = 90;
        double nodeH = 55;
        double gap = 40;


        // HEAD + Flecha hacía el primer nodo
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(14));
        gc.fillText("HEAD", startX - 50, y + 5);

        {
            double hx1 = startX - 15;
            double hx2 = startX;
            gc.setStroke(Color.web("#f5a623"));
            gc.setLineWidth(2);
            gc.strokeLine(hx1, y, hx2, y);
            // punta de flecha hacia el nodo
            gc.strokeLine(hx2 - 6, y - 6, hx2, y);
            gc.strokeLine(hx2 - 6, y + 6, hx2, y);
        }

        Node<Employee> aux = doublyList.getHead();
        double x = startX;

        while (aux != null) {
            boolean highlight = (aux == currentEmployeeNode);

            gc.setFill(highlight ? Color.web("#8ef28e") : Color.web("#1f2a44"));
            gc.fillRoundRect(x, y - nodeH / 2, nodeW, nodeH, 8, 8);

            gc.setStroke(Color.BLACK);
            gc.strokeRoundRect(x, y - nodeH / 2, nodeW, nodeH, 8, 8);

            // Representación del ID en el canvas
            gc.setFill(Color.WHITE);
            String id = (aux.data != null && aux.data.getId() != null) ? aux.data.getId() : "?";
            String text = "👤: " + id;
            gc.fillText(text, x + 10, y + 5);


            //Dibujado de flechas
            if (aux.next != null) {
                double ax1 = x + nodeW;
                double ax2 = x + nodeW + gap;

                gc.setStroke(Color.web("#f5a623"));
                gc.setLineWidth(2);

                gc.strokeLine(ax1, y, ax2, y);
                gc.strokeLine(ax2 - 6, y - 6, ax2, y);
                gc.strokeLine(ax2 - 6, y + 6, ax2, y);

                gc.strokeLine(ax2, y + 12, ax1, y + 12);
                gc.strokeLine(ax1 + 6, y + 12 - 6, ax1, y + 12);
                gc.strokeLine(ax1 + 6, y + 12 + 6, ax1, y + 12);
            }

            x += nodeW + gap;
            aux = aux.next;
        }

        //NULL + Flecha hacía Null
        {
            double lx1 = x - gap;
            double lx2 = x;
            gc.setStroke(Color.web("#f5a623"));
            gc.setLineWidth(2);
            gc.strokeLine(lx1, y, lx2, y);
            // punta hacia NULL
            gc.strokeLine(lx2 - 6, y - 6, lx2, y);
            gc.strokeLine(lx2 - 6, y + 6, lx2, y);
        }

        gc.setFill(Color.BLACK);
        gc.fillText("NULL", x + 10, y + 5);
    }


}