package cr.ac.ucr.sga.model.entities;

import com.google.gson.annotations.Expose;

public class Course implements Comparable<Course> {

    @Expose
    private String id;
    @Expose private String name;
    @Expose private int credits;
    @Expose private double grade;
    @Expose private String status;

    public Course(String id, String name, int credits, double grade, String status) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.grade = grade;
        this.status = status;
    }
    public Course(Builder builder) {
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
      //  return String.format("%s [ID: %s, Créditos: %d, Nota: %.1f, Estado: %s]",name, id, credits, grade, status);
        return name + " [ID: " + id + "]";
    }

  

    @Override
    public int compareTo(Course o) {
        return this.id.compareToIgnoreCase(o.id);
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