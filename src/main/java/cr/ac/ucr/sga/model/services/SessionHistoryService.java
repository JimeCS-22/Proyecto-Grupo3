package cr.ac.ucr.sga.model.services;

import cr.ac.ucr.sga.model.structures.lists.CircularDoublyLinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

public class SessionHistoryService {

    private static SessionHistoryService instance;

    private final CircularDoublyLinkedList<Integer> history;
    private int current;

    private SessionHistoryService() {
        this.history = new CircularDoublyLinkedList<>();
        this.current = 0;
    }

    public static SessionHistoryService getInstance() {
        if (instance == null) {
            instance = new SessionHistoryService();
        }
        return instance;
    }

    // Agregar nuevo índice de pestaña al historial
    public void addTabIndex(int index) throws ListException {
        if (history.size() == 0) {
            history.add(index);
            current = 1;
        } else {
            while (history.size() > current) {
                history.removeLast();
            }
            history.add(index);
            current = history.size();
        }
    }

    // Navegar hacia atrás en historial de pestañas
    public Integer backTab() throws ListException {
        if (history.size() == 0 || current <= 1) {
            return null;
        }
        current--;
        return history.get(current);
    }

    // Navegar hacia adelante en historial de pestañas
    public Integer forwardTab() throws ListException {
        if (history.size() == 0 || current >= history.size()) {
            return null;
        }
        current++;
        return history.get(current);
    }
}