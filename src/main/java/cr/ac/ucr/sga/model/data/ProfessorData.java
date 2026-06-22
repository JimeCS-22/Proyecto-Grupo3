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

    private LinkedList<Professor> loadProfessors() {
        try (FileReader reader = new FileReader(FILE_PATH)) {

            Type listType = new TypeToken<java.util.List<Professor>>() {
            }.getType();
            java.util.List<Professor> tempList = gson.fromJson(reader, listType);

            LinkedList<Professor> linkedList = new LinkedList<>();

            if (tempList != null) {
                for (Professor p : tempList) {
                    linkedList.add(p);
                }
            }

            return linkedList;

        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    private void saveProfessors() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {

            gson.toJson(professors.toList(), writer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Professor addProfessor(Professor professor) {
        Professor professorReturn = null;
        if (professor != null && findProfessorById(professor.getId()) == null) {
            professors.add(professor);
            saveProfessors();
            professorReturn = professor;
        }
        return professorReturn;
    }

    public LinkedList<Professor> getAllProfessors() {
        return professors;
    }

    public Professor findProfessorById(String id) {
        for (Professor professor : professors.toList()) {
            if (professor.getId().equalsIgnoreCase(id)) {
                return professor;
            }
        }
        return null;
    }

    public boolean updateProfessor(Professor updatedProfessor) {

        Professor existing = findProfessorById(updatedProfessor.getId());

        if (existing != null) {

            existing.setName(updatedProfessor.getName());
            existing.setCareerId(updatedProfessor.getCareerId());
            existing.setUsername(updatedProfessor.getUsername());
            existing.setPassword(updatedProfessor.getPassword());

            saveProfessors();
            return true;
        }

        return false;
    }

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
        for (Professor professor : professors.toList()) {
            if (username.equalsIgnoreCase(professor.getUsername())) {
                return professor;
            }
        }
        return null;
    }
}


