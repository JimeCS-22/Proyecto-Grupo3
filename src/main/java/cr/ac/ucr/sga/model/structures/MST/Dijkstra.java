package cr.ac.ucr.sga.model.structures.MST;
import cr.ac.ucr.sga.model.structures.graph.AdjacencyMatrixGraph;
import cr.ac.ucr.sga.model.structures.graph.GraphException;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.util.Arrays;


public class Dijkstra {

    private LinkedList<DijkstraStep> steps;

    public Dijkstra(){
        steps = new LinkedList<>();
    }

    public LinkedList<DijkstraStep> getSteps() {
        return steps;
    }

    public void setSteps(LinkedList<DijkstraStep> steps) {
        this.steps = steps;
    }

    public void execute(AdjacencyMatrixGraph<String> graph, int start)
            throws GraphException, ListException {

        steps.clear();

        int n=graph.counter;

        int[] dist=new int[n];

        int[] prev=new int[n];

        boolean[] visited=new boolean[n];

        Arrays.fill(dist,Integer.MAX_VALUE);

        Arrays.fill(prev,-1);

        dist[start]=0;

        steps.add(new DijkstraStep(
                start,
                dist,
                prev,
                visited,
                "Inicio: dist["+graph.getVertexByIndex(start).data+"]=0"
        ));

        while(true){

            int u=minDistance(dist,visited);

            if(u==-1)
                break;

            visited[u]=true;

            steps.add(new DijkstraStep(
                    u,
                    dist,
                    prev,
                    visited,
                    "Extraer mínimo: "+graph.getVertexByIndex(u).data+
                            " (dist="+dist[u]+")"
            ));

            for(int v=0;v<n;v++){

                if(graph.containsEdge(
                        graph.getVertexByIndex(u).data,
                        graph.getVertexByIndex(v).data)){

                    if(!visited[v]){

                        int weight=graph.getWeight(
                                graph.getVertexByIndex(u).data,
                                graph.getVertexByIndex(v).data
                        );

                        if(dist[u]!=Integer.MAX_VALUE &&
                                dist[u]+weight<dist[v]){

                            dist[v]=dist[u]+weight;

                            prev[v]=u;

                            steps.add(new DijkstraStep(
                                    u,
                                    dist,
                                    prev,
                                    visited,
                                    "Relajar: "+
                                            graph.getVertexByIndex(u).data+
                                            " → "+
                                            graph.getVertexByIndex(v).data+
                                            " (nuevo="+dist[v]+")"
                            ));

                        }

                    }

                }

            }

        }

        steps.add(new DijkstraStep(
                -1,
                dist,
                prev,
                visited,
                "Dijkstra terminado."
        ));

    }

    private int minDistance(int[] dist,boolean[] visited){

        int min=Integer.MAX_VALUE;

        int index=-1;

        for(int i=0;i<dist.length;i++){

            if(!visited[i]&&dist[i]<min){

                min=dist[i];

                index=i;

            }

        }

        return index;

    }

}
