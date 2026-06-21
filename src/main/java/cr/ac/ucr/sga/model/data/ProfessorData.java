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

    private final LinkedList<Professor> professors ;
    private static final String FILE_PATH = "data/professor.json";
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public ProfessorData() {

        File file = new File(FILE_PATH);

        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        professors = loadProfessor();
    }

    private LinkedList<Professor> loadProfessor(){

        try (FileReader reader = new FileReader(FILE_PATH)) {

            Type listType =
                    new TypeToken<LinkedList<Professor>>() {
                    }.getType();

            LinkedList<Professor> loadedProfessors   =
                    gson.fromJson(reader, listType);

            return (loadedProfessors != null)
                    ? loadedProfessors
                    : new LinkedList<>();

        } catch (Exception e) {

            return new LinkedList<>();
        }
    }

    private void saveProfessor() {

        try (FileWriter writer = new FileWriter(FILE_PATH)) {

            gson.toJson(professors, writer);

            writer.flush();

        } catch (Exception e) {

            System.out.println(
                    "Error saving professors: "
                            + e.getMessage()
            );
        }
    }

    public Professor addProfessor(Professor professor) {

        Professor professorReturn = null;

        if (professor != null
                && findProfeesorById(professor.getId()) == null) {

            professors.add(professor);

            saveProfessor();

                professorReturn = professor;
        }

        return professorReturn;
    }

    public LinkedList<Professor> getAllProfessors() {

        return professors;
    }

    public Professor findProfeesorById(String id) {

        Professor professorReturn = null;

        for (Professor professor : professors.toList()) {

            if (professor.getId()
                    .equalsIgnoreCase(id)) {

                professorReturn = professor;
            }
        }

        return professorReturn;
    }

    public boolean updateProfessor(Professor updatedProfessor) throws ListException {

        for (int i = 0; i < professors.size(); i++) {

            if (professors.get(i)
                    .getId()
                    .equalsIgnoreCase(updatedProfessor.getId())) {

                professors.add(i, updatedProfessor);

                saveProfessor();

                return true;
            }
        }

        return false;
    }

    public boolean deleteProfessor(String id) throws ListException {

        Professor professor = findProfeesorById(id);

        if (professor != null) {

            professors.remove(professor);

            saveProfessor();

            return true;
        }

        return false;
    }

    public int getProfessorCount() throws ListException {

        return professors.size();
    }

    public Professor findByUserName(String username) {

        for (Professor professor : professors.toList()) {

            if (
                    professor.getUsername() != null
                            &&
                            professor.getUsername()
                                    .equalsIgnoreCase(username)
            ) {

                return professor;
            }
        }

        return null;
    }


}
