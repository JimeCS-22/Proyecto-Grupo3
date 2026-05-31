package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.EnrollmentRequest;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import cr.ac.ucr.sga.model.structures.queues.PriorityLinkedQueue;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;

import java.util.*;


/**
 * CRUD de solicitudes de matrícula usando JSON
 */
public class EnrollmentRequestData {

    private static PriorityLinkedQueue<EnrollmentRequest> requests;

    private static final String FILE_PATH =
            "src/main/resources/data/enrollment_requests.json";

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    // =========================
    // CONSTRUCTOR
    // =========================

    public EnrollmentRequestData() {

        File folder = new File("src/main/resources/data");

        if (requests == null) {
            requests = loadRequests();
        }

        if (!folder.exists()) {
            folder.mkdirs();
        }

        requests = loadRequests();
    }

    // LOAD Requests
    private PriorityLinkedQueue<EnrollmentRequest> loadRequests() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ArrayList<EnrollmentRequestDTO>>() {}.getType();
            ArrayList<EnrollmentRequestDTO> list = gson.fromJson(reader, listType);

            PriorityLinkedQueue<EnrollmentRequest> queue = new PriorityLinkedQueue<>();
            if (list != null) {
                StudentData studentData = new StudentData();
                CourseData courseData = new CourseData();
                for (EnrollmentRequestDTO dto : list) {
                    // BUSCA el estudiante por su id
                    Student student = studentData.findStudentById(dto.getStudentId());
                    LinkedList<Course> courses = new LinkedList<>();
                    if (dto.getCourseCodes() != null) {
                        for (String code : dto.getCourseCodes()) {
                            Course c = courseData.findCourseById(code);
                            if (c != null) courses.add(c);
                        }
                    }

                    EnrollmentRequest req = new EnrollmentRequest(
                            student,
                            dto.getPriority(),
                            dto.getStatus(),
                            courses
                    );
                    queue.enQueue(req, req.getPriority());
                }
            }
            return queue;

        } catch (Exception e) {
            return new PriorityLinkedQueue<>();
        }
    }


    // =========================
    // SAVE
    // =========================
    private void saveRequests() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            ArrayList<EnrollmentRequestDTO> list = new ArrayList<>();

            for (EnrollmentRequest req : requests.toList()) {
                list.add(new EnrollmentRequestDTO(req));
            }

            gson.toJson(list, writer);

        } catch (Exception e) {
            System.out.println("Error saving requests: " + e.getMessage());
        }
    }


    // =========================
    // CREATE
    // =========================

    public EnrollmentRequest addRequest(EnrollmentRequest request) {

        if (request == null) return null;

        try {
            requests.enQueue(request, request.getPriority());
            saveRequests();
            return request;

        } catch (Exception e) {
            System.out.println("Error adding request: " + e.getMessage());
            return null;
        }
    }

    // =========================
    // READ ALL
    // =========================

    public PriorityLinkedQueue<EnrollmentRequest> getAllRequests() {
        return requests;
    }

    // =========================
    // FIND BY STUDENT ID
    // =========================



    // =========================
    // UPDATE STATUS
    // =========================

    public boolean updateStatus(EnrollmentRequest request, String status) {

        if (request == null) return false;

        try {
            request.setStatus(status);
            saveRequests();
            return true;

        } catch (Exception e) {
            System.out.println("Error updating status: " + e.getMessage());
            return false;
        }
    }


    public boolean deleteRequest(EnrollmentRequest request) {
        if (request == null) {
            return false;
        }

        try {
            PriorityLinkedQueue<EnrollmentRequest> updated = new PriorityLinkedQueue<>();
            boolean removed = false;

            for (EnrollmentRequest current : requests.toList()) {
                if (!removed && sameRequest(current, request)) {
                    removed = true;
                    continue;
                }

                updated.enQueue(current, current.getPriority());
            }

            requests = updated;
            saveRequests();
            return removed;

        } catch (Exception e) {
            System.out.println("Error deleting request: " + e.getMessage());
            return false;
        }
    }

    private boolean sameRequest(EnrollmentRequest a, EnrollmentRequest b) {
        return Objects.equals(a.getStudentId(), b.getStudentId())
                && Objects.equals(a.getStatus(), b.getStatus());
    }

    // =========================
    // CLEAR ALL
    // =========================

    public void clearAll() {

        try {
            requests.clear();
            saveRequests();

        } catch (Exception e) {
            System.out.println("Error clearing requests: " + e.getMessage());
        }
    }

    // =========================
    // COUNT
    // =========================

    public int getRequestsCount() throws ListException {
        return requests.size();
    }


        public Iterable<EnrollmentRequest> getRequests() {
            return requests.toList();
        }

    }

