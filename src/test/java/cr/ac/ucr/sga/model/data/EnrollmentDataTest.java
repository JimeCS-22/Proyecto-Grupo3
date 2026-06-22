package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Enrollment;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class EnrollmentDataTest {

    @Test
    void test() {

        EnrollmentData enrollmentData = new EnrollmentData();

        // CREAR matrícula
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId("20240001");
        enrollment.setCourseId("MAT101");
        enrollment.setProfessorId("prof01");
        enrollment.setGrade(0.0);
        enrollment.setStatus("En curso");

        Enrollment added = enrollmentData.addEnrollment(enrollment);

        System.out.println("Matrícula agregada: "
                + (added != null ? added.getId() : "No se pudo agregar"));

        // LEER TODAS
        System.out.println("\nTodas las matrículas:");

        ArrayList<Enrollment> all = enrollmentData.loadAll().toList();

        for (Enrollment e : all) {
            System.out.println(e);
        }

        // BUSCAR POR ID
        if (added != null) {
            Enrollment byId = enrollmentData.findById(added.getId());

            System.out.println("\nBuscar por ID:");
            System.out.println(byId != null ? byId : "No encontrada");
        }

        // BUSCAR POR ESTUDIANTE Y CURSO
        Enrollment byStudentCourse =
                enrollmentData.findByStudentAndCourse("20240001", "MAT101");

        System.out.println("\nBuscar por estudiante y curso:");
        System.out.println(byStudentCourse != null
                ? byStudentCourse
                : "No encontrada");

        // BUSCAR POR PROFESOR
        System.out.println("\nMatrículas del profesor prof01:");

        for (Enrollment e :
                enrollmentData.getEnrollmentsByProfessor("prof01").toList()) {
            System.out.println(e);
        }

        // BUSCAR POR CURSO Y PROFESOR
        System.out.println("\nMatrículas de MAT101 impartido por prof01:");

        for (Enrollment e :
                enrollmentData.getEnrollmentsByCourseAndProfessor(
                        "MAT101", "prof01").toList()) {
            System.out.println(e);
        }

        // BUSCAR POR ESTUDIANTE
        System.out.println("\nMatrículas del estudiante 20240001:");

        for (Enrollment e :
                enrollmentData.getEnrollmentsByStudent("20240001").toList()) {
            System.out.println(e);
        }

        // ACTUALIZAR
        if (added != null) {

            added.setGrade(85.5);
            added.setStatus("Aprobado");

            boolean updated =
                    enrollmentData.updateEnrollment(added);

            System.out.println("\nMatrícula actualizada: " + updated);
        }

        // ACTUALIZAR NOTA
        if (added != null) {

            boolean gradeUpdated =
                    enrollmentData.updateGrade(
                            added.getId(),
                            95.0,
                            "Aprobado"
                    );

            System.out.println("Nota actualizada: " + gradeUpdated);
        }

        // BUSCAR POR ESTADO
        System.out.println("\nMatrículas aprobadas:");

        for (Enrollment e :
                enrollmentData.getEnrollmentsByStatus("Aprobado").toList()) {
            System.out.println(e);
        }

        // ELIMINAR
        if (added != null) {

            boolean deleted =
                    enrollmentData.deleteEnrollment(added.getId());

            System.out.println("\nMatrícula eliminada: " + deleted);
        }

        // MOSTRAR TODAS AL FINAL
        System.out.println("\nMatrículas finales:");

        for (Enrollment e : enrollmentData.loadAll().toList()) {
            System.out.println(e);
        }
    }
}