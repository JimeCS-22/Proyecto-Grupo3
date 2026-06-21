package cr.ac.ucr.sga.model.entities;

public class Enrollment {

    private String id;

    private String studentId;

    private String courseId;

    private String professorId;

    private double grade;

    private String status;

    public Enrollment() {

    }

    public Enrollment(String id, String studentId, String courseId, String professorId, double grade, String status) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.professorId = professorId;
        this.grade = grade;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
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
