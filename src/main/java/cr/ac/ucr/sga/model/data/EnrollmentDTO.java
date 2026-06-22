package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Enrollment;

public class EnrollmentDTO {
    private String id;
    private String studentId;
    private String courseId;
    private String professorId;
    private double grade;
    private String status;

    public EnrollmentDTO() {}

    public EnrollmentDTO(Enrollment e) {
        this.id = e.getId();
        this.studentId = e.getStudentId();
        this.courseId = e.getCourseId();
        this.professorId = e.getProfessorId();
        this.grade = e.getGrade();
        this.status = e.getStatus();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getProfessorId() { return professorId; }
    public void setProfessorId(String professorId) { this.professorId = professorId; }

    public double getGrade() { return grade; }
    public void setGrade(double grade) { this.grade = grade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}