package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.MatriculaAprobada;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentApprovedDataTest {

    @Test
    public void test() throws Exception {

        EnrollmentApprovedData data = new EnrollmentApprovedData();

        // Crear estudiante
        Student student = new Student.Builder()
                .setId("202600001")
                .setName("Carlos Ramirez")
                .setCarnet("C10001")
                .setAge(21)
                .setUsername("carlosr")
                .setPassword("123")
                .build();

        // Crear cursos
        Course c1 = new Course.Builder()
                .setId("IF3001")
                .setName("Algoritmos")
                .setCredits(4)
                .setGrade(0)
                .setStatus("Aprobado")
                .build();

        Course c2 = new Course.Builder()
                .setId("MA1001")
                .setName("Cálculo")
                .setCredits(4)
                .setGrade(0)
                .setStatus("Aprobado")
                .build();

        LinkedList<Course> courses = new LinkedList<>();
        courses.add(c1);
        courses.add(c2);

        // Crear matrícula aprobada
        MatriculaAprobada matricula =
                new MatriculaAprobada("MAT-001", student, courses);

        matricula.setStatus("APROBADA");

        // AGREGAR
        data.addOrUpdate(matricula);
        System.out.println("Matrícula agregada");

        // CARGAR TODOS
        List<MatriculaAprobada> all = data.loadAll();

        System.out.println("Matrículas registradas:");
        for (MatriculaAprobada m : all) {
            System.out.println(m);
        }

        // BUSCAR POR ID
        MatriculaAprobada encontrada =
                data.findByStudentId("202600001");

        System.out.println(
                "Encontrada por estudiante: "
                        + (encontrada != null ? encontrada : "No encontrada")
        );

        // BORRAR
        data.delete("MAT-001");

        System.out.println("Matrícula eliminada");
    }
}