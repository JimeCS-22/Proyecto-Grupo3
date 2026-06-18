package cr.ac.ucr.sga.model.structures.MST;

import cr.ac.ucr.sga.model.structures.graph.AdjacencyListGraph;
import cr.ac.ucr.sga.model.structures.graph.AdjacencyMatrixGraph;
import cr.ac.ucr.sga.model.structures.graph.LinkedGraph;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;

public class Prim {

    private LinkedList<PrimStep> steps;
    private double totalWeight;

    public Prim() {
        steps = new LinkedList<>();
        totalWeight = 0;
    }

    public LinkedList<PrimStep> getSteps() {
        return steps;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    //=================================================
    // MATRIZ
    //=================================================
    public void executeMatrixGraph(
            AdjacencyMatrixGraph<String> graph,
            int start
    ) throws Exception {

        steps.clear();
        totalWeight = 0;

        int n = graph.counter;

        int[] key = new int[n];
        int[] parent = new int[n];
        boolean[] inMST = new boolean[n];

        for (int i = 0; i < n; i++) {
            key[i] = Integer.MAX_VALUE;
            parent[i] = -1;
            inMST[i] = false;
        }

        key[start] = 0;

        steps.add(new PrimStep(
                start,
                key,
                parent,
                inMST,
                "Inicio en " + graph.getVertexByIndex(start).data,
                totalWeight
        ));

        for (int count = 0; count < n; count++) {

            int u = minKey(key, inMST, n);

            if (u == -1)
                break;

            inMST[u] = true;

            if (parent[u] != -1) {
                totalWeight += key[u];
            }

            steps.add(new PrimStep(
                    u,
                    key,
                    parent,
                    inMST,
                    "Se agrega " + graph.getVertexByIndex(u).data + " al MST",
                    totalWeight
            ));

            for (int v = 0; v < n; v++) {

                if (!inMST[v]
                        && graph.containsEdge(
                        graph.getVertexByIndex(u).data,
                        graph.getVertexByIndex(v).data)) {

                    int weight = graph.getWeight(
                            graph.getVertexByIndex(u).data,
                            graph.getVertexByIndex(v).data);

                    if (weight < key[v]) {

                        key[v] = weight;
                        parent[v] = u;

                        steps.add(new PrimStep(
                                u,
                                key,
                                parent,
                                inMST,
                                "Actualizar "
                                        + graph.getVertexByIndex(v).data
                                        + " con peso "
                                        + weight,
                                totalWeight
                        ));
                    }
                }
            }
        }

        steps.add(new PrimStep(
                -1,
                key,
                parent,
                inMST,
                "Prim finalizado",
                totalWeight
        ));
    }

    //=================================================
    // LISTA
    //=================================================
    public void executeListGraph(
            AdjacencyListGraph<String> graph,
            int start
    ) throws Exception {

        steps.clear();
        totalWeight = 0;

        int n = graph.counter;

        int[] key = new int[n];
        int[] parent = new int[n];
        boolean[] inMST = new boolean[n];

        for (int i = 0; i < n; i++) {
            key[i] = Integer.MAX_VALUE;
            parent[i] = -1;
            inMST[i] = false;
        }

        key[start] = 0;

        steps.add(new PrimStep(
                start,
                key,
                parent,
                inMST,
                "Inicio en " + graph.getVertexByIndex(start).data,
                totalWeight
        ));

        for (int count = 0; count < n; count++) {

            int u = minKey(key, inMST, n);

            if (u == -1)
                break;

            inMST[u] = true;

            if (parent[u] != -1) {
                totalWeight += key[u];
            }

            steps.add(new PrimStep(
                    u,
                    key,
                    parent,
                    inMST,
                    "Se agrega " + graph.getVertexByIndex(u).data + " al MST",
                    totalWeight
            ));

            for (int v = 0; v < n; v++) {

                if (!inMST[v]
                        && graph.containsEdge(
                        graph.getVertexByIndex(u).data,
                        graph.getVertexByIndex(v).data)) {

                    int weight = graph.getWeight(
                            graph.getVertexByIndex(u).data,
                            graph.getVertexByIndex(v).data);

                    if (weight < key[v]) {

                        key[v] = weight;
                        parent[v] = u;

                        steps.add(new PrimStep(
                                u,
                                key,
                                parent,
                                inMST,
                                "Actualizar "
                                        + graph.getVertexByIndex(v).data
                                        + " con peso "
                                        + weight,
                                totalWeight
                        ));
                    }
                }
            }
        }

        steps.add(new PrimStep(
                -1,
                key,
                parent,
                inMST,
                "Prim finalizado",
                totalWeight
        ));
    }

    //=================================================
    // LINKED GRAPH
    //=================================================
    public void executeLinkedGraph(
            LinkedGraph<String> graph,
            int start
    ) throws Exception {

        steps.clear();
        totalWeight = 0;

        int n = graph.size();

        int[] key = new int[n];
        int[] parent = new int[n];
        boolean[] inMST = new boolean[n];

        for (int i = 0; i < n; i++) {
            key[i] = Integer.MAX_VALUE;
            parent[i] = -1;
            inMST[i] = false;
        }

        key[start] = 0;

        steps.add(new PrimStep(
                start,
                key,
                parent,
                inMST,
                "Inicio en " + graph.getVertexByIndex(start).data,
                totalWeight
        ));

        for (int count = 0; count < n; count++) {

            int u = minKey(key, inMST, n);

            if (u == -1)
                break;

            inMST[u] = true;

            if (parent[u] != -1) {
                totalWeight += key[u];
            }

            steps.add(new PrimStep(
                    u,
                    key,
                    parent,
                    inMST,
                    "Se agrega " + graph.getVertexByIndex(u).data + " al MST",
                    totalWeight
            ));

            for (int v = 0; v < n; v++) {

                if (!inMST[v]
                        && graph.containsEdge(
                        graph.getVertexByIndex(u).data,
                        graph.getVertexByIndex(v).data)) {

                    int weight = graph.getWeight(
                            graph.getVertexByIndex(u).data,
                            graph.getVertexByIndex(v).data);

                    if (weight < key[v]) {

                        key[v] = weight;
                        parent[v] = u;

                        steps.add(new PrimStep(
                                u,
                                key,
                                parent,
                                inMST,
                                "Actualizar "
                                        + graph.getVertexByIndex(v).data
                                        + " con peso "
                                        + weight,
                                totalWeight
                        ));
                    }
                }
            }
        }

        steps.add(new PrimStep(
                -1,
                key,
                parent,
                inMST,
                "Prim finalizado",
                totalWeight
        ));
    }

    private int minKey(
            int[] key,
            boolean[] inMST,
            int n
    ) {

        int min = Integer.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < n; i++) {

            if (!inMST[i] && key[i] < min) {
                min = key[i];
                index = i;
            }
        }

        return index;
    }
}