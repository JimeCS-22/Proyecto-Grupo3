package cr.ac.ucr.sga.model.entities;

public class Tramite {
    private String id;
    private String tipo;
    private String descripcion;
    private String estado;
    private Student estudiante;

    public Tramite(String tipo, String descripcion, Student estudiante) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.estado = "Pendiente";
        this.estudiante = estudiante;
    }

    public String getId() { return id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Student getEstudiante() { return estudiante; }
    public void setEstudiante(Student estudiante) { this.estudiante = estudiante; }
}