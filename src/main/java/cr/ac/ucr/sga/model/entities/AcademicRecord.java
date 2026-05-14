package cr.ac.ucr.sga.model.entities;

import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

public class AcademicRecord {

    private final Student student;
    private final DoublyLinkedList<Course> courses;

    public AcademicRecord(Student student) {

        if (student == null) {
            throw new IllegalArgumentException("El estudiante no puede ser null");
        }

        this.student = student;
        this.courses = new DoublyLinkedList<>();
    }

    public Student getStudent() {
        return student;
    }

    public DoublyLinkedList<Course> getCourses() {
        return courses;
    }

    // Agregar curso
    public void addCourse(Course course) {

        if (course == null) {
            throw new IllegalArgumentException("El curso no puede ser null");
        }

        courses.add(course);
    }

    // Eliminar curso
    public void removeCourse(Course course) throws ListException {
        courses.remove(course);
    }

    // Cantidad de cursos
    public int totalCourses() throws ListException {
        return courses.size();
    }

    @Override
    public String toString() {
        try {
            return "AcademicRecord{" +
                    "student=" + student +
                    ", totalCourses=" + courses.size() +
                    '}';
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
    }


    }

