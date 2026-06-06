package cr.ac.ucr.sga.model.entities;

import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;

public class Career {
    private DoublyLinkedList<Course> courses;
    private String name;
    private int totalCredits;

    public DoublyLinkedList<Course> getCourses() {
        return courses;
    }

    public void setCourses(DoublyLinkedList<Course> courses) {
        this.courses = courses;
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
        return  name ;
    }
}
