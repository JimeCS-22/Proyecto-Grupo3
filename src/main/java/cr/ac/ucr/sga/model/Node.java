package cr.ac.ucr.sga.model;

public class Node<T> {

    public T data;
    public Node<T> next; //apuntador al nodo siguiente
    public Node<T> prev;//apuntador al nodo siguiente
    public Integer priority;// 1-Alta,2-media y 3-baja

    public Node(T data, Node<T> next) {
        this.data = data;
        this.next = null;//Por default que apunta a nulo
        this.prev = null;
    }

    public Node(T data) {
        this.data = data;
    }

    public Node() {
        this.data = null;
        this.next = null;
    }

    //Constructor para la Cola de Prioridad
    public Node(T element, Integer priority) {
        this.data = element;
        this.priority = priority;
        this.next = null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    //esta clase es para llenar de una mejor manera la tabla de Linked List
    public static class NodeInfo {
        private String posicion;
        private final String valor;
        private final String referencia;

        public NodeInfo(String posicion, String valor, String referencia) {
            this.posicion = posicion;
            this.valor = valor;
            this.referencia = referencia;
        }

        public String getPosicion() { return posicion; }
        public String getValor() { return valor; }
        public String getReferencia() { return referencia; }
        //for the table
        public void setPosicion(String posicion) {
            this.posicion = posicion;
        }
    }

}
