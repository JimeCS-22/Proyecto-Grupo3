package cr.ac.ucr.sga.model.entities;

import cr.ac.ucr.sga.model.structures.lists.LinkedList;

public class MatriculaAprobada {
    private String id;
    private Student student;
    private LinkedList<Enrollment> enrollments;
    private String status;

    public MatriculaAprobada(String id,
                             Student student,
                             LinkedList<Enrollment> enrollments) {

        this.id = id;
        this.student = student;
        this.enrollments = enrollments;
        this.status = "APPROVED";
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public LinkedList<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(LinkedList<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "MatriculaAprobada{" +
                "id='" + id + '\'' +
                ", student=" + student +
                ", status='" + status + '\'' +
                '}';
    }
}