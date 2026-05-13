package cr.ac.ucr.sga.model.structures.queues;

public interface MyQueue <T>{

    public int size(); // devuelve el número de elementos en la cola
    public void clear(); //elimina / inicializa la Cola
    public boolean isEmpty(); // true si la Cola está vacía
    public int indexOf(T element) throws QueueException; //devuelve la posicion de un elemento en la Cola
    public void enQueue(T element) throws QueueException; // encola un elemento por el extremo posterior(final) de la cola
    public T deQueue() throws QueueException; //suprime y retorna el elemento que está en la parte anterior(frente/inicio) de la cola
    public void enQueue(T element, Integer priority) throws QueueException; //encola un elemento por el extremo posterior, de acuerdo al a prioridad indicada
    public boolean contains(T element) throws QueueException; //true si el elemento fue encolado
    public T peek() throws QueueException; //devuelve el elemento que está en el frente/inicio de la Cola
    public T front() throws QueueException; //devuelve el elemento que está en el frente/inicio de la Cola
}
