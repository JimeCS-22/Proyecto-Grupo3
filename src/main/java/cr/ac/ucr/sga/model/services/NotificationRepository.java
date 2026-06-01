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
}