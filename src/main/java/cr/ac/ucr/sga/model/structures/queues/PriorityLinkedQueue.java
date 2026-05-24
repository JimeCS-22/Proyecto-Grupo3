package cr.ac.ucr.sga.model.structures.queues;


import cr.ac.ucr.sga.model.Node;

public class PriorityLinkedQueue<T> implements MyQueue<T> {
    private Node<T> front; //anterior
    private Node<T> rear; //posterior
    private int size; //control de elementos encolados

    public PriorityLinkedQueue() {
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
    public void enQueue(T element) throws QueueException {
        Node<T> newNode = new Node<>(element);
        rear.next = newNode;
        rear = newNode;
        size++; //Actualizo el contador de elementos encolados
    }

    @Override
    public int indexOf(T element) throws QueueException {
        if (isEmpty())
            throw new QueueException("Priority Linked Queue is empty");

        PriorityLinkedQueue<T> aux = new PriorityLinkedQueue<>();
        int index = 1;
        int pos = -1;
        while (!isEmpty()) {

            if (equals(front(), element)){
                pos = index;
            }

            aux.enQueue(deQueue());
            index++;
        }

        while (!aux.isEmpty()) {

            enQueue(aux.deQueue());
        }
        return pos;
    }


    @Override
    public T deQueue() throws QueueException {
        if (isEmpty()) throw new QueueException("Priority Linked Queue is empty");

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
        Node<T> node = new Node<>(element, priority);
        if (isEmpty()) {
            front = rear = node;

        }else{
            Node<T> aux = front;
            Node<T> prev = front;
            while(aux != null && aux.priority <= priority){
                prev = aux; //dejamos un rastro
                aux = aux.next;
            }
//se sale del while cuando alcanza nulo o la prioridad
//del nuevo elemento es mayor
            if (aux == front) { //si el nuevo element tiene prioridad mayor
                node.next = front;
                front = node;
            }else if (aux == null) {
                prev.next = node;
                rear = node ;
            }else{ //en cualquier otro caso
                prev.next = node;
                node.next = aux;
            }
        }
    }

    @Override
    public boolean contains(T element) throws QueueException {
        if (isEmpty()) throw new QueueException("Priority Linked Queue is empty");
        PriorityLinkedQueue<T> aux = new PriorityLinkedQueue<>();
        boolean finded = false;
        while (!isEmpty()) {
            if(equals(front(), element)) {
                finded = true;
            }
            aux.enQueue(deQueue());
        }
        //Al final dejamos el tda cola en su estado original
        while(!aux.isEmpty()) {
            enQueue(aux.deQueue());
        }
        return finded;
    }

    //Peek y Front son lo mismo
    @Override
    public T peek() throws QueueException {
        if (isEmpty()) throw new QueueException("Priority Linked Queue is empty");
        return front.data;
    }

    @Override
    public T front() throws QueueException {
        if (isEmpty()) throw new QueueException("Priority Linked Queue is empty");
        return front.data;
    }

    @Override
    public String toString(){
        if(isEmpty()) return "Priority Linked Queue is empty";
        StringBuilder sb = new StringBuilder("FRONT ➡️ ");
        PriorityLinkedQueue<T> auxQueue = new PriorityLinkedQueue<>();
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

    private boolean equals(T a, T b) {
        return a == null ? b == null : a.equals(b);
    }
}
