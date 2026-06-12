package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.MatriculaAprobada;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * CRUD para Matrículas Aprobadas (después de que admin aprueba pre-matrícula).
 */
public class EnrollmentApprovedData {

    private static final String FILE_PATH = "src/main/resources/data/enrollment_approved.json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public EnrollmentApprovedData() {
        File folder = new File("src/main/resources/data");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    // =========================
    // LOAD
    // =========================
    public LinkedList<MatriculaAprobada> loadAll() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ArrayList<MatriculaAprobadaDTO>>() {}.getType();
            ArrayList<MatriculaAprobadaDTO> dtoList = gson.fromJson(reader, listType);

            LinkedList<MatriculaAprobada> result = new LinkedList<>();
            if (dtoList != null) {
                StudentData studentData = new StudentData();
                CourseData courseData = new CourseData();

                for (MatriculaAprobadaDTO dto : dtoList) {
                    Student student = studentData.findStudentById(dto.getStudentId());
                    LinkedList<Course> courses = new LinkedList<>();

                    if (dto.getCourseCodes() != null) {
                        for (String code : dto.getCourseCodes().toList()) {
                            Course c = courseData.findCourseById(code);
                            if (c != null) courses.add(c);
                        }
                    }

                    MatriculaAprobada mat = new MatriculaAprobada(dto.getId(), student, courses);
                    mat.setStatus(dto.getStatus());
                    result.add(mat);
                }
            }
            return result;

        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    // =========================
    // SAVE
    // =========================
    public void saveAll(LinkedList<MatriculaAprobada> list) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            ArrayList<MatriculaAprobadaDTO> dtoList = new ArrayList<>();

            for (MatriculaAprobada mat : list.toList()) {
                dtoList.add(new MatriculaAprobadaDTO(mat));
            }

            gson.toJson(dtoList, writer);

        } catch (Exception e) {
            System.out.println("Error saving matricula aprobada: " + e.getMessage());
        }
    }

    // =========================
    // CREATE/ADD
    // =========================
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
            e.printStackTrace();
            return null;
        }
    }


    // =========================
    // FIND BY ID
    // =========================
    public MatriculaAprobada findByMatriculaId(String Id) {
        for (MatriculaAprobada mat : loadAll().toList()) {
            if (mat.getId() != null && mat.getId().equals(Id)) {
                return mat;
            }
        }
        return null;
    }
    // =========================
    // FIND BY STUDENT
    // =========================
    public MatriculaAprobada findByStudentId(String studentId) {
        for (MatriculaAprobada mat : loadAll().toList()) {
            if (mat.getStudent() != null && mat.getStudent().getId().equals(studentId)) {
                return mat;
            }
        }
        return null;
    }

    // =========================
    // DELETE
    // =========================
    public boolean delete(String matriculaId) throws ListException {
        LinkedList<MatriculaAprobada> all = loadAll();
        MatriculaAprobada record = findByMatriculaId(matriculaId);
        if (all != null) {

            all.remove(record);

            saveAll(all);

            return true;
        }

        return false;

    }

    // =========================
    // DTO
    // =========================
    public static class MatriculaAprobadaDTO {
        private String id;
        private String studentId;
        private LinkedList<String> courseCodes;
        private String status;

        public MatriculaAprobadaDTO(MatriculaAprobada mat) throws Exception {
            this.id = mat.getId();
            this.studentId = mat.getStudent() != null ? mat.getStudent().getId() : null;
            this.status = mat.getStatus();
            this.courseCodes = new LinkedList<>();

            if (mat.getCoursesApproved() != null && mat.getCoursesApproved().size() > 0) {
                for (int i = 1; i <= mat.getCoursesApproved().size(); i++) {
                    Course c = mat.getCoursesApproved().get(i);
                    if (c != null) this.courseCodes.add(c.getId());
                }
            }
        }

        public MatriculaAprobadaDTO() {}

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getStudentId() { return studentId; }
        public void setStudentId(String studentId) { this.studentId = studentId; }

        public LinkedList<String> getCourseCodes() { return courseCodes; }
        public void setCourseCodes(LinkedList<String> courseCodes) { this.courseCodes = courseCodes; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
