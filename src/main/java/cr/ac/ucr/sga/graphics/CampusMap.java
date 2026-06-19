package cr.ac.ucr.sga.graphics;

import cr.ac.ucr.sga.model.Node;
import cr.ac.ucr.sga.model.entities.Building;
import cr.ac.ucr.sga.model.structures.graph.AdjacencyListGraph;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import javafx.scene.layout.Pane;

public class CampusMap extends Pane {

    private AdjacencyListGraph<Building> graph;

    private LinkedList<Building> buildings;

    private LinkedList<BuildingView> buildingViews;

    private LinkedList<ConnectionView> connections;

    private double zoom = 1.0;

    public CampusMap(){

        buildings = new LinkedList<>();
        buildingViews = new LinkedList<>();
        connections = new LinkedList<>();

        graph = new AdjacencyListGraph<>(20,false);

        setPrefSize(1500,900);

        createDemoCampus();

    }

    private void createDemoCampus(){

        Building biblioteca =
                new Building(
                        "B01",
                        "Biblioteca",
                        "Biblioteca Central",
                        "📚",
                        250,
                        150
                );

        Building ingenieria =
                new Building(
                        "B02",
                        "Ingeniería",
                        "Facultad Ingeniería",
                        "⚙",
                        650,
                        150
                );

        Building AulaMagna =
                new Building("B03",
                        "Aula Magna",
                        "Aula Magna",
                        "\uD83C\uDFAD",
                        1000,
                        150
                        );

        Building Laboratorios =
                new Building(
                        "B04",
                        "Laboratorios",
                        "Laboratorios Universitarios",
                        "\uD83E\uDDEA",
                        250,
                        420
                );

        Building cafeteria =
                new Building(
                        "B05",
                        "Cafetería",
                        "Comedor Universitario",
                        "🍔",
                        650,
                        420
                );

        Building Administracion=
                new Building(
                        "B06",
                        "Administracion",
                        "Administracion Universitarios",
                        "\uD83C\uDFE2",
                        1000,
                        420
                );

        Building Gimnasio=
                new Building(
                        "B07",
                        "Gimnasio",
                        "Gimnasio Universitario",
                        "\uD83C\uDFCB",
                        650,
                        420
                );

        addBuilding(biblioteca);
        addBuilding(ingenieria);
        addBuilding(AulaMagna);
        addBuilding(Laboratorios);
        addBuilding(cafeteria);
        addBuilding(Administracion);
        addBuilding(Gimnasio);

        connect(biblioteca, ingenieria, 120);

        connect(ingenieria, AulaMagna, 80);

        connect(biblioteca, Laboratorios, 90);

        connect(ingenieria, cafeteria, 60);

        connect(AulaMagna, Administracion, 75);

        connect(Laboratorios, cafeteria, 50);

        connect(cafeteria, Administracion, 45);

        connect(cafeteria, Gimnasio, 40);


    }

    private void addBuilding(Building building){

        buildings.add(building);

        try{
            graph.addVertex(building);
        }catch(Exception e){
            e.printStackTrace();
        }

        BuildingView view = new BuildingView(building);

        buildingViews.add(view);

        getChildren().add(view);

    }
    private void connect(Building a,
                         Building b,
                         int weight) {

        try {

            graph.addEdge(a, b);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ConnectionView connection =
                new ConnectionView(
                        a.getCenterX(),
                        a.getCenterY(),
                        b.getCenterX(),
                        b.getCenterY()
                );

        connections.add(connection);

        getChildren().add(0, connection);
    }

    public LinkedList<Building> getBuildings() {
        return buildings;
    }

    public LinkedList<BuildingView> getBuildingViews() {
        return buildingViews;
    }

    public LinkedList<ConnectionView> getConnections() {
        return connections;
    }

    public void selectBuilding(Building building) {

        Node<BuildingView> current = buildingViews.getHead();

        while (current != null) {

            BuildingView view = current.data;

            if (view.getBuilding().equals(building)) {

                view.select();

            } else {

                view.unselect();

            }

            current = current.next;
        }

    }

    public void zoom(double factor) {

        zoom *= factor;

        if (zoom < 0.6)
            zoom = 0.6;

        if (zoom > 2.5)
            zoom = 2.5;

        setScaleX(zoom);
        setScaleY(zoom);

    }

    public double getZoom() {
        return zoom;
    }
}

