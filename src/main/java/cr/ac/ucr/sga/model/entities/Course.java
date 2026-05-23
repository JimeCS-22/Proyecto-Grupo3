package cr.ac.ucr.sga.model.entities;

public class Course {

    private String id;
    private String name;
    private int credits;
    private double grade;
    private String status;

    private Course(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.credits = builder.credits;
        this.grade = builder.grade;
        this.status = builder.status;
    }
    public Course() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public double getGrade() {
        return grade;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", grade=" + grade +
                ", status='" + status + '\'' +
                '}';
    }

    // Clase interna de Builder
    public static class Builder {

        private String id;
        private String name;
        private int credits;
        private double grade;
        private String status;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCredits(int credits) {
            this.credits = credits;
            return this;
        }

        public Builder setGrade(double grade) {
            this.grade = grade;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Course build() {

            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("El ID no puede estar vacío");
            }

            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }

            if (credits < 0) {
                throw new IllegalArgumentException("Los créditos no pueden ser negativos");
            }

            return new Course(this);
        }

        @Override
        public String toString() {
            return name;
        }
    }
}