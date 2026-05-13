package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.Student;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class StudentData {

    private ArrayList<Student> students;

    private static final String FILE_PATH = "src/main/resources/data/students.json";

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public StudentData() {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }

        students = loadStudents();
    }

    // ================= LOAD =================
    private ArrayList<Student> loadStudents() {
        try (FileReader reader = new FileReader(FILE_PATH)) {

            Type listType = new TypeToken<ArrayList<Student>>() {}.getType();

            ArrayList<Student> data = gson.fromJson(reader, listType);

            return (data != null) ? data : new ArrayList<>();

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // ================= SAVE =================
    private void saveStudents() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(students, writer);
        } catch (Exception e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    // ================= CREATE =================
    public Student addStudent(Student student) {

        if (student != null && findStudentById(student.getId()) == null) {
            students.add(student);
            saveStudents();
            return student;
        }

        return null;
    }

    // ================= READ ALL =================
    public ArrayList<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    // ================= READ BY ID =================
    public Student findStudentById(String id) {

        for (Student s : students) {
            if (s.getId().equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }

    // ================= UPDATE =================
    public boolean updateStudent(Student updatedStudent) {

        for (int i = 0; i < students.size(); i++) {

            if (students.get(i).getId().equalsIgnoreCase(updatedStudent.getId())) {

                students.set(i, updatedStudent);
                saveStudents();
                return true;
            }
        }

        return false;
    }

    // ================= DELETE =================
    public boolean deleteStudent(String id) {

        Student student = findStudentById(id);

        if (student != null) {
            students.remove(student);
            saveStudents();
            return true;
        }

        return false;
    }

    // ================= COUNT =================
    public int getStudentsCount() {
        return students.size();
    }
}