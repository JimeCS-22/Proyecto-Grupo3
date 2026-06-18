package cr.ac.ucr.sga.graphics;

import cr.ac.ucr.sga.model.Node;
import cr.ac.ucr.sga.model.entities.Building;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import javafx.scene.layout.Pane;

public class CampusMap extends Pane {

    private LinkedList<Building> buildings;

    private LinkedList<BuildingView> buildingViews;

    private LinkedList<ConnectionView> connections;

    private double zoom = 1.0;

    public CampusMap(){

        buildings = new LinkedList<>();
        buildingViews = new LinkedList<>();
        connections = new LinkedList<>();

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

        buildings.add(biblioteca);
        buildings.add(ingenieria);
        buildings.add(AulaMagna);
        buildings.add(Laboratorios);
        buildings.add(cafeteria);
        buildings.add(Administracion);
        buildings.add(Gimnasio);

        BuildingView bibliotecaView = new BuildingView(biblioteca);
        BuildingView ingenieriaView = new BuildingView(ingenieria);
        BuildingView cafeteriaView = new BuildingView(cafeteria);
        BuildingView AdministracionView = new BuildingView(Administracion);
        BuildingView GimnasioView = new BuildingView(Gimnasio);
        BuildingView AulaMagnaView = new BuildingView(AulaMagna);
        BuildingView LaboratoriosView = new BuildingView(Laboratorios);

        buildingViews.add(bibliotecaView);
        buildingViews.add(ingenieriaView);
        buildingViews.add(cafeteriaView);
        buildingViews.add(AdministracionView);
        buildingViews.add(GimnasioView);
        buildingViews.add(AulaMagnaView);
        buildingViews.add(LaboratoriosView);

        ConnectionView c1 = new ConnectionView(
                335,
                195,
                735,
                195
        );

        ConnectionView c2 = new ConnectionView(
                735,
                195,
                1135,
                195
        );

        ConnectionView c3 = new ConnectionView(
                335,
                195,
                335,
                495
        );
        ConnectionView c4 = new ConnectionView(
                735,
                195,
                735,
                495
        );
        ConnectionView c5 = new ConnectionView(
                1135,
                195,
                1135,
                495
        );
        ConnectionView c6 = new ConnectionView(
                335,
                495,
                735,
                495
        );
        ConnectionView c7 = new ConnectionView(
                735,
                495,
                1135,
                495
        );
        ConnectionView c8 = new ConnectionView(
                735,
                495,
                735,
                765
        );

        connections.add(c1);
        connections.add(c2);
        connections.add(c3);
        connections.add(c4);
        connections.add(c5);
        connections.add(c6);
        connections.add(c7);
        connections.add(c8);

        getChildren().addAll(
                c1,
                c2,
                c3,
                c4,
                c5,
                c6,
                c7,
                c8
        );


        getChildren().addAll(
                bibliotecaView,
                ingenieriaView,
                cafeteriaView,
                AdministracionView,
                GimnasioView,
                AulaMagnaView,
                LaboratoriosView
        );

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

