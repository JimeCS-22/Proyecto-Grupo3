package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * CRUD de cursos usando JSON
 */
public class CourseData {

    private final DoublyLinkedList<Course> courses;

    private static final String FILE_PATH = "src/main/resources/data/courses.json";

    private final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

    public CourseData() {

        File folder = new File("data");

        if (!folder.exists()) {
            folder.mkdir();
        }

        courses = loadCourses();
    }

    /**
     * Carga los cursos desde el JSON
     */
    private DoublyLinkedList<Course> loadCourses() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ArrayList<Course>>() {}.getType();
            ArrayList<Course> temp = gson.fromJson(reader, listType);

            DoublyLinkedList<Course> list = new DoublyLinkedList<>();
            if (temp != null) {
                for (Course c : temp) {
                    list.add(c);
                }
            }
            return list;
        } catch (Exception e) {
            return new DoublyLinkedList<>();
        }
    }


    /**
     * Guarda los cursos en el JSON
     */
    private void saveCourses() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(courses.toList(), writer); // convertir a ArrayList antes de guardar
        } catch (Exception e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }


    /**
     * CREATE
     */
    public Course addCourse(Course course) {

        Course courseToReturn = null;

        if (course != null && findCourseById(course.getId()) == null) {

            courses.add(course);

            saveCourses();

            courseToReturn = course;
        }

        return courseToReturn;
    }

    /**
     * READ ALL
     */
    public DoublyLinkedList<Course> getAllCourses() {

        return courses;
    }

    /**
     * READ BY ID
     */
    public Course findCourseById(String id) {

        Course courseToReturn = null;

        for (Course course : courses.toList()) {

            if (course.getId().equalsIgnoreCase(id)) {

                courseToReturn = course;
            }
        }

        return courseToReturn;
    }

    /**
     * UPDATE
     */
    public boolean updateCourse(Course updatedCourse) throws ListException {

        for (int i = 1; i < courses.size(); i++) {

            if (courses.get(i).getId().equalsIgnoreCase(updatedCourse.getId())) {

                courses.remove(courses.get(i));
                courses.add(i, updatedCourse);

                saveCourses();

                return true;
            }
        }

        return false;
    }

    /**
     * DELETE
     */
    public boolean removeCourse(String id) throws ListException {

        Course course = findCourseById(id);

        if (course != null) {

            courses.remove(course);

            saveCourses();

            return true;
        }

        return false;
    }

    /**
     * Cantidad de cursos
     */
    public int getCoursesCount() throws ListException {

        return courses.size();
    }
}
