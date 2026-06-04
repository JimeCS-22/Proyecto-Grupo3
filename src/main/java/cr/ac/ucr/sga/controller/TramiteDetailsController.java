package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.TramiteData;
import cr.ac.ucr.sga.model.data.TramiteDetailsData;
import cr.ac.ucr.sga.model.entities.Comentario;
import cr.ac.ucr.sga.model.entities.Tramite;
import cr.ac.ucr.sga.model.entities.TramiteDetails;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TramiteDetailsController implements Initializable {

    @FXML
    private Label lblTramiteInfo;

    @FXML
    private ListView<String> lstComentarios;

    @FXML
    private ComboBox<String> cmbTipoComentario;

    @FXML
    private TextArea txtNuevoComentario;

    @FXML
    private Button btnAgregarComentario;

    private Tramite tramite;
    private boolean readOnly;

    private final TramiteDetailsData tramiteDetailsData = new TramiteDetailsData();
    private final TramiteData tramiteData = new TramiteData();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cmbTipoComentario.setItems(FXCollections.observableArrayList("INFO", "RECORDATORIO", "RESOLUCION"));
        cmbTipoComentario.getSelectionModel().select("INFO");
    }

    public void setContext(Tramite tramite, boolean readOnly) {
        this.tramite = tramite;
        this.readOnly = readOnly;
        applyMode();
        cargarDetalles();
    }

    @FXML
    private void agregarComentario() throws ListException {
        if (tramite == null || readOnly) {
            return;
        }

        String contenido = txtNuevoComentario.getText() != null ? txtNuevoComentario.getText().trim() : "";
        if (contenido.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo requerido", "Debes escribir un comentario.");
            return;
        }

        String tipo = cmbTipoComentario.getSelectionModel().getSelectedItem();
        if (tipo == null || tipo.isEmpty()) {
            tipo = "INFO";
        }

        Comentario comentario = new Comentario("Admin", contenido, LocalDateTime.now(), tipo);

        TramiteDetails details = tramite.getDetalles();
        details.agregarComentario(comentario);
        tramite.setDetalles(details);

        tramiteDetailsData.saveDetails(details);
        tramiteData.updateTramite(tramite);

        txtNuevoComentario.clear();
        cargarComentarios();
    }

    @FXML
    private void volver() {
        if (lblTramiteInfo != null && lblTramiteInfo.getScene() != null) {
            lblTramiteInfo.getScene().getWindow().hide();
        }
    }

    private void cargarDetalles() {
        if (tramite == null) {
            return;
        }

        TramiteDetails details = tramiteDetailsData.getDetailsByTramiteId(tramite.getId());
        tramite.setDetalles(details);

        lblTramiteInfo.setText("Trámite " + tramite.getId() + " - " + tramite.getTipo() + " (" + tramite.getNombreEstado() + ")");
        cargarComentarios();
    }

    private void cargarComentarios() {
        lstComentarios.getItems().clear();

        if (tramite == null || tramite.getDetalles() == null) {
            return;
        }

        for (Comentario comentario : tramite.getDetalles().getComentarios().toList()) {
            String fecha = comentario.getFecha() != null ? comentario.getFecha().format(formatter) : "Sin fecha";
            String tipo = comentario.getTipo() != null ? comentario.getTipo() : "INFO";
            String autor = comentario.getAutor() != null ? comentario.getAutor() : "Sistema";
            String contenido = comentario.getContenido() != null ? comentario.getContenido() : "";
            lstComentarios.getItems().add("[" + tipo + "] " + fecha + " - " + autor + ": " + contenido);
        }
    }

    private void applyMode() {
        boolean editable = !readOnly;
        btnAgregarComentario.setVisible(editable);
        btnAgregarComentario.setManaged(editable);
        txtNuevoComentario.setDisable(!editable);
        cmbTipoComentario.setDisable(!editable);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
