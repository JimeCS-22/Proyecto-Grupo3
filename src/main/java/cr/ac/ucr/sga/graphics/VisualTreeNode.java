package cr.ac.ucr.sga.graphics;

import cr.ac.ucr.sga.model.structures.trees.BTreeNode;
import javafx.scene.paint.Color;

public class VisualTreeNode<T>{

    private BTreeNode<T> node;

    private double x;
    private double y;

    private Color color;

    public VisualTreeNode(BTreeNode<T> node) {

        this.node = node;

        this.color = Color.web("#89B4FA");
    }

    public BTreeNode<T> getNode() {
        return node;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setPosition(double x,double y){
        this.x = x;
        this.y = y;
    }
}
