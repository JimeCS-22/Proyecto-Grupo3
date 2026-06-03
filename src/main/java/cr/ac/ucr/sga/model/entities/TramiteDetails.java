package cr.ac.ucr.sga.model.entities;

import java.util.ArrayList;
import java.util.List;

public class TramiteDetails {
    private String tramiteId;
    private List<Comentario> comentarios;

    public TramiteDetails() {
        this.comentarios = new ArrayList<>();
    }

    public TramiteDetails(String tramiteId) {
        this.tramiteId = tramiteId;
        this.comentarios = new ArrayList<>();
    }

    public String getTramiteId() {
        return tramiteId;
    }

    public void setTramiteId(String tramiteId) {
        this.tramiteId = tramiteId;
    }

    public List<Comentario> getComentarios() {
        if (comentarios == null) {
            comentarios = new ArrayList<>();
        }
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public void agregarComentario(Comentario comentario) {
        if (comentario == null) {
            return;
        }
        getComentarios().add(comentario);
    }
}
