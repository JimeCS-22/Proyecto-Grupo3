package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.TramiteData;
import cr.ac.ucr.sga.model.entities.Tramite;
import cr.ac.ucr.sga.model.services.NotificationService;
import cr.ac.ucr.sga.model.services.StudentNotification;
import cr.ac.ucr.sga.model.structures.stacks.LinkedStack;
import cr.ac.ucr.sga.model.structures.stacks.StackException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TramiteReviewController implements Initializable {

    @FXML
    private TableView<Tramite> tblTramites;

    @FXML
    private TableColumn<Tramite, String> colTipo;

    @FXML
    private TableColumn<Tramite, String> colDescripcion;

    @FXML
    private TableColumn<Tramite, String> colEstado;

    @FXML
    private Button btnProcesar;

    @FXML
    private Button btnResolver;

    private final LinkedStack<Tramite> pilaTramites = new LinkedStack<>();
    private final TramiteData tramiteData = new TramiteData();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar columnas de la tabla
        setupTableColumns();

        // ✅ CARGAR TRAMITES DEL JSON
        cargarTramitesDelJSON();

        // Configurar botones
        btnProcesar.setOnAction(e -> procesarTramiteMasReciente());

        if (btnResolver != null) {
            btnResolver.setOnAction(e -> resolverTramiteMasReciente());
        }

        System.out.println("✓ TramiteReviewController inicializado");
        System.out.println("tblTramites = " + tblTramites);
        System.out.println("colTipo = " + colTipo);
        System.out.println("colDescripcion = " + colDescripcion);
        System.out.println("colEstado = " + colEstado);
    }

    // =========================
    // CONFIGURAR COLUMNAS
    // =========================
    private void setupTableColumns() {

        System.out.println(
                "Columnas encontradas: " +
                        tblTramites.getColumns().size()
        );
        colTipo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipo())
        );

        colDescripcion.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDescripcion())
        );

        colEstado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombreEstado())
        );
    }

    // =========================
    // CARGAR TRAMITES DEL JSON
    // =========================
    private void cargarTramitesDelJSON() {
        List<Tramite> tramitesPendientes = tramiteData.getTramitesPendientes();

        // Agregar a la pila en ORDEN INVERSO (para LIFO correcto)
        for (int i = tramitesPendientes.size() - 1; i >= 0; i--) {
            try {
                pilaTramites.push(tramitesPendientes.get(i));
            } catch (StackException e) {
                e.printStackTrace();
            }
        }

        loadTramites();
        System.out.println("✓ Trámites cargados del JSON. Total: " + tramitesPendientes.size());
    }

    // =========================
    // AGREGAR TRAMITE A LA PILA (desde estudiante)
    // =========================
    public void pushTramite(Tramite t) {
        try {
            pilaTramites.push(t);
            System.out.println("✓ Trámite agregado a la pila: " + t.getId());
            loadTramites();
        } catch (StackException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // CARGAR TRAMITES EN LA TABLA
    // =========================
    private void loadTramites() {

        tblTramites.getItems().clear();

        try {

            LinkedStack<Tramite> aux = new LinkedStack<>();

            while (!pilaTramites.isEmpty()) {

                Tramite t = pilaTramites.pop();

                System.out.println(
                        "Fila -> " +
                                t.getTipo() + " | " +
                                t.getDescripcion() + " | " +
                                t.getNombreEstado()
                );

                tblTramites.getItems().add(t);

                aux.push(t);
            }

            while (!aux.isEmpty()) {
                pilaTramites.push(aux.pop());
            }

            System.out.println(
                    "Items en tabla = " +
                            tblTramites.getItems().size()
            );

            tblTramites.refresh();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // PROCESAR TRAMITE MAS RECIENTE (LIFO)
    // =========================
    @FXML
    private void procesarTramiteMasReciente() {
        try {

            if (pilaTramites.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING,
                        "Sin trámites",
                        "No hay trámites para procesar.");
                return;
            }

            // Sacar temporalmente el trámite
            Tramite t = pilaTramites.pop();

            System.out.println("Procesando ID = " + t.getId());
            System.out.println("Estado actual = " + t.getNombreEstado());

            // Solo procesar si está pendiente
            if (t.getNombreEstado().equals("Pendiente")) {

                t.procesar();

                System.out.println("Nuevo estado = " + t.getNombreEstado());

                tramiteData.updateTramite(t);

//                notificarEstudiante(
//                        t,
//                        "⏳ Tu trámite '" + t.getTipo()
//                                + "' está siendo PROCESADO.\nID: "
//                                + t.getId()
//                );
            }

            // IMPORTANTE:
            // devolver el trámite a la pila porque aún no está resuelto
            pilaTramites.push(t);

            loadTramites();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // RESOLVER TRAMITE
    // =========================
    @FXML
    private void resolverTramiteMasReciente() {

        try {

            if (pilaTramites.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING,
                        "Sin trámites",
                        "No hay trámites para resolver.");
                return;
            }

            Tramite t = pilaTramites.pop();

            System.out.println("Resolviendo ID = " + t.getId());
            System.out.println("Estado actual = " + t.getNombreEstado());

            if (!t.getNombreEstado().equals("Procesando")) {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Operación inválida",
                        "Primero debes procesar el trámite."
                );

                // devolver a la pila
                pilaTramites.push(t);

                return;
            }

            t.resolver();

            System.out.println("Nuevo estado = " + t.getNombreEstado());

            tramiteData.updateTramite(t);

//            notificarEstudiante(
//                    t,
//                    "✅ Tu trámite '" + t.getTipo()
//                            + "' ha sido RESUELTO.\nID: "
//                            + t.getId()
//            );

            // OJO:
            // NO lo volvemos a meter a la pila porque ya terminó

            loadTramites();

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Trámite resuelto",
                    "El trámite fue resuelto correctamente."
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // NOTIFICAR AL ESTUDIANTE (Observer Pattern)
    // =========================
//    private void notificarEstudiante(Tramite tramite, String mensaje) {
//        try {
//            NotificationService notificationService = NotificationService.getInstance();
//
//            // Crear observer para el estudiante del trámite
//            StudentNotification observer = new StudentNotification(
//                    tramite.getEstudiante().getEmail()
//            );
//
//            // Agregar observer
//            notificationService.addObserver(observer);
//            System.out.println("✓ Observer agregado para: " + tramite.getEstudiante().getEmail());
//
//            // Notificar
//            notificationService.notifyObservers(mensaje);
//            System.out.println("✓ Notificación enviada al estudiante");
//
//            // Remover observer
//            notificationService.removeObserver(observer);
//            System.out.println("✓ Observer removido");
//
//        } catch (Exception e) {
//            System.err.println("❌ Error al notificar: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    // =========================
    // MOSTRAR ALERTAS
    // =========================
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // =========================
    // GETTERS
    // =========================
    public LinkedStack<Tramite> getPilaTramites() {
        return pilaTramites;
    }
}
