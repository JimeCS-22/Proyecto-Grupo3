package cr.ac.ucr.sga.graphics;

import cr.ac.ucr.sga.model.entities.Building;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class ConnectionView extends Line {

    private Building from;
    private Building to;

    public ConnectionView(Building from, Building to) {

        this.from = from;
        this.to = to;

        setStartX(from.getCenterX());
        setStartY(from.getCenterY());

        setEndX(to.getCenterX());
        setEndY(to.getCenterY());

        setStrokeWidth(3);
        setStroke(Color.web("#95A5A6"));
    }

    public Building getFrom() {
        return from;
    }

    public Building getTo() {
        return to;
    }

    public boolean connects(Building a, Building b) {

        return (from.equals(a) && to.equals(b))
                || (from.equals(b) && to.equals(a));
    }

    public void highlight() {

        setStroke(Color.RED);
        setStrokeWidth(6);
    }

    public void clear() {

        setStroke(Color.web("#95A5A6"));
        setStrokeWidth(3);
    }
}