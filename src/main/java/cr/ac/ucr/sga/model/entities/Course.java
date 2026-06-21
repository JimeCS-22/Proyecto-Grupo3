package cr.ac.ucr.sga.model.entities;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

public class Course implements Comparable<Course> {

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private int credits;
    @Expose
    private double grade;
    @Expose
    private String status;
    @Expose
    private String careerId;
    @Expose
    private String professorId;

    // =========================
    // NUEVOS CAMPOS
    // =========================
    @Expose
    private int semestre; // 1-8 (semestre académico)
    @Expose
    private List<String> prerequisitosIds; // IDs de cursos prerequisito
    @Expose
    private List<String> corequisitosIds;  // IDs de cursos corequisito

    // =========================
    // CONSTRUCTORES
    // =========================

    public Course(String id, String name, int credits, double grade, String status) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.grade = grade;
        this.status = status;
        this.semestre = 1;
        this.prerequisitosIds = new ArrayList<>();
        this.corequisitosIds = new ArrayList<>();
    }

    public Course(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.credits = builder.credits;
        this.grade = builder.grade;
        this.status = builder.status;
        this.semestre = builder.semestre;
        this.prerequisitosIds = builder.prerequisitosIds != null ? builder.prerequisitosIds : new ArrayList<>();
        this.corequisitosIds = builder.corequisitosIds != null ? builder.corequisitosIds : new ArrayList<>();
        this.careerId = builder.careerId;
        this.professorId = builder.professorId;
    }

    public Course() {
        this.prerequisitosIds = new ArrayList<>();
        this.corequisitosIds = new ArrayList<>();
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

    public int getCredits() {
        return credits;
    }

    public double getGrade() {
        return grade;
    }

    public String getStatus() {
        return status;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public List<String> getPrerequisitosIds() {
        return prerequisitosIds;
    }

    public void setPrerequisitosIds(List<String> ids) {
        this.prerequisitosIds = ids;
    }

    public String getCareerId() {
        return careerId;
    }

    public void setCareerId(String careerId) {
        this.careerId = careerId;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public void addPrerequisito(String courseId) {
        if (!this.prerequisitosIds.contains(courseId)) {
            this.prerequisitosIds.add(courseId);
        }
    }

    public void removePrerequisito(String courseId) {
        this.prerequisitosIds.remove(courseId);
    }

    public List<String> getCorequisitosIds() {
        return corequisitosIds;
    }

    public void setCorequisitosIds(List<String> ids) {
        this.corequisitosIds = ids;
    }

    public void addCorequisito(String courseId) {
        if (!this.corequisitosIds.contains(courseId)) {
            this.corequisitosIds.add(courseId);
        }
    }

    public void removeCorequisito(String courseId) {
        this.corequisitosIds.remove(courseId);
    }

    // =========================
    // TO STRING
    // =========================

    @Override
    public String toString() {
        return name + " [ID: " + id + "]";
    }

    // =========================
    // COMPARABLE
    // =========================

    @Override
    public int compareTo(Course o) {
        return this.id.compareToIgnoreCase(o.id);
    }

    // =========================
    // EQUALS Y HASHCODE
    // =========================

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(obj == null || getClass() != obj.getClass())
            return false;

        Course other = (Course) obj;

        return id.equalsIgnoreCase(other.id);
    }

    @Override
    public int hashCode() {
        return id.toLowerCase().hashCode();
    }

    // =========================
    // BUILDER
    // =========================

    public static class Builder {

        private String id;
        private String name;
        private int credits;
        private double grade;
        private String status;
        private int semestre = 1;
        private List<String> prerequisitosIds;
        private List<String> corequisitosIds;
        private String careerId;
        private  String professorId;

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

        public Builder setSemestre(int semestre) {
            this.semestre = semestre;
            return this;
        }

        public Builder setPrerequisitosIds(List<String> ids) {
            this.prerequisitosIds = ids;
            return this;
        }

        public Builder setCorequisitosIds(List<String> ids) {
            this.corequisitosIds = ids;
            return this;
        }

        public Builder setCareerId(String careerId) {
            this.careerId = careerId;
            return this;
        }

        public Builder addPrerequisito(String courseId) {
            if (this.prerequisitosIds == null) {
                this.prerequisitosIds = new ArrayList<>();
            }
            this.prerequisitosIds.add(courseId);
            return this;
        }

        public Builder addCorequisito(String courseId) {
            if (this.corequisitosIds == null) {
                this.corequisitosIds = new ArrayList<>();
            }
            this.corequisitosIds.add(courseId);
            return this;
        }

        public Builder setProfessorId(String professorId) {
            this.professorId = professorId;
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