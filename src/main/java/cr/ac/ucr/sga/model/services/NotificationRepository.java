package cr.ac.ucr.sga.model.services;

import cr.ac.ucr.sga.model.entities.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationRepository {

    private static NotificationRepository instance;

    private final List<Notification> notifications = new ArrayList<>();

    private NotificationRepository() {}

    public static NotificationRepository getInstance() {
        if (instance == null) {
            instance = new NotificationRepository();
        }
        return instance;
    }

    public void addNotification(Notification n) {
        notifications.add(n);
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public boolean exists(String studentId, String mensaje) {

        for (Notification n : notifications) {

            if (
                    n.getStudentId().equals(studentId)
                            &&
                            n.getMensaje().equals(mensaje)
            ) {
                return true;
            }
        }

        return false;
    }
}