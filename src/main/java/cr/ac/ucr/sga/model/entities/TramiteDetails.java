package cr.ac.ucr.sga.model.entities;


import cr.ac.ucr.sga.model.structures.lists.LinkedList;

public class TramiteDetails {
    private String tramiteId;
    private LinkedList<Comentario> comentarios;

    public TramiteDetails() {
        this.comentarios = new LinkedList<>();
    }

    public TramiteDetails(String tramiteId) {
        this.tramiteId = tramiteId;
        this.comentarios = new LinkedList<>();
    }

    public String getTramiteId() {
        return tramiteId;
    }

    public void setTramiteId(String tramiteId) {
        this.tramiteId = tramiteId;
    }

    public LinkedList<Comentario> getComentarios() {
        if (comentarios == null) {
            comentarios = new LinkedList<>();
        }
        return comentarios;
    }

    public void agregarComentario(Comentario comentario) {
        if (comentario == null) {
            return;
        }
        getComentarios().add(comentario);
    }
}