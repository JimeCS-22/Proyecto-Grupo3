package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;


/**
 * CRUD de estudiantes usando JSON
 */
public class StudentData {

    private final LinkedList<Student> students;

    private static final String FILE_PATH =
            "src/main/resources/data/students.json";

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public StudentData() {

        File folder = new File("src/main/resources/data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        students = loadStudents();
    }

    /**
     * LOAD
     */
    private LinkedList<Student> loadStudents() {

        try (FileReader reader = new FileReader(FILE_PATH)) {

            Type listType =
                    new TypeToken<LinkedList<Student>>() {
                    }.getType();

            LinkedList<Student> loadedStudents =
                    gson.fromJson(reader, listType);

            return (loadedStudents != null)
                    ? loadedStudents
                    : new LinkedList<>();

        } catch (Exception e) {

            return new LinkedList<>();
        }
    }

    /**
     * SAVE
     */
    private void saveStudents() {

        try (FileWriter writer = new FileWriter(FILE_PATH)) {

            gson.toJson(students, writer);

            writer.flush();

        } catch (Exception e) {

            System.out.println(
                    "Error saving students: "
                            + e.getMessage()
            );
        }
    }

    /**
     * CREATE
     */
    public Student addStudent(Student student) {

        Student studentToReturn = null;

        if (student != null
                && findStudentById(student.getId()) == null) {

            students.add(student);

            saveStudents();

            studentToReturn = student;
        }

        return studentToReturn;
    }

    /**
     * READ ALL
     */
    public LinkedList<Student> getAllStudents() {

        return students;
    }

    /**
     * READ BY ID
     */
    public Student findStudentById(String id) {

        Student studentToReturn = null;

        for (Student student : students.toList()) {

            if (student.getId()
                    .equalsIgnoreCase(id)) {

                studentToReturn = student;
            }
        }

        return studentToReturn;
    }

    /**
     * UPDATE
     */
    public boolean updateStudent(Student updatedStudent) throws ListException {

        for (int i = 0; i < students.size(); i++) {

            if (students.get(i)
                    .getId()
                    .equalsIgnoreCase(updatedStudent.getId())) {

                students.add(i, updatedStudent);

                saveStudents();

                return true;
            }
        }

        return false;
    }

    /**
     * DELETE
     */
    public boolean deleteStudent(String id) throws ListException {

        Student student = findStudentById(id);

        if (student != null) {

            students.remove(student);

            saveStudents();

            return true;
        }

        return false;
    }

    /**
     * COUNT
     */
    public int getStudentsCount() throws ListException {

        return students.size();
    }

    public Student findByUsername(String username) {

        for (Student student : students.toList()) {

            if (
                    student.getUsername() != null
                            &&
                            student.getUsername()
                                    .equalsIgnoreCase(username)
            ) {

                return student;
            }
        }

        return null;
    }
}
