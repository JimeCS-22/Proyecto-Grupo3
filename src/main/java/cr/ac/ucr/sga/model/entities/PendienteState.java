
package cr.ac.ucr.sga.model.entities;

public class PendienteState implements TramiteState {
    @Override
    public String getNombre() {
        return "Pendiente";
    }

    @Override
    public void procesar(Tramite tramite) {
        tramite.setEstado(new ProcesandoState());
    }

    @Override
    public void resolver(Tramite tramite) {
        // No se puede resolver sin procesar primero
    }
}



