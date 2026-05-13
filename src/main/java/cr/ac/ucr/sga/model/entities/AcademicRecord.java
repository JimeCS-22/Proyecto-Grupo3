package cr.ac.ucr.sga.model.entities;

import cr.ac.ucr.sga.model.structures.lists.LinkedList;

public class AcademicRecord {

        private Student student;
        private LinkedList<Course> courses;

        // Constructor
        public AcademicRecord(Student student) {
            this.student = student;
            this.courses = new LinkedList<>();
        }

        // Getters & Setters
        public Student getStudent() {
            return student;
        }

        public void setStudent(Student student) {
            this.student = student;
        }

        public LinkedList<Course> getCourses() {
            return courses;
        }

        public void setCourses(LinkedList<Course> courses) {
            this.courses = courses;
        }

        // Agregar curso
        public void addCourse(Course course) {
            courses.add(course);
        }


    }

