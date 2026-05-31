package cr.ac.ucr.sga.model.entities;

public class ResueltoState implements TramiteState {
    @Override
    public String getNombre() {
        return "Resuelto";
    }

    @Override
    public void procesar(Tramite tramite) {
        // No se puede procesar de nuevo
    }

    @Override
    public void resolver(Tramite tramite) {
        // Ya está resuelto
    }
}
