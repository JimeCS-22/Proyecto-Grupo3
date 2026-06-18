package cr.ac.ucr.sga.model.structures.graph;

import cr.ac.ucr.sga.model.Node;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import cr.ac.ucr.sga.model.structures.queues.LinkedQueue;
import cr.ac.ucr.sga.model.structures.queues.QueueException;
import cr.ac.ucr.sga.model.structures.stacks.LinkedStack;
import cr.ac.ucr.sga.model.structures.stacks.StackException;

public class LinkedGraph <T extends Comparable<T>> extends LinkedList<T> implements Graph<T> {
    //atributos para los recorridos dfs, bfs
    public final boolean directed;
    public LinkedStack<Integer> stack;
    public LinkedQueue<Integer> queue;


    public LinkedGraph(boolean directed) {
        super();
        this.directed = directed;
        stack = new LinkedStack<>();
        queue = new LinkedQueue<>();
    }

    @Override
    public boolean containsVertex(T element) throws GraphException, ListException {
        return contains(element);
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        Node<T> nodeA = getNode(a);
        Node<T> nodeB = null;

        if (!directed) {
            nodeB = getNode(b);

        }
        return !directed ? getNodeNeighbor(nodeA, b) != null && getNodeNeighbor(nodeB,a) != null
                : getNodeNeighbor(nodeA,b)!= null;

    }
    private Node<T> getNodeNeighbor(Node<T> headNode, T element) {
        if(headNode.neighbor==null) return null;
        Node<T> aux = headNode.neighbor;
        while(aux!=null){
            if(aux.data.compareTo(element)==0) return aux;
            aux = aux.neighbor; //movemos aux al sgte nodo vecino
        }
        return null; //si llega aquí, no encontró el nodo
    }
    @Override
    public void addVertex(T element) throws GraphException, ListException {
        if (isEmpty()) add(element); //si la lista enlazada está vacia agregue el elemento
        else if (!contains(element)) add(element);
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjancency List Graph Not Contains Vertex");
        if (!containsEdge(a,b)) {
            Node<T>nodeA = getNode(a);
            addNeighbor(nodeA,b,null); //null
            if(!directed){
                Node<T>nodeB = getNode(b);
                addNeighbor(nodeB,a,null); //null para el peso
            }
        }
    }

    private void addNeighbor(Node<T> headNode, T element, Object weight) {
        Node<T> node = new Node<>(element, weight);
        if(headNode.neighbor == null)
            headNode.neighbor = node;
        else{
            Node<T> aux = headNode.neighbor;
            //me muevo por la lista hasta el ult nodo
            while(aux.neighbor != null)
                aux = aux.neighbor; //se mueve al sgte nodo vecino
            //se sale cuando aux.neighbor es nulo
            aux.neighbor = node; //entonces conectados el nodo al final
        }
    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        if (!containsEdge(a,b)) {
            Node<T>nodeA = getNode(a);
            //recuperamos la lista de vecinos del nodo A  para settear el peso
            getNodeNeighbor(nodeA,b).weight = weight; //null  para el peso
            if(!directed){
                Node<T>nodeB = getNode(b);
                getNodeNeighbor(nodeB,a).weight = weight; //null para el peso
            }
        }
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Linked Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            Node<T> nodeA = getNode(a);
            addNeighbor(nodeA, b, weight);
            if(!directed) {
                Node<T> nodeB = getNode(b);
                // a partir del node B contruya
                addNeighbor(nodeB, a, weight);
            }
        }
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        if(!containsVertex(element))
            throw new GraphException("Linked Graph Not Contains Vertex");
        remove(element); //eliminamos el vertice del grafo
        //buscamos el rastro del vértice en las listas enlazadas de vecinos de los otros vértices
        int len = size();
        for (int i = 1; i <= len; i++) {
            Node<T> node = getNodeByIndex(i);
            removeNeighbor(node, element);
        }
    }
    private void removeNeighbor(Node<T> headNode, T element) throws ListException {
        if(headNode == null)throw new ListException("Linked List in Graph is Empty");
        //Caso 1: el elemento a suprimir es el primero
        if ( headNode.neighbor != null && equals(headNode.neighbor.data,element)) {
            headNode.neighbor = headNode.neighbor.neighbor; //queda apuntando al sgte nodo vecino
            return;
            //caso 2.El elemento a suprimir puede estar en medio o al final de la lista
        }else{
            Node<T> prev = headNode;
            while (prev.neighbor != null) {
                if (equals(prev.neighbor.data, element)) {
                    prev.neighbor = prev.neighbor.neighbor; // elimina el nodo
                    return; // termina tras eliminar
                }
                prev = prev.neighbor;
            }
        }
    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Linked Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            throw new GraphException("Linked Graph Not Contains Edge");
        }
        Node<T> nodeA = getNode(a);
        removeNeighbor(nodeA,b);
        if (!directed ) {
            Node<T> nodeB = getNode(b);
            removeNeighbor(nodeB,a);
        }
    }

    //TODO

    @Override
    public String dfs() throws GraphException, StackException, ListException {

        if(isEmpty())
            throw new GraphException("Empty Graph");

        int n = size();
        boolean[] visited = new boolean[n];

        StringBuilder sb = new StringBuilder();

        stack.clear();

        visited[0] = true;
        sb.append(getNodeByIndex(1).data).append(" ");

        stack.push(0);

        while(!stack.isEmpty()){

            int current = (int) stack.top();

            int neighbor = adjacentVertexNotVisited(current, visited);

            if(neighbor == -1){
                stack.pop();
            }else{
                visited[neighbor] = true;
                sb.append(getNodeByIndex(neighbor + 1).data).append(" ");
                stack.push(neighbor);
            }
        }

        return sb.toString();
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {

        if(isEmpty())
            throw new GraphException("Empty Graph");

        int n = size();
        boolean[] visited = new boolean[n];

        StringBuilder sb = new StringBuilder();

        queue.clear();

        visited[0] = true;
        sb.append(getNodeByIndex(1).data).append(" ");

        queue.enQueue(0);

        while(!queue.isEmpty()){

            int current = queue.deQueue();

            int neighbor;

            while((neighbor = adjacentVertexNotVisited(current, visited)) != -1){

                visited[neighbor] = true;
                sb.append(getNodeByIndex(neighbor + 1).data).append(" ");
                queue.enQueue(neighbor);
            }
        }

        return sb.toString();
    }

    private int adjacentVertexNotVisited(int indexVertex, boolean[] visited)
            throws ListException {

        Node<T> headNode = getNodeByIndex(indexVertex + 1);
        Node<T> aux = headNode.neighbor;

        while(aux != null){

            int indexNeighbor = indexOf(aux.data) - 1;

            if(indexNeighbor != -1 && !visited[indexNeighbor]){
                return indexNeighbor;
            }

            aux = aux.neighbor;
        }

        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("|-------|  | Linked Graph |---------| |\n");
        String graphType = directed ? "Directed" : "Undirected";
        sb.append("****** Graph Type: ").append(graphType).append("\n");

        sb.append(super.toString());//para mostrar la lista enlazada de vértices

        //mostramos todos los vértices
        try {
            int len = size(); //llamamos al método de la lista enlazada
            for (int i = 1; i <= len; i++) {

                sb.append("\n(").append(i).append(")-------Vertex [").append(getNodeByIndex(i).data).append("]");
                Node<T> aux = getNodeByIndex(i).neighbor;
                while(aux != null){
                    sb.append("\n*** Edge: \"").append(aux.data).append(", weight: ").append(aux.weight);
                    aux = aux.neighbor;
                }
            }

        } catch(ListException e){
            throw new RuntimeException(e);
        }
        return super.toString() + sb;
    }

    /**
     * Metodo que devuelve el grado del vertice del elemento dado,
     * de un grafo dirgido grado de salida + grado de entrada
     **/
    public int getVertexDegree(T element) throws GraphException, ListException {

        if(isEmpty())
            throw new GraphException("Linked Graph is Empty");

        if(!containsVertex(element))
            throw new GraphException("Linked Graph Not Contains Vertex");

        int degree = 0;

        Node<T> vertex = getNode(element);

        if(!directed){

            // contar vecinos del vértice
            Node<T> aux = vertex.neighbor;

            while(aux != null){
                degree++;
                aux = aux.neighbor;
            }

        }else{

            // grado de salida
            Node<T> aux = vertex.neighbor;

            while(aux != null){
                degree++;
                aux = aux.neighbor;
            }

            // grado de entrada
            int len = size();

            for(int i = 1; i <= len; i++){

                aux = getNodeByIndex(i).neighbor;

                while(aux != null){

                    if(equals(aux.data, element))
                        degree++;

                    aux = aux.neighbor;
                }
            }
        }

        return degree;
    }

    /**
     *Metodo que devuelva el grado del grafo
     **/
    public int getGraphDegree() throws GraphException, ListException {

        if(isEmpty()) throw new GraphException("Linked Graph is Empty");

        int maxDegree = 0;
        int len = size();

        for(int i = 1; i <= len; i++){

            Node<T> vertex = getNodeByIndex(i);
            int degree = getVertexDegree(vertex.data);

            if(degree > maxDegree) maxDegree = degree;
        }

        return maxDegree;

    }

    /**
     * Metodo que devuelva el número total de aristas existentes en el grafo
     **/
    public int totalEdges() throws GraphException, ListException {

        if(isEmpty())
            throw new GraphException("Linked Graph is Empty");

        int edges = 0;

        int len = size();

        for(int i = 1; i <= len; i++) {

            Node<T> aux = getNodeByIndex(i).neighbor;

            while(aux != null) {
                edges++;
                aux = aux.neighbor;
            }
        }

        return directed ? edges : edges / 2;
    }

    /**Metodos de ayuda**/
    public boolean equals(T a, T b)  {
        return a==null ? b==null : a.equals(b);
    }

    //metodo generico de comparacion
    public int compareElement(T a, T b) {
        return a.compareTo(b);
    }

    public int getWeight(T a,T b)throws GraphException,ListException{

        Node<T> node=getNode(a);

        Node<T> aux=node.neighbor;

        while(aux!=null){

            if(aux.weight instanceof String)
                return Integer.parseInt((String)aux.weight);
        }
        return ((Number)aux.weight).intValue();

    }

    public Node<T> getVertexByIndex(int index) throws ListException {

        if(isEmpty())
            throw new ListException("Linked Graph is Empty");

        if(index < 0 || index >= size())
            throw new ListException("Invalid Vertex Index");

        return getNodeByIndex(index + 1);
    }
}
