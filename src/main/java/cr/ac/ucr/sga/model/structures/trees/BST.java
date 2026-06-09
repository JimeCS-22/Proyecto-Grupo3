package cr.ac.ucr.sga.model.structures.trees;

import javafx.animation.Timeline;

import java.util.List;

public class BST<T extends Comparable<T>> extends BTree<T> {

    @Override
    public void add(T element) {
        this.root = add(root, element);
    }

    //Inicialmente el arbol estará apuntando a Null, luego pasará ser (Null, [Valor]), luego de eso se agregará
    //Despues se llama recursivamente para seguir con el mismo proceso con todos los elementos
    private BTreeNode<T>add(BTreeNode<T> node, T element){
        if(node == null){
            node = new BTreeNode<>(element);
        }else if(compareElement(element, node.data) < 0){
           node.left = add(node.left, element);
        } else if(compareElement(element, node.data) > 0){
            node.right = add(node.right, element);
        }
        return node; //Retorna el árbol modificado

    }


    @Override
    public boolean contains(T element) throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Search Tree is Empty");
        return binarySearch(this.root, element);
    }

    public boolean binarySearch(BTreeNode<T> node, T element){
        if(node == null) return false;
        if(equals(node.data, element)) return true;
        else if(compareElement(element, node.data) < 0)
            return binarySearch(node.left, element);
        else return binarySearch(node.right, element);
    }

    @Override
    public void remove(T element) throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Search Tree is Empty");
        root = remove(root, element);
    }

    private BTreeNode<T>remove(BTreeNode<T> node, T element){
        if(node != null){
            if(compareElement(element, node.data) < 0)
            node.left = remove(node.left, element);
            else if(compareElement(element, node.data) > 0)
            node.right = remove(node.right, element);
            else if(equals(node.data, element)){ //Ya lo encontró
                //Caso 1: El nodo a suprimir no tiene hijos, es una hoja
                if (node.left == null && node.right == null)return null;
                //Caso 2: El nodo a suprimir solo tiene un hijo
                //  En este caso, el nodo es reemplazado por su hijo
                else if (node.left != null && node.right == null) return node.left;
                else if (node.left == null && node.right != null) return node.right;
                //Caso 3: El nodo a suprimir tiene dos hijos
                else{
                    //Se obtiene el elemento menor del subarbol derecho
                    //Se reemplaza la data del nodo por ese valor
                    //Se elimina el valor
                    T minValue = min(node.right);
                    node.data = minValue;
                    node.right = remove(node.right, minValue);
                }
            }
        }
        return node;
    }

    @Override
    public T min() throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Search Tree is empty");
        return min(root);
    }
//publico para que se pueda usar en AVL
    public T min(BTreeNode<T> node){
        if(node.left != null) return min(node.left);
        return node.data;
    }

    @Override
    public T max() throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Search Tree is empty");
        return max(root);
    }

    public T max(BTreeNode<T> node)  {
        if(node.right != null) return max(node.right);
        return node.data;
    }

    @Override
    public String preOrder() throws TreeException {
        if(isEmpty()) throw new TreeException("Binary Search Tree is empty");
        return preOrder(root);
    }

    //Recorrido: N-L-R
    private String preOrder(BTreeNode<T> node){
        String result = "";
        if(node != null) {
            result  = node.data + ", ";
            result += preOrder(node.left);
            result += preOrder(node.right);

        }
        return result;
    }

    @Override
    public String toString() {
        if(isEmpty()) return "Binary Search Tree is empty";
        String result = "Binary Tree Tour\n";
        try {
        result += "PreOrder (N-L-R): " + preOrder() + "\n";
        result += "InOrder (L-N-R): "  + inOrder() + "\n";
        result += "PostOrder (L-R-N): "  + postOrder() + "\n";

        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    //Uso para la parte grafica
    public List<BTreeNode<T>>
    getSearchPath(T element){
        return null;
    }

    private void getSearchPath(
            BTreeNode<T> node,
            T element,
            List<BTreeNode<T>> path){

        if(node == null)
            return;

        path.add(node);

        if(node.data.equals(element))
            return;

        if(compareElement(
                element,
                node.data) < 0){

            getSearchPath(
                    node.left,
                    element,
                    path
            );
        }
        else{

            getSearchPath(
                    node.right,
                    element,
                    path
            );
        }
    }

}
