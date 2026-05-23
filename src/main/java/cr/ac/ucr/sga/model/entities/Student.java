package cr.ac.ucr.sga.model.entities;

public class Student {

    private String id;
    private String name;
    private String email;
    private String carnet;
    private int age;

    // NUEVO
    private String username;
    private String password;

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

        // NUEVO
        this.username = builder.username;
        this.password = builder.password;
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

    // NUEVOS GETTERS

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
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
                ", username='" + username + '\'' +
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

        // NUEVOS
        private String username;
        private String password;

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

        // NUEVOS SETTERS

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
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

            if (username == null || username.isBlank()) {

                throw new IllegalArgumentException(
                        "El usuario no puede estar vacío"
                );
            }

            if (password == null || password.isBlank()) {

                throw new IllegalArgumentException(
                        "La contraseña no puede estar vacía"
                );
            }

            return new Student(this);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}