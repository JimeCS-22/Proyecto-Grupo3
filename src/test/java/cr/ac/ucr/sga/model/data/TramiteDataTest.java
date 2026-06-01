package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.entities.Tramite;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TramiteDataTest {

    @Test
    public void test() {

        TramiteData data = new TramiteData();

        // Crear estudiante
        Student student = new Student.Builder()
                .setId("202600003")
                .setName("María López")
                .setCarnet("C30003")
                .setAge(22)
                .setUsername("marial")
                .setPassword("123")
                .build();

        // Crear trámite
        Tramite tramite =
                new Tramite(
                        "Retiro de curso",
                        "Necesito retirar el curso IF3001",
                        student
                );

        // ADD
        data.addTramite(tramite);

        System.out.println(
                "Trámite agregado: "
                        + tramite.getId()
        );

        // GET ALL
        List<Tramite> all =
                data.getAllTramites();

        System.out.println("Todos los trámites:");

        for (Tramite t : all) {
            System.out.println(t);
        }

        // GET PENDIENTES
        List<Tramite> pendientes =
                data.getTramitesPendientes();

        System.out.println("Trámites pendientes:");

        for (Tramite t : pendientes) {
            System.out.println(t);
        }

        // UPDATE
        tramite.procesar();

        data.updateTramite(tramite);

        System.out.println(
                "Estado actualizado a: "
                        + tramite.getNombreEstado()
        );

        // FIND BY ID
        Tramite encontrado =
                data.getTramiteById(tramite.getId());

        System.out.println(
                "Encontrado por ID: "
                        + (encontrado != null ? encontrado : "No encontrado")
        );
    }
}