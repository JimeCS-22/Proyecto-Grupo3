package cr.ac.ucr.sga.model.structures.trees;

import java.util.*;

public class BTree<T extends Comparable<T>> implements Tree<T> {
    public BTreeNode<T> root; //representa la unica entrada al árbol

    //Constructor
    public BTree(){
        this.root =null;
    }
    @Override
    public int size() throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Tree is empty");
        return size(root);
    }

    private int size(BTreeNode<T> nodo){
        if(nodo == null) return 0;
        return size(nodo.left) + size(nodo.right) + 1;
    }

    @Override
    public void clear() {
        this.root = null;
    }

    @Override
    public boolean isEmpty() {
        return  this.root == null;
    }

    @Override
    public boolean contains(T element) throws TreeException {
        if (isEmpty()) throw new TreeException("Binary Tree is empty");
        return binarySearch(this.root, element);
    }

    public boolean binarySearch( BTreeNode<T> node, T element){
        if (node == null) return false;
        else if (equals(node.data, element)) return true;
        else return  binarySearch(node.left, element) || binarySearch(node.right, element);
    }


    @Override
    public void add(T element) {
        this.root = add(root, element, "root");
    }

    private BTreeNode<T>add(BTreeNode<T> node, T element){

        if(node == null){

            node = new BTreeNode<>(element);

        }else{
            //debemos establecer algún criterio para insertar elementos
            int value = new Random().nextInt(10);
            if (value % 2==0) //si el valor es para enserte por la izquierda
                node.left = add(node.left, element);
            else node.right = add(node.right, element);
        }

        return node;

    }

    private BTreeNode<T>add(BTreeNode<T> node, T element, String path){

        if(node == null){

            node = new BTreeNode<>(element, path);

        }else{
            //debemos establecer algún criterio para insertar elementos
            int value = new Random().nextInt(10);
            if (value % 2==0) //si el valor es para enserte por la izquierda
                node.left = add(node.left, element,  path + " /left");
            else node.right = add(node.right, element,   path + " /right");
        }

        return node;

    }


    @Override
    public void remove(T element) throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Tree is empty");
        root = remove(root, element);
    }

    private BTreeNode<T> remove(BTreeNode<T> node, T element){
        if (node == null) {
            return null; // caso base: no hay nada que eliminar
        }

        if (equals(node.data, element)) { // encontró el elemento
            // Caso 1: sin hijos
            if (node.left == null && node.right == null) {
                return null;
            }
            // Caso 2: un solo hijo
            else if (node.left != null && node.right == null) {
                node.left = newPath(node.left, node.path);
                return node.left;
            } else if (node.left == null && node.right != null) {
                node.right = newPath(node.right, node.path);
                return node.right;
            }
            // Caso 3: dos hijos
            else {
                T minValue = minNode(node.right);
                node.data = minValue;
                node.right = remove(node.right, minValue);
            }
        } else {
            // recorrer recursivamente
            node.left = remove(node.left, element);
            node.right = remove(node.right, element);
        }

        return node;
    }


    /**
     * Este metodo sirve para actualizar la ruta de todos los nodos del subarbol
     */
    private BTreeNode<T> newPath(BTreeNode<T> node, String path) {
        if (node != null) {
            node.path = path;
            newPath(node.left, path+"/left");
            newPath(node.right, path+"/right");
        }
        return node;
    }


    //devuelve la altura de un elemento especifico dentro del arbol
    @Override
    public int height(T element) throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Tree is empty");
        return height(root, element,0);
    }
    private int height(BTreeNode<T> node, T element, int count){
        if (node == null) {
            return 0;
        } else if (equals(node.data, element)) {
            return count;
        }else return Math.max(height(node.left,element,++count), height(node.right,element,count));
        //Con el método propio amerita cambios
       //return (int) maxElement(height(node.left,element,++count), height(node.right,element,count));
    }

    //Devuelve la altura del árbol
    @Override
    public int height() throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Tree is empty");
        return height(root)-1;//-1 porque la altura de la raiz es 0
    }

    public int height(BTreeNode<T> node){
        if (node == null) {
            return 0;
        }else return Math.max(height(node.left),height(node.right))+1;
    }
    @Override
    public T min() throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Tree is empty");
        return minNode(root);
    }
    //distinto nombre al de BST para que se pueda llamar correctamente en el AVL
    private T minNode(BTreeNode<T> node){

        if(node.left != null && node.right != null)//Caso 1. Cuando el nodo tiene los dos hijos
            return minElement(node.data,minElement(minNode(node.left), minNode(node.right)));
         else if (node.left != null) //Caso 2. Cuando el nodo tiene un solo hijo izquierdo
            return minElement(node.data, minNode(node.left));
        else if (node.right != null) //Caso 3. Cuando el nodo tiene un solo hijo derecho
            return minElement(node.data, minNode(node.right));
        else
        return node.data;// es una hoja
    }

    private T minElement(T a, T b){

        if(a == null) return b;
        else if(b == null) return a;
        return compareElement(a,b) <= 0 ? a : b;

    }



    @Override
    public T max() throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Tree is empty");
        return max(root);
    }
    private T max(BTreeNode<T> node){

        if(node.left != null && node.right != null)//Caso 1. Cuando el nodo tiene los dos hijos
            return maxElement(node.data,maxElement(max(node.left), max(node.right)));
        else if (node.left != null) //Caso 2. Cuando el nodo tiene un solo hijo izquierdo
            return maxElement(node.data, max(node.left));
        else if (node.right != null) //Caso 3. Cuando el nodo tiene un solo hijo derecho
            return maxElement(node.data, max(node.right));
        else
            return node.data;// es una hoja
    }

    private T maxElement(T a, T b){
        if(a == null) return b;
        else if(b == null) return a;
        return compareElement(a,b) >= 0 ? a : b;

    }

    @Override
    public String preOrder() throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Tree is empty");
        return preOrder(root);
    }

    //Recorrido: N-L-R
    private String preOrder(BTreeNode<T> node){
        String result = "";
        if(node != null) {
            result  = node.data + "( " + node.path + " ) ";
            result += preOrder(node.left);
            result += preOrder(node.right);

        }
        return result;
    }

    @Override
    public String inOrder() throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Tree is empty");
        return inOrder(root);
    }

    //Recorrido: L-N-R
    private String inOrder(BTreeNode<T> node){
        String result = "";
        if(node != null) {
            result  = inOrder(node.left);
            result += node.data + ", " ;
            result += inOrder(node.right);
        }
        return result;
    }

    @Override
    public String postOrder() throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Tree is empty");
        return postOrder(root);
    }

    //Recorrido: L-R-N
    private String postOrder(BTreeNode<T> node){
        String result = "";
        if(node != null) {
            result  = postOrder(node.left);
            result += postOrder(node.right);
            result += node.data + ", " ;

        }
        return result;
    }

    //TAREA
//muestra por consola la altura de cada elemento del arbol
    @Override
    public String nodeHeight() throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Tree is empty");

        StringBuilder result = new StringBuilder();

        nodeHeight(root, result);

        return result.toString();
    }
    private void nodeHeight(BTreeNode<T> node, StringBuilder result)
            throws TreeException {

        if (node != null) {

            result.append("Elemento: ")
                    .append(node.data)
                    .append(" -> Altura: ")
                    .append(height(node.data))
                    .append("\n");

            nodeHeight(node.left, result);
            nodeHeight(node.right, result);
        }
    }
    @Override
    public String toString() {
        if(isEmpty()) return "Binary Tree is empty";
        String result = "Binary Tree Tour\n";
        result += "PreOrder (N-L-R): " + preOrder(root) + "\n";
        result += "InOrder (L-N-R): "  + inOrder(root) + "\n";
        result += "PostOrder (L-R-N): "  + postOrder(root) + "\n";
        return result;
    }

    /**Metodos de ayuda**/
    public boolean equals(T a, T b)  {
        return a==null ? b==null : a.equals(b);
    }

    //metodo generico de comparacion
    public int compareElement(T a, T b) {
        return a.compareTo(b);
    }

    /**Para que el árbol sea BFS, se necesita un metodo diferente de add**/

    public void addBFS(T element) {

        BTreeNode<T> newNode =
                new BTreeNode<>(element);

        if(root == null){
            root = newNode;
            root.path = "root";
            return;
        }

        Queue<BTreeNode<T>> queue =
                new LinkedList<>();

        queue.offer(root);

        while(!queue.isEmpty()){

            BTreeNode<T> current =
                    queue.poll();

            if(current.left == null){

                current.left = newNode;
                newNode.path =
                        current.path + "/left";
                return;
            }

            if(current.right == null){

                current.right = newNode;
                newNode.path =
                        current.path + "/right";
                return;
            }

            queue.offer(current.left);
            queue.offer(current.right);
        }
    }

    public List<BTreeNode<T>>
    getInOrderNodes(){

        List<BTreeNode<T>> nodes =
                new ArrayList<>();

        getInOrderNodes(
                root,
                nodes
        );

        return nodes;
    }

    private void getInOrderNodes(
            BTreeNode<T> node,
            List<BTreeNode<T>> nodes
    ){

        if(node == null)
            return;

        getInOrderNodes(
                node.left,
                nodes
        );

        nodes.add(node);

        getInOrderNodes(
                node.right,
                nodes
        );
    }

    public List<BTreeNode<T>>
    getPreOrderNodes(){

        List<BTreeNode<T>> nodes =
                new ArrayList<>();

        getPreOrderNodes(
                root,
                nodes
        );

        return nodes;
    }

    private void getPreOrderNodes(
            BTreeNode<T> node,
            List<BTreeNode<T>> nodes
    ){

        if(node == null)
            return;

        nodes.add(node);

        getPreOrderNodes(
                node.left,
                nodes
        );

        getPreOrderNodes(
                node.right,
                nodes
        );
    }

    public List<BTreeNode<T>>
    getPostOrderNodes(){

        List<BTreeNode<T>> nodes =
                new ArrayList<>();

        getPostOrderNodes(
                root,
                nodes
        );

        return nodes;
    }
    private void getPostOrderNodes(
            BTreeNode<T> node,
            List<BTreeNode<T>> nodes
    ){

        if(node == null)
            return;

        getPostOrderNodes(
                node.left,
                nodes
        );

        getPostOrderNodes(
                node.right,
                nodes
        );

        nodes.add(node);
    }
    public BTreeNode<T> getRoot() {
        return root;
    }

    public List<BTreeNode<T>> getSearchPath(T element) {

        List<BTreeNode<T>> path = new ArrayList<>();

        getSearchPath(root, element, path);

        return path;
    }

    private boolean getSearchPath(
            BTreeNode<T> node,
            T element,
            List<BTreeNode<T>> path) {

        if (node == null) return false;

        path.add(node);

        int cmp = compareElement(element, node.data);

        if (cmp == 0) return true;

        boolean found;

        if (cmp < 0) {
            found = getSearchPath(node.left, element, path);
        } else {
            found = getSearchPath(node.right, element, path);
        }

        if (!found) {
            path.remove(path.size() - 1);
        }

        return found;
    }
}