package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;


public class AcademicRecordDTO {
    public Student student;
    public DoublyLinkedList<Course> courses;

    public AcademicRecordDTO() {}

    public AcademicRecordDTO(AcademicRecord record) {
        this.student = record.getStudent();
        this.courses = record.getCourses();
    }

    public Student getStudent() {
        return student;
    }

    public DoublyLinkedList<Course> getCourses() {
        return courses;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourses(DoublyLinkedList<Course> courses) {
        this.courses = courses;
    }
}