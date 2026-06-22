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

    @FXML private TextField txtTipo;
    @FXML private TextArea txtDescripcion;
    @FXML private Button btnEnviar;
    @FXML private Button btnLimpiar;
    @FXML private TableView<Tramite> tblMisTramites;
    @FXML private TableColumn<Tramite, String> colTipo;
    @FXML private TableColumn<Tramite, String> colDescripcion;
    @FXML private TableColumn<Tramite, String> colEstado;
    @FXML private TableColumn<Tramite, String> colFecha;
    @FXML private TableColumn<Tramite, Tramite> colAcciones;

    private Student estudiante;
    private MainController mainController;
    private final ObservableList<Tramite> misTramites = FXCollections.observableArrayList();
    private final TramiteData tramiteData = new TramiteData();
    private final TramiteDetailsData tramiteDetailsData = new TramiteDetailsData();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        btnEnviar.setOnAction(e -> {
            try {
                enviarTramite();
            } catch (ListException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnLimpiar.setOnAction(e -> limpiarCampos());
        tblMisTramites.setItems(misTramites);
    }

    private void setupTableColumns() {
        colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipo()));
        colDescripcion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreEstado()));
        colFecha.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFechaEnvio()));

        if (colAcciones != null) {
            colAcciones.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue()));
            colAcciones.setCellFactory(param -> new TableCell<>() {
                private final Button btnDetalles = new Button("Ver Detalles");
                private final HBox panel = new HBox(10, btnDetalles);
                {
                    btnDetalles.setOnAction(event -> abrirDetalles(getTableView().getItems().get(getIndex())));
                }
                @Override
                protected void updateItem(Tramite item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : panel);
                }
            });
        }
    }

    public void setEstudiante(Student estudiante) {
        this.estudiante = estudiante;
        cargarMisTramitesDelJSON();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void cargarMisTramitesDelJSON() {
        if (estudiante == null) return;
        misTramites.clear();
        List<Tramite> todosTramites = tramiteData.getAllTramites().toList();
        for (Tramite t : todosTramites) {
            if (t.getEstudiante().getId().equals(estudiante.getId())) {
                t.setDetalles(tramiteDetailsData.getDetailsByTramiteId(t.getId()));
                misTramites.add(t);
            }
        }
    }

    @FXML
    private void enviarTramite() throws ListException {
        if (estudiante == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No hay estudiante en sesión.");
            return;
        }
        String tipo = txtTipo.getText().trim();
        String descripcion = txtDescripcion.getText().trim();

        if (tipo.isEmpty() || descripcion.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido", "Todos los campos son obligatorios.");
            return;
        }

        Tramite nuevoTramite = new Tramite(tipo, descripcion, estudiante);
        tramiteData.addTramite(nuevoTramite);
        tramiteDetailsData.saveDetails(nuevoTramite.getDetalles());
        misTramites.add(nuevoTramite);

        if (mainController != null) {
            TramiteReviewController tramiteReviewController = mainController.getTramiteReviewController();
            if (tramiteReviewController != null) {
                tramiteReviewController.pushTramite(nuevoTramite);
            }
        }

        NotificationService.getInstance().notifyObservers(estudiante.getId(),
                "SOLICITUD ENVIADA: " + tipo + " (ID: " + nuevoTramite.getId() + ")");

        mostrarAlerta(Alert.AlertType.INFORMATION, "Solicitud Enviada", "La solicitud fue enviada correctamente.");
        limpiarCampos();
    }

    @FXML
    private void limpiarCampos() {
        txtTipo.clear();
        txtDescripcion.clear();
        txtTipo.requestFocus();
    }

    private void abrirDetalles(Tramite tramite) {
        if (tramite == null) return;
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
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudieron abrir los detalles.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public ObservableList<Tramite> getMisTramites() {
        return misTramites;
    }
}