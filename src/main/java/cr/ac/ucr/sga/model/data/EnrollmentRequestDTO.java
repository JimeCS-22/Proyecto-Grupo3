package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.EnrollmentRequest;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentRequestDTO {
    private String studentId;
    private List<String> courseCodes;
    private int priority;
    private String status;


    public EnrollmentRequestDTO(EnrollmentRequest req) throws ListException {
        this.studentId = req.getStudentId();
        this.priority = req.getPriority();
        this.status = req.getStatus();

        this.courseCodes = new ArrayList<>();
        LinkedList<Course> courses = req.getCourses();
        if (courses != null && !courses.isEmpty()) {
            for (int i = 1; i <= courses.size(); i++) { // LinkedList es 1-based
                Course course = courses.get(i);
                if (course != null) {
                    this.courseCodes.add(course.getId());
                }
            }
        }
    }

    // Constructor vacío para GSON
    public EnrollmentRequestDTO() {}

    // Getters y setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public List<String> getCourseCodes() { return courseCodes; }
    public void setCourseCodes(List<String> courseCodes) { this.courseCodes = courseCodes; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}