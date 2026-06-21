package cr.ac.ucr.sga.model.entities;

public class StudentCourse {

    private Course course;

    private double grade;

    private String status;

    public StudentCourse() {
    }

    public StudentCourse(Course course) {
        this.course = course;
        this.grade = 0;
        this.status = "Matriculado";
    }

    public StudentCourse(Course course, double grade, String status) {
        this.course = course;
        this.grade = grade;
        this.status = status;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
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
