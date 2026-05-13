package cr.ac.ucr.sga.model.structures.stacks;

public interface MyStack<T>{

    public int size(); // devuelve el número de elementos en la pila
    public void clear(); //anula la pila
    public boolean isEmpty(); // true si la pila está vacía
    public T peek() throws StackException; // devuelve el elemento del tope de la pila
    public T top() throws StackException; // devuelve el elemento del tope de la pila
    public void push(T element) throws StackException; // apila un elemento en el tope de la pila
    public T pop() throws StackException; //desapila el elemento del tope de la pila y lo retorna
}
