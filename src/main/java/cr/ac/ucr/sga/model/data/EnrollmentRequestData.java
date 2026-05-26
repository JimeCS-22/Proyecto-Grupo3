package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.EnrollmentRequest;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;

import java.util.Collection;


/**
 * CRUD de solicitudes de matrícula usando JSON
 */
public class EnrollmentRequestData {

    private final LinkedList<EnrollmentRequest> requests;

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

        if (!folder.exists()) {
            folder.mkdirs();
        }

        requests = loadRequests();
    }

    // =========================
    // LOAD
    // =========================

    private LinkedList<EnrollmentRequest> loadRequests() {

        try (FileReader reader = new FileReader(FILE_PATH)) {

            Type listType =
                    new TypeToken<LinkedList<EnrollmentRequest>>() {
                    }.getType();

            LinkedList<EnrollmentRequest> loaded =
                    gson.fromJson(reader, listType);

            return (loaded != null)
                    ? loaded
                    : new LinkedList<>();

        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    // =========================
    // SAVE
    // =========================

    private void saveRequests() {

        try (FileWriter writer = new FileWriter(FILE_PATH)) {

            gson.toJson(requests, writer);

            writer.flush();

        } catch (Exception e) {
            System.out.println(
                    "Error saving requests: " + e.getMessage()
            );
        }
    }

    // =========================
    // CREATE
    // =========================

    public EnrollmentRequest addRequest(EnrollmentRequest request) {

        if (request == null) return null;

        try {
            requests.addLast(request);
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

    public LinkedList<EnrollmentRequest> getAllRequests() {
        return requests;
    }

    // =========================
    // FIND BY STUDENT ID
    // =========================

    public LinkedList<EnrollmentRequest> getByStudentId(String studentId) {

        LinkedList<EnrollmentRequest> result = new LinkedList<>();

        try {

            int size = requests.size();

            for (int i = 1; i <= size; i++) {

                EnrollmentRequest request = requests.get(i);

                if (request.getStudent().getId().equals(studentId)) {
                    result.add(request);
                }
            }

        } catch (Exception e) {
            System.out.println("Error searching requests: " + e.getMessage());
        }

        return result;
    }

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

    // =========================
    // DELETE
    // =========================
/*
    public boolean deleteRequest(EnrollmentRequest request) {

        try {

            if (request != null && requests.remove(request)) {
                saveRequests();
                return true;
            }

        } catch (Exception e) {
            System.out.println("Error deleting request: " + e.getMessage());
        }

        return false;
    }
*/
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
}