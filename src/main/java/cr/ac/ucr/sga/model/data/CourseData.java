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
 * Ahora soporta: semestre, prerequisitos y corequisitos
 */
public class CourseData {

    private final DoublyLinkedList<Course> courses;

    private static final String FILE_PATH = "src/main/resources/data/courses.json";

    private final Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .create();

    // =========================
    // CONSTRUCTOR
    // =========================
    public CourseData() {

        File folder = new File("src/main/resources/data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        courses = loadCourses();
    }

    // =========================
    // CARGAR CURSOS DESDE JSON
    // =========================
    /**
     * Carga los cursos desde el JSON
     * Incluye semestre, prerequisitos y corequisitos
     */
    private DoublyLinkedList<Course> loadCourses() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ArrayList<Course>>() {}.getType();
            ArrayList<Course> temp = gson.fromJson(reader, listType);

            DoublyLinkedList<Course> list = new DoublyLinkedList<>();
            if (temp != null) {
                for (Course c : temp) {
                    // Validar que los campos nuevos no sean null
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
            System.out.println("Error loading courses: " + e.getMessage());
            return new DoublyLinkedList<>();
        }
    }


    // =========================
    // GUARDAR CURSOS EN JSON
    // =========================
    /**
     * Guarda los cursos en el JSON
     * Persiste semestre, prerequisitos y corequisitos
     */
    private void saveCourses() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(courses.toList(), writer);
            System.out.println("✓ Cursos guardados correctamente en JSON");
        } catch (Exception e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }


    // =========================
    // CREATE (AGREGAR CURSO)
    // =========================
    /**
     * Agrega un nuevo curso a la lista
     * Verifica que no exista un curso con el mismo ID
     */
    public Course addCourse(Course course) {

        Course courseToReturn = null;

        if (course != null && findCourseById(course.getId()) == null) {

            courses.add(course);

            saveCourses();

            courseToReturn = course;
            System.out.println("✓ Curso agregado: " + course.getId());
        } else {
            System.out.println("⚠️ El curso " + (course != null ? course.getId() : "null") + " ya existe o es null");
        }

        return courseToReturn;
    }

    // =========================
    // READ ALL (OBTENER TODOS)
    // =========================
    /**
     * Retorna la lista completa de cursos
     */
    public DoublyLinkedList<Course> getAllCourses() {

        return courses;
    }

    // =========================
    // READ BY ID (BUSCAR POR ID)
    // =========================
    /**
     * Busca un curso por su ID (case-insensitive)
     * @param id el código del curso
     * @return el curso encontrado o null
     */
    public Course findCourseById(String id) {

        Course courseToReturn = null;

        for (Course course : courses.toList()) {

            if (course.getId().equalsIgnoreCase(id)) {

                courseToReturn = course;
                break; // Encontrado, salir del loop
            }
        }

        return courseToReturn;
    }

    // =========================
    // READ BY SEMESTRE
    // =========================
    /**
     * Obtiene todos los cursos de un semestre específico
     * @param semestre número del semestre (1-8)
     * @return lista de cursos del semestre
     */
    public DoublyLinkedList<Course> getCoursesBySemestre(int semestre) throws ListException {

        DoublyLinkedList<Course> result = new DoublyLinkedList<>();

        for (Course course : courses.toList()) {

            if (course.getSemestre() == semestre) {

                result.add(course);
            }
        }

        return result;
    }

    // =========================
// UPDATE (ACTUALIZAR CURSO)
// =========================
    /**
     * Actualiza un curso existente
     * Incluye cambios en semestre, prerequisitos y corequisitos
     * @param updatedCourse el curso con los datos actualizados
     * @return true si se actualizó exitosamente
     */
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

                saveCourses();

                System.out.println("✓ Curso actualizado: " + updatedCourse.getId());
                return true;
            }
        }

        System.out.println("⚠️ No se encontró el curso: " + updatedCourse.getId());
        return false;
    }

    // =========================
    // DELETE (ELIMINAR CURSO)
    // =========================
    /**
     * Elimina un curso de la lista
     * @param id el código del curso a eliminar
     * @return true si se eliminó exitosamente
     */
    public boolean removeCourse(String id) throws ListException {

        Course course = findCourseById(id);

        if (course != null) {

            courses.remove(course);

            saveCourses();

            System.out.println("✓ Curso eliminado: " + id);
            return true;
        }

        System.out.println("⚠️ No se encontró el curso a eliminar: " + id);
        return false;
    }

    // =========================
    // COUNT (CONTAR CURSOS)
    // =========================
    /**
     * Retorna la cantidad total de cursos
     */
    public int getCoursesCount() throws ListException {

        return courses.size();
    }

    // =========================
    // VERIFICAR PREREQUISITOS
    // =========================
    /**
     * Verifica si un curso tiene prerequisitos
     * @param courseId el ID del curso a verificar
     * @return true si tiene prerequisitos
     */
    public boolean tienePrerequisitos(String courseId) {

        Course course = findCourseById(courseId);

        return course != null && !course.getPrerequisitosIds().isEmpty();
    }

    // =========================
    // VERIFICAR COREQUISITOS
    // =========================
    /**
     * Verifica si un curso tiene corequisitos
     * @param courseId el ID del curso a verificar
     * @return true si tiene corequisitos
     */
    public boolean tieneCorequisitos(String courseId) {

        Course course = findCourseById(courseId);

        return course != null && !course.getCorequisitosIds().isEmpty();
    }

    // =========================
    // OBTENER CURSOS POR PREREQUISITO
    // =========================
    /**
     * Obtiene todos los cursos que tienen un curso específico como prerequisito
     * @param prerequisitoId el ID del curso prerequisito
     * @return lista de cursos que requieren este prerequisito
     */
    public DoublyLinkedList<Course> getCursosQueRequieren(String prerequisitoId) throws ListException {

        DoublyLinkedList<Course> result = new DoublyLinkedList<>();

        for (Course course : courses.toList()) {

            if (course.getPrerequisitosIds().contains(prerequisitoId)) {

                result.add(course);
            }
        }

        return result;
    }
}