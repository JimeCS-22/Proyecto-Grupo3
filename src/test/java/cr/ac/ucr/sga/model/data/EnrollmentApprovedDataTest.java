package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.*;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import org.junit.jupiter.api.Test;

class EnrollmentApprovedDataTest {

    @Test
    public void test() throws Exception {

        EnrollmentApprovedData data = new EnrollmentApprovedData();

        // =========================
        // STUDENT
        // =========================
        Student student = new Student.Builder()
                .setId("202600001")
                .setName("Carlos Ramirez")
                .setCarnet("C10001")
                .setAge(21)
                .setUsername("carlosr")
                .setPassword("123")
                .build();

        // =========================
        // ENROLLMENTS (lo correcto)
        // =========================
        Enrollment e1 = new Enrollment();
        e1.setId("E-001");
        e1.setStudentId(student.getId());
        e1.setCourseId("IF3001");
        e1.setProfessorId("PROF01");
        e1.setGrade(95);
        e1.setStatus("APROBADO");

        Enrollment e2 = new Enrollment();
        e2.setId("E-002");
        e2.setStudentId(student.getId());
        e2.setCourseId("MA1001");
        e2.setProfessorId("PROF01");
        e2.setGrade(88);
        e2.setStatus("APROBADO");

        LinkedList<Enrollment> enrollments = new LinkedList<>();
        enrollments.add(e1);
        enrollments.add(e2);

        // =========================
        // MATRÍCULA APROBADA
        // =========================
        MatriculaAprobada matricula =
                new MatriculaAprobada("MAT-001", student, enrollments);

        System.out.println("=== CREANDO MATRÍCULA ===");
        System.out.println(matricula);

        // =========================
        // AGREGAR / ACTUALIZAR
        // =========================
        data.addOrUpdate(matricula);
        System.out.println("\nMatrícula agregada o actualizada");

        // =========================
        // LEER TODAS
        // =========================
        System.out.println("\n=== TODAS LAS MATRÍCULAS ===");

        for (MatriculaAprobada m : data.loadAll().toList()) {
            System.out.println(m);

            System.out.println("Enrollments:");
            for (Enrollment e : m.getEnrollments().toList()) {
                System.out.println(" - " + e.getId()
                        + " | course: " + e.getCourseId()
                        + " | grade: " + e.getGrade()
                        + " | status: " + e.getStatus());
            }
        }

        // =========================
        // BUSCAR POR ID
        // =========================
        System.out.println("\n=== BUSCAR POR ID ===");

        MatriculaAprobada encontrada =
                data.findByMatriculaId("MAT-001");

        System.out.println(encontrada != null
                ? encontrada
                : "No encontrada");

        // =========================
        // BUSCAR POR ESTUDIANTE
        // =========================
        System.out.println("\n=== BUSCAR POR ESTUDIANTE ===");

        MatriculaAprobada porEstudiante =
                data.findByStudentId("202600001");

        System.out.println(porEstudiante != null
                ? porEstudiante
                : "No encontrada");

        // =========================
        // FILTRAR POR PROFESOR
        // =========================
        System.out.println("\n=== POR PROFESOR PROF01 ===");

        for (MatriculaAprobada m :
                data.getMatriculasByProfessor("PROF01").toList()) {
            System.out.println(m);
        }

        // =========================
        // DELETE
        // =========================
        System.out.println("\n=== ELIMINAR ===");

        boolean deleted = data.delete("MAT-001");
        System.out.println("Eliminada: " + deleted);

        // =========================
        // FINAL
        // =========================
        System.out.println("\n=== MATRÍCULAS FINALES ===");

        for (MatriculaAprobada m : data.loadAll().toList()) {
            System.out.println(m);
        }
    }
}