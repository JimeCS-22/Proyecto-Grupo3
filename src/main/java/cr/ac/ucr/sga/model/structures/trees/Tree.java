package cr.ac.ucr.sga.model.structures.trees;

public interface Tree<T> {

    //devuelve el número de elementos en el árbol
    public int size() throws TreeException;
    //private int size(BTreeNode nodo)

    //remueve todos los elementos del árbol
    public void clear();

    //true si el árbol está vacío
    public boolean isEmpty();

    //true si el elemento existe en el árbol
    public boolean contains(T element) throws TreeException;
    //private boolean binarySearch(BTreeNode node, Object element)

    //inserta un elemento en el árbol
    public void add (T element);
    //private BtreeNode add(BtreeNode node, Object element)

    //suprime un elemento del árbol
    //Caso 1. El nodo a suprimir no tiene hijos
    //Caso 2. El nodo a suprimir solo tiene un hijo
    //Caso 3. El nodo a suprimir tiene dos hijos
    public void remove(T element) throws TreeException;
    //private BTreeNode remove(BTreeNode node, Object element)

    //devuelve la altura de un nodo (el número de ancestros)
    public int height(T element) throws TreeException;
    //private int height(BTreeNode node, Object element)

    //devuelve la altura del árbol (altura máxima de la raíz a
    //cualquier hoja del árbol)
    public int height() throws TreeException;
    //private int height(BTreeNode node)

    //devuelve el valor mínimo contenido en el árbol
    public T min() throws TreeException;
    //private T min(BTreeNode node)

    //devuelve el valor máximo contenido en el árbol
    public T max() throws TreeException;
    //private T max(BTreeNode node)

    //Pre Order Transversal Tour: N-L-R
    public String preOrder() throws TreeException;
    //private String preOrder(BTreeNode node)

    //In Order Transversal Tour: L-N-R
    public String inOrder() throws TreeException;
    //private String inOrder(BTreeNode node)

    //Post Order Transversal Tour: L-R-N
    public String postOrder() throws TreeException;
    //private String postOrder(BTreeNode node)

    //muestra por consola al altura de cada elemento del arbol
    public String nodeHeight() throws TreeException;
}
