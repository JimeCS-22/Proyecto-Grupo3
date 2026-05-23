package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;
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
    private LinkedList<AcademicRecord> records;
    private static final String FILE_PATH = "data/academic_records.json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public AcademicRecordData() {
        File folder = new File("data");
        if (!folder.exists()) folder.mkdir();
        records = loadRecords();
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
            return new LinkedList<>();
        }
    }

    private void saveRecords() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            ArrayList<AcademicRecordDTO> temp = new ArrayList<>();
            try {
                int size = records.size();
                for (int i = 1; i <= size; i++) {
                    AcademicRecord r = records.get(i);
                    temp.add(new AcademicRecordDTO(r));
                }
            } catch (ListException e) {
                System.out.println("Error reading list: " + e.getMessage());
            }
            gson.toJson(temp, writer);
        } catch (Exception e) {
            System.out.println("Error saving records: " + e.getMessage());
        }
    }

    public AcademicRecord addRecord(AcademicRecord record) {
        if (record != null && findByStudentId(record.getStudent().getId()) == null) {
            records.add(record);
            saveRecords();
            return record;
        }
        return null;
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

    public boolean addCourseToStudent(String studentId, Course course) {
        AcademicRecord record = findByStudentId(studentId);
        if (record != null) {
            record.addCourse(course);
            saveRecords();
            return true;
        }
        return false;
    }


}