package cr.ac.ucr.sga.model.structures.stacks;

import cr.ac.ucr.sga.model.Node;

public class LinkedStack<T> implements MyStack<T> {
    private Node<T> top; //es un apuntador
    private int size; //elementos apilados

    public LinkedStack() {
        top = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        this.top = null;
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {

        return top == null;
    }

    @Override
    public T peek() throws StackException {
        if (isEmpty())
            throw new StackException("Linked Stack is empty");
        return top.data;
    }

    @Override
    public T top() throws StackException {
        if (isEmpty())
            throw new StackException("Linked Stack is empty");
        return top.data;
    }

    @Override
    public void push(T element) throws StackException {

        Node<T> node = new Node<>(element);
        node.next = top;
        top = node;
        size++;
    }

    @Override
    public T pop() throws StackException {
        if(isEmpty())
            throw new StackException("Linked Stack is empty");
        T data = top.data;//elemento del tope de la pila
        top = top.next;
        size--;//al eliminar un elemento, decremento el contador
        return data;//la data del nodo eliminado
    }

    public boolean contains(T element) throws StackException {

        if (isEmpty())
            return false;

        LinkedStack<T> aux = new LinkedStack<>();
        boolean found = false;

        while (!isEmpty()) {

            T value = pop();

            if (value.equals(element)) {
                found = true;
            }

            aux.push(value);
        }

        // restaurar pila
        while (!aux.isEmpty()) {
            push(aux.pop());
        }

        return found;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Linked Stack is empty";

        StringBuilder sb = new StringBuilder("TOP ➡️ ");
        try{
            LinkedStack<T> auxStack = new LinkedStack<>();

            while(!isEmpty()){

                sb.append("[").append(peek()).append("]");
                auxStack.push(pop());

                if(!isEmpty()) sb.append(" , ");
            }
            //dejamos la pila original como al inicio
            while(!auxStack.isEmpty()) push(auxStack.pop());

        }catch(StackException e){
            throw new RuntimeException(e);
        }

        sb.append("➡️ BOTTOM");
        return sb.toString();


    }
}
