package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Professor;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class ProfessorDataTest {

    @Test
    void test() throws ListException {

        ProfessorData professorData = new ProfessorData();

        // CREAR profesor
        Professor professor = new Professor();
        professor.setId("PROF01");
        professor.setUsername("juanp");
        professor.setName("Juan Pérez");

        // Agregar profesor
        Professor added = professorData.addProfessor(professor);

        System.out.println("Profesor agregado: "
                + (added != null ? added : "Ya existía"));

        // LEER TODOS
        System.out.println("\nProfesores registrados:");

        ArrayList<Professor> all =
                professorData.getAllProfessors().toList();

        for (Professor p : all) {
            System.out.println(p);
        }

        // Buscar por ID
        Professor byId = professorData.findProfessorById("PROF01");

        System.out.println("\nProfesor encontrado por ID:");
        System.out.println(byId != null ? byId : "No encontrado");

        // Buscar por username
        Professor byUsername =
                professorData.findProfessorByUsername("juanp");

        System.out.println("\nProfesor encontrado por username:");
        System.out.println(byUsername != null
                ? byUsername
                : "No encontrado");


        // Mostrar después de actualizar
        System.out.println("\nProfesores después de actualizar:");

        for (Professor p : professorData.getAllProfessors().toList()) {
            System.out.println(p);
        }

        // DELETE
        boolean deleted =
                professorData.deleteProfessor("PROF01");

        System.out.println("\nProfesor eliminado: " + deleted);

        // Mostrar lista final
        System.out.println("\nProfesores finales:");

        for (Professor p : professorData.getAllProfessors().toList()) {
            System.out.println(p);
        }

        // CONTAR
        System.out.println("\nTotal de profesores: "
                + professorData.getProfessorCount());
    }
}