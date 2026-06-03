package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.TramiteData;
import cr.ac.ucr.sga.model.data.TramiteDetailsData;
import cr.ac.ucr.sga.model.entities.Tramite;
import cr.ac.ucr.sga.model.services.NotificationService;
import cr.ac.ucr.sga.model.structures.stacks.LinkedStack;
import cr.ac.ucr.sga.model.structures.stacks.StackException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private TableColumn<Tramite, Tramite> colAcciones;

    @FXML
    private Button btnProcesar;

    @FXML
    private Button btnResolver;

    private final LinkedStack<Tramite> pilaTramites = new LinkedStack<>();
    private final TramiteData tramiteData = new TramiteData();
    private final TramiteDetailsData tramiteDetailsData = new TramiteDetailsData();


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

        if (colAcciones != null) {
            colAcciones.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue()));
            colAcciones.setCellFactory(param -> new TableCell<>() {
                private final Button btnDetalles = new Button("Gestionar Detalles");
                private final HBox panel = new HBox(10, btnDetalles);

                {
                    btnDetalles.setOnAction(event -> {
                        Tramite tramite = getTableView().getItems().get(getIndex());
                        abrirDetalles(tramite);
                    });
                }

                @Override
                protected void updateItem(Tramite item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : panel);
                }
            });
        }
    }

    // =========================
    // CARGAR TRAMITES DEL JSON
    // =========================
    private void cargarTramitesDelJSON() {
        List<Tramite> tramitesPendientes = tramiteData.getTramitesPendientes();

        // Agregar a la pila en ORDEN INVERSO (para LIFO correcto)
        for (int i = tramitesPendientes.size() - 1; i >= 0; i--) {
            try {
                Tramite tramite = tramitesPendientes.get(i);
                tramite.setDetalles(tramiteDetailsData.getDetailsByTramiteId(tramite.getId()));
                pilaTramites.push(tramite);
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
            t.setDetalles(tramiteDetailsData.getDetailsByTramiteId(t.getId()));
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

                tblTramites.getItems().add(0, t);

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

                NotificationService notificationService =
                        NotificationService.getInstance();

                notificationService.notifyObservers(
                        t.getEstudiante().getId(),
                        "⏳ Tu trámite '" +
                                t.getTipo() +
                                "' está siendo procesado."
                );
            }

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
            t.setDetalles(tramiteDetailsData.getDetailsByTramiteId(t.getId()));

            System.out.println("Nuevo estado = " + t.getNombreEstado());

            tramiteData.updateTramite(t);

            NotificationService notificationService =
                    NotificationService.getInstance();

            notificationService.notifyObservers(
                    t.getEstudiante().getId(),
                    "✅ Tu trámite '" +
                            t.getTipo() +
                            "' fue resuelto."
            );



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

    private void abrirDetalles(Tramite tramite) {
        if (tramite == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tramite-details-view.fxml"));
            Parent root = loader.load();

            TramiteDetailsController controller = loader.getController();
            controller.setContext(tramite, false);

            Stage stage = new Stage();
            stage.setTitle("Detalles del Trámite");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            if (tblTramites != null && tblTramites.getScene() != null) {
                stage.initOwner(tblTramites.getScene().getWindow());
            }
            stage.showAndWait();

            tramiteData.updateTramite(tramite);
            loadTramites();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron abrir los detalles del trámite.");
        }
    }

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