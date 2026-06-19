package cr.ac.ucr.sga.model.entities;

import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;

public class Career {
    private String id;
    private String code;
    private String name;
    private int totalCredits;

    public Career(){}

    public Career(String id, String code, String name, int totalCredits) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.totalCredits = totalCredits;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    @Override
    public String toString() {
        return name;
    }
}
