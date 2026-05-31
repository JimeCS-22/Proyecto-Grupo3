package cr.ac.ucr.sga.model.entities;

public interface TramiteState {
    String getNombre();
    void procesar(Tramite tramite);      // transición a 'Procesando'
    void resolver(Tramite tramite);      // transición a 'Resuelto'
}
