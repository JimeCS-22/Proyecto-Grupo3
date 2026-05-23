package cr.ac.ucr.sga.model.services;

import cr.ac.ucr.sga.model.data.UserData;
import cr.ac.ucr.sga.model.entities.User;

public class AuthService {

    private final UserData userData =
            new UserData();

    public User login(
            String username,
            String password
    ) {

        return userData.login(
                username,
                password
        );
    }
}