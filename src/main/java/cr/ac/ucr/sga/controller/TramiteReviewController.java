package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.entities.Tramite;
import cr.ac.ucr.sga.model.structures.stacks.LinkedStack;
import cr.ac.ucr.sga.model.structures.stacks.StackException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class TramiteReviewController implements Initializable {

    @FXML
    private TableView<Tramite> tblTramites;

    @FXML
    private TableColumn<Tramite, String> colTipo, colDescripcion, colEstado;

    @FXML
    private Button btnProcesar;

    private final LinkedStack<Tramite> pilaTramites = new LinkedStack<>();
    @FXML
    private TextField txtTipo;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private Button btnAgregarTramite;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));
        colDescripcion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescripcion()));
       // colEstado.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));
        loadTramites();
        //admibtnProcesar.setOnAction(e -> procesarTramiteMasReciente());
    }

    public void pushTramite(Tramite t) {
        try {
            pilaTramites.push(t);
            loadTramites();
        } catch (StackException e) {
            e.printStackTrace();
        }
    }

    private void loadTramites() {
        tblTramites.getItems().clear();

        try {
            LinkedStack<Tramite> aux = new LinkedStack<>();
            while (!pilaTramites.isEmpty()) {
                Tramite t = pilaTramites.pop();
                tblTramites.getItems().add(t);
                aux.push(t);
            }

            while (!aux.isEmpty()) pilaTramites.push(aux.pop());
        } catch (StackException ex) {
            ex.printStackTrace();
        }
    }

//    private void procesarTramiteMasReciente() {
//        try {
//            Tramite t = pilaTramites.pop();
//            t.setEstado("Procesando");
//
//            loadTramites();
//            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Trámite " + t.getTipo() + " procesado: " + t.getDescripcion());
//            alert.showAndWait();
//        } catch (StackException ex) {
//            Alert alert = new Alert(Alert.AlertType.WARNING, "No hay trámites para procesar");
//            alert.showAndWait();
//        }
//    }
}