package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CourseData {

    private final DoublyLinkedList<Course> courses;

    private static final String FILE_PATH = "src/main/resources/data/courses.json";

    private final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

    public CourseData() {
        File folder = new File("src/main/resources/data");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        courses = loadCourses();
    }

    private DoublyLinkedList<Course> loadCourses() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ArrayList<Course>>() {}.getType();
            ArrayList<Course> temp = gson.fromJson(reader, listType);

            DoublyLinkedList<Course> list = new DoublyLinkedList<>();
            if (temp != null) {
                for (Course c : temp) {
                    if (c.getPrerequisitosIds() == null) {
                        c.setPrerequisitosIds(new ArrayList<>());
                    }
                    if (c.getCorequisitosIds() == null) {
                        c.setCorequisitosIds(new ArrayList<>());
                    }
                    list.add(c);
                }
            }
            return list;
        } catch (Exception e) {
            return new DoublyLinkedList<>();
        }
    }

    private void saveCourses() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(courses.toList(), writer);
        } catch (Exception e) {
        }
    }

    public Course addCourse(Course course) {
        if (course != null && findCourseById(course.getId()) == null) {
            courses.add(course);
            saveCourses();
            return course;
        }
        return null;
    }

    public DoublyLinkedList<Course> getAllCourses() {
        return courses;
    }

    public Course findCourseById(String id) {
        for (Course course : courses.toList()) {
            if (course.getId().equalsIgnoreCase(id)) {
                return course;
            }
        }
        return null;
    }

    public DoublyLinkedList<Course> getCoursesBySemestre(int semestre) throws ListException {
        DoublyLinkedList<Course> result = new DoublyLinkedList<>();
        for (Course course : courses.toList()) {
            if (course.getSemestre() == semestre) {
                result.add(course);
            }
        }
        return result;
    }

    public boolean updateCourse(Course updatedCourse) throws ListException {
        for (int i = 1; i <= courses.size(); i++) {
            if (courses.get(i).getId().equalsIgnoreCase(updatedCourse.getId())) {
                Course oldCourse = courses.get(i);
                oldCourse.setName(updatedCourse.getName());
                oldCourse.setCredits(updatedCourse.getCredits());
                oldCourse.setStatus(updatedCourse.getStatus());
                oldCourse.setSemestre(updatedCourse.getSemestre());
                oldCourse.setPrerequisitosIds(updatedCourse.getPrerequisitosIds());
                oldCourse.setCorequisitosIds(updatedCourse.getCorequisitosIds());
                oldCourse.setCareerId(updatedCourse.getCareerId());
                oldCourse.setProfessorId(updatedCourse.getProfessorId());
                saveCourses();
                return true;
            }
        }
        return false;
    }

    public boolean removeCourse(String id) throws ListException {
        Course course = findCourseById(id);
        if (course != null) {
            courses.remove(course);
            saveCourses();
            return true;
        }
        return false;
    }

    public int getCoursesCount() throws ListException {
        return courses.size();
    }

    public boolean tienePrerequisitos(String courseId) {
        Course course = findCourseById(courseId);
        return course != null && !course.getPrerequisitosIds().isEmpty();
    }

    public boolean tieneCorequisitos(String courseId) {
        Course course = findCourseById(courseId);
        return course != null && !course.getCorequisitosIds().isEmpty();
    }

    public DoublyLinkedList<Course> getCursosQueRequieren(String prerequisitoId) throws ListException {
        DoublyLinkedList<Course> result = new DoublyLinkedList<>();
        for (Course course : courses.toList()) {
            if (course.getPrerequisitosIds().contains(prerequisitoId)) {
                result.add(course);
            }
        }
        return result;
    }

    public DoublyLinkedList<Course> getCoursesByProfessor(String username) throws ListException {
        DoublyLinkedList<Course> result = new DoublyLinkedList<>();
        if (username == null || username.trim().isEmpty()) {
            return result;
        }
        for (Course c : courses.toList()) {
            if (c.getProfessorId() != null && c.getProfessorId().equalsIgnoreCase(username.trim())) {
                result.add(c);
            }
        }
        return result;
    }
}