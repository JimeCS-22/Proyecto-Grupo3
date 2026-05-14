package cr.ac.ucr.sga.model.services;

import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

public class AcademicRecordService {

    // SINGLETON
    private static AcademicRecordService instance;

    private final DoublyLinkedList<AcademicRecord> records;

    // Constructor privado
    private AcademicRecordService() {
        this.records = new DoublyLinkedList<>();
    }

    // Obtener única instancia
    public static AcademicRecordService getInstance() {

        if (instance == null) {
            instance = new AcademicRecordService();
        }

        return instance;
    }

    // Agregar historial académico
    public void addAcademicRecord(AcademicRecord record) {

        if (record == null) {
            throw new IllegalArgumentException("El historial no puede ser null");
        }

        records.add(record);
    }

    // Buscar historial por ID del estudiante
    public AcademicRecord findByStudentId(String studentId)
            throws ListException {

        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("El ID no puede estar vacío");
        }

        if (records.isEmpty()) {
            return null;
        }

        for (int i = 1; i <= records.size(); i++) {

            AcademicRecord record = records.get(i);

            if (studentId.equals(record.getStudent().getId())) {
                return record;
            }
        }

        return null;
    }

    // Matricular curso
    public boolean enrollCourse(String studentId, Course course)
            throws ListException {

        if (course == null) {
            throw new IllegalArgumentException("El curso no puede ser null");
        }

        AcademicRecord record = findByStudentId(studentId);

        if (record != null) {

            // Usar método encapsulado
            record.addCourse(course);

            return true;
        }

        return false;
    }

    // Eliminar curso
    public boolean removeCourse(String studentId, String courseCode)
            throws ListException {

        AcademicRecord record = findByStudentId(studentId);

        if (record == null) {
            return false;
        }

        if (record.getCourses().isEmpty()) {
            return false;
        }

        for (int i = 1; i <= record.getCourses().size(); i++) {

            Course course = record.getCourses().get(i);

            if (courseCode.equals(course.getId())) {

                // Usar método encapsulado
                record.removeCourse(course);

                return true;
            }
        }

        return false;
    }

    // Obtener todos los historiales
    public DoublyLinkedList<AcademicRecord> getRecords() {
        return records;
    }

    // Cantidad de historiales
    public int size() {

        if (records.isEmpty()) {
            return 0;
        }

        try {
            return records.size();
        } catch (ListException e) {
            return 0;
        }
    }

    // Verificar si está vacío
    public boolean isEmpty() {
        return records.isEmpty();
    }
}