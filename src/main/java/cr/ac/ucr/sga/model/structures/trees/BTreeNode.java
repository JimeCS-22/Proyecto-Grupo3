package cr.ac.ucr.sga.model.structures.trees;

import java.util.ArrayList;
import java.util.List;

public class BTreeNode<T> {

        public T data;
        public BTreeNode<T> left, right;
        public String path;//Es la ruta de inserción Ejemplo: RAIZ-IZQUIERDO-DERECHO

        // contador para los recorridos
        public int counterTranversal;

        public BTreeNode(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
            this.counterTranversal=0;
        }

    public BTreeNode(T data, String path) {
        this.data = data;
        this.path = path;
        this.left = right = null;
    }

}