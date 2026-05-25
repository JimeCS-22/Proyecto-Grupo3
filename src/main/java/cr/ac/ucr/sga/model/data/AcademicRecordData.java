package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import cr.ac.ucr.sga.model.structures.queues.MyQueue;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Usa un DTO para serializar y deserializar listas nativas.
 */
public class AcademicRecordData {
    private static LinkedList<AcademicRecord> records;
    private static final String FILE_PATH = "src/main/resources/data/academic_records.json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public AcademicRecordData() {
        File folder = new File("src/main/resources/data");
        if (!folder.exists()) folder.mkdir();
        if (records == null) {
            records = loadRecords();
        }

    }

    private LinkedList<AcademicRecord> loadRecords() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ArrayList<AcademicRecordDTO>>() {}.getType();
            ArrayList<AcademicRecordDTO> temp = gson.fromJson(reader, listType);
            LinkedList<AcademicRecord> list = new LinkedList<>();
            if (temp != null) {
                for (AcademicRecordDTO dto : temp) {
                    AcademicRecord record = new AcademicRecord(dto.getStudent());
                    record.setCoursesFromList(dto.getCourses());
                    list.add(record);
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }

    private void saveRecords() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            System.out.println("💾 Guardando records...");

            ArrayList<AcademicRecordDTO> temp = new ArrayList<>();

            int size = records.size();
            System.out.println("SIZE records = " + size);

            for (int i = 1; i <= size; i++) {
                AcademicRecord r = records.get(i);

                if (r == null) {
                    System.out.println("⚠️ Record null en índice " + i);
                    continue;
                }

                System.out.println("👤 Student: " + r.getStudent().getName());
                System.out.println("📚 Cursos: " + r.getCourses().size());

                temp.add(new AcademicRecordDTO(r));
            }

            gson.toJson(temp, writer);
            System.out.println("✅ JSON escrito correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public AcademicRecord addRecord(AcademicRecord record) {
        AcademicRecord academicRecordToReturn = null;
        if (record != null && findByStudentId(record.getStudent().getId()) == null) {
            records.add(record);
            saveRecords();
            academicRecordToReturn = record;
        }
        return academicRecordToReturn;
    }

    public LinkedList<AcademicRecord> getAll() {
        return records;
    }

    public AcademicRecord findByStudentId(String id) {
        try {
            int size = records.size();
            for (int i = 1; i <= size; i++) {
                AcademicRecord r = records.get(i);
                if (r.getStudent().getId().equalsIgnoreCase(id)) {
                    return r;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean deleteRecord(String studentId) {
        try {
            int size = records.size();
            for (int i = 1; i <= size; i++) {
                AcademicRecord r = records.get(i);
                if (r.getStudent().getId().equalsIgnoreCase(studentId)) {
                    records.remove(r);
                    saveRecords();
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean addCourseToStudent(String studentId, Course course) throws ListException {
        AcademicRecord record = findByStudentId(studentId);
        if (record != null) {
            // VALIDACIÓN DE DUPLICADOS
            for (int i = 1; i <= record.getCourses().size(); i++) {
                Course c = record.getCourses().get(i);
                if (c.getId().equalsIgnoreCase(course.getId())) {
                    // Ya existe el curso
                    return false;
                }
            }
            record.addCourse(course);
            saveRecords();
            return true;
        }
        return false;
    }

    public AcademicRecord findByUsername(String username) {

        try {

            int size = records.size();

            for (int i = 1; i <= size; i++) {

                AcademicRecord record = records.get(i);

                if (
                        record.getStudent()
                                .getUsername()
                                .equalsIgnoreCase(username)
                ) {

                    return record;
                }
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return null;
    }

    public ArrayList<Student> getAllStudentsFromRecords() {

        ArrayList<Student> list =
                new ArrayList<>();

        try {

            int size = records.size();

            for (
                    int i = 1;
                    i <= size;
                    i++
            ) {

                AcademicRecord record =
                        records.get(i);

                list.add(
                        record.getStudent()
                );
            }

        } catch (Exception e) {

            System.out.println(
                    e.getMessage()
            );
        }

        return list;
    }



}