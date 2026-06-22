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

public class StudentData {

    private final LinkedList<Student> students;

    private static final String FILE_PATH = "src/main/resources/data/students.json";

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

    private LinkedList<Student> loadStudents() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<LinkedList<Student>>() {}.getType();
            LinkedList<Student> loadedStudents = gson.fromJson(reader, listType);
            return (loadedStudents != null) ? loadedStudents : new LinkedList<>();
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    private void saveStudents() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(students, writer);
            writer.flush();
        } catch (Exception e) {
        }
    }

    public Student addStudent(Student student) {
        Student studentToReturn = null;
        if (student != null && findStudentById(student.getId()) == null) {
            students.add(student);
            saveStudents();
            studentToReturn = student;
        }
        return studentToReturn;
    }

    public LinkedList<Student> getAllStudents() {
        return students;
    }

    public Student findStudentById(String id) {
        Student studentToReturn = null;
        for (Student student : students.toList()) {
            if (student.getId().equalsIgnoreCase(id)) {
                studentToReturn = student;
            }
        }
        return studentToReturn;
    }

    public boolean updateStudent(Student updatedStudent) {
        java.util.ArrayList<Student> list = students.toList();
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equalsIgnoreCase(updatedStudent.getId())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        list.set(index, updatedStudent);
        students.clear();
        for (Student s : list) {
            students.add(s);
        }
        saveStudents();
        return true;
    }

    public boolean deleteStudent(String id) throws ListException {
        Student student = findStudentById(id);
        if (student != null) {
            students.remove(student);
            saveStudents();
            return true;
        }
        return false;
    }

    public int getStudentsCount() throws ListException {
        return students.size();
    }

    public Student findByUsername(String username) {
        for (Student student : students.toList()) {
            if (student.getUsername() != null && student.getUsername().equalsIgnoreCase(username)) {
                return student;
            }
        }
        return null;
    }
}