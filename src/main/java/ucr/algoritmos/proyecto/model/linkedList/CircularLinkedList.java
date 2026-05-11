package ucr.algoritmos.proyecto.model.linkedList;

import ucr.algoritmos.proyecto.model.Node;

public class CircularLinkedList<T> implements List<T> {

    private Node<T> head; //Inicio de la lista
    private Node<T> tail; //Fin de la lista

    public CircularLinkedList() {
        this.head = null;
        this.tail = null;
    }

    public CircularLinkedList(Node<T> head, Node<T> tail) {
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
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
        int counter = 0;
        Node<T> aux = head;
        while (aux != tail) {
            counter++;
            aux = aux.next;
        }
        return counter+1; //para que cuente el ultimo nodo
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
            tail = node; //Dejo tail en el ultimo nodo
        }
        //Hago el enlace circular
        tail.next = head;
    }

    @Override
    public void addFirst(T element) {
        Node<T> node = new Node<>(element);
        node.next = head;
        head = node; //Porque el nuevo queda  de primero
        //Hago el enlace circular
        tail.next = head;
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
            throw new ListException("Circular Circular Linked List is empty");

        // Caso 1: Cuando el elemento a suprimir es el primero en la listaq
        if (equals(head.data, element)) {
            head = head.next;
        }
        //Caso general: El elemento a suprimir puede estar en el medio o al final
        else {
            Node<T> prev = head;
            while (prev != tail) {

                if (equals(prev.next.data, element)) {

                    //Ya encontre el elemnto a eliminar
                    Node<T> removed = prev.next;

                    //Desenlazo el nodo
                    prev.next = removed.next;//Se brinca el nodo a suprimir

                }

                prev = prev.next;
                if (prev == null) break;

            }
            //Se sale del while cuando tail está en el último nodo
            //Que pasa solo si queda en un nodo y es el que quiero eliminar
            if(head == tail && equals(tail.data, element)){
                clear(); //Anulo la lista
                return; //Se sale del metodo
            }

            //Al final dejamos tail en el ultimo nodo
            //  Si la lista queda vacia, se asigna nulo
            tail = tail != null ? getNodeByIndex(indexOf(getLast())) : null;


        }
    }

    @Override
    public T removeFirst() throws ListException {
        if (isEmpty())
            throw new ListException("Circular Circular Linked List is empty");
        T first = head.data;
        head = head.next;
        //Que pasa solo si queda en un nodo y es el que quiero eliminar
        if(head == tail){
            clear(); //Anulo la lista
        } else
            //Hago el enlace circular
            tail.next = head;
        return first;
    }

    @Override
    public T removeLast() throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");

        Node<T> aux = head;
        Node<T> prev = head;
        while (aux.next != tail) {
            prev = aux;//dejamos un rastro en el modo auxiliar
            aux = aux.next;
        }
        //Se sale del while cuando aux esta en el ultimo nodo
        T last = aux.data;//la data del nodo
        prev.next = head; //Lo enlazamos con el primer nodo
        tail = prev;//para que tail quede apuntando al ult nodo

        //Validacion si solo queda un nodo
        if (head == tail) clear();//anulamos la lista

        //Hacemos el enlace circular
        if(tail != null){
            tail.next = head;
        }
        return last;
    }

    @Override
    public boolean contains(T element) throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
        Node<T> aux = head;
        while (aux != tail) {
            if (equals(aux.data, element)) return true;
            aux = aux.next;
        }
        return equals(tail.data, element);
    }

    @Override
    public void sort() throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
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
            throw new ListException("Circular Linked List is empty");
        Node<T> aux = head;
        int index = 1; // Primer elemento es 1, si deseas cambia a 0
        while (aux != tail) {
            if (equals(aux.data, element)) return index;
            index++;
            aux = aux.next;
        }
        return equals(aux.data, element) ? index : -1;
    }

    @Override
    public T getFirst() throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
        return head.data;
    }

    @Override
    public T getLast() throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
        return tail.data;
    }

    @Override
    public T getPrev(T element) throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");

        Node<T> aux = head;
        Node<T> prev = null;

        if(equals(head.data, element)) return tail.data;
        while (aux != tail) {
            if (equals(aux.next.data, element)) return aux.data;
            aux = aux.next;
        }
        return null;
    }

    @Override
    public T getNext(T element) throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");

        Node<T> aux = head;
        while (aux != tail) {
            if (equals(aux.data, element)) {
                return aux.next.data;
            }
            aux = aux.next;
        }
        //Se sale del While cuando estamos en el ultimo nodo
        return equals(tail.data, element) ? tail.data : null;
    }

    @Override
    public T get(int index) throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
        if (index < 1 || index > size())
            throw new ListException("Index out of bounds");

        Node<T> aux = head;
        int count = 1;
        while (aux != tail) {
            if (count++ == index) {
                return aux.data;
            }
            aux = aux.next;
        }
        //Se sale del while cuando aux está em eñ último nodo
        return count==index ? tail.data : null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HEAD →");//➡️
        Node<T> aux = head;

        while (aux != tail) {
            sb.append("[").append(aux.data).append("]");
            if (aux.next != null) sb.append("→");
            aux = aux.next;
        }
        //Falta agregar la info del ultimo nodo
        if(tail!=null) //Para evitar el NullPointException
            sb.append("[").append(tail.data).append("]");
        sb.append("→ HEAD "); //Que apunte a nulo
        return sb.toString();
    }


    /* ---- AYUDAS----- */
    private boolean equals(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }

    private Node<T> getNode(int index) throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
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

    private Node<T> getNode(T element) throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
        Node<T> aux = head;
        while (aux != null) {
            if (equals(aux.data, element)) return aux;
            aux = aux.next;
        }
        return null;
    }

    private Node<T> getNodeByIndex(int index) throws ListException {
        if (isEmpty())
            throw new ListException("Circular Linked List is empty");
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