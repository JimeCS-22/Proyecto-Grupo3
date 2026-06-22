package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.Enrollment;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;

public class EnrollmentData {

    private static final String FILE_PATH = "src/main/resources/data/enrollments.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public EnrollmentData() {
        File folder = new File("src/main/resources/data");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public LinkedList<Enrollment> loadAll() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ArrayList<Enrollment>>() {}.getType();
            ArrayList<Enrollment> list = gson.fromJson(reader, listType);

            LinkedList<Enrollment> result = new LinkedList<>();
            if (list != null) {
                for (Enrollment e : list) {
                    if (e.getId() == null || e.getId().isEmpty()) {
                        e.setId(UUID.randomUUID().toString());
                    }
                    result.add(e);
                }
            }
            return result;
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    private void saveAll(LinkedList<Enrollment> enrollments) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(enrollments.toList(), writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Enrollment> getEnrollmentsByProfessor(String professorUsername) {
        LinkedList<Enrollment> result = new LinkedList<>();
        LinkedList<Enrollment> all = loadAll();

        for (Enrollment e : all.toList()) {
            if (e.getProfessorId() != null && e.getProfessorId().equalsIgnoreCase(professorUsername)) {
                result.add(e);
            }
        }
        return result;
    }

    public LinkedList<Enrollment> getEnrollmentsByCourseAndProfessor(String courseId, String professorUsername) {
        LinkedList<Enrollment> result = new LinkedList<>();
        LinkedList<Enrollment> all = loadAll();

        for (Enrollment e : all.toList()) {
            if (e.getCourseId() != null && e.getCourseId().equalsIgnoreCase(courseId) &&
                    e.getProfessorId() != null && e.getProfessorId().equalsIgnoreCase(professorUsername)) {
                result.add(e);
            }
        }
        return result;
    }

    public LinkedList<Enrollment> getEnrollmentsByStudent(String studentId) {
        LinkedList<Enrollment> result = new LinkedList<>();
        LinkedList<Enrollment> all = loadAll();

        for (Enrollment e : all.toList()) {
            if (e.getStudentId() != null && e.getStudentId().equals(studentId)) {
                result.add(e);
            }
        }
        return result;
    }

    public Enrollment findById(String id) {
        LinkedList<Enrollment> all = loadAll();
        for (Enrollment e : all.toList()) {
            if (e.getId() != null && e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    public Enrollment findByStudentAndCourse(String studentId, String courseId) {
        LinkedList<Enrollment> all = loadAll();
        for (Enrollment e : all.toList()) {
            if (e.getStudentId() != null && e.getStudentId().equals(studentId) &&
                    e.getCourseId() != null && e.getCourseId().equals(courseId)) {
                return e;
            }
        }
        return null;
    }

    public Enrollment addEnrollment(Enrollment enrollment) {
        if (enrollment == null) return null;

        if (enrollment.getId() == null || enrollment.getId().isEmpty()) {
            enrollment.setId(UUID.randomUUID().toString());
        }

        LinkedList<Enrollment> all = loadAll();
        all.add(enrollment);
        saveAll(all);
        return enrollment;
    }

    public boolean updateEnrollment(Enrollment updated) {
        if (updated == null || updated.getId() == null) {
            return false;
        }

        LinkedList<Enrollment> all = loadAll();
        LinkedList<Enrollment> newList = new LinkedList<>();
        boolean found = false;

        for (Enrollment e : all.toList()) {
            if (e.getId() != null && e.getId().equals(updated.getId())) {
                newList.add(updated);
                found = true;
            } else {
                newList.add(e);
            }
        }

        if (found) {
            saveAll(newList);
            return true;
        }
        return false;
    }

    public boolean updateGrade(String enrollmentId, double grade, String status) {
        Enrollment enrollment = findById(enrollmentId);
        if (enrollment == null) return false;

        enrollment.setGrade(grade);
        enrollment.setStatus(status);
        return updateEnrollment(enrollment);
    }

    public boolean deleteEnrollment(String id) {
        LinkedList<Enrollment> all = loadAll();
        LinkedList<Enrollment> newList = new LinkedList<>();
        boolean found = false;

        for (Enrollment e : all.toList()) {
            if (e.getId() != null && e.getId().equals(id)) {
                found = true;
            } else {
                newList.add(e);
            }
        }

        if (found) {
            saveAll(newList);
            return true;
        }
        return false;
    }

    public boolean deleteEnrollmentsByStudent(String studentId) {
        LinkedList<Enrollment> all = loadAll();
        LinkedList<Enrollment> newList = new LinkedList<>();

        for (Enrollment e : all.toList()) {
            if (e.getStudentId() == null || !e.getStudentId().equals(studentId)) {
                newList.add(e);
            }
        }
        saveAll(newList);
        return true;
    }

    public LinkedList<Enrollment> getEnrollmentsByStatus(String status) {
        LinkedList<Enrollment> result = new LinkedList<>();
        LinkedList<Enrollment> all = loadAll();

        for (Enrollment e : all.toList()) {
            if (e.getStatus() != null && e.getStatus().equalsIgnoreCase(status)) {
                result.add(e);
            }
        }
        return result;
    }


}