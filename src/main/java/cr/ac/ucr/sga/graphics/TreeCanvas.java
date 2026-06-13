package cr.ac.ucr.sga.graphics;

import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.structures.trees.AVL;
import cr.ac.ucr.sga.model.structures.trees.BTree;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class TreeCanvas extends Canvas {

    private BTree<Course> tree;

    public TreeCanvas(double width,
                      double height) {

        super(width,height);
    }

    public void setTree(
            BTree<Course> tree){

        this.tree = tree;

        draw();
    }

    public void draw(){

        GraphicsContext gc =
                getGraphicsContext2D();

        gc.clearRect(
                0,
                0,
                getWidth(),
                getHeight()
        );

        if(tree == null)
            return;

        TreeRenderer renderer =
                new TreeRenderer(gc);

        renderer.render( (AVL<Course>) tree,tree.root);
    }
}
