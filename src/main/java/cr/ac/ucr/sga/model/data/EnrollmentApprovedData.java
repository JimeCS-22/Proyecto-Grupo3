package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.Enrollment;
import cr.ac.ucr.sga.model.entities.MatriculaAprobada;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class EnrollmentApprovedData {

    private static final String FILE_PATH = "src/main/resources/data/enrollment_approved.json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public EnrollmentApprovedData() {
        File folder = new File("src/main/resources/data");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public LinkedList<MatriculaAprobada> loadAll() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ArrayList<MatriculaAprobadaDTO>>() {}.getType();
            ArrayList<MatriculaAprobadaDTO> dtoList = gson.fromJson(reader, listType);

            LinkedList<MatriculaAprobada> result = new LinkedList<>();
            if (dtoList != null) {
                StudentData studentData = new StudentData();

                for (MatriculaAprobadaDTO dto : dtoList) {
                    Student student = studentData.findStudentById(dto.getStudentId());
                    LinkedList<Enrollment> enrollments = new LinkedList<>();

                    for (EnrollmentDTO dtoE : dto.getEnrollments().toList()) {
                        Enrollment e = new Enrollment();
                        e.setId(dtoE.getId());
                        e.setStudentId(dto.getStudentId());
                        e.setCourseId(dtoE.getCourseId());
                        e.setProfessorId(dtoE.getProfessorId());
                        e.setGrade(dtoE.getGrade());
                        e.setStatus(dtoE.getStatus());
                        enrollments.add(e);
                    }
                    MatriculaAprobada mat = new MatriculaAprobada(dto.getId(), student, enrollments);
                    mat.setStatus(dto.getStatus());
                    result.add(mat);
                }
            }
            return result;
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    public void saveAll(LinkedList<MatriculaAprobada> list) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            ArrayList<MatriculaAprobadaDTO> dtoList = new ArrayList<>();
            for (MatriculaAprobada mat : list.toList()) {
                dtoList.add(new MatriculaAprobadaDTO(mat));
            }
            gson.toJson(dtoList, writer);
        } catch (Exception e) {
        }
    }

    public MatriculaAprobada addOrUpdate(MatriculaAprobada matricula) {
        if (matricula == null) {
            return null;
        }
        LinkedList<MatriculaAprobada> allMatriculas = loadAll();
        try {
            MatriculaAprobada existente = null;
            for (MatriculaAprobada m : allMatriculas.toList()) {
                if (m.getStudent() != null && matricula.getStudent() != null
                        && m.getStudent().getId().equals(matricula.getStudent().getId())) {
                    existente = m;
                    break;
                }
            }
            if (existente != null) {
                allMatriculas.remove(existente);
            }
            allMatriculas.add(matricula);
            saveAll(allMatriculas);
            return matricula;
        } catch (Exception e) {
            return null;
        }
    }

    public MatriculaAprobada findByMatriculaId(String Id) {
        for (MatriculaAprobada mat : loadAll().toList()) {
            if (mat.getId() != null && mat.getId().equals(Id)) {
                return mat;
            }
        }
        return null;
    }

    public MatriculaAprobada findByStudentId(String studentId) {
        for (MatriculaAprobada mat : loadAll().toList()) {
            if (mat.getStudent() != null && mat.getStudent().getId().equals(studentId)) {
                return mat;
            }
        }
        return null;
    }

    public boolean delete(String matriculaId) throws ListException {
        LinkedList<MatriculaAprobada> all = loadAll();
        MatriculaAprobada record = findByMatriculaId(matriculaId);
        if (all != null && record != null) {
            all.remove(record);
            saveAll(all);
            return true;
        }
        return false;
    }

    public static class MatriculaAprobadaDTO {
        private String id;
        private String studentId;
        private LinkedList<EnrollmentDTO> enrollments;
        private String status;

        public MatriculaAprobadaDTO(MatriculaAprobada mat) {
            this.id = mat.getId();
            this.studentId = (mat.getStudent() != null) ? mat.getStudent().getId() : null;
            this.status = mat.getStatus();
            this.enrollments = new LinkedList<>();

            if (mat.getEnrollments() != null) {
                for (Enrollment e : mat.getEnrollments().toList()) {
                    EnrollmentDTO dto = new EnrollmentDTO();
                    dto.setId(e.getId());
                    dto.setStudentId(e.getStudentId());
                    dto.setCourseId(e.getCourseId());
                    dto.setProfessorId(e.getProfessorId());
                    dto.setGrade(e.getGrade());
                    dto.setStatus(e.getStatus());
                    enrollments.add(dto);
                }
            }
        }

        public MatriculaAprobadaDTO() {}

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }
        public LinkedList<EnrollmentDTO> getEnrollments() { return enrollments; }
        public void setEnrollments(LinkedList<EnrollmentDTO> enrollments) { this.enrollments = enrollments; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public LinkedList<MatriculaAprobada> getMatriculasByProfessor(String professorUsername) {
        LinkedList<MatriculaAprobada> result = new LinkedList<>();
        LinkedList<MatriculaAprobada> all = loadAll();
        CourseData courseData = new CourseData();

        for (MatriculaAprobada mat : all.toList()) {
            if (mat.getEnrollments() == null) continue;

            boolean foundMatch = false;
            for (Enrollment e : mat.getEnrollments().toList()) {
                String enrollmentProfessor = e.getProfessorId();
                if (enrollmentProfessor == null || enrollmentProfessor.isEmpty()) {
                    Course course = courseData.findCourseById(e.getCourseId());
                    if (course != null && course.getProfessorId() != null) {
                        enrollmentProfessor = course.getProfessorId();
                        e.setProfessorId(enrollmentProfessor);
                    }
                }
                if (enrollmentProfessor != null && enrollmentProfessor.equalsIgnoreCase(professorUsername)) {
                    foundMatch = true;
                    break;
                }
            }
            if (foundMatch) {
                result.add(mat);
            }
        }
        saveAll(all);
        return result;
    }
}