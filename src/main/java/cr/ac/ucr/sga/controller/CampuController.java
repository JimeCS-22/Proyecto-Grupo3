package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.graphics.CampusMap;
import cr.ac.ucr.sga.model.Node;
import cr.ac.ucr.sga.model.entities.Building;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class CampuController implements Initializable {

    @FXML private TextField searchField;
    @FXML private Pane mapPane;
    @FXML private VBox infoPanel;
    @FXML private Label statusLabel;
    @FXML private Label zoomLabel;
    @FXML private VBox buildingList;
    @FXML private Button btnBFS;
    @FXML private Button btnDFS;
    @FXML private Label lblTours;
    @FXML private ScrollPane scrollPane;
    @FXML private Button btnCalcularRuta;
    @FXML private ComboBox<Building> cmbOrigen;
    @FXML private ComboBox<Building> cmbDestino;

    private CampusMap campusMap;
    private double mouseOldX;
    private double mouseOldY;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statusLabel.setText("Campus cargado");
        zoomLabel.setText("100%");

        createPanels();

        Rectangle border = new Rectangle(1300, 800);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.LIGHTGRAY);
        border.getStrokeDashArray().addAll(8.0, 8.0);

        mapPane.getChildren().add(border);

        campusMap = new CampusMap();
        mapPane.getChildren().add(campusMap);

        if (scrollPane != null) {
            scrollPane.setPannable(true);
        }

        mapPane.setOnScroll(event -> {
            if (event.getDeltaY() > 0) {
                campusMap.zoom(1.1);
            } else {
                campusMap.zoom(0.9);
            }

            zoomLabel.setText(String.format("%.0f%%", campusMap.getZoom() * 100));
            event.consume();
        });

        campusMap.setOnMousePressed(e -> {
            mouseOldX = e.getSceneX();
            mouseOldY = e.getSceneY();
        });

        campusMap.setOnMouseDragged(e -> {
            double dx = e.getSceneX() - mouseOldX;
            double dy = e.getSceneY() - mouseOldY;

            campusMap.setTranslateX(campusMap.getTranslateX() + dx);
            campusMap.setTranslateY(campusMap.getTranslateY() + dy);

            mouseOldX = e.getSceneX();
            mouseOldY = e.getSceneY();
        });

        loadBuildingList("");

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> loadBuildingList(newValue)
        );

        btnBFS.setOnAction(e -> runBFS());
        btnDFS.setOnAction(e -> runDFS());

        loadComboBoxes();
        btnCalcularRuta.setOnAction(this::calculateShortestPath);
    }

    @FXML
    private void runBFS() {
        statusLabel.setText("Recorrido BFS en ejecución...");
        campusMap.animateBFS();
    }

    @FXML
    private void runDFS() {
        statusLabel.setText("Recorrido DFS en ejecución...");
        campusMap.animateDFS();
    }

    private void createPanels() {
        Label buildingsTitle = new Label("📍 EDIFICIOS");
        buildingsTitle.getStyleClass().add("panel-title");
        buildingList.getChildren().add(buildingsTitle);

        Label infoTitle = new Label("ℹ INFORMACIÓN");
        infoTitle.getStyleClass().add("panel-title");
        infoPanel.getChildren().add(infoTitle);
    }

    private HBox createBuildingItem(Building building) {
        Label icon = new Label(building.getIcon());
        icon.getStyleClass().add("building-icon");

        Label name = new Label(building.getName());
        name.getStyleClass().add("building-name");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label arrow = new Label("➜");
        arrow.getStyleClass().add("building-arrow");

        HBox item = new HBox(10);
        item.getChildren().addAll(icon, name, spacer, arrow);
        item.getStyleClass().add("building-item");
        item.setAlignment(Pos.CENTER_LEFT);

        item.setOnMouseClicked(e -> {
            campusMap.selectBuilding(building);
            showBuildingInformation(building);
        });

        return item;
    }

    private void loadBuildingList(String filter) {
        buildingList.getChildren().clear();

        Label buildingsTitle = new Label("📍 EDIFICIOS");
        buildingsTitle.getStyleClass().add("panel-title");
        buildingList.getChildren().add(buildingsTitle);

        Node<Building> current = campusMap.getBuildings().getHead();
        Building found = null;
        int matches = 0;

        while (current != null) {
            Building building = current.data;
            if (building.getName().toLowerCase().contains(filter.toLowerCase())) {
                buildingList.getChildren().add(createBuildingItem(building));
                matches++;
                found = building;
            }
            current = current.next;
        }

        if (matches == 1 && found != null) {
            campusMap.selectBuilding(found);
            showBuildingInformation(found);
        }

        if (matches == 0) {
            infoPanel.getChildren().clear();
            Label title = new Label("ℹ INFORMACIÓN");
            title.getStyleClass().add("panel-title");
            infoPanel.getChildren().add(title);
        }

        buildingList.getChildren().add(lblTours);
        buildingList.getChildren().add(btnBFS);
        buildingList.getChildren().add(btnDFS);
    }

    private void showBuildingInformation(Building building) {
        infoPanel.getChildren().clear();

        Label title = new Label("ℹ INFORMACIÓN");
        title.getStyleClass().add("panel-title");

        Label icon = new Label(building.getIcon());
        icon.setStyle("-fx-font-size:40;");

        Label name = new Label(building.getName());
        name.setStyle("-fx-font-size:18; -fx-font-weight:bold;");

        Label id = new Label("ID: " + building.getId());
        Label description = new Label(building.getDescription());
        Label coordinates = new Label("Ubicación: (" + building.getX() + ", " + building.getY() + ")");

        infoPanel.getChildren().addAll(title, icon, name, id, description, coordinates);
    }

    private void loadComboBoxes() {
        Node<Building> aux = campusMap.getBuildings().getHead();
        while (aux != null) {
            cmbOrigen.getItems().add(aux.data);
            cmbDestino.getItems().add(aux.data);
            aux = aux.next;
        }
    }

    @FXML
    public void calculateShortestPath(ActionEvent event) {
        Building origen = cmbOrigen.getValue();
        Building destino = cmbDestino.getValue();

        if (origen == null || destino == null) {
            statusLabel.setText("Seleccione origen y destino");
            return;
        }

        try {
            statusLabel.setText("Calculando ruta...");
            LinkedList<Building> ruta = campusMap.shortestPath(origen, destino);

            if (ruta == null || ruta.size() == 0) {
                statusLabel.setText("No existe una ruta entre los edificios");
                return;
            }

            campusMap.animatePath(ruta);
            statusLabel.setText("Ruta calculada exitosamente");
        } catch (Exception ex) {
            statusLabel.setText("Error calculando ruta: " + ex.getMessage());
        }
    }
}