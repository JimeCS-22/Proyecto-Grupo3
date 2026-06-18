package cr.ac.ucr.sga.model.structures.graph;


import cr.ac.ucr.sga.model.structures.lists.ListException;
import cr.ac.ucr.sga.model.structures.queues.QueueException;
import cr.ac.ucr.sga.model.structures.stacks.StackException;

;

/**
 * @author Profesor Gilberth Chaves A <gchavesav@ucr.ac.cr>
 */
public interface Graph<T> {
    // devuelve el número de vértices que tiene el grafo
    public int size() throws ListException;
    //elimina todo el Grafo
    public void clear();
    // true si el grafo está vacío
    public boolean isEmpty();
    // true si el vértice indicado forma parte del grafo
    public boolean containsVertex(T element)throws GraphException, ListException;
    // true si existe una artista que une los dos vértices indicados
    public boolean containsEdge(T a, T b)throws GraphException, ListException;
    // agrega un vértice al grafo
    public void addVertex(T element)throws GraphException, ListException;
    // agrega una artista que permita unir dos vértices (el grafo es no dirigido)
    public void addEdge(T a, T b)throws GraphException, ListException;
    // agrega peso a una artista que une dos vértices (el grafo es no dirigido)
    public void addWeight(T a, T b, T weight)throws GraphException, ListException;
    //agrega una arista y peso entre dos vértices
    public void addEdgeAndWeight(T a, T b, T c) throws GraphException, ListException;
    // suprime el vértice indicado. Si tiene aristas asociadas, también seran suprimidas
    public void removeVertex(T element)throws GraphException, ListException;
    // suprime la arista(si existe) entre los vertices a y b
    public void removeEdge(T a, T b)throws GraphException, ListException;
    // recorre el grafo utilizando el algoritmo de búsqueda en profundidad
    // depth-first search
    public String dfs()throws GraphException, StackException, ListException;
    // recorre el grafo utilizando el algoritmo de búsqueda en amplitud
    // breadth-first search
    public String bfs()throws GraphException, QueueException, ListException;

}
