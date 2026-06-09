package cr.ac.ucr.sga.view.graphics;

import cr.ac.ucr.sga.model.entities.Course;

public class NodeViewInfo {

    private final double x;
    private final double y;
    private final Course course;

    public NodeViewInfo(
            double x,
            double y,
            Course course
    ) {
        this.x = x;
        this.y = y;
        this.course = course;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Course getCourse() {
        return course;
    }
}