package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Enrollment;

public class EnrollmentDTO {

    private String studentId;
    private String courseId;
    private String professorId;
    private double grade;
    private String status;

    public EnrollmentDTO() {}

    public EnrollmentDTO(Enrollment e) {
        this.studentId = e.getStudentId();
        this.courseId = e.getCourseId();
        this.professorId = e.getProfessorId();
        this.grade = e.getGrade();
        this.status = e.getStatus();
    }

    public Enrollment toEntity() {
        Enrollment e = new Enrollment();
        e.setStudentId(studentId);
        e.setCourseId(courseId);
        e.setProfessorId(professorId);
        e.setGrade(grade);
        e.setStatus(status);
        return e;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}