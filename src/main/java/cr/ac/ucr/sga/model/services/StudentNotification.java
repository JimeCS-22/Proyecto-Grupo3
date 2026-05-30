package cr.ac.ucr.sga.model.services;

public class StudentNotification implements NotificationObserver {
    private String studentEmail;

    public StudentNotification(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    @Override
    public void onNotification(String message) {
        // Implementar envío real de notificación o imprimir en consola
        System.out.println("Notificando a " + studentEmail + ": " + message);
    }
}