package cr.ac.ucr.sga.model.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tramite {
    private String id;
    private String tipo;
    private String descripcion;
    private String estadoNombre;
    private transient TramiteState estado;
    private Student estudiante;
    private LocalDateTime fechaEnvio;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Tramite(String tipo, String descripcion, Student estudiante) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.estado = new PendienteState();
        this.estadoNombre = "Pendiente";
        this.estudiante = estudiante;
        this.fechaEnvio = LocalDateTime.now();
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TramiteState getEstado() {
        // ✅ Si no hay objeto estado (viene del JSON), reconstruirlo
        if (this.estado == null) {
            reconstruirEstadoDesdeNombre();
        }
        return estado;
    }

    public void setEstado(TramiteState estado) {
        this.estado = estado;
        this.estadoNombre = estado.getNombre();
    }

    public Student getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Student estudiante) {
        this.estudiante = estudiante;
    }

    public LocalDateTime getFechaEnvioDateTime() {
        return fechaEnvio;
    }

    public String getFechaEnvio() {
        return fechaEnvio != null ? fechaEnvio.format(formatter) : "N/A";
    }

    // =========================
    // METODOS DEL NEGOCIO
    // =========================

    public String getNombreEstado() {
        if (this.estado == null) {
            reconstruirEstadoDesdeNombre();
        }
        return estado.getNombre();
    }

    private void reconstruirEstadoDesdeNombre() {
        if (this.estadoNombre == null) {
            this.estadoNombre = "Pendiente";
        }

        switch (this.estadoNombre) {
            case "Pendiente":
                this.estado = new PendienteState();
                break;
            case "Procesando":
                this.estado = new ProcesandoState();
                break;
            case "Resuelto":
                this.estado = new ResueltoState();
                break;
            default:
                this.estado = new PendienteState();
        }
    }

    public void procesar() {
        estado.procesar(this);
        this.estadoNombre = estado.getNombre();
    }

    public void resolver() {
        estado.resolver(this);
        this.estadoNombre = estado.getNombre();
    }

    @Override
    public String toString() {
        return "Tramite{" +
                "id='" + id + '\'' +
                ", tipo='" + tipo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado=" + getNombreEstado() +
                ", estudiante=" + (estudiante != null ? estudiante.getName() : "null") +
                ", fechaEnvio=" + fechaEnvio +
                '}';
    }
}
