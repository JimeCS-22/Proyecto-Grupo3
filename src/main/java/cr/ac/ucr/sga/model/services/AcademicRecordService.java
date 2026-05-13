package cr.ac.ucr.sga.model.services;

import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

public class AcademicRecordService {
    private DoublyLinkedList<AcademicRecord> records;

    // Constructor
    public AcademicRecordService() {
        this.records = new DoublyLinkedList<>();
    }

    // Agregar historial académico
    public void addAcademicRecord(AcademicRecord record) {
        records.add(record);
    }

    // Buscar historial por cédula o ID
    public AcademicRecord findByStudentId(String studentId) throws ListException {

        for (int i = 1; i <= records.size(); i++) {

            AcademicRecord record = records.get(i);

            if (record.getStudent().getId().equals(studentId)) {
                return record;
            }
        }

        return null;
    }

    // Matricular curso
    public boolean enrollCourse(String studentId, Course course)
            throws ListException {

        AcademicRecord record = findByStudentId(studentId);

        if (record != null) {

            record.getCourses().add(course);
            return true;
        }

        return false;
    }

    // Eliminar curso
    public boolean removeCourse(String studentId, String courseCode)
            throws ListException {

        AcademicRecord record = findByStudentId(studentId);

        if (record != null) {

            for (int i = 1; i <= record.getCourses().size(); i++) {

                Course c = record.getCourses().get(i);

                if (c.getId().equals(courseCode)) {

                    record.getCourses().remove(c);
                    return true;
                }
            }
        }

        return false;
    }

    // Mostrar historiales
    public void showAllRecords() throws ListException {

        if (records.isEmpty()) {
            System.out.println("No hay historiales registrados");
            return;
        }

        for (int i = 1; i <= records.size(); i++) {

            System.out.println(records.get(i));
        }
    }

    // Cantidad de historiales
    public int size() throws ListException {

        if (records.isEmpty()) {
            return 0;
        }

        return records.size();
    }

    // Verificar si está vacío
    public boolean isEmpty() {
        return records.isEmpty();
    }




}
