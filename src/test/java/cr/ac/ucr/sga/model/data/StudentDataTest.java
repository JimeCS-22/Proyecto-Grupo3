package cr.ac.ucr.sga.model.data;


import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDataTest {

    @Test
    public void test() throws ListException {

        StudentData stData = new StudentData();

        // CREAR estudiante con builder
        Student s = new Student.Builder()
                .setId("202355589")
                .setName("Ana González")
                .setCarnet("C11588")
                .setAge(19)
                .setUsername("anagonz")
                .setPassword("mipass123")
                .build();

        Student agregado = stData.addStudent(s);
        System.out.println("Estudiante agregado: " + (agregado != null ? agregado : "Ya existía"));

        // LEER TODOS
        LinkedList<Student> students = stData.getAllStudents();
        System.out.println("Lista actual:");
        for (Student stu : students.toList()) {
            System.out.println(stu);
        }

        // Buscar por id
        Student byId = stData.findStudentById("202355589");
        System.out.println("Encontrado por id: " + (byId != null ? byId : "No encontrado"));

        // Buscar por username
        Student byUser = stData.findByUsername("anagonz");
        System.out.println("Encontrado por username: " + (byUser != null ? byUser : "No encontrado"));

        // UPDATE (solo simula, campos modificados)
        Student actualizado = new Student.Builder()
                .setId("202355589")
                .setName("Ana G. Actualizada")
                .setCarnet("C11588")
                .setAge(20)
                .setUsername("anagonz")
                .setPassword("nuevaClave")
                .build();

        boolean modif = stData.updateStudent(actualizado);
        System.out.println("Estudiante actualizado: " + modif);

        // DELETE
        boolean deleted = stData.deleteStudent("202355589");
        System.out.println("Estudiante eliminado: " + deleted);

        // CONTAR
        System.out.println("Total estudiantes: " + stData.getStudentsCount());
    }
    }


