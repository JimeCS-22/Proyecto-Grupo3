package cr.ac.ucr.sga.model.entities;

import java.time.LocalDateTime;

public class Comentario {
    private String autor;
    private String contenido;
    private LocalDateTime fecha;
    private String tipo;

    public Comentario() {
    }

    public Comentario(String autor, String contenido, LocalDateTime fecha, String tipo) {
        this.autor = autor;
        this.contenido = contenido;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}