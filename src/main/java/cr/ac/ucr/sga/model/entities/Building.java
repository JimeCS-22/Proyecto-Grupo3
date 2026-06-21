package cr.ac.ucr.sga.model.entities;

public class Building implements Comparable<Building> {

    private String id;
    private String name;
    private String description;
    private String icon;

    private double x;
    private double y;

    public Building(String id,
                    String name,
                    String description,
                    String icon,
                    double x,
                    double y) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.x = x;
        this.y = y;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getCenterX() {
        return x + 85;
    }

    public double getCenterY() {
        return y + 45;
    }

    @Override
    public int compareTo(Building o) {
        if (o == null) return 1;
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Building other = (Building) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


}
