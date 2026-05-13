package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.Course;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * CRUD de cursos usando JSON
 */
public class CourseData {

    private LinkedList<Course> courses;

    private static final String FILE_PATH = "src/main/resources/data/courses.json";

    private final Gson gson = new GsonBuilder()
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
    private LinkedList<Course> loadCourses() {

        try (FileReader reader = new FileReader(FILE_PATH)) {

            Type listType = new TypeToken<LinkedList<Course>>() {
            }.getType();

            LinkedList<Course> loadedCourses = gson.fromJson(reader, listType);

            return (loadedCourses != null)
                    ? loadedCourses
                    : new LinkedList<>();

        } catch (Exception e) {

            return new LinkedList<>();
        }
    }

    /**
     * Guarda los cursos en el JSON
     */
    private void saveCourses() {

        try (FileWriter writer = new FileWriter(FILE_PATH)) {

            gson.toJson(courses, writer);

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
    public ArrayList<Course> getAllCourses() {

        return new ArrayList<>(courses);
    }

    /**
     * READ BY ID
     */
    public Course findCourseById(String id) {

        Course courseToReturn = null;

        for (Course course : courses) {

            if (course.getId().equalsIgnoreCase(id)) {

                courseToReturn = course;
            }
        }

        return courseToReturn;
    }

    /**
     * UPDATE
     */
    public boolean updateCourse(Course updatedCourse) {

        for (int i = 0; i < courses.size(); i++) {

            if (courses.get(i).getId().equalsIgnoreCase(updatedCourse.getId())) {

                courses.set(i, updatedCourse);

                saveCourses();

                return true;
            }
        }

        return false;
    }

    /**
     * DELETE
     */
    public boolean removeCourse(String id) {

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
    public int getCoursesCount() {

        return courses.size();
    }
}
