package cr.ac.ucr.sga.model.entities;

public class Student {

    private String id;
    private String name;
    private String email;
    private String carnet;

    private Student(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.carnet = builder.carnet;
    }

    // Getters
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

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", carnet='" + carnet + '\'' +
                '}';
    }

    //Clase interna de Builder
    public static class Builder {

        private String id;
        private String name;
        private String email;
        private String carnet;

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

        public Student build() {

            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("El ID no puede estar vacío");
            }

            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }

            return new Student(this);
        }
    }


}
