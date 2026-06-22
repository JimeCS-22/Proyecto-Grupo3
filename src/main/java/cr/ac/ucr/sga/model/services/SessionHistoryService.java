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

    public void addTabIndex(int index) throws ListException {

        if (!history.isEmpty()) {
            Integer currentTab = history.get(current);

            if (currentTab != null && currentTab == index) {
                return;
            }
        }

        history.add(index);

        current = history.size();
    }

    public Integer backTab() throws ListException {

        if (history.isEmpty()) {
            return null;
        }

        current--;

        if (current < 1) {
            current = history.size();
        }

        return history.get(current);
    }

    public Integer forwardTab() throws ListException {

        if (history.isEmpty()) {
            return null;
        }

        current++;

        if (current > history.size()) {
            current = 1;
        }

        return history.get(current);
    }
}