package cr.ac.ucr.sga.model.structures.graph;

import cr.ac.ucr.sga.model.Node;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import cr.ac.ucr.sga.model.structures.queues.QueueException;
import cr.ac.ucr.sga.model.structures.stacks.StackException;


public class AdjacencyListGraph<T extends Comparable<T>> extends AdjacencyMatrixGraph<T> {

    public AdjacencyListGraph(int n, boolean directed) {
        super(n, directed);
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        if(isEmpty()) throw new GraphException("Adjacency Matrix Graph is Empty");
        Vertex<T> vertexA = getVertex(a);
        boolean getVertexA = getNodeNeighbor(vertexA.headNode, b)!=null;
        boolean getVertexB = false;
        if(!directed) {
            Vertex<T> vertexB = getVertex(b);
            getVertexB = getNodeNeighbor(vertexB.headNode, a) != null;
        }
        return !directed ? getVertexA && getVertexB : getVertexA;
    }

    private Node<T> getNodeNeighbor(Node<T> headNode, T element) {
        if(headNode==null) return null;
        Node<T> aux = headNode;
        while(aux!=null){
            if(aux.data.compareTo(element)==0) return aux;
            aux = aux.neighbor; //movemos aux al sgte nodo vecino
        }
        return null; //si llega aquí, no encontró el nodo
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency List Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            Vertex<T> vertexA = getVertex(a);
            vertexA.headNode = addNeighbor(vertexA.headNode, b, null);
            if(!directed) {
                Vertex<T> vertexB = getVertex(b);
                vertexB.headNode =  addNeighbor(vertexB.headNode, a, null);
            }
        }
    }

    private Node<T> addNeighbor(Node<T> headNode, T element, Object weight) {
        Node<T> node = new Node<>(element, weight);
        if(headNode == null)
            headNode = node;
        else{
            Node<T> aux = headNode;
            //me muevo por la lista hasta el ult nodo
            while(aux.neighbor != null)
                aux = aux.neighbor; //se mueve al sgte nodo vecino
            //se sale cuando aux.neighbor es nulo
            aux.neighbor = node; //entonces conectados el nodo al final
        }
        return headNode; //si llegó nulo, lo devuelve con un nodo
    }

    private Vertex<T> getVertex(T element) {
        for (int i = 0; i < counter; i++)
            if(equals(vertexList[i].data, element)) return vertexList[i];
        return null; //no existe el vértice
    }
    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        super.addWeight(a, b, weight);
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjacency List Graph Not Contains Vertex");
        if(!containsEdge(a, b)) {
            Vertex<T> vertexA = getVertex(a);
            vertexA.headNode = addNeighbor(vertexA.headNode, b, weight);
            if(!directed) {
                Vertex<T> vertexB = getVertex(b);
                vertexB.headNode =  addNeighbor(vertexB.headNode, a, weight);
            }
        }
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        if (!containsVertex(element))
            throw new GraphException("Adjacency List Graph Not Contains Vertex");
        //ahora que sabemos que el vèrtice existe, procedemos a buscarlo para eliminarlo
        int index = indexOf(element);//devuelve el indice del vértice a eliminar
        //si el vértice existe en la lista de vértices
        if (index != -1) {//devuelve el indidce del vertice a eliminar
            for (int i = index; i < counter - 1; i++) {
                vertexList[i] = vertexList[i + 1];
            }
            counter--;//lo debemos decrementar por el vértice suprimido
        }

        //ahora debemos buscar el rastro del vértice suprimido en las listas enlazadas
        //de vecinos de los otros vértices
        for (int i = 0; i < counter; i++) {
            Vertex<T> vertex = vertexList[i];
            vertex.headNode = removeNeighbor(vertex.headNode, element);
        }
    }

    private Node<T> removeNeighbor(Node<T> headNode, T element) throws ListException {

            // Si la lista está vacía no hay nada que eliminar
            if (headNode == null)
                return null;

            // Caso 1:
            // El vecino a eliminar es el primero de la lista
            if (equals(headNode.data, element)) {
                return headNode.neighbor;
            }

            // Caso 2:
            // El vecino está en medio o al final
            Node<T> prev = headNode;

            while (prev != null && prev.neighbor != null) {

                // Encontramos el nodo a eliminar
                if (equals(prev.neighbor.data, element)) {

                    // Saltamos el nodo
                    prev.neighbor = prev.neighbor.neighbor;

                    // ya eliminamos el nodo, salimos del método
                    return headNode;
                }

                // Avanzamos al siguiente nodo
                prev = prev.neighbor;
            }

            // Si no encontró el elemento devuelve la lista original sin el elemento eliminado
            return headNode;
    }


    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjancency Matrix Graph Not Contains Vertex");

        if (!containsEdge(a,b))
            throw new GraphException("Adjancency Matrix Graph Not Contains Edge");

        if (containsEdge(a,b)) {

            Vertex<T> vertexA = getVertex(a);
            vertexA.headNode = removeNeighbor(vertexA.headNode, b);

            if (!directed) {
                Vertex<T> vertexB = getVertex(b);
                vertexB.headNode = removeNeighbor(vertexB.headNode, a);
            }

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("|——————| |Adjacency List Graph|——————| |\n");
        String graphType = directed ? "Directed" : "Undirected";
        sb.append("※※※※※※Graph Type: ").append(graphType).append("\n");
        //mostramos todos los vértices
        for (int i = 0; i < counter; i++) {
            sb.append("\nThe vertex in position [").append(i).append("] is: ")
                    .append(vertexList[i].data);
        }
        //mostramos la información de aristas y pesos
        for (int i = 0; i < counter; i++) {
            sb.append("\n( ").append(i).append(" )----Vertex [ ").append(getVertexByIndex(i).data).append(" ]");
            Node<T> aux = getVertexByIndex(i).headNode; //llamanos al inicio de la lista enlazada del nodo
            while(aux!=null){
                sb.append("\n※※※ Edge: ").append(aux.data).append(", weight: ").append(aux.weight);
                aux = aux.neighbor; //lo movemos al sgte vecino con quien tiene una arista
            }
        }
        return sb.toString();
    }

    //TODO
    public String dfs() throws GraphException, StackException, ListException {
        if (isEmpty()) throw new GraphException("Graph is empty");

        setVisited(false);
        StringBuilder sb = new StringBuilder();
        stack.clear();

        for (int i = 0; i < size(); i++) {
            if (!vertexList[i].isVisited()) {
                vertexList[i].setVisited(true);
                sb.append(vertexList[i].data).append(", ");
                stack.push(i);

                while (!stack.isEmpty()) {
                    int current = (int) stack.pop();
                    int neighbor;
                    while ((neighbor = adjacentVertexNotVisited(current)) != -1) {
                        vertexList[neighbor].setVisited(true);
                        sb.append(vertexList[neighbor].data).append(", ");
                        stack.push(neighbor);
                    }
                }
            }
        }
        return sb.toString();
    }


    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        if (isEmpty())
            throw new GraphException("Adjacency List Graph is Empty");

        setVisited(false);
        StringBuilder sb = new StringBuilder();
        queue.clear();

        // Recorre todos los vértices para cubrir componentes desconectados
        for (int i = 0; i < size(); i++) {
            if (!vertexList[i].isVisited()) {
                vertexList[i].setVisited(true);
                sb.append(vertexList[i].data).append(", ");
                queue.enQueue(i);

                while (!queue.isEmpty()) {
                    int current = (int) queue.deQueue();

                    // Itera sobre la lista de adyacencia del vértice actual
                    Node<T> aux = vertexList[current].headNode;
                    while (aux != null) {
                        int neighbor = indexOf(aux.data);
                        if (neighbor != -1 && !vertexList[neighbor].isVisited()) {
                            vertexList[neighbor].setVisited(true);
                            sb.append(vertexList[neighbor].data).append(", ");
                            queue.enQueue(neighbor);
                        }
                        aux = aux.next; // usa next en tu LinkedList
                    }
                }
            }
        }
        return sb.toString();
    }


    private void setVisited(boolean value) {
        for (int i = 0; i < counter; i++) {
            vertexList[i].setVisited(value); //value==true o false
        }//for
    }

    private int adjacentVertexNotVisited(int indexVertex) throws ListException {

        Node<T> aux = vertexList[indexVertex].headNode;

        while(aux != null){

            int indexNeighbor = indexOf(aux.data);

            if(indexNeighbor != -1 && !vertexList[indexNeighbor].isVisited()){
                return indexNeighbor;
            }

            aux = aux.neighbor;
        }

        return -1;
    }

    /**
     * Metodo que devuelve el grado del vertice del elemento dado, de un grafo dirgido grado de salida + grado de entrada
     *
     **/

    public int getVertexDegree(T element) throws GraphException, ListException {

        if (isEmpty()) throw new ListException("Adjacency List Graph is Empty");

        if (!containsVertex(element)) throw new ListException("Adjacency List Graph Not Contains Vertex");

        int degree = 0; //El va a devolver el grado del vertice

        Vertex<T> vertex = getVertex(element);

        if (!directed) {

            Node<T> aux = vertex.headNode;

            while (aux != null) {
                degree++;
                aux = aux.neighbor;
            }

        } else {

            // Grado de salida
            Node<T> aux = vertex.headNode;

            while (aux != null) {
                degree++;
                aux = aux.neighbor;
            }

            // Grado de entrada
            for (int i = 0; i < counter; i++) {

                aux = vertexList[i].headNode;

                while (aux != null) {

                    if (equals(aux.data, element))
                        degree++;

                    aux = aux.neighbor;
                }
            }
        }

        return degree;
    }

    /**
     *Metodo que devuelve el grado del grafo
     **/
    public int getGraphDegree() throws GraphException, ListException {

        if (isEmpty()) throw new ListException("Adjacency List Graph is Empty");

        int maxDegree = 0;

        for (int i = 0; i < counter; i++) {

            int degree = getVertexDegree(vertexList[i].data);

            if (degree > maxDegree) maxDegree = degree;
        }

        return maxDegree;

    }

    /**
     * Metodo que devuelva el número total de aristas existentes en el grafo
     **/
    public int totalEdges() throws GraphException, ListException {

        if(isEmpty())
            throw new GraphException("Adjacency List Graph is Empty");

        int edges = 0;

        for(int i = 0; i < counter; i++) {

            Node<T> aux = vertexList[i].headNode;

            while(aux != null) {
                edges++;
                aux = aux.neighbor;
            }
        }

        return directed ? edges : edges / 2;
    }

    public int getWeight(T a, T b) throws GraphException, ListException{

        Vertex<T> vertex = getVertex(a);

        Node<T> aux = vertex.headNode;

        while(aux!=null){

            if(aux.weight instanceof String)
                return Integer.parseInt((String)aux.weight);
}
            return ((Number)aux.weight).intValue();

    }

    //para animar los recorridos
    public LinkedList<T> bfsTraversal() throws ListException, QueueException {
        if (isEmpty())
            throw new GraphException("Graph is empty");

        setVisited(false);
        queue.clear();

        cr.ac.ucr.sga.model.structures.lists.LinkedList<T> recorrido =
                new cr.ac.ucr.sga.model.structures.lists.LinkedList<>();

        for (int i = 0; i < size(); i++) {

            if (!vertexList[i].isVisited()) {

                vertexList[i].setVisited(true);

                recorrido.add(vertexList[i].data);

                queue.enQueue(i);

                while (!queue.isEmpty()) {

                    int current = (int) queue.deQueue();

                    Node<T> aux = vertexList[current].headNode;

                    while (aux != null) {

                        int neighbor = indexOf(aux.data);

                        if (neighbor != -1 &&
                                !vertexList[neighbor].isVisited()) {

                            vertexList[neighbor].setVisited(true);

                            recorrido.add(vertexList[neighbor].data);

                            queue.enQueue(neighbor);

                        }

                        aux = aux.neighbor;
                    }
                }
            }
        }

        return recorrido;
    }

    public LinkedList<T> dfsTraversal() throws StackException, ListException {
        if (isEmpty())
            throw new GraphException("Graph is empty");

        setVisited(false);
        stack.clear();

        LinkedList<T> recorrido = new LinkedList<>();

        for (int i = 0; i < size(); i++) {

            if (!vertexList[i].isVisited()) {

                vertexList[i].setVisited(true);
                recorrido.add(vertexList[i].data);

                stack.push(i);

                while (!stack.isEmpty()) {

                    int current = (int) stack.pop();

                    int neighbor;

                    while ((neighbor = adjacentVertexNotVisited(current)) != -1) {

                        vertexList[neighbor].setVisited(true);

                        recorrido.add(vertexList[neighbor].data);

                        stack.push(neighbor);

                    }
                }
            }
        }

        return recorrido;
    }
}