package cr.ac.ucr.sga.model.entities;

public class Student extends Person {

    private String carnet;

    public Student(String name, String id, int age, double height, double weight, String carnet) {
        super(name, id, age, height, weight);
        this.carnet = carnet;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    @Override
    public String getRoleDescription() {
        return "Estudiante " + getName() +
                "Carnet: " + getCarnet();
    }

    @Override
    public String toString() {
        return  super.toString() + "\n " + getRoleDescription();
    }
}
