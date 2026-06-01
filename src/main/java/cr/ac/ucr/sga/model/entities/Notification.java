package cr.ac.ucr.sga.model.entities;

import java.time.LocalDateTime;

public class Notification {

    private String studentId;
    private String mensaje;
    private LocalDateTime fecha;

    public Notification(String studentId, String mensaje) {
        this.studentId = studentId;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}