package cr.ac.ucr.sga.model.services;

import cr.ac.ucr.sga.model.entities.Role;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

public class AuthService {

    private LinkedList<User> users;

    public AuthService() {

        users = new LinkedList<>();

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

        users.add(
                new User(
                        "estudiante",
                        "123",
                        Role.STUDENT
                )
        );
    }

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
                        user.getUsername().equals(username)
                                &&
                                user.getPassword().equals(password)
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
}