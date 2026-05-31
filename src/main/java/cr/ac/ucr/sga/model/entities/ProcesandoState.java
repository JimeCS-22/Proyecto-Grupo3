package cr.ac.ucr.sga.model.entities;

public class ProcesandoState implements TramiteState {
    @Override
    public String getNombre() {
        return "Procesando";
    }

    @Override
    public void procesar(Tramite tramite) {
        // Ya está procesando, no hace nada
    }

    @Override
    public void resolver(Tramite tramite) {
        tramite.setEstado(new ResueltoState());
    }
}