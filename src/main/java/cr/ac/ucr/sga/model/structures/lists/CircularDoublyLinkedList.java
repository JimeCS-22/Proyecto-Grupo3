package cr.ac.ucr.sga.model.structures.lists;

import cr.ac.ucr.sga.model.Node;

/**
 * Lista enlazada circular y doble
 * */
public class CircularDoublyLinkedList<T> implements List<T>, Cloneable {

    private Node<T> head; //inicio de la lista
    private Node<T> tail; //cola o final de la lista
    //private ListType listType;

    public CircularDoublyLinkedList() {
        this.head = this.tail = null;
    }

//    /** settea el tipo de lista*/
//    public void setListType(ListType listType) {
//        this.listType = listType;
//    }
//
//    @Override
//    public ListType getListType() {
//        return this.listType;
//    }

    /** Devuelve el nodo HEAD (puede ser null). */
    public Node<T> getHead() {
        return head;
    }

    public Node<Integer> getHead2() {
        return (Node<Integer>) head;
    }

    /** Devuelve el nodo TAIL (ult de la lista) (puede ser null). */
    public Node<T> getTail() {
        return tail;
    }

    @Override
    public int size() throws ListException {
        if(isEmpty()){
            return 0;
        }
        Node<T> aux = head;
        int count=0;
        while(aux!=tail){
            count++;
            aux = aux.next; //lo movemos al sgte nodo
        }
        return count+1; //+1 para que cuente el últ nodo

    }

    @Override
    public void clear() {
        head = tail = null;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Busca el valor en la lista. O(n).
     * Marca el nodo encontrado con FOUND, o el último con NOT_FOUND.
     * @return true si encuentra el nodo, false en caso contrario
     */
    @Override
    public boolean contains(T element) throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is empty");
        }
        Node<T> aux = head;
        while(aux!=tail){
            if(equals(aux.data, element)){
                return true;
            }
            aux = aux.next; //lo movemos al sgte nodo
        }
        //se sale cuando estamos en el últ nodo
        //solo queda validar la data del últ nodo
        return equals(tail.data, element);

    }

    @Override
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if(isEmpty()){
            head = tail = newNode;
        }
        else{
            tail.next = newNode;
            tail = newNode;
        }
        //hago el enlace circular
        tail.next = head;
        //hago el doble enlace
        head.prev = tail;

    }


    @Override
    public void addFirst(T element) {
        Node<T> node = new Node<>(element);
        node.next = head;
        head = node;
        //hago el enlace circular
        tail.next = head;
        //hago el doble enlace
        head.prev = tail;
    }

    @Override
    public void addLast(T element) {
        add(element);
    }

    @Override
    public void addInSortedList(T element) {

    }

    @Override
    public void remove(T element) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Doubly Linked List is empty");
        }
        //Caso 1. El elemento a suprimir es el primero
        if (equals(head.data, element)) {
            head = head.next;
        }
        //Caso general. Elemento puede estar en medio o al final
        else {
            Node<T> prev = head; //anterior
            while (prev != tail) {
                if (equals(prev.next.data, element)) {
                    Node<T> removed = prev.next;
                    //desenlanza el nodo
                    prev.next = removed.next;
                    //mantengo el doble enlace
                    removed.next.prev = prev;
                    return; //rompe el bucle
                }
                prev = prev.next; //se mueve al sgte nodo
            }
        }

        //q pasa si solo queda un nodo
        //y es el q quiero eliminar
        if(head==tail&&equals(tail.data, element)){
            clear(); //anulo la lista
            return; //se sale
        }

        //al final dejamos tail en el ult nodo
        //si la lista queda vacía se asigna null
        tail = head != null ? getNodeByIndex(indexOf(getLast())) : null;
        //hago el enlace circular y doble
        if (tail != null) {
            tail.next = head;
            head.prev = tail;
        }
    }

    @Override
    public T removeFirst() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Doubly Linked List is empty");
        }
        T first = head.data;
        head = head.next;
        //q pasa si solo queda un nodo
        if(head==tail){
            clear(); //anulo la lista
        }
        //hago el enlace circular y doble
        if (tail != null) {
            tail.next = head;
            head.prev = tail;
        }
        return first;
    }

    @Override
    public T removeLast() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Doubly Linked List is empty");
        }
        Node<T> prev = head; //anterior
        Node<T> aux = head;
        while (aux != tail) {
            prev = aux; //el nodo anterior al aux
            aux = aux.next; //se mueve al sgte nodo
        }
        //se sale cuando aux está en el ult nodo y prev está en el nodo anterior
        T last = aux.data;
        prev.next = head; //lo enlazamos con el primer nodo
        tail = prev; //ahora corresponde al ult nodo

        //q pasa si solo queda un nodo
        if(head==tail){
            clear(); //anulo la lista
        }
        //hago el enlace circular y doble
        if (tail != null) {
            tail.next = head;
            head.prev = tail;
        }
        return last;
    }

    @Override
    public void sort() throws ListException {

    }

    @Override
    public int indexOf(T element) throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is empty");
        }
        Node<T> aux = head;
        int index=1;
        while(aux!=null){
            if(equals(aux.data, element)){
                return index;
            }
            index++;
            aux = aux.next; //lo movemos al sgte nodo
        }
        return -1; //indica q el elemento no existe
    }

    @Override
    public T getFirst() throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is empty");
        }
        return head.data;

    }

    @Override
    public T getLast() throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is empty");
        }
        return tail.data; //es el ultimo en la lista
    }

    @Override
    public T getPrev(T element) throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is empty");
        }
        if(equals(head.data, element)){
            return tail.data; // (T) "It's the first, it has no previous"
        }
        Node<T> aux = head;
        while(aux!=tail){
            if(equals(aux.next.data, element)){
                return aux.data;
            }
            aux = aux.next;
        }
        return null; //(T)"Does not exist in single linked list"

    }

    @Override
    public T getNext(T element) throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is empty");
        }
        Node<T> aux = head; //dejar un rastro
        while(aux!=tail){
            if(equals(aux.data, element)){
                return aux.next.data;
            }
            aux = aux.next; //lo movemos al sgte nodo
        }
        //se sale del while cuando estamos en el últ nodo
        return equals(tail.data, element) ? head.data : null;
    }

    @Override
    public T get(int index) throws ListException {
        return getNodeByIndex(index).data;
    }


    public Node<T> getNodeByIndex(int index) throws ListException {
        if(isEmpty()){
            throw new ListException("Circular Doubly Linked List is empty");
        }
        Node<T> aux = head;
        int i = 1; //posicion del primer nodo
        while(aux!=tail){
            if(index == i){
                return aux;
            }
            i++;
            aux = aux.next; //lo movemos al sgte nodo
        }
        return index==i ? aux : null; //analizamos si el índice corresponde al últ nodo
    }

    /** Representación texto para el log. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HEAD ←→ ");
        Node<T> cur = head;
        while (head!=null && cur != tail) {
            sb.append("[").append(cur.data).append("]");
            if (cur.next != null) sb.append(" ←→ ");
            cur = cur.next;
        }
        if(tail!=null) //para evitar NullPointerException
            sb.append("[").append(tail.data).append("]");
        sb.append(" ←→ HEAD"); //termina en HEAD xq es una lista circular
        return sb.toString();
    }

    //===========AYUDAS=========//

    private boolean equals(T a, T b)  {
        return a==null ? b==null : a.equals(b);
    }

    public Node<T> getNode(T element) throws ListException {
        if(isEmpty()) {
            throw new ListException("Circular Doubly Linked List is empty");
        }
        Node<T> aux = head;
        while(aux!=null){
            if(equals(aux.data, element)) return aux;
            aux = aux.next;
        }
        return null;
    }



}

