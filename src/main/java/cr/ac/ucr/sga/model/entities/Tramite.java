package cr.ac.ucr.sga.model.entities;

public class Tramite {
    private String id;
    private String tipo;
    private String descripcion;
    private TramiteState estado;
    private Student estudiante;

    public Tramite(String tipo, String descripcion, Student estudiante) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.estado = new PendienteState();   // Estado inicial
        this.estudiante = estudiante;
    }

    // Getters y setters
    public String getId() { return id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public TramiteState getEstado() { return estado; }
    public void setEstado(TramiteState estado) { this.estado = estado; }
    public Student getEstudiante() { return estudiante; }
    public void setEstudiante(Student estudiante) { this.estudiante = estudiante; }

    // Para mostrar el nombre actual del estado en tus tablas:
    public String getNombreEstado() {
        return estado.getNombre();
    }

    // Métodos para transiciones
    public void procesar() { estado.procesar(this); }
    public void resolver() { estado.resolver(this); }
}