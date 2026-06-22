package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.entities.Tramite;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TramiteDataTest {

    @Test
    public void test() throws ListException {

        TramiteData data = new TramiteData();

        Student student = new Student.Builder()
                .setId("202600003")
                .setName("María López")
                .setCarnet("C30003")
                .setAge(22)
                .setUsername("marial")
                .setPassword("123")
                .build();

        Tramite tramite = new Tramite(
                "TR-001",
                "Retiro de curso",
                student
        );

        // ADD
        data.addTramite(tramite);

        System.out.println("\nTrámite agregado: " + tramite.getId());

        // GET ALL
        System.out.println("\nTodos los trámites:");
        for (Tramite t : data.getAllTramites().toList()) {
            System.out.println(t);
        }

        // PENDIENTES
        System.out.println("\nTrámites pendientes:");
        LinkedList<Tramite> pendientes = data.getTramitesPendientes();
        for (Tramite t : pendientes.toList()) {
            System.out.println(t);
        }


        // FIND
        Tramite encontrado = data.getTramiteById("TR-001");

        System.out.println("\nEncontrado:");
        System.out.println(encontrado);
    }
}