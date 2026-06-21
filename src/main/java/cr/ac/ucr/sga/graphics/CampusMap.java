package cr.ac.ucr.sga.graphics;

import cr.ac.ucr.sga.model.Node;
import cr.ac.ucr.sga.model.entities.Building;
import cr.ac.ucr.sga.model.structures.graph.AdjacencyListGraph;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

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
                        620
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

    private void addBuilding(Building building) {
        buildings.add(building);
        try {
            graph.addVertex(building);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BuildingView view = new BuildingView(building);
        buildingViews.add(view);
        getChildren().add(view);
    }

    private void connect(Building a, Building b, int weight) {
        try {
            graph.addEdgeAndWeightInt(a, b, weight);

        } catch (Exception e) {
            e.printStackTrace();
        }
        ConnectionView connection = new ConnectionView(a, b);
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

    public void animateBFS() {

        try {

            resetTraversal();

            LinkedList<Building> recorrido =
                    graph.bfsTraversal();

            animateTraversal(recorrido);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void animateDFS() {
        try {

            resetTraversal();

            LinkedList<Building> recorrido = graph.dfsTraversal();

            animateTraversal(recorrido);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void animateTraversal(LinkedList<Building> recorrido) {

        Timeline timeline = new Timeline();

        Node<Building> aux = recorrido.getHead();

        int tiempo = 0;

        while (aux != null) {

            Building edificio = aux.data;

            timeline.getKeyFrames().add(

                    new KeyFrame(

                            Duration.seconds(tiempo * 0.7),

                            e -> {

                                BuildingView view = findView(edificio);

                                if (view != null) {
                                    view.visit();
                                }

                            })

            );

            tiempo++;

            aux = aux.next;
        }

        timeline.play();

    }

    private BuildingView findView(Building building) {

        Node<BuildingView> aux = buildingViews.getHead();

        while (aux != null) {

            if (aux.data.getBuilding().equals(building)) {
                return aux.data;
            }

            aux = aux.next;
        }

        return null;
    }

    public void resetTraversal() {

        Node<BuildingView> aux = buildingViews.getHead();

        while(aux != null){

            aux.data.unselect();

            aux = aux.next;
        }
    }

    public void clearPath() {

        Node<ConnectionView> aux =
                connections.getHead();

        while(aux != null){

            aux.data.clear();

            aux = aux.next;
        }

        resetTraversal();
    }

    private void highlightConnection(
            Building a,
            Building b){

        Node<ConnectionView> aux =
                connections.getHead();

        while(aux != null){

            if(aux.data.connects(a,b)){

                aux.data.highlight();
                return;
            }

            aux = aux.next;
        }
    }

    public void animatePath(
            LinkedList<Building> path){

        clearPath();

        Timeline timeline = new Timeline();

        Node<Building> aux = path.getHead();

        int time = 0;

        while(aux != null){

            Building current = aux.data;

            timeline.getKeyFrames().add(

                    new KeyFrame(

                            Duration.seconds(time),

                            e -> {

                                BuildingView view =
                                        findView(current);

                                if(view != null){

                                    view.visit();
                                }
                            }
                    )
            );

            if(aux.next != null){

                Building next =
                        aux.next.data;

                timeline.getKeyFrames().add(

                        new KeyFrame(

                                Duration.seconds(time),

                                e -> highlightConnection(
                                        current,
                                        next)
                        )
                );
            }

            time++;

            aux = aux.next;
        }

        timeline.play();
    }

    public LinkedList<Building> shortestPath(Building origen, Building destino) throws Exception {
        int start = graph.indexOf(origen);
        int end = graph.indexOf(destino);
        if (start == -1 || end == -1)
            throw new Exception("Edificio no encontrado en grafo");

        int n = graph.size();
        int[] dist = new int[n];
        int[] prev = new int[n];
        boolean[] visited = new boolean[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
            visited[i] = false;
        }
        dist[start] = 0;

        for (int i = 0; i < n; i++) {
            int u = -1;
            int minDist = Integer.MAX_VALUE;

            // Encontrar el nodo no visitado con menor distancia
            for (int j = 0; j < n; j++) {
                if (!visited[j] && dist[j] < minDist) {
                    minDist = dist[j];
                    u = j;
                }
            }
            if (u == -1 || u == end) break;
            visited[u] = true;

            // Recorrer vecinos usando la lista de adyacencia
            Node<Building> neighborNode = graph.getVertexByIndex(u).headNode;
            while (neighborNode != null) {
                Building vecino = neighborNode.data;
                int v = graph.indexOf(vecino);

                if (v != -1 && !visited[v]) {
                    // Obtener el peso correctamente
                    int weight = graph.getWeight(origen, vecino); // Esto es incorrecto, debe ser desde el nodo actual (u)
                    // Corrección: Obtener peso entre u y v
                    int pesoArista = graph.getWeight(graph.getVertexByIndex(u).data, vecino);

                    if (dist[u] != Integer.MAX_VALUE && dist[u] + pesoArista < dist[v]) {
                        dist[v] = dist[u] + pesoArista;
                        prev[v] = u;
                    }
                }
                neighborNode = neighborNode.neighbor;
            }
        }

        // Reconstruir la ruta
        LinkedList<Building> ruta = new LinkedList<>();
        int at = end;
        while (at != -1) {
            ruta.addFirst(graph.getVertexByIndex(at).data);
            at = prev[at];
        }

        if (ruta.size() == 0 || !ruta.getHead().data.equals(origen)) {
            return null; // No hay camino
        }

        System.out.println("Ruta encontrada:");
        Node<Building> curr = ruta.getHead();
        while (curr != null) {
            System.out.println(" - " + curr.data.getName());
            curr = curr.next;
        }

        return ruta;
    }

    private Building findBuilding(String nombre){

        Node<Building> aux =
                buildings.getHead();

        while(aux != null){

            if(aux.data.getName().equals(nombre))
                return aux.data;

            aux = aux.next;
        }

        return null;
    }
}

