package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Career;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class CareerDataTest {

    @Test
    void test() throws ListException {

        CareerData careerData = new CareerData();

        // CREAR
        Career career = new Career(
                "1",
                "INF",
                "Informática Empresarial",
                140
        );

        Career added = careerData.addCareer(career);
        System.out.println("Carrera agregada: "
                + (added != null ? added : "Ya existía"));

        // LEER TODAS
        System.out.println("\nCarreras registradas:");
        ArrayList<Career> careers = careerData.getAllCareers().toList();

        for (Career c : careers) {
            System.out.println(c);
        }

        // BUSCAR POR ID
        Career byId = careerData.findCareerById("1");
        System.out.println("\nBuscar por ID:");
        System.out.println(byId != null ? byId : "No encontrada");

        // BUSCAR POR NOMBRE
        Career byName =
                careerData.findCareerByName("Informática Empresarial");

        System.out.println("\nBuscar por nombre:");
        System.out.println(byName != null ? byName : "No encontrada");


        // ELIMINAR
        boolean deleted = careerData.deleteCareer("1");
        System.out.println("\nCarrera eliminada: " + deleted);

        System.out.println("\nCarreras finales:");
        for (Career c : careerData.getAllCareers().toList()) {
            System.out.println(c);
        }
    }
}