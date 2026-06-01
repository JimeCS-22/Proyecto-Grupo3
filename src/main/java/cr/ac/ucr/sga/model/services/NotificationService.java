package cr.ac.ucr.sga.model.services;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private static NotificationService instance;
    private List<NotificationObserver> observers = new ArrayList<>();

    // Constructor privado
    private NotificationService() {}

    // Singleton getter
    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    // Métodos para observer pattern
    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String studentId, String message) {

        for (NotificationObserver o : observers) {
            o.onNotification(studentId, message);
        }

    }
}