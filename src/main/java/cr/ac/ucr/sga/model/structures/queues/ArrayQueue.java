package cr.ac.ucr.sga.model.structures.queues;

public class ArrayQueue<T> implements MyQueue<T> {
    private int n; //el tam max de la cola
    private T[] data; //arreglo de objetos
    private Integer[] priorityQueue; //para el manejo de prioridades
    private int front, rear; //anterior, posterior, me permite manejar los extremos de la cola

    //Constructor
    public ArrayQueue(int n){
        if(n<=0) System.exit(1); //se sale
        this.n = n;
        this.data = (T[]) new Object[n];
        this.priorityQueue = new Integer[n];
        this.rear = n-1; //ultimo elemento de la cola
        this.front = rear;
    }

    @Override
    public int size() {
        // cantidad de elementos en cola circular
        return rear - front;
    }

    @Override
    public void clear() {
        this.data = (T[]) new Object[n];
        this.rear = n-1; //ultimo elemento de la cola
        this.front = rear;
    }

    @Override
    public boolean isEmpty() {
        return front == rear;
    }

    //Revisar
    @Override
    public int indexOf(T element) throws QueueException {
        if(isEmpty()) throw new QueueException("Array Queue is empty");
        int pos = 0;
        int i = (front + 1) & n;
        while (i != (rear + 1) % n) {
            if ((data[i] == null && element == null) || (data[i] != null && data[i].equals(element)))
                return pos;
            i = (i + 1) % n;
            pos++;
        }
        return -1; // no encontrado
    }


    @Override
    public void enQueue(T element) throws QueueException {
        if(size() == data.length) throw new QueueException("Array Queue is full");
        //La primera vez cuando la cola está vacía, no entra al for
        for (int i = front; i < rear; i++) {
            data[i] = data[i+1]; // Mueve el elemento una posición a la izquierda
        }
        //Siempre encola por el extremo posterior
        data[rear] = element;
        front--; //La idea es que anterior quede en un campo vacío
    }

    //Revisar
    @Override
    public T deQueue() throws QueueException {
        if (isEmpty()) throw new QueueException("Array Queue is empty");
        return data[++front];
    }

    //Revisar
    @Override
    public void enQueue(T element, Integer priority) throws QueueException {
        if (priority == null) throw new QueueException("Priority cannot be null");
        if (size() == data.length) throw new QueueException("Array Queue is full");

        // Si está vacía, encola directo
        if (isEmpty()) {
            rear = (rear + 1) % n;
            data[rear] = element;
            priorityQueue[rear] = priority;
            return;
        }

        // Estrategia:
        // 1) Copiar la cola a arreglos temporales en orden lógico (frente -> final)
        // 2) Insertar (element, priority) en la posición correcta por prioridad
        // 3) Reconstruir la cola

        int s = size();
        Object[] tempData = new Object[s + 1];
        Integer[] tempPr = new Integer[s + 1];

        // Copia en orden
        int idx = 0;
        int i = (front + 1) % n;
        while (i != (rear + 1) % n) {
            tempData[idx] = data[i];
            tempPr[idx] = priorityQueue[i] == null ? Integer.MAX_VALUE : priorityQueue[i];
            idx++;
            i = (i + 1) % n;
        }

        // Insertar nuevo con su prioridad
        int newPr = priority;
        int insertAt = 0;
        while (insertAt < s && tempPr[insertAt] <= newPr) {
            insertAt++;
        }

        // Desplazar y colocar
        for (int j = s; j > insertAt; j--) {
            tempData[j] = tempData[j - 1];
            tempPr[j] = tempPr[j - 1];
        }
        tempData[insertAt] = element;
        tempPr[insertAt] = newPr;

        // Reconstruir cola vacía
        clear();

        // Re-encolar en el nuevo orden
        for (int j = 0; j < s + 1; j++) {
            rear = (rear + 1) % n;
            data[rear] = (T) tempData[j];
            // si quieres conservar "sin prioridad", aquí todo queda con prioridad numérica
            priorityQueue[rear] = tempPr[j] == Integer.MAX_VALUE ? null : tempPr[j];
        }
    }

    @Override
    public boolean contains(T element) throws QueueException {
        return indexOf(element) != -1;
    }

    @Override
    public T peek() throws QueueException {
        if (isEmpty()) throw new QueueException("Array Queue is empty");
        return data[front+1];
    }

    @Override
    public T front() throws QueueException {
        if (isEmpty()) throw new QueueException("Array Queue is empty");
        return data[front + 1];
    }

    @Override
    public String toString(){
        if(isEmpty()) return "Array Queue is empty";
        StringBuilder sb = new StringBuilder("FRONT ➡️ ");
        ArrayQueue<T> auxQueue = new ArrayQueue<>(size());
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
