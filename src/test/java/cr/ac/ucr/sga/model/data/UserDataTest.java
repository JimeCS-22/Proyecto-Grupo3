package cr.ac.ucr.sga.model.data;

import cr.ac.ucr.sga.model.entities.Role;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserDataTest {

    @Test
    public void test(){
        UserData userData = new UserData();

        // Crear nuevo usuario
        User user = new User("anaadmin", "clave123", Role.ADMIN);

        // Agregar usuario
        User agregado = userData.addUser(user);
        System.out.println("Usuario agregado: " + (agregado != null ? agregado : "Ya existía"));

        // Buscar por username
        User byUsername = userData.findByUsername("anaadmin");
        System.out.println("Encontrado por usuario: " + (byUsername != null ? byUsername : "No encontrado"));

        // Probar login correcto
        User login = userData.login("anaadmin", "clave123");
        System.out.println("Login correcto: " + (login != null ? login : "Fallido"));

        // Probar login incorrecto
        User wrongLogin = userData.login("anaadmin", "incorrecta");
        System.out.println("Login fallido: " + (wrongLogin != null ? "Inesperado" : "Correctamente denegado"));

        // Listar todos
        LinkedList<User> users = userData.getAllUsers();
        for (User u : users.toList()) System.out.println(u.getUsername() + " (" + u.getRole() + ")");
    }

}