package cr.ac.ucr.sga.model.entities;

import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

public class EnrollmentRequest {
    private Student student;

    private int priority;

    private String status;

    private LinkedList<Course> courses;

    public EnrollmentRequest() {
        this.courses = new LinkedList<>();
    }

    public EnrollmentRequest(Student student, int priority, String status, LinkedList<Course> courses) {
        this.student = student;
        this.priority = priority;
        this.status = status;
        this.courses = courses;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LinkedList<Course> getCourses() {
        return courses;
    }

    public void setCourses(LinkedList<Course> courses) {
        this.courses = courses;
    }

    public String getStudentId() {
        return (student != null) ? student.getId() : null;
    }

    public String getCourseCode() throws ListException {
       return courses.getFirst().getId();
    }
}
