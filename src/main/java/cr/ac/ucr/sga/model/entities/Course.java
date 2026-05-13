package cr.ac.ucr.sga.model.entities;

public class Course {
    private String id;
    private String name;
    private int credits;

    public Course() {
        this.id = null;
        this.name = null;
        this.credits = 0;
    }

    public Course(String id, String name, int credits) {
        this.id = id;
        this.name = name;
        this.credits = credits;
    }

    public Course(String id, String name) {
        this.id=id;
        this.name= name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "ID= " + id + " || NOMBRE= " + name + " || CREDITS= " + credits;
    }
}
