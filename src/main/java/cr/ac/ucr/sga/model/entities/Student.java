package cr.ac.ucr.sga.model.entities;

public class Student {

    private String id;
    private String name;
    private String email;
    private String carnet;
    private int age;

    // Constructor vacío para Gson
    public Student() {
    }

    // Constructor privado usado por Builder
    private Student(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.carnet = builder.carnet;
        this.age = builder.age;
    }

    // =========================
    // GETTERS
    // =========================

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCarnet() {
        return carnet;
    }

    public int getAge() {
        return age;
    }

    // =========================
    // TO STRING
    // =========================

    @Override
    public String toString() {

        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", carnet='" + carnet + '\'' +
                ", age=" + age +
                '}';
    }

    // =========================
    // BUILDER
    // =========================

    public static class Builder {

        private String id;
        private String name;
        private String email;
        private String carnet;
        private int age;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setCarnet(String carnet) {
            this.carnet = carnet;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Student build() {

            // Validaciones

            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException(
                        "El ID no puede estar vacío"
                );
            }

            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException(
                        "El nombre no puede estar vacío"
                );
            }

            return new Student(this);
        }
    }
}