package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.EnrollmentRequest;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import cr.ac.ucr.sga.model.structures.queues.PriorityLinkedQueue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentRequestDataTest {

    @Test
    public void test() throws Exception {

        EnrollmentRequestData data =
                new EnrollmentRequestData();

        // Crear estudiante
        Student student = new Student.Builder()
                .setId("202600002")
                .setName("Laura Vargas")
                .setCarnet("C20002")
                .setAge(20)
                .setUsername("laurav")
                .setPassword("123")
                .build();

        // Crear cursos
        Course c1 = new Course.Builder()
                .setId("IF2000")
                .setName("Programación")
                .setCredits(4)
                .setGrade(0)
                .setStatus("Pendiente")
                .build();

        Course c2 = new Course.Builder()
                .setId("IF3000")
                .setName("Estructuras")
                .setCredits(4)
                .setGrade(0)
                .setStatus("Pendiente")
                .build();

        LinkedList<Course> courses = new LinkedList<>();
        courses.add(c1);
        courses.add(c2);

        // Crear request
        EnrollmentRequest request =
                new EnrollmentRequest(
                        student,
                        1,
                        "PENDIENTE",
                        courses
                );

        // AGRERGAR
        EnrollmentRequest added =
                data.addRequest(request);

        System.out.println(
                "Solicitud agregada: "
                        + (added != null ? added : "Error")
        );

        // TODOS
        PriorityLinkedQueue<EnrollmentRequest> queue =
                data.getAllRequests();

        System.out.println("Solicitudes registradas:");

        for (EnrollmentRequest r : queue.toList()) {
            System.out.println(r);
        }

        // ACTUALIZAR STATUS
        boolean updated =
                data.updateStatus(request, "APROBADA");

        System.out.println(
                "Estado actualizado: "
                        + updated
        );

        // CONTAR
        System.out.println(
                "Cantidad solicitudes: "
                        + data.getRequestsCount()
        );

        // BORRAR
        boolean deleted =
                data.deleteRequest(request);

        System.out.println(
                "Solicitud eliminada: "
                        + deleted
        );

        // CLEAR ALL
        data.clearAll();

        System.out.println("Todas las solicitudes eliminadas");
    }
}