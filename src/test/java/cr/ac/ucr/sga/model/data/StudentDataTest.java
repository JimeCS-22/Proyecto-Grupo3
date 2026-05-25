package cr.ac.ucr.sga.model.data;


import cr.ac.ucr.sga.model.entities.Student;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StudentDataTest {

    @Test
    public void test(){

        StudentData stData = new StudentData();

        // CREAR estudiante con builder
        Student s = new Student.Builder()
                .setId("202355589")
                .setName("Ana González")
                .setEmail("ana.glez@ucr.ac.cr")
                .setCarnet("C11588")
                .setAge(19)
                .setUsername("anagonz")
                .setPassword("mipass123")
                .build();

        Student agregado = stData.addStudent(s);
        System.out.println("Estudiante agregado: " + (agregado != null ? agregado : "Ya existía"));

        // LEER TODOS
        ArrayList<Student> students = stData.getAllStudents();
        System.out.println("Lista actual:");
        for (Student stu : students) {
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
                .setEmail("ana.nuevo@ucr.ac.cr")
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


