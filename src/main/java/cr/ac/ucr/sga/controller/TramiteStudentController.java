package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.TramiteData;
import cr.ac.ucr.sga.model.data.TramiteDetailsData;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.entities.Tramite;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import cr.ac.ucr.sga.model.services.NotificationService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TramiteStudentController implements Initializable {

    // =========================
    // CAMPOS DEL FORMULARIO
    // =========================
    @FXML
    private TextField txtTipo;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private Button btnEnviar;

    @FXML
    private Button btnLimpiar;

    // =========================
    // TABLA DE MIS TRAMITES
    // =========================
    @FXML
    private TableView<Tramite> tblMisTramites;

    @FXML
    private TableColumn<Tramite, String> colTipo;

    @FXML
    private TableColumn<Tramite, String> colDescripcion;

    @FXML
    private TableColumn<Tramite, String> colEstado;

    @FXML
    private TableColumn<Tramite, String> colFecha;

    @FXML
    private TableColumn<Tramite, Tramite> colAcciones;

    // =========================
    // ESTADO DEL CONTROLADOR
    // =========================
    private Student estudiante;
    private MainController mainController;
    private final ObservableList<Tramite> misTramites = FXCollections.observableArrayList();
    private final TramiteData tramiteData = new TramiteData();
    private final TramiteDetailsData tramiteDetailsData = new TramiteDetailsData();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar tabla
        setupTableColumns();

        // Configurar botones
        btnEnviar.setOnAction(e -> {
            try {
                enviarTramite();
            } catch (ListException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnLimpiar.setOnAction(e -> limpiarCampos());

        // Asignar lista de trámites a la tabla
        tblMisTramites.setItems(misTramites);

        System.out.println("✓ TramiteStudentController inicializado");
    }

    // =========================
    // CONFIGURAR TABLA
    // =========================
    private void setupTableColumns() {
        colTipo.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTipo())
        );

        colDescripcion.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDescripcion())
        );

        colEstado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNombreEstado())
        );

        colFecha.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFechaEnvio())
        );

        if (colAcciones != null) {
            colAcciones.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue()));
            colAcciones.setCellFactory(param -> new TableCell<>() {
                private final Button btnDetalles = new Button("Ver Detalles");
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
    // SETTERS
    // =========================
    public void setEstudiante(Student estudiante) {
        this.estudiante = estudiante;
        System.out.println("✓ Estudiante establecido: " + estudiante.getName());

        cargarMisTramitesDelJSON();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        System.out.println("✓ MainController establecido en TramiteStudentController");
    }

    // =========================
    // CARGAR MIS TRAMITES DEL JSON
    // =========================
    private void cargarMisTramitesDelJSON() {
        if (estudiante == null) {
            return;
        }

        misTramites.clear();
        List<Tramite> todosTramites = tramiteData.getAllTramites().toList();

        // Filtrar solo los trámites del estudiante actual
        for (Tramite t : todosTramites) {
            if (t.getEstudiante().getId().equals(estudiante.getId())) {
                t.setDetalles(tramiteDetailsData.getDetailsByTramiteId(t.getId()));
                misTramites.add(t);
            }
        }

        System.out.println("✓ Mis trámites cargados del JSON. Total: " + misTramites.size());
    }

    // =========================
    // US-05: ENVIAR TRAMITE EN ESTADO PENDIENTE
    // =========================
    @FXML
    private void enviarTramite() throws ListException {
        // Validar que el estudiante esté establecido
        if (estudiante == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error",
                    "No hay estudiante en sesión. Por favor, inicia sesión nuevamente.");
            return;
        }

        // Obtener datos del formulario
        String tipo = txtTipo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        // Validar campos no vacíos
        if (tipo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido",
                    "Por favor ingresa el tipo de solicitud.");
            txtTipo.requestFocus();
            return;
        }

        if (descripcion.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido",
                    "Por favor ingresa una descripción detallada.");
            txtDescripcion.requestFocus();
            return;
        }

        // =========================
        // 1. CREAR TRAMITE EN ESTADO PENDIENTE
        // =========================
        Tramite nuevoTramite = new Tramite(tipo, descripcion, estudiante);
        System.out.println("✓ Trámite creado: " + nuevoTramite.getId() + " - Estado: " + nuevoTramite.getNombreEstado());

        // =========================
        // 2. GUARDAR EN JSON (persistencia)
        // =========================
        tramiteData.addTramite(nuevoTramite);
        tramiteDetailsData.saveDetails(nuevoTramite.getDetalles());
        System.out.println("✓ Trámite guardado en JSON");

        // =========================
        // 3. AGREGAR A LISTA LOCAL (tabla del estudiante)
        // =========================
        misTramites.add(nuevoTramite);
        System.out.println("✓ Trámite agregado a la lista local del estudiante");

        // =========================
        // 4. AGREGAR A PILA DEL SISTEMA (para que lo vea el admin)
        // =========================
        if (mainController != null) {
            TramiteReviewController tramiteReviewController = mainController.getTramiteReviewController();
            if (tramiteReviewController != null) {
                tramiteReviewController.pushTramite(nuevoTramite);
                System.out.println("✓ Trámite agregado a la pila del sistema");
            } else {
                System.out.println("⚠️ Advertencia: TramiteReviewController no está disponible");
            }
        }

        // =========================
        // 5. NOTIFICAR AL ESTUDIANTE (Patrón Observer)
        // =========================

        NotificationService.getInstance().notifyObservers(
                estudiante.getId(),
                "✓ SOLICITUD ENVIADA\n"
                        + "Tu trámite '" + tipo
                        + "' fue enviado correctamente.\n"
                        + "ID: " + nuevoTramite.getId()
        );


        // =========================
        // 6. MOSTRAR CONFIRMACIÓN
        // =========================
        mostrarAlerta(Alert.AlertType.INFORMATION, "✓ Solicitud Enviada",
                "Tu solicitud de trámite ha sido enviada correctamente.\n\n" +
                        "Tipo: " + tipo + "\n" +
                        "Estado: PENDIENTE\n" +
                        "ID: " + nuevoTramite.getId() + "\n\n" +
                        "Podrás ver el progreso en la tabla de abajo."
        );

        // =========================
        // 7. LIMPIAR FORMULARIO
        // =========================
        limpiarCampos();
    }

    // =========================
    // LIMPIAR CAMPOS
    // =========================
    @FXML
    private void limpiarCampos() {
        txtTipo.clear();
        txtDescripcion.clear();
        txtTipo.requestFocus();
        System.out.println("✓ Campos limpiados");
    }

    private void abrirDetalles(Tramite tramite) {
        if (tramite == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/tramite-details-view.fxml"));
            Parent root = loader.load();

            TramiteDetailsController controller = loader.getController();
            controller.setContext(tramite, true);

            Stage stage = new Stage();
            stage.setTitle("Detalles del Trámite");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            if (tblMisTramites != null && tblMisTramites.getScene() != null) {
                stage.initOwner(tblMisTramites.getScene().getWindow());
            }
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron abrir los detalles del trámite.");
        }
    }

    // =========================
    // NOTIFICAR AL ESTUDIANTE (Observer Pattern)
    // =========================
//    private void notificarEstudiante(String titulo, String mensaje) {
//        try {
//            NotificationService notificationService = NotificationService.getInstance();
//
//            // Crear observer para este estudiante
//            StudentNotification observer = new StudentNotification(estudiante.getEmail());
//
//            // Agregar observer
//            notificationService.addObserver(observer);
//            System.out.println("✓ Observer agregado para: " + estudiante.getEmail());
//
//            // Notificar
//            notificationService.notifyObservers(mensaje);
//            System.out.println("✓ Notificación enviada: " + titulo);
//
//            // Remover observer (para no duplicar notificaciones)
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
    // GETTER DE MIS TRAMITES
    // =========================
    public ObservableList<Tramite> getMisTramites() {
        return misTramites;
    }
}