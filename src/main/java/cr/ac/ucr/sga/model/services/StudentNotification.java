package cr.ac.ucr.sga.model.services;

import cr.ac.ucr.sga.model.entities.Notification;

public class StudentNotification implements NotificationObserver {

    @Override
    public void onNotification(String studentId, String message) {

        NotificationRepository
                .getInstance()
                .addNotification(
                        new Notification(studentId, message)
                );

        System.out.println(
                "Nueva notificación para " +
                        studentId +
                        ": " +
                        message
        );
    }
}