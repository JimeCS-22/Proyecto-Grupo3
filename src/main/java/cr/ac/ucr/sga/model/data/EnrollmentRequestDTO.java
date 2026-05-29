package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.EnrollmentRequest;
import cr.ac.ucr.sga.model.structures.lists.ListException;


public class EnrollmentRequestDTO {
    private String studentId;
    private String courseCode;
    private int priority;
    private String status;

    // Constructor que recibe el objeto original
    public EnrollmentRequestDTO(EnrollmentRequest req) throws ListException {
        this.studentId = req.getStudentId();
        this.courseCode = req.getCourseCode();
        this.priority = req.getPriority();
        this.status = req.getStatus();
    }

    // Getters y setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
