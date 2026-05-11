package ucr.algoritmos.proyecto.model.stack;

public class ArrayStack<T> implements MyStack<T> {

    private int n; //el tam max de la pila
    private int top; //para llevar el control del tope de la pila
    private T[] data; //arreglo para guardar los elementos de la pila

    public ArrayStack(int n) {
        if(n<=0) System.exit(1); //se sale
        this.n = n;
        this.top = -1;//fuera de cualquier indice del arreglo
        this.data = (T[]) new Object[n];
    }

    @Override
    public int size() {
        return top+1;
    }

    @Override
    public void clear() {
        this.data = (T[]) new Object[n];
        this.top = -1;
    }

    @Override
    public boolean isEmpty() {
        return top==-1;
    }

    @Override
    public T peek() throws StackException {
        if (isEmpty())
            throw new StackException("Array Stack is empty");

        return this.data[top];
    }

    @Override
    public T top() throws StackException {
        if (isEmpty())
            throw new StackException("Array Stack is empty");
        return this.data[top];
    }

    @Override
    public void push(T element) throws StackException {
        if(top==n-1)//if(top==data.length-1)
            throw new StackException("Array Stack is full");
        data[++top] = element;
    }

    @Override
    public T pop() throws StackException {
        if (isEmpty())
            throw new StackException("Array Stack is empty");

        return this.data[top--];
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Array Stack is empty";

        StringBuilder sb = new StringBuilder("TOP ➡️ ");
        try{
            ArrayStack<T> auxStack = new ArrayStack<>(n);

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
