package cr.ac.ucr.sga.model.structures.trees;

import cr.ac.ucr.sga.model.entities.Course;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Dibuja el árbol de llamadas recursivas en un Canvas de JavaFX.
 * Calcula posiciones (x,y) usando un recorrido post-order para asignar
 * espacios de forma que los subárboles no se solapen.
 */
public class TreePainter {

    // ── Colores ───────────────────────────────────────────────────────────────
    private static final Color COL_NORMAL   = Color.web("#1F3868");
    private static final Color COL_BASE     = Color.web("#1A8C7B");
    private static final Color COL_MEMO     = Color.web("#E8A020");
    private static final Color COL_RESULT   = Color.web("#2E5FAC");
    private static final Color COL_EDGE     = Color.web("#8896A5");
    private static final Color COL_TEXT     = Color.WHITE;
    private static final Color COL_BG       = Color.web("#F4F6FA");

    private static final double NODE_R  = 26;   // radio del nodo
    private static final double H_GAP   = 18;   // espacio horizontal mínimo entre nodos
    private static final double V_GAP   = 70;   // espacio vertical entre niveles

    public static void drawTreeNode(GraphicsContext gc, BTreeNode<Course> node,
                                    double x, double y,
                                    double horizontalSpacing,
                                    AVL<Course> avl,
                                    boolean isRoot) {
        if (node == null) return;

        double radius = 15;
        double diameter = radius * 2;

        // FACTOR DE BALANCEO
        int fb = avl.getBalanceFactor(node);

        gc.setFont(new Font(10));
        gc.setFill(Color.BLACK);
        gc.fillText("FB=" + fb, x - 12, y - 22);

        // COLOR SEGÚN BALANCEO
        Color nodeColor = getBalanceColor(fb, isRoot);
        gc.setFill(nodeColor);

        gc.fillOval(x - radius, y - radius, diameter, diameter);
        gc.setStroke(Color.BLACK);
        gc.strokeOval(x - radius, y - radius, diameter, diameter);

        // valor nodo
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(15));
        gc.fillText(String.valueOf(node.data), x - 7, y + 5);

        if (node.counterTranversal != 0) {
            gc.setFont(new Font(10));
            gc.fillText(String.valueOf(node.counterTranversal), x - 7, y + 25);
        }

        double verticalSpacing = 60;

        if (node.left != null) {
            gc.strokeLine(x, y + radius, x - horizontalSpacing, y + verticalSpacing - radius);
            drawTreeNode(gc, node.left,
                    x - horizontalSpacing,
                    y + verticalSpacing,
                    horizontalSpacing / 2,
                    avl,
                    false);
        }

        if (node.right != null) {
            gc.strokeLine(x, y + radius, x + horizontalSpacing, y + verticalSpacing - radius);
            drawTreeNode(gc, node.right,
                    x + horizontalSpacing,
                    y + verticalSpacing,
                    horizontalSpacing / 2,
                    avl,
                    false);
        }
    }

    private static Color getBalanceColor(int fb, boolean isRoot) {
        if (isRoot) return Color.LIGHTBLUE;

        if (fb == 0) return Color.GREEN;

        if (fb == -1 || fb == 1) return Color.YELLOW;

        return Color.RED; // -2, 2 o más
    }

    public static void drawSimpleNode(
            GraphicsContext gc,
            BTreeNode<Integer> node,
            double x,
            double y,
            double spacing,
            boolean isRoot
    ){

        if(node == null)
            return;

        double radius = 22;

        Color color =
                getNodeColor(node,isRoot);

        gc.setFill(color);

        gc.fillOval(
                x-radius,
                y-radius,
                radius*2,
                radius*2
        );

        gc.setStroke(Color.LIGHTGRAY);

        gc.strokeOval(
                x-radius,
                y-radius,
                radius*2,
                radius*2
        );

        gc.setFill(Color.WHITE);

        gc.setFont(new Font(14));

        gc.fillText(
                node.data.toString(),
                x-7,
                y+5
        );

        double vGap = 90;

        gc.setStroke(
                Color.web("#506080")
        );

        if(node.left != null){

            gc.strokeLine(
                    x,
                    y,
                    x-spacing,
                    y+vGap
            );

            drawSimpleNode(
                    gc,
                    node.left,
                    x-spacing,
                    y+vGap,
                    spacing/2,
                    false
            );
        }

        if(node.right != null){

            gc.strokeLine(
                    x,
                    y,
                    x+spacing,
                    y+vGap
            );

            drawSimpleNode(
                    gc,
                    node.right,
                    x+spacing,
                    y+vGap,
                    spacing/2,
                    false
            );
        }
    }

    private static Color getNodeColor(
            BTreeNode<Integer> node,
            boolean root
    ){

        if(root)
            return Color.web("#29A3E1");

        if(node.left == null &&
                node.right == null)
            return Color.web("#10C98A");

        return Color.web("#274D7D");
    }

}
