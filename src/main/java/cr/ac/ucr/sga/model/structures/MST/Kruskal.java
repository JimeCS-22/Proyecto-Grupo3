package cr.ac.ucr.sga.model.structures.MST;

import cr.ac.ucr.sga.model.structures.graph.AdjacencyListGraph;
import cr.ac.ucr.sga.model.structures.graph.AdjacencyMatrixGraph;
import cr.ac.ucr.sga.model.structures.graph.LinkedGraph;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;


import java.util.ArrayList;
import java.util.Collections;

public class Kruskal {

    private LinkedList<KruskalStep> steps;
    private double totalWeight;

    public Kruskal() {
        steps = new LinkedList<>();
    }

    public LinkedList<KruskalStep> getSteps() {
        return steps;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void executeMatrixGraph(AdjacencyMatrixGraph<String> graph, ArrayList<KruskalEdge> edges) throws Exception {

        Collections.sort(edges);

        UnionFind uf = new UnionFind(graph.size());

        LinkedList<KruskalEdge> mst =
                new LinkedList<>();

        LinkedList<KruskalEdge> rejected =
                new LinkedList<>();

        double totalWeight = 0;

        for (KruskalEdge edge : edges) {

            int source =
                    getVertexIndex(graph, edge.getSource());

            int destination =
                    getVertexIndex(graph, edge.getDestination());

            boolean accepted =
                    uf.union(source, destination);

            if (accepted) {

                mst.add(edge);

                totalWeight += edge.getWeight();

                steps.add(
                        new KruskalStep(
                                edge,
                                copy(mst),
                                copy(rejected),
                                KruskalAction.ACCEPTED,
                                totalWeight
                        )
                );
            } else {

                rejected.add(edge);

                steps.add(
                        new KruskalStep(
                                edge,
                                copy(mst),
                                copy(rejected),
                                KruskalAction.REJECTED,
                                totalWeight
                        )
                );
            }
        }
    }

    public void executeListGraph(AdjacencyListGraph<String> graph, ArrayList<KruskalEdge> edges) throws Exception {

        Collections.sort(edges);

        UnionFind uf = new UnionFind(graph.size());

        LinkedList<KruskalEdge> mst =
                new LinkedList<>();

        LinkedList<KruskalEdge> rejected =
                new LinkedList<>();

        totalWeight = 0;

        for (KruskalEdge edge : edges) {

            int source =
                    getVertexIndex(graph, edge.getSource());

            int destination =
                    getVertexIndex(graph, edge.getDestination());

            boolean accepted =
                    uf.union(source, destination);

            if (accepted) {

                mst.add(edge);

                totalWeight += edge.getWeight();

                steps.add(
                        new KruskalStep(
                                edge,
                                copy(mst),
                                copy(rejected),
                                KruskalAction.ACCEPTED,
                                totalWeight
                        )
                );
            } else {

                rejected.add(edge);

                steps.add(
                        new KruskalStep(
                                edge,
                                copy(mst),
                                copy(rejected),
                                KruskalAction.REJECTED,
                                totalWeight
                        )
                );
            }
        }
    }

    public void executeLinkedGraph(LinkedGraph<String> graph, ArrayList<KruskalEdge> edges) throws Exception {

        Collections.sort(edges);

        UnionFind uf = new UnionFind(graph.size());

        LinkedList<KruskalEdge> mst =
                new LinkedList<>();

        LinkedList<KruskalEdge> rejected =
                new LinkedList<>();

        double totalWeight = 0;

        for (KruskalEdge edge : edges) {

            int source =
                    getVertexIndex(graph, edge.getSource());

            int destination =
                    getVertexIndex(graph, edge.getDestination());

            boolean accepted =
                    uf.union(source, destination);

            if (accepted) {

                mst.add(edge);

                totalWeight += edge.getWeight();

                steps.add(
                        new KruskalStep(
                                edge,
                                copy(mst),
                                copy(rejected),
                                KruskalAction.ACCEPTED,
                                totalWeight
                        )
                );
            } else {

                rejected.add(edge);

                steps.add(
                        new KruskalStep(
                                edge,
                                copy(mst),
                                copy(rejected),
                                KruskalAction.REJECTED,
                                totalWeight
                        )
                );
            }
        }
    }

    public static class KruskalEdge implements Comparable<KruskalEdge> {

        private String source;
        private String destination;
        private double weight;

        public KruskalEdge(String source, String destination, double weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public String getSource() {
            return source;
        }

        public String getDestination() {
            return destination;
        }

        public double getWeight() {
            return weight;
        }

        @Override
        public int compareTo(KruskalEdge other) {
            return Double.compare(this.weight, other.weight);
        }

        @Override
        public String toString() {
            return source + "-" + destination + " (" + weight + ")";
        }
    }
    public class UnionFind {

        private int[] parent;
        private int[] rank;

        public UnionFind(int size) {

            parent = new int[size];
            rank = new int[size];

            for(int i=0;i<size;i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int x) {

            if(parent[x] != x)
                parent[x] = find(parent[x]);

            return parent[x];
        }

        public boolean union(int x, int y) {

            int rootX = find(x);
            int rootY = find(y);

            if(rootX == rootY)
                return false;

            if(rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            }
            else if(rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            }
            else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }

            return true;
        }
    }

    public enum KruskalAction {

        ACCEPTED,
        REJECTED
    }

    public class KruskalStep {

        private KruskalEdge currentEdge;

        private LinkedList<KruskalEdge> mstEdges;

        private LinkedList<KruskalEdge> rejectedEdges;

        private KruskalAction action;

        private double currentWeight;

        public KruskalStep(
                KruskalEdge currentEdge,
                LinkedList<KruskalEdge> mstEdges,
                LinkedList<KruskalEdge> rejectedEdges,
                KruskalAction action,
                double currentWeight
        ) {
            this.currentEdge = currentEdge;
            this.mstEdges = mstEdges;
            this.rejectedEdges = rejectedEdges;
            this.action = action;
            this.currentWeight = currentWeight;
        }

        public KruskalEdge getCurrentEdge() {
            return currentEdge;
        }

        public LinkedList<KruskalEdge> getMstEdges() {
            return mstEdges;
        }

        public LinkedList<KruskalEdge> getRejectedEdges() {
            return rejectedEdges;
        }

        public KruskalAction getAction() {
            return action;
        }

        public double getCurrentWeight() {
            return currentWeight;
        }
    }

    private int getVertexIndex(
            AdjacencyMatrixGraph<String> graph,
            String vertex
    ) throws Exception {

        for(int i=0;i<graph.counter;i++) {

            if(graph.getVertexByIndex(i)
                    .data.equals(vertex))
                return i;
        }

        return -1;
    }

    private int getVertexIndex(
            LinkedGraph<String> graph,
            String vertex
    ) throws Exception {

        for(int i=0;i<graph.size();i++) {

            if(graph.getVertexByIndex(i)
                    .data.equals(vertex))
                return i;
        }

        return -1;
    }


    private LinkedList<KruskalEdge> copy(
            LinkedList<KruskalEdge> original
    ) throws Exception {

        LinkedList<KruskalEdge> copy =
                new LinkedList<>();

        for(int i=1;i<=original.size();i++) {
            copy.add(original.get(i));
        }

        return copy;
    }



}



