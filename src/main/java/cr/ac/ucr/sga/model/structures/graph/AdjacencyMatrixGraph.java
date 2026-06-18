package cr.ac.ucr.sga.model.structures.graph;

import cr.ac.ucr.sga.model.Node;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import cr.ac.ucr.sga.model.structures.queues.LinkedQueue;
import cr.ac.ucr.sga.model.structures.queues.QueueException;
import cr.ac.ucr.sga.model.structures.stacks.LinkedStack;
import cr.ac.ucr.sga.model.structures.stacks.StackException;

public class AdjacencyMatrixGraph<T extends Comparable<T>> implements Graph<T> {
    public int n; //tam máximo de la matriz
    public Vertex<T>[] vertexList;//arreglo estático de objetos tipo Vertex
    private T[][] adjancencyMatrix; //arreglo multidimensional tipo matriz
    public int counter;
    public final boolean directed;

    //Atributos para los recorridos
    public LinkedStack<Integer> stack;
    public LinkedQueue<Integer> queue;


    public AdjacencyMatrixGraph(int n, boolean directed){
        if (n <= 0) {
            System.exit(1);
        }
        this.n = n;
        this.vertexList = new Vertex[n];
        this.adjancencyMatrix = (T[][]) new Comparable[n][n];
        this.counter = 0;
        this.directed = directed;
        this.stack = new LinkedStack<>();
        this.queue = new LinkedQueue<Integer>();
        initMatrix();
    }

    private void initMatrix() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjancencyMatrix[i][j] = (T)Integer.valueOf(0);
            }
        }
    }

    @Override
    public int size() throws ListException {
        return counter;
    }

    @Override
    public void clear() {
        this.vertexList = new Vertex[n];
        this.adjancencyMatrix = (T[][]) new Comparable[n][n];
        this.counter = 0;
        initMatrix();
    }

    @Override
    public boolean isEmpty() {
        return counter == 0;
    }

    @Override
    public boolean containsVertex(T element) throws GraphException, ListException {
        if(isEmpty())throw new GraphException("Adjacency Matrix Graph is Empty");
        for (int i = 0; i < counter; i++) {
            if(element.equals(vertexList[i].data))return true;
        }
        return false; //No lo encontró
    }

    @Override
    public boolean containsEdge(T a, T b) throws GraphException, ListException {
        if(isEmpty())throw new GraphException("Adjacency Matrix Graph is Empty");
        return !equals(adjancencyMatrix[indexOf(a)][indexOf(b)],(T)Integer.valueOf(0));
    }

    @Override
    public void addVertex(T element) throws GraphException, ListException {
        if(counter >= vertexList.length)
            throw new GraphException("Adjacency Matrix Graph is full");
        vertexList[counter++] = new Vertex<>(element);
    }

    @Override
    public void addEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjancency Matrix Graph Not Contains Vertex");
        if(!containsEdge(a,b)) {
            adjancencyMatrix[indexOf(a)][indexOf(b)] = (T) Integer.valueOf(1);
            //grafo no dirigido
            if (!directed) {
                adjancencyMatrix[indexOf(b)][indexOf(a)] = (T) Integer.valueOf(1);
            }
        }
    }

    int indexOf(T element) {
        for (int i = 0; i < counter; i++) {
            if(element.equals(vertexList[i].data))return i;
        }return -1;//no encontró la data del vértice
    }

    @Override
    public void addWeight(T a, T b, T weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b)) throw new GraphException("Adjancency Matrix Graph Not Contains Vertex");
        if(containsEdge(a,b)) {
            adjancencyMatrix[indexOf(a)][indexOf(b)] = weight;
            //grafo no dirigido
            if (!directed) {
                adjancencyMatrix[indexOf(b)][indexOf(a)] = weight;
            }
        }
    }

    @Override
    public void addEdgeAndWeight(T a, T b, T weight) throws GraphException, ListException {
        if(!containsVertex(a) || !containsVertex(b)) throw new GraphException("Adjancency Matrix Graph Not Contains Vertex");
        if(!containsEdge(a,b)) {
            adjancencyMatrix[indexOf(a)][indexOf(b)] = weight;
            //grafo no dirigido
            if (!directed) {
                adjancencyMatrix[indexOf(b)][indexOf(a)] = weight;
            }
        }
    }

    @Override
    public void removeVertex(T element) throws GraphException, ListException {
        if(isEmpty()) throw new GraphException("Adjacency Matrix Graph is Empty");//ver si si

        int index = indexOf(element);//devuelve el indice del vértice a eliminar
        //si el vértice existe en la lista de vértices
        if(index != -1) {
            //Se desplaza los vértices hacia la izquierda
            for (int i = index; i < counter-1 ; i++) {
                vertexList[i] = vertexList[i + 1];

                //ahora hacemos lo mismo en la matriz con las filas, columnas
                //es decir, eliminamos las aristas
                //primero movemos todas las filas, una posición hacia arriba
                for (int j = 0; j < counter; j++) {
                    adjancencyMatrix[i][j] = adjancencyMatrix[i][j+1];
                }
            }
            //luego movemos todas las columnas una pos a la izq
            for (int i = 0; i < counter; i++) {
                for (int j = index; j < counter-1; j++) {
                    adjancencyMatrix[i][j] = adjancencyMatrix[i][j+1];
                }
            }
            //al final se debe decrementar el contador de vértices existente
            //eliminamos los datos "sucios" de la lista de vétices y la matriz
            vertexList[counter] = null;
            counter--;

            for (int i = 0; i <= counter; i++) {
                //cambia las filas y la col es la ult(columna "sucia")
                adjancencyMatrix[i][counter] = (T)Integer.valueOf(0);
                adjancencyMatrix[counter][i] = (T)Integer.valueOf(0);
            }
        }
    }

    @Override
    public void removeEdge(T a, T b) throws GraphException, ListException {
        if (!containsVertex(a) || !containsVertex(b))
            throw new GraphException("Adjancency Matrix Graph Not Contains Vertex");

        if (!containsEdge(a,b))
            throw new GraphException("Adjancency Matrix Graph Not Contains Edge");

        int i = indexOf(a);//Obtenemos la pos del vertice "a"
        int j = indexOf(b);//Obtenemos el pos del vertice "b"

        if (i!=-1 && j!= -1){//Si los dos vértices existen
            adjancencyMatrix[i][j] = (T) Integer.valueOf(0);
            if(!directed) adjancencyMatrix[j][i] = (T) Integer.valueOf(0);

        }

    }

    public String printMatrix() {
        String result = "";
        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < counter; j++) {
                result += adjancencyMatrix[i][j] + " ";
            }

            result += "\n";
        }

        return result;
    }

    @Override
    public String dfs() throws GraphException, StackException, ListException {
        setVisited(false);//marca todos los vertices como no vistados
        // inicia en el vertice 0
        String info =vertexList[0].data+", ";
        vertexList[0].setVisited(true); // lo marca
        stack.clear();
        stack.push(0); //lo apila
        while( !stack.isEmpty() ){
            // obtiene un vertice adyacente no visitado,
            //el que esta en el tope de la pila
            int index = adjacentVertexNotVisited((int) stack.top());
            if(index==-1) // no lo encontro
                stack.pop();
            else{
                vertexList[index].setVisited(true); // lo marca
                info+=vertexList[index].data+", "; //lo muestra
                stack.push(index); //inserta la posicion
            }
        }
        return info;
    }

    @Override
    public String bfs() throws GraphException, QueueException, ListException {
        setVisited(false);//marca todos los vertices como no visitados
        // inicia en el vertice 0
        String info =vertexList[0].data+", ";
        vertexList[0].setVisited(true); // lo marca
        queue.clear();
        queue.enQueue(0); // encola el elemento
        int v2;
        while(!queue.isEmpty()){
            int v1 = (int) queue.deQueue(); // remueve el vertice de la cola
            // hasta que no tenga vecinos sin visitar
            while((v2=adjacentVertexNotVisited(v1)) != -1 ){
                // obtiene uno
                vertexList[v2].setVisited(true); // lo marca
                info+=vertexList[v2].data+", "; //lo muestra
                queue.enQueue(v2); // lo encola
            }
        }
        return info;
    }

    //setteamos el atributo visitado del vertice respectivo
    private void setVisited(boolean value) {
        for (int i = 0; i < counter; i++) {
            vertexList[i].setVisited(value); //value==true o false
        }//for
    }

    private int adjacentVertexNotVisited(int index) {
        for (int i = 0; i < counter; i++) {
            if(!adjancencyMatrix[index][i].equals(0)
                    && !vertexList[i].isVisited())
                return i;//retorna la posicion del vertice adyacente no visitado
        }//for i
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency Matrix Graph\n");
        String graphType = directed ? "Directed":"Undirected";
        sb.append("****** Graph Type: ").append(graphType).append("\n");
        //mostramos los vértices
        for (int i = 0; i < counter; i++) {
            sb.append("\nThe vertex in position [").append(i).append("] is:")
                    .append(vertexList[i].data + "\n");

        }
        //mostramos todas las aristas de cada vértice
        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < counter; j++) {
                if(!adjancencyMatrix[i][j].equals(0)){
                    sb.append("\nThere is edge between the vertexes: ")
                            .append(vertexList[i].data)
                            .append("...................").append(vertexList[j].data);
                    //Valido que tenga pesos, si es el caso que se muestran
                    if(!adjancencyMatrix[j][i].equals(1))
                        sb.append(" .weight: ").append(adjancencyMatrix[j][i]);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Metodo que devuelve el grado del vertice del elemento dado, de un grafo dirgido grado de salida + grado de entrada
     *
     **/

    public int getVertexDegree(T element) throws GraphException, ListException {

        if (isEmpty()) throw new ListException("Adjacency Matrix Graph is Empty");

        if (!containsVertex(element)) throw new ListException("Adjacency Matrix Graph Not Contains Vertex");

        int value = indexOf(element);//Encontramos la posicion del elemento
        int degree = 0; //El va a devolver el grado del vertice

        //Grafo no dirigido
        if (!directed){

            for (int i = 0; i < counter; i++) {
                if (!equals(adjancencyMatrix[value][i], (T) Integer.valueOf(0))) {
                    degree++;
                }
            }

        }else{//Grafo dirigido

            // salida
            for (int i = 0; i < counter; i++) {
                if (!equals(adjancencyMatrix[value][i], (T) Integer.valueOf(0))) {
                    degree++;
                }
            }

            // entrada
            for (int i = 0; i < counter; i++) {
                if (!equals(adjancencyMatrix[i][value], (T) Integer.valueOf(0))) {
                    degree++;
                }
            }

        }

        return degree;

    }

    /**
     *Metodo que devuelva el grado del grafo
     **/
    public int getGraphDegree() throws GraphException, ListException {

        if (isEmpty()) throw new ListException("Adjacency Matrix Graph is Empty");

        int maxDegree = 0;

        for (int i = 0; i < counter; i++) {

            int degree =  getVertexDegree(vertexList[i].data);

            if (degree > maxDegree) maxDegree = degree;

        }

        return maxDegree;
    }

    /**
     * Metodo que devuelva el número total de aristas existentes en el grafo
     **/
    public int totalEdges() throws GraphException, ListException {

        if(isEmpty())
            throw new GraphException("Adjacency Matrix Graph is Empty");

        int edges = 0;

        for(int i = 0; i < counter; i++) {
            for(int j = 0; j < counter; j++) {

                if(!equals(adjancencyMatrix[i][j], (T) Integer.valueOf(0))) {
                    edges++;
                }
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

    public Vertex<T> getVertexByIndex(int index){
        if(index < 0 || index >= counter)
            return null;

        return vertexList[index];
    }

    public int getWeight(T a, T b) throws GraphException, ListException {

        if(!containsEdge(a,b))
            return Integer.MAX_VALUE;

        Object value = adjancencyMatrix[indexOf(a)][indexOf(b)];

        if(value instanceof String){
            return Integer.parseInt((String)value);
        }

        return ((Number)value).intValue();
    }
}
