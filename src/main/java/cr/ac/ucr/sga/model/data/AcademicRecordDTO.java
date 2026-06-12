package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.Student;

public class AcademicRecordDTO {

    private Student student;
    private Course[] courses;

    public AcademicRecordDTO() {
    }

    public AcademicRecordDTO(AcademicRecord record) {

        this.student = record.getStudent();

        try {

            int size = record.getCourses().size();

            this.courses = new Course[size];

            for (int i = 1; i <= size; i++) {

                courses[i - 1] =
                        record.getCourses().get(i);
            }

        } catch (Exception e) {

            e.printStackTrace();

            this.courses = new Course[0];
        }
    }

    public Student getStudent() {
        return student;
    }

    public Course[] getCourses() {
        return courses;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourses(Course[] courses) {
        this.courses = courses;
    }
}