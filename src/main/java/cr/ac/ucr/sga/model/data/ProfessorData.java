package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.Professor;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;

public class ProfessorData {

    private final LinkedList<Professor> professors;

    private static final String FILE_PATH = "src/main/resources/data/professors.json";

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public ProfessorData() {
        File folder = new File("src/main/resources/data");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        professors = loadProfessors();
    }

    /**
     * LOAD - Igual que StudentData
     */
    private LinkedList<Professor> loadProfessors() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<LinkedList<Professor>>() {}.getType();
            LinkedList<Professor> loadedProfessors = gson.fromJson(reader, listType);
            return (loadedProfessors != null) ? loadedProfessors : new LinkedList<>();
        } catch (Exception e) {
            System.err.println("Error loading professors: " + e.getMessage());
            return new LinkedList<>();
        }
    }

    /**
     * SAVE - Igual que StudentData
     */
    private void saveProfessors() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(professors, writer);
            writer.flush();
        } catch (Exception e) {
            System.err.println("Error saving professors: " + e.getMessage());
        }
    }

    /**
     * CREATE
     */
    public Professor addProfessor(Professor professor) {
        Professor professorReturn = null;
        if (professor != null && findProfessorById(professor.getId()) == null) {
            professors.add(professor);
            saveProfessors();
            professorReturn = professor;
        }
        return professorReturn;
    }

    /**
     * READ ALL
     */
    public LinkedList<Professor> getAllProfessors() {
        return professors;
    }

    /**
     * READ BY ID - CORREGIDO el typo
     */
    public Professor findProfessorById(String id) {
        for (Professor professor : professors.toList()) {
            if (professor.getId().equalsIgnoreCase(id)) {
                return professor;
            }
        }
        return null;
    }

    /**
     * UPDATE
     */
    public boolean updateProfessor(Professor updatedProfessor) throws ListException {
        for (int i = 0; i < professors.size(); i++) {
            if (professors.get(i).getId().equalsIgnoreCase(updatedProfessor.getId())) {
                professors.add(i, updatedProfessor);
                saveProfessors();
                return true;
            }
        }
        return false;
    }

    /**
     * DELETE
     */
    public boolean deleteProfessor(String id) throws ListException {
        Professor professor = findProfessorById(id);
        if (professor != null) {
            professors.remove(professor);
            saveProfessors();
            return true;
        }
        return false;
    }


    public int getProfessorCount() throws ListException {
        return professors.size();
    }


    public Professor findProfessorByUsername(String username) {
        if (username == null || username.isEmpty()) {
            return null;
        }

        try {
            for (Professor professor : professors.toList()) {
                if (username.equalsIgnoreCase(professor.getUsername())) {
                    return professor;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}