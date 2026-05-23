package cr.ac.ucr.sga.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import cr.ac.ucr.sga.model.entities.Role;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class UserData {

    private LinkedList<User> users;

    private static final String FILE_PATH =
            "src/main/resources/data/users.json";

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public UserData() {

        File folder =
                new File("src/main/resources/data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        users = loadUsers();

        createDefaultUsers();
    }

    // =========================
    // LOAD
    // =========================

    private LinkedList<User> loadUsers() {

        try (FileReader reader =
                     new FileReader(FILE_PATH)) {

            Type listType =
                    new TypeToken<ArrayList<User>>() {
                    }.getType();

            ArrayList<User> temp =
                    gson.fromJson(reader, listType);

            LinkedList<User> loadedUsers =
                    new LinkedList<>();

            if (temp != null) {

                for (User user : temp) {
                    loadedUsers.add(user);
                }
            }

            return loadedUsers;

        } catch (Exception e) {

            return new LinkedList<>();
        }
    }

    // =========================
    // SAVE
    // =========================

    private void saveUsers() {

        try (FileWriter writer =
                     new FileWriter(FILE_PATH)) {

            ArrayList<User> temp =
                    new ArrayList<>();

            try {

                for (
                        int i = 1;
                        i <= users.size();
                        i++
                ) {

                    temp.add(users.get(i));
                }

            } catch (ListException e) {

                System.out.println(
                        "Error reading users: "
                                + e.getMessage()
                );
            }

            gson.toJson(temp, writer);

            writer.flush();

        } catch (Exception e) {

            System.out.println(
                    "Error saving users: "
                            + e.getMessage()
            );
        }
    }

    // =========================
    // DEFAULT USERS
    // =========================

    private void createDefaultUsers() {

        if (users.isEmpty()) {

            users.add(
                    new User(
                            "admin",
                            "123",
                            Role.ADMIN
                    )
            );

            users.add(
                    new User(
                            "profesor",
                            "123",
                            Role.PROFESSOR
                    )
            );

            saveUsers();
        }
    }

    // =========================
    // ADD
    // =========================

    public User addUser(User user) {

        if (
                user != null
                        &&
                        findByUsername(
                                user.getUsername()
                        ) == null
        ) {

            users.add(user);

            saveUsers();

            return user;
        }

        return null;
    }

    // =========================
    // FIND USERNAME
    // =========================

    public User findByUsername(String username) {

        try {

            for (
                    int i = 1;
                    i <= users.size();
                    i++
            ) {

                User user = users.get(i);

                if (
                        user.getUsername()
                                .equalsIgnoreCase(username)
                ) {

                    return user;
                }
            }

        } catch (ListException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }

        return null;
    }

    // =========================
    // LOGIN
    // =========================

    public User login(
            String username,
            String password
    ) {

        try {

            for (
                    int i = 1;
                    i <= users.size();
                    i++
            ) {

                User user = users.get(i);

                if (
                        user.getUsername()
                                .equalsIgnoreCase(username)
                                &&
                                user.getPassword()
                                        .equals(password)
                ) {

                    return user;
                }
            }

        } catch (ListException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }

        return null;
    }

    // =========================
    // GET ALL
    // =========================

    public ArrayList<User> getAllUsers() {

        ArrayList<User> temp =
                new ArrayList<>();

        try {

            for (
                    int i = 1;
                    i <= users.size();
                    i++
            ) {

                temp.add(users.get(i));
            }

        } catch (ListException e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }

        return temp;
    }
}