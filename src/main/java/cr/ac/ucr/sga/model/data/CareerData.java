package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.Career;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;

public class CareerData {

    private final LinkedList<Career> careers;

    private static final String FILE_PATH =
            "src/main/resources/data/careers.json";

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public CareerData() {

        File folder = new File("src/main/resources/data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        careers = loadCareers();
    }

    // =========================
    // LOAD
    // =========================

    private LinkedList<Career> loadCareers() {

        LinkedList<Career> result = new LinkedList<>();

        try (FileReader reader = new FileReader(FILE_PATH)) {

            Type listType = new TypeToken<java.util.List<Career>>() {}.getType();

            java.util.List<Career> loaded =
                    gson.fromJson(reader, listType);

            if (loaded != null) {
                for (Career career : loaded) {
                    result.add(career);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // =========================
    // SAVE
    // =========================

    private void saveCareers() {

        try (FileWriter writer = new FileWriter(FILE_PATH)) {

            gson.toJson(
                    careers.toList(),
                    writer
            );

        } catch (Exception e) {

            System.out.println(
                    "Error saving careers: " + e.getMessage()
            );
        }
    }

    // =========================
    // CREATE
    // =========================

    public Career addCareer(Career career) {

        if (career == null) return null;

        if (findCareerById(career.getId()) != null) {
            return null;
        }

        careers.add(career);

        saveCareers();

        return career;
    }

    // =========================
    // READ ALL
    // =========================

    public LinkedList<Career> getAllCareers() {

        return careers;
    }

    // =========================
    // READ BY ID
    // =========================

    public Career findCareerById(String id) {

        for (Career career : careers.toList()) {

            if (career.getId().equalsIgnoreCase(id)) {

                return career;
            }
        }

        return null;
    }

    // =========================
    // READ BY NAME
    // =========================

    public Career findCareerByName(String name) {

        for (Career career : careers.toList()) {

            if (career.getName().equalsIgnoreCase(name)) {

                return career;
            }
        }

        return null;
    }

    // =========================
    // UPDATE
    // =========================

    public boolean updateCareer(Career updatedCareer) throws ListException {

        for (int i = 0; i < careers.size(); i++) {

            if (careers.get(i).getId()
                    .equalsIgnoreCase(updatedCareer.getId())) {

                careers.add(i, updatedCareer);

                saveCareers();

                return true;
            }
        }

        return false;
    }

    // =========================
    // DELETE
    // =========================

    public boolean deleteCareer(String id) throws ListException {

        Career career = findCareerById(id);

        if (career != null) {

            careers.remove(career);

            saveCareers();

            return true;
        }

        return false;
    }
}