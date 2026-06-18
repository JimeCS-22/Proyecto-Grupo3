package cr.ac.ucr.sga.graphics;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class ConnectionView extends Line{

    public ConnectionView(double x1,double y1,double x2,double y2){

        setStartX(x1);

        setStartY(y1);

        setEndX(x2);

        setEndY(y2);

        setStrokeWidth(3);

        setStroke(Color.web("#95A5A6"));

    }

}
