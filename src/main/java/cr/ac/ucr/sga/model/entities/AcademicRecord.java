package cr.ac.ucr.sga.model.entities;

import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import java.util.ArrayList;
import java.util.List;

public class AcademicRecord {

    private final Student student;
    private DoublyLinkedList<Course> courses;

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

    // ------- SERIALIZACIÓN SEGURA -------
    // Convierte cursos a lista estándar solo si hay cursos

    public List<Course> getCoursesAsList() {

        List<Course> list = new ArrayList<>();

        try {

            int size = courses.size();

            System.out.println("SIZE = " + size);

            for (int i = 1; i <= size; i++) {

                Course c = courses.get(i);

                System.out.println(c);

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Inicializa los cursos desde una lista estándar
    public void setCoursesFromList(List<Course> list) {
        this.courses = new DoublyLinkedList<>();
        if (list != null) {
            for (Course c : list) {
                courses.add(c);
            }
        }
    }

    // ------- OPERACIONES -------
    public void addCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("El curso no puede ser null");
        }
        courses.add(course);
    }

    public void removeCourse(Course course) throws ListException {
        courses.remove(course);
    }

    public int totalCourses() throws ListException {
        if (!courses.isEmpty()) {
            return courses.size();
        }
        return 0;
    }

    public List<Course> getCoursesForJson() {

        List<Course> list = new ArrayList<>();

        try {

            int size = courses.size();

            for (int i = 1; i <= size; i++) {
                list.add(courses.get(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public void setCoursesFromJson(List<Course> list) {

        this.courses = new DoublyLinkedList<>();

        if (list != null) {
            for (Course c : list) {
                courses.add(c);
            }
        }
    }
    @Override
    public String toString() {
        try {
            return "AcademicRecord{" +
                    "student=" + student +
                    ", totalCourses=" + (courses.isEmpty() ? 0 : courses.size()) +
                    '}';
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
    }
}