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

    // =========================
    // AGREGAR NUEVA PESTAÑA
    // =========================
    public void addTabIndex(int index) throws ListException {

        // Evita duplicados consecutivos
        if (!history.isEmpty()) {
            Integer currentTab = history.get(current);

            if (currentTab != null && currentTab == index) {
                return;
            }
        }

        history.add(index);

        // El current siempre apunta al último agregado
        current = history.size();
    }

    // =========================
    // IR HACIA ATRÁS (CIRCULAR)
    // =========================
    public Integer backTab() throws ListException {

        if (history.isEmpty()) {
            return null;
        }

        current--;

        // Si pasa del inicio → vuelve al final
        if (current < 1) {
            current = history.size();
        }

        return history.get(current);
    }

    // =========================
    // IR HACIA ADELANTE (CIRCULAR)
    // =========================
    public Integer forwardTab() throws ListException {

        if (history.isEmpty()) {
            return null;
        }

        current++;

        // Si pasa del final → vuelve al inicio
        if (current > history.size()) {
            current = 1;
        }

        return history.get(current);
    }

    // =========================
    // DEBUG
    // =========================
    public void printHistory() {
        System.out.println(history);
        System.out.println("Current index: " + current);
    }
}