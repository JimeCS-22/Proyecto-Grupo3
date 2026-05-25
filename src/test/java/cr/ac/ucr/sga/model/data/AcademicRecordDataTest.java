package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AcademicRecordDataTest {

    @Test
    public void test(){
        AcademicRecordData arData = new AcademicRecordData();

        // Crear estudiante y curso usando los builders correspondientes
        Student stud = new Student.Builder()
                .setId("198766543")
                .setName("Pedro Chaves")
                .setEmail("pedro.chaves@ucr.ac.cr")
                .setCarnet("C12999")
                .setAge(22)
                .setUsername("pedrocha")
                .setPassword("pedroclave")
                .build();

        Course c1 = new Course.Builder()
                .setId("MATH101")
                .setName("Cálculo 1")
                .setCredits(4)
                .setGrade(0)
                .setStatus("En curso")
                .build();
        Course c2 = new Course.Builder()
                .setId("QUIM102")
                .setName("Química General")
                .setCredits(3)
                .setGrade(0)
                .setStatus("En curso")
                .build();

        // Crear record académico
        AcademicRecord record = new AcademicRecord(stud);
        record.addCourse(c1);

        // Agregar record
        AcademicRecord added = arData.addRecord(record);
        System.out.println("Record agregado: " + (added != null ? added : "Ya existía"));

        // Agregar segundo curso al record de Pedro
        boolean add2 = false;
        try {
            add2 = arData.addCourseToStudent("198766543", c2);
        } catch (ListException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Segundo curso agregado: " + add2);

        // Buscar record por ID
        AcademicRecord buscado = arData.findByStudentId("198766543");
        System.out.println("Record encontrado: " + (buscado != null ? buscado : "No encontrado"));

        // Mostrar todos los estudiantes con récord académico
        ArrayList<Student> conRecords = arData.getAllStudentsFromRecords();
        System.out.println("Estudiantes con récord académico:");
        for (Student s : conRecords) {
            System.out.println(s);
        }

        // Eliminar record
        boolean eliminado = arData.deleteRecord("198766543");
        System.out.println("Record eliminado: " + eliminado);
    }
    }

