package cr.ac.ucr.sga.graphics;

import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.structures.trees.BTreeNode;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class TreeRenderer {

    private final GraphicsContext gc;

    private final Map<BTreeNode<Course>, Point2D> positions;

    private final Map<BTreeNode<Course>, cr.ac.ucr.sga.view.graphics.NodeViewInfo> nodeViews;

    private BTreeNode<Course> foundNode;

    private BTreeNode<Course> highlightedNode;
    public TreeRenderer(GraphicsContext gc) {

        this.gc = gc;

        this.positions = new HashMap<>();

        this.nodeViews = new HashMap<>();
    }

    public void render(BTreeNode<Course> root){

        positions.clear();

        nodeViews.clear();

        if(root == null)
            return;

        calculatePositions(
                root,
                800,
                100,
                300
        );

        drawEdges(root);

        drawNodes(root);
    }

    private void calculatePositions(
            BTreeNode<Course> node,
            double x,
            double y,
            double offset
    ){

        if(node == null)
            return;

        positions.put(
                node,
                new Point2D(x,y)
        );

        nodeViews.put(
                node,
                new cr.ac.ucr.sga.view.graphics.NodeViewInfo(
                        x,
                        y,
                        node.data
                )
        );

        calculatePositions(
                node.left,
                x-offset,
                y+120,
                offset/2
        );

        calculatePositions(
                node.right,
                x+offset,
                y+120,
                offset/2
        );
    }

    private void drawEdges(
            BTreeNode<Course> node
    ){

        if(node == null)
            return;

        Point2D p =
                positions.get(node);

        gc.setStroke(Color.WHITE);

        if(node.left != null){

            Point2D left =
                    positions.get(node.left);

            gc.strokeLine(
                    p.getX(),
                    p.getY(),
                    left.getX(),
                    left.getY()
            );
        }

        if(node.right != null){

            Point2D right =
                    positions.get(node.right);

            gc.strokeLine(
                    p.getX(),
                    p.getY(),
                    right.getX(),
                    right.getY()
            );
        }

        drawEdges(node.left);

        drawEdges(node.right);
    }

    private void drawNodes(
            BTreeNode<Course> node
    ){

        if(node == null)
            return;

        Point2D p =
                positions.get(node);

        Course course =
                node.data;

        gc.setFill(
                Color.rgb(
                        0,
                        0,
                        0,
                        0.20
                )
        );

        gc.fillRoundRect(
                p.getX()-72,
                p.getY()-37,
                150,
                80,
                20,
                20
        );

        if(node == foundNode){

            gc.setFill(
                    Color.LIMEGREEN
            );

        }else if(node == highlightedNode){

            gc.setFill(
                    Color.ORANGERED
            );

        }else{

            gc.setFill(
                    Color.web("#89B4FA")
            );
        }

        gc.fillRoundRect(
                p.getX()-75,
                p.getY()-40,
                150,
                80,
                20,
                20
        );

        gc.setStroke(Color.WHITE);

        gc.strokeRoundRect(
                p.getX()-75,
                p.getY()-40,
                150,
                80,
                20,
                20
        );

        gc.setFill(Color.WHITE);

        gc.fillText(
                course.getId(),
                p.getX()-55,
                p.getY()-10
        );

        gc.fillText(
                course.getName(),
                p.getX()-55,
                p.getY()+15
        );

        drawNodes(node.left);

        drawNodes(node.right);
    }

    public Course findCourseAt(
            double mouseX,
            double mouseY
    ){

        for(cr.ac.ucr.sga.view.graphics.NodeViewInfo info :
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

    public void setFoundNode(
            BTreeNode<Course> node){

        foundNode = node;
    }

    public void  setHighlightedNode( BTreeNode<Course> node){
        highlightedNode = node;
    }

    public BTreeNode<Course> findNodeByCourse(
            BTreeNode<Course> root,
            Course course){

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