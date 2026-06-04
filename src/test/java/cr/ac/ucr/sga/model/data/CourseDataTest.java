package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CourseDataTest {

    @Test
    public void test() throws ListException {

        CourseData courseData = new CourseData();

        // CREAR un curso con el builder
        Course curso = new Course.Builder()
                .setId("MAT101")
                .setName("Matemática Básica")
                .setCredits(4)
                .setGrade(0.0)     // normalmente nota inicial
                .setStatus("En curso")
                .build();

        // Agregar curso
        Course added = courseData.addCourse(curso);
        System.out.println("Curso agregado: " + (added != null ? added : "Ya existía"));

        // LEER TODOS
        ArrayList<Course> all = courseData.getAllCourses().toList();
        System.out.println("Cursos registrados:");
        for (Course c : all) System.out.println(c);

        // Buscar por id
        Course byId = courseData.findCourseById("MAT101");
        System.out.println("Encontrado por ID: " + (byId != null ? byId : "No encontrado"));

        // UPDATE
        Course actualizado = new Course.Builder()
                .setId("MAT101")
                .setName("Matemática General")
                .setCredits(5)
                .setGrade(0.0)
                .setStatus("En curso") // O cualquier otro estado permitido
                .build();

        boolean modif = courseData.updateCourse(actualizado);
        System.out.println("Curso actualizado: " + modif);

        // DELETE
        boolean deleted = courseData.removeCourse("MAT101");
        System.out.println("Curso eliminado: " + deleted);

        // CONTAR
        System.out.println("Total de cursos: " + courseData.getCoursesCount());
    }


    }



