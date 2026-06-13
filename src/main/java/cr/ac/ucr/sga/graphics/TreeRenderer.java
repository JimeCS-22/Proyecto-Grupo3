package cr.ac.ucr.sga.graphics;

import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.structures.trees.AVL;
import cr.ac.ucr.sga.model.structures.trees.BTreeNode;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import cr.ac.ucr.sga.view.graphics.NodeViewInfo;

import java.util.HashMap;
import java.util.Map;

public class TreeRenderer {

    private final GraphicsContext gc;
    private final Map<BTreeNode<Course>, Point2D> positions;
    private final Map<BTreeNode<Course>, NodeViewInfo> nodeViews;

    private BTreeNode<Course> foundNode;
    private BTreeNode<Course> highlightedNode;

    public TreeRenderer(GraphicsContext gc) {
        this.gc = gc;
        this.positions = new HashMap<>();
        this.nodeViews = new HashMap<>();
    }

    public void render(AVL<Course> avl, BTreeNode<Course> root) {
        positions.clear();
        nodeViews.clear();

        if (root == null) return;

        calculatePositions(root, 800, 100, 300);
        drawEdges(root);
        drawNodes(avl, root, true);
    }

    private void calculatePositions(BTreeNode<Course> node, double x, double y, double offset) {
        if (node == null) return;

        positions.put(node, new Point2D(x, y));
        nodeViews.put(node, new NodeViewInfo(x, y, node.data));

        calculatePositions(node.left, x - offset, y + 180, offset / 1.5);
        calculatePositions(node.right, x + offset, y + 180, offset / 1.5);
    }

    private void drawEdges(BTreeNode<Course> node) {
        if (node == null) return;

        Point2D p = positions.get(node);
        gc.setStroke(Color.BLACK);

        if (node.left != null) {
            Point2D left = positions.get(node.left);
            gc.strokeLine(p.getX(), p.getY(), left.getX(), left.getY());
        }

        if (node.right != null) {
            Point2D right = positions.get(node.right);
            gc.strokeLine(p.getX(), p.getY(), right.getX(), right.getY());
        }

        drawEdges(node.left);
        drawEdges(node.right);
    }

    private void drawNodes(AVL<Course> avl, BTreeNode<Course> node, boolean isRoot) {
        if (node == null) return;

        Point2D p = positions.get(node);
        Course course = node.data;

        // Factor de balanceo
        int fb = avl.getBalanceFactor(node);
        gc.setFont(new Font(12));
        gc.setFill(Color.DARKBLUE);
        gc.fillText("FB=" + fb, p.getX() - 20, p.getY() - 70);

        // Medir ancho dinámico según la palabra más larga
        Font fontId = new Font(14);
        Font fontName = new Font(12);

        double idWidth = getTextWidth(course.getId(), fontId);

        // Dividir el nombre en palabras
        String[] words = course.getName().split(" ");
        double maxWordWidth = 0;
        for (String w : words) {
            maxWordWidth = Math.max(maxWordWidth, getTextWidth(w, fontName));
        }

        double nodeWidth = Math.max(120, Math.max(idWidth, maxWordWidth) + 40);
        double nodeHeight = 40 + (words.length * 20); // alto dinámico según cantidad de palabras

        // Fondo sombra
        gc.setFill(Color.rgb(0, 0, 0, 0.20));
        gc.fillRoundRect(p.getX() - nodeWidth / 2, p.getY() - nodeHeight / 2,
                nodeWidth, nodeHeight, 15, 15);

        // Nodo principal
        if (node == foundNode) {
            gc.setFill(Color.LIMEGREEN);
        } else if (node == highlightedNode) {
            gc.setFill(Color.ORANGERED);
        } else {
            gc.setFill(Color.web("#89B4FA"));
        }

        gc.fillRoundRect(p.getX() - nodeWidth / 2, p.getY() - nodeHeight / 2,
                nodeWidth, nodeHeight, 15, 15);

        gc.setStroke(Color.WHITE);
        gc.strokeRoundRect(p.getX() - nodeWidth / 2, p.getY() - nodeHeight / 2,
                nodeWidth, nodeHeight, 15, 15);

        // Texto centrado
        gc.setFill(Color.BLACK);
        gc.setFont(fontId);
        gc.fillText(course.getId(), p.getX() - idWidth / 2, p.getY() - (nodeHeight / 2) + 20);

        gc.setFont(fontName);
        double textY = p.getY() - (nodeHeight / 2) + 40;
        for (String w : words) {
            double wWidth = getTextWidth(w, fontName);
            gc.fillText(w, p.getX() - wWidth / 2, textY);
            textY += 18; // espacio entre palabras
        }

        // Recursión
        drawNodes(avl, node.left, isRoot);
        drawNodes(avl, node.right, isRoot);
    }

    private double getTextWidth(String text, Font font) {
        Text helper = new Text(text);
        helper.setFont(font);
        return helper.getLayoutBounds().getWidth();
    }


    public void setFoundNode(BTreeNode<Course> node) {
        foundNode = node;
    }

    public void setHighlightedNode(BTreeNode<Course> node) {
        highlightedNode = node;
    }
    public Course findCourseAt(double mouseX, double mouseY){

        for(NodeViewInfo info :
                nodeViews.values()){

            double x = info.getX();

            double y = info.getY();

            if(mouseX >= x-75 &&
                    mouseX <= x+75 &&
                    mouseY >= y-40 &&
                    mouseY <= y+40){

                return info.getCourse();
            }
        }

        return null;
    }

    public BTreeNode<Course> findNodeByCourse(BTreeNode<Course> root, Course course){

        if(root == null)
            return null;

        if(root.data.compareTo(course) == 0)
            return root;

        BTreeNode<Course> left =
                findNodeByCourse(
                        root.left,
                        course
                );

        if(left != null)
            return left;

        return findNodeByCourse(
                root.right,
                course
        );
    }
}