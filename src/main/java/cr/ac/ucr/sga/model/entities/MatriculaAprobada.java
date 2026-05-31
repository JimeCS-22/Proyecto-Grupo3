package cr.ac.ucr.sga.model.entities;

import cr.ac.ucr.sga.model.structures.lists.LinkedList;

/**
 * Representa los cursos aprobados en pre-matrícula, listos para ser matriculados por el estudiante.
 */
public class MatriculaAprobada {
    private String id; // ID de la solicitud aprobada
    private Student student;
    private LinkedList<Course> coursesApproved; // Cursos que pasaron la revisión admin
    private String status;

    public MatriculaAprobada(String id, Student student, LinkedList<Course> courses) {
        this.id = id;
        this.student = student;
        this.coursesApproved = courses;
        this.status = "APPROVED";
    }

    public MatriculaAprobada() {
        this.coursesApproved = new LinkedList<>();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public LinkedList<Course> getCoursesApproved() { return coursesApproved; }
    public void setCoursesApproved(LinkedList<Course> courses) { this.coursesApproved = courses; }

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