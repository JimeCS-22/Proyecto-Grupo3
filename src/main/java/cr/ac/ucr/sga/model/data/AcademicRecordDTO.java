package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.AcademicRecord;
import java.util.List;

public class AcademicRecordDTO {
    public Student student;
    public List<Course> courses;

    public AcademicRecordDTO() {}

    public AcademicRecordDTO(AcademicRecord record) {
        this.student = record.getStudent();
        this.courses = record.getCoursesAsList();
    }

    public Student getStudent() {
        return student;
    }

    public List<Course> getCourses() {
        return courses;
    }
}