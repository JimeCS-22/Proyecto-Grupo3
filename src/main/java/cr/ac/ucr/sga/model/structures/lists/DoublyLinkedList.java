package cr.ac.ucr.sga.model.structures.lists;

import cr.ac.ucr.sga.model.Node;

public class DoublyLinkedList<T> implements List<T> {

    private Node<T> head;//Inicio de la lista
    private Node<T> tail;//Final de la lista

    public DoublyLinkedList() {
    }

    //Constructor
    public DoublyLinkedList(Node<T> head,  Node<T> tail) {
        this.head = head;
        this.tail = tail;
    }


    public Node<T> getHead() {
        return head;
    }

    public Node<T> getTail() {
        return tail;
    }

    @Override
    public int size() throws ListException {

        int count = 0;
        Node<T> aux = head;

        while (aux != null) {
            count++;
            aux = aux.next;
        }

        return count;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public void add(T element) {
        Node<T> node = new Node<>(element);
         if (head == null) {
             head = node;
             tail = node;
         }else {//Significa que head apunta a un nodo existente

             Node<T> aux = head;
             //me muevo por la derecha hasta alcanzar el ultimo elemento
             while (aux.next != null) {
                 aux = aux.getNext();
             }

             //Cuando se sale de while aux.next == null
             aux.next = node;
             // Hacemos el doble enlace
             node.prev = aux;
             tail = node;
         }

    }

    @Override
    public void addFirst(T element) {
        Node<T> node = new Node<>(element);

        if (isEmpty()) {          // <-- agregado
            head = node;
            tail = node;
            return;
        }

        node.next = head;
        //hacemos el doble enlace
        head.prev = node;
        head = node;//pq el nuevo nodo quede de primero

    }

    @Override
    public void addLast(T element) {
        add(element);

    }

    @Override
    public void addInSortedList(T element) {
        Node<T> node = new Node<>(element);

        if (isEmpty()) {
            head = node;
            tail = node;
            return;
        }

        Comparable<T> comp = (Comparable<T>) element;

        // Insertar al inicio
        if (comp.compareTo(head.data) <= 0) {
            node.next = head;
            head.prev = node;
            head = node;
            return;
        }

        Node<T> aux = head;

        while (aux.next != null && comp.compareTo(aux.next.data) > 0) {
            aux = aux.next;
        }

        node.next = aux.next;
        if (aux.next != null) aux.next.prev = node;

        aux.next = node;
        node.prev = aux;

        if (node.next == null) tail = node;

    }

    @Override
    public void remove(T element) throws ListException {

        if(isEmpty()) throw new ListException("Doubly Linked List is empty");

        //Caso 1: EL elemento a suprimir es el primero de la lista
        if (equals(head.data, element)) {
            head = head.next;

            if (head != null) {
                head.prev = null;//para que no quede apuntando al nodo suprimido
            } else {
                tail = null; // si se borra el único nodo
            }
        }
        else {
                Node<T> prev = head;
                while (prev != null && prev.next != null) {

                    if (equals(prev.next.data, element)) {

                        //Ya encontre el elemnto a eliminar
                        Node<T> removed = prev.next;

                        //Desenlazo el nodo
                        prev.next = removed.next;//Se brinca el nodo a suprimir

                        //dejamos el doble enlace
                        if (removed.next != null) {
                            removed.next.prev = prev;
                        } else {
                            tail = prev;
                        }
                        break;


                    }

                    prev = prev.next;

                    if (prev == null) break;

                }
                //Al final dejamos tail en el ultimo nodo
                //  Si la lista queda vacia, se asigna nulo
                //tail = tail != null ? getNodeByIndex(indexOf(getLast())) : null;


            }
        }




    @Override
    public T removeFirst() throws ListException {
        if (isEmpty()) throw new ListException("Doubly Linked List is empty");

        T first = head.data;
        head = head.next;

        if (head != null) {
            head.prev = null;
        } else {
            tail = null; // solo si la lista quedó vacía
        }

        return first;
    }



    @Override
    public T removeLast() throws ListException {
        if (isEmpty())
            throw new ListException("Doubly Linked List is empty");

        Node<T> aux = head;
        Node<T> prev = head;
        while (aux.next != null) {
            prev = aux;//dejamos un rastro en el modo auxiliar
            aux = aux.next;
        }
        //Se sale del while cuando aux esta en el ultimo nodo
        T last = aux.data;//la data del nodo
        prev.next = null;//Para desconectar el ult nodo
        tail = prev;//para que tail quede apuntando al ult nodo
        //Validacion si solo queda un nodo
        if (prev == aux) clear();//anulamos la lista
        return last;
    }

    @Override
    public boolean contains(T element) throws ListException {

        if (isEmpty()) throw new ListException("Doubly Linked List is empty");

        Node<T> aux = head;

        while (aux != null) {
            if (equals(aux.data, element)) return true;//ya lo encontro
            aux = aux.next;//muevo el aux al nodo siguiente
        }

        return false;
    }

    @Override
    public void sort() throws ListException {

        if (isEmpty()) throw new ListException("Doubly Linked List is empty");

        boolean swapped;

        do {
            swapped = false;
            Node<T> aux = head;

            while (aux.next != null) {
                Comparable<T> a = (Comparable<T>) aux.data;

                if (a.compareTo(aux.next.data) > 0) {
                    // intercambiar datos
                    T temp = aux.data;
                    aux.data = aux.next.data;
                    aux.next.data = temp;

                    swapped = true;
                }
                aux = aux.next;
            }

        } while (swapped);

    }

    @Override
    public int indexOf(T element) throws ListException {
        if (isEmpty()) throw new ListException("Doubly Linked List is empty");

        Node<T> aux = head;
        int index = 1;

        while (aux != null) {
            if (equals(aux.data, element)) return index;
            index++;
            aux = aux.next;
        }
        return -1;
    }

    @Override
    public T getFirst() throws ListException {
        if (isEmpty()) throw new ListException("Doubly Linked List is empty");

        return head.data;
    }

    @Override
    public T getLast() throws ListException {
        if (isEmpty()) throw new ListException("Doubly Linked List is empty");
        return tail.data;
    }

    @Override
    public T getPrev(T element) throws ListException {

        if (isEmpty()) throw new ListException("Doubly Linked List is empty");

        Node<T> aux = head;

        while (aux != null) {
            if (equals(aux.data, element)) {
                return (aux.prev != null) ? aux.prev.data : null;
            }

            aux = aux.next;
        }

        return null;
    }

    @Override
    public T getNext(T element) throws ListException {

        if (isEmpty()) throw new ListException("Doubly Linked List is empty");

        Node<T>  aux = head;

        while (aux != null) {
            if (equals(aux.data, element)) {
                return (aux.next != null) ? aux.next.data : null;
            }

            aux = aux.next;
        }
        return null;
    }

    @Override
    public T get(int index) throws ListException {
        return getNode(index).data;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HEAD ->\n");
        Node<T> aux = head;

        while (aux != null) {
            sb.append("[");

            sb.append(aux.prev != null ? aux.prev.data : "NULL");
            sb.append(" ⬅️");
            sb.append(aux.data);
            sb.append(" ➡️ ");
            sb.append(aux.next != null ? aux.next.data : "NULL");

            sb.append("]");

            if (aux.next != null) sb.append(" ↔ ->\n");
            aux = aux.next;
        }

        return sb.toString();
    }


    /* ---- AYUDAS----- */
    private boolean equals(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    private Node<T> getNode(int index) throws ListException {
        if (isEmpty())
            throw new ListException(" Doubly Linked List is empty");
        if (index < 1 || index > size())
            throw new ListException("Index out of bounds");
        Node<T> aux = head;
        int count = 1;
        while (aux != null) {
            if (count == index) return aux;
            count++;
            aux = aux.next;
        }
        return null;
    }

    private Node<T> getNodeByIndex(int index) throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");
        Node<T> aux = head;
        int pos = 1;//la posicion del primer nodo
        while (aux != null) {
            if (pos == index) return aux;
            aux = aux.next;
            pos++;
        }
        return null;
    }
}