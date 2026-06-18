package cr.ac.ucr.sga.model.structures.lists;

import cr.ac.ucr.sga.model.Node;

import java.util.ArrayList;



public class LinkedList<T> implements List<T> {

    private Node<T> head; //Inicio de la lista
    private Node<T> tail; //Fin de la lista

    public LinkedList() {
        this.head = null;
        this.tail = null;
    }

    public LinkedList(Node<T> head, Node<T> tail) {
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
        int counter = 0;
        Node<T> aux = head;
        while (aux != null) {
            counter++;
            aux = aux.next;
        }
        return counter;
    }

    @Override
    public void clear() {
        this.head = null;
        this.tail = null; // Es recomendable limpiar tail también
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
        } else {
            tail.next = node;
            tail = node;
        }
    }

    //agregar a la lista en una cierta posición (index)
    public void add(int index, T element) throws ListException {
        int size= size();

        if (index < 0 || index > size) {
            throw new ListException("Index out of bounds");
        }

        Node<T> node = new Node<>(element);

        // Caso 1: lista vacía
        if (head == null) {
            head = node;
            tail = node;
        }
        // Caso 2: insertar al inicio
        else if (index == 0) {
            node.next = head;
            head = node;
        }
        // Caso 3: insertar al final
        else if (index == size) {
            tail.next = node;
            tail = node;
        }
        // Caso 4: insertar en la posición index
        else {
            Node<T> prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
            }
            node.next = prev.next;
            prev.next = node;
        }

        size ++;
    }


    @Override
    public void addFirst(T element) {
        Node<T> node = new Node<>(element);
        node.next = head;
        head = node;
        if (tail == null)
            tail = node; // si estaba vacío
    }

    @Override
    public void addLast(T element) {
        add(element); // el método add siempre agrega al final
    }

    @Override
    public void addInSortedList(T element) {
        Node<T> newNode = new Node<>(element);

        // Lista vacía o debe ir al inicio
        if (isEmpty() || ((Comparable<T>) element).compareTo(head.data) < 0) {
            newNode.next = head;
            head = newNode;
            if (tail == null)
                tail = newNode;
            return;
        }

        Node<T> current = head;
        // Buscar dónde insertar
        while (current.next != null && ((Comparable<T>) element).compareTo(current.next.data) > 0) {
            current = current.next;
        }

        // Insertar después de current
        newNode.next = current.next;
        current.next = newNode;
        // Si se insertó al final, actualizar tail
        if (newNode.next == null)
            tail = newNode;
    }

    @Override
    public void remove(T element) throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");

        // Caso 1: Cuando el elemento a suprimir es el primero en la listaq
        if (equals(head.data, element)) {
            head = head.next;
            if (head == null) tail = null; // si quedó vacía
            return;
        }
        //Caso general: El elemento a suprimir puede estar en el medio o al final
        else {
            Node<T> prev = head;
            while (prev != null && prev.next != null) {
                if (equals(prev.next.data, element)) {

                    //Ya encontre el elemnto a eliminar
                    Node<T> removed = prev.next;

                    //Desenlazo el nodo
                    prev.next = removed.next;//Se brinca el nodo a suprimir

                }

                prev = prev.next;
                if (prev == null) break;


            }

            //Al final dejamos tail en el ultimo nodo
            //  Si la lista queda vacia, se asigna nulo
            tail = tail != null ? getNodeByIndex(indexOf(getLast())) : null;


        }
    }

    @Override
    public T removeFirst() throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");
        T first = head.data;
        head = head.next;
        if (head == null) tail = null;
        return first;
    }

    @Override
    public T removeLast() throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");

        Node<T> aux = head;
        Node<T> prev = head;
        while (aux.next != null) {
            prev = aux;//dejamos un rastro en el modo auxiliar
            aux = aux.next;
        }
        //Se sale del while cuando aux esta en el ultimo nodo
        T last = aux.data;//la data del nodo
        prev.next = null;
        tail = prev;//para que tail quede apuntando al ult nodo
        //Validacion si solo queda un nodo
        if (prev == aux) clear();//anulamos la lista
        return last;
    }

    @Override
    public boolean contains(T element) throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");
        Node<T> aux = head;
        while (aux != null) {
            if (equals(aux.data, element)) return true;
            aux = aux.next;
        }
        return false;
    }

    @Override
    public void sort() throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");
        int n = size();
        for (int i = 1; i <= n; i++) {
            for (int j = i+1; j <= n; j++) {
                Node<T> nodeI = getNode(i);
                Node<T> nodeJ = getNode(j);
                if (util.Utility.compare(nodeJ.data, nodeI.data) < 0) {
                    T temp = nodeI.data;
                    nodeI.data = nodeJ.data;
                    nodeJ.data = temp;
                }
            }
        }
    }

    @Override
    public int indexOf(T element) throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");
        Node<T> aux = head;
        int index = 1; // Primer elemento es 1, si deseas cambia a 0
        while (aux != null) {
            if (equals(aux.data, element)) return index;
            index++;
            aux = aux.next;
        }
        return -1;
    }

    @Override
    public T getFirst() throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");
        return head.data;
    }

    @Override
    public T getLast() throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");
        return tail.data;
    }

    @Override
    public T getPrev(T element) throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");

        Node<T> aux = head;
        Node<T> prev = null;

        while (aux != null) {
            if (equals(aux.data, element)) {
                return (prev == null) ? null : prev.data;
            }
            prev = aux;
            aux = aux.next;
        }
        return null;
    }

    @Override
    public T getNext(T element) throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");

        Node<T> aux = head;
        while (aux != null) {
            if (equals(aux.data, element)) {
                return aux.next != null ? aux.next.data : null;
            }
            aux = aux.next;
        }
        return null;
    }

    @Override
    public T get(int index) throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");
        if (index < 1 || index > size())
            throw new ListException("Index out of bounds");

        Node<T> aux = head;
        int count = 1;
        while (aux != null) {
            if (count == index) {
                return aux.data;
            }
            count++;
            aux = aux.next;
        }
        return null; // Nunca debería llegar aquí
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HEAD →");//➡️
        Node<T> aux = head;

        while (aux != null) {
            sb.append("[").append(aux.data).append("]");
            if (aux.next != null) sb.append("→");
            aux = aux.next;
        }
        sb.append("→ NULL ");
        return sb.toString();
    }


    /* ---- AYUDAS----- */
    private boolean equals(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    private Node<T> getNode(int index) throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");
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

    public Node<T> getNode(T element) throws ListException {
        if (isEmpty())
            throw new ListException("Linked List is empty");
        Node<T> aux = head;
        while (aux != null) {
            if (equals(aux.data, element)) return aux;
            aux = aux.next;
        }
        return null;
    }

    public Node<T> getNodeByIndex(int index) throws ListException {
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
    //-----------AYUDAS-----------
    //para la manipulación se pasa la LinkedList propia a la Lista de java para usar en los componentes gráficos más fácilmente
    public ArrayList<T> toList() {
        ArrayList<T> arrayList = new ArrayList<>();
        Node<T> aux = head;

        while (aux != null) {
            arrayList.add(aux.data);
            aux = aux.next;
        }

        return arrayList;
    }
}