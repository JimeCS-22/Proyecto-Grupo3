package cr.ac.ucr.sga.model.structures.graph;


import cr.ac.ucr.sga.model.Node;

public class Vertex<T> {
    public T data;
    public Node<T> headNode; //Sirve para crear la lista enlazada de aristas y pesos
    public boolean visited; // Sirve para los recorridos DFS y BFS

    public Vertex(T data) {
        this.data = data;
        this.headNode = null;
    }

    public void setVisited(boolean value) {
        this.visited = value;
    }

    public boolean isVisited() {
        return visited;
    }
}
