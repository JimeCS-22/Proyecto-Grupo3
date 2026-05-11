package ucr.algoritmos.proyecto.model;

public abstract class  Person {

    private String id;
    private String name;
    private int age;
    private double height;
    private double weight;

    public Person(String name, String id, int age, double height, double weight) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.height = height;
        this.weight = weight;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public abstract String getRoleDescription();

    @Override
    public String toString() {
        return "id='" + id + "', name='" + name + "', age=" + age +
                ", height=" + height + ", weight=" + weight;
    }
}
