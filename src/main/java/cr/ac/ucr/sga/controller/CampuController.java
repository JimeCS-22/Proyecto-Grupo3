package cr.ac.ucr.sga.controller;


import cr.ac.ucr.sga.graphics.CampusMap;
import cr.ac.ucr.sga.model.Node;
import cr.ac.ucr.sga.model.entities.Building;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


import java.net.URL;
import java.util.ResourceBundle;

public class CampuController implements Initializable {
    @javafx.fxml.FXML
    private TextField searchField;
    @javafx.fxml.FXML
    private Pane mapPane;
    @javafx.fxml.FXML
    private VBox infoPanel;
    @javafx.fxml.FXML
    private Label statusLabel;
    @javafx.fxml.FXML
    private Label zoomLabel;
    @javafx.fxml.FXML
    private VBox buildingList;

    private CampusMap campusMap;
    @javafx.fxml.FXML
    private Button btnBFS;
    @javafx.fxml.FXML
    private Button btnDFS;
    @FXML
    private Label lblTours;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        statusLabel.setText("Campus cargado");
        zoomLabel.setText("100%");

        createPanels();

        Rectangle border = new Rectangle(1300,800);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.LIGHTGRAY);
        border.getStrokeDashArray().addAll(8.0,8.0);

        mapPane.getChildren().add(border);

        campusMap = new CampusMap();

        mapPane.getChildren().add(campusMap);

        mapPane.setOnScroll(event -> {

            if(event.getDeltaY() > 0){

                campusMap.zoom(1.1);

            }else{

                campusMap.zoom(0.9);

            }

            zoomLabel.setText(
                    String.format("%.0f%%",
                            campusMap.getZoom()*100)
            );

        });

        loadBuildingList("");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            loadBuildingList(newValue);

        });
        buildingList.getChildren().add(lblTours);
        buildingList.getChildren().add(btnDFS);
        buildingList.getChildren().add(btnBFS);
        btnBFS.setOnAction(e-> runBFS());
        btnBFS.setOnAction(e-> runBFS());
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

                buildingList.getChildren().add(
                        createBuildingItem(building)
                );

                matches++;
                found = building;

            }

            if (matches == 1) {

                campusMap.selectBuilding(found);

                showBuildingInformation(found);

            }

            if (matches == 0) {
                infoPanel.getChildren().clear();

                Label title = new Label("ℹ INFORMACIÓN");
                title.getStyleClass().add("panel-title");

                infoPanel.getChildren().add(title);
            }

            current = current.next;
        }

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

        Label coordinates = new Label(
                "Ubicación: (" +
                        building.getX() +
                        ", " +
                        building.getY() +
                        ")"
        );

        infoPanel.getChildren().addAll(
                title,
                icon,
                name,
                id,
                description,
                coordinates
        );

    }

}
