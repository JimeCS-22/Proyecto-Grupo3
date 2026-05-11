package ucr.algoritmos.proyecto.model.Queue;

import ucr.algoritmos.proyecto.model.Node;

public class LinkedQueue<T> implements MyQueue<T> {
    private Node<T> front; //anterior
    private Node<T> rear; //posterior
    private int size; //control de elementos encolados

    public LinkedQueue() {
        front = rear = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        front = rear = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return front == null;
    }

    @Override
    public int indexOf(T element) throws QueueException {
        if (isEmpty())
            throw new QueueException("Linked Queue is empty");

        int index = 0;
        Node<T> current = front;
        while (current != null) {
            if ((current.data == null && element == null) || (current.data != null && current.data.equals(element)))
                return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    @Override
    public void enQueue(T element) throws QueueException {
        Node<T> newNode = new Node<>(element);

        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++; //Actualizo el contador de elementos encolados
    }

    @Override
    public T deQueue() throws QueueException {
        if (isEmpty()) throw new QueueException("Linked Queue is empty");

        T element = front.data;
        //Caso 1. Cuando solo hay un elemento
        if(front == rear) clear();

        //Caso 2. Cuando hay más de un elemento
        else {
            front = front.next;
        }
        size--;
        return element;
    }

    @Override
    public void enQueue(T element, Integer priority) throws QueueException {
        if (priority == null)
            throw new QueueException("Priority cannot be null");
        enQueue(element);
    }

    @Override
    public boolean contains(T element) throws QueueException {
        return indexOf(element) != -1;
    }

    //Peek y Front son lo mismo
    @Override
    public T peek() throws QueueException {
        if (isEmpty()) throw new QueueException("Linked Queue is empty");
        return front.data;
    }

    @Override
    public T front() throws QueueException {
        if (isEmpty()) throw new QueueException("Linked Queue is empty");
        return front.data;
    }

    @Override
    public String toString(){
        if(isEmpty()) return "Array Queue is empty";
        StringBuilder sb = new StringBuilder("FRONT ➡️ ");
        LinkedQueue<T> auxQueue = new LinkedQueue<>();
        try {
            while(!isEmpty()) {
                sb.append("[").append(peek()).append("] ");
                auxQueue.enQueue(deQueue());
                if (!isEmpty()) sb.append(", ");
            }

            //Al final dejamos la cola en su estado original
            while(!auxQueue.isEmpty()) {
                enQueue(auxQueue.deQueue());
            }

        } catch (QueueException e) {
            throw new RuntimeException(e);
        }
        sb.append(" ➡️ REAR");
        return sb.toString();
    }
}
