package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;

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
            Type listType = new TypeToken<LinkedList<AcademicRecordDTO>>() {}.getType();
            LinkedList<AcademicRecordDTO> temp = gson.fromJson(reader, listType);
            LinkedList<AcademicRecord> list = new LinkedList<>();
            if (temp != null) {
                for (AcademicRecordDTO dto : temp.toList()) {
                    AcademicRecord record = new AcademicRecord(dto.getStudent());
                    if (dto.getCourses() != null) {
                        for (Course course : dto.getCourses()) {
                            record.addCourse(course);
                        }
                    }
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
            LinkedList<AcademicRecordDTO> temp = new LinkedList<>();
            int size = records.size();
            for (int i = 1; i <= size; i++) {
                AcademicRecord r = records.get(i);
                if (r != null) {
                    temp.add(new AcademicRecordDTO(r));
                }
            }
            gson.toJson(temp, writer);
        } catch (Exception e) {
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
        }
        return false;
    }

    public boolean addCourseToStudent(String studentId, Course course) throws ListException {
        AcademicRecord record = findByStudentId(studentId);
        if (record != null) {
            for (int i = 1; i <= record.getCourses().size(); i++) {
                Course c = record.getCourses().get(i);
                if (c.getId().equalsIgnoreCase(course.getId())) {
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
                if (record.getStudent().getUsername().equalsIgnoreCase(username)) {
                    return record;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public LinkedList<Student> getAllStudentsFromRecords() {
        LinkedList<Student> list = new LinkedList<>();
        try {
            int size = records.size();
            for (int i = 1; i <= size; i++) {
                AcademicRecord record = records.get(i);
                list.add(record.getStudent());
            }
        } catch (Exception e) {
        }
        return list;
    }
}