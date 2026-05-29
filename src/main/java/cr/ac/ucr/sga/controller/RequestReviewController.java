package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.EnrollmentRequestData;
import cr.ac.ucr.sga.model.entities.EnrollmentRequest;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.Student;

import cr.ac.ucr.sga.model.structures.lists.ListException;
import cr.ac.ucr.sga.model.structures.queues.PriorityLinkedQueue;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class RequestReviewController implements Initializable {

    @FXML
    private TableView<EnrollmentRequest> tblRequests;

    @FXML
    private TableColumn<EnrollmentRequest, String> colStudent;

    @FXML
    private TableColumn<EnrollmentRequest, String> colCourses;

    @FXML
    private TableColumn<EnrollmentRequest, String> colPriority;

    @FXML
    private TableColumn<EnrollmentRequest, String> colStatus;

    @FXML
    private TableColumn<EnrollmentRequest, EnrollmentRequest> colActions;

    private final EnrollmentRequestData requestData = new EnrollmentRequestData();

    private final ObservableList<EnrollmentRequest> requests =
            FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        loadPendingRequests();
    }

    private void initializeTable() {
        // Estudiante (nombre + carnet)
        colStudent.setCellValueFactory(data -> {
            Student s = data.getValue().getStudent();
            String txt = (s != null) ? s.getName() + " (" + s.getCarnet() + ")" : "";
            return new SimpleStringProperty(txt);
        });

        // Cursos solicitados (soporta tus dos tipos de lista)
        colCourses.setCellValueFactory(data -> {
            EnrollmentRequest req = data.getValue();
            try {
                if (req.getCourses() == null || req.getCourses().size() == 0)
                    return new SimpleStringProperty("Ninguno");
            } catch (ListException e) {
                throw new RuntimeException(e);
            }

            StringBuilder nombres = new StringBuilder();
            try {
                // Siempre de 1 a size() en TU LinkedList personalizada
                for (int i = 1; i <= req.getCourses().size(); i++) {
                    Course c = req.getCourses().get(i);
                    if (c != null) {
                        nombres.append(c.getName());
                        if (i < req.getCourses().size()) nombres.append(", ");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new SimpleStringProperty("Error");
            }
            return new SimpleStringProperty(nombres.toString());
        });

        // Prioridad en texto
        colPriority.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getPriority()))
        );

        // Estado (cast en español)
        colStatus.setCellValueFactory(data -> {
            String status = data.getValue().getStatus();
            if ("PENDING".equalsIgnoreCase(status) || "PENDIENTE".equalsIgnoreCase(status)) return new SimpleStringProperty("PENDIENTE");
            if ("APPROVED".equalsIgnoreCase(status) || "APROBADO".equalsIgnoreCase(status)) return new SimpleStringProperty("APROBADO");
            if ("REJECTED".equalsIgnoreCase(status) || "RECHAZADO".equalsIgnoreCase(status)) return new SimpleStringProperty("RECHAZADO");
            return new SimpleStringProperty(status);
        });

        // Columna de acciones (aceptar / rechazar), con ancho amplio y HBox espacioso
        colActions.setCellFactory(param -> new TableCell<EnrollmentRequest, EnrollmentRequest>() {
            private final Button btnAprobar = new Button("Aceptar");
            private final Button btnRechazar = new Button("Rechazar");
            // Más espacio entre botones
            private final HBox panel = new HBox(25, btnAprobar, btnRechazar);

            {
                btnAprobar.setMinWidth(90);
                btnRechazar.setMinWidth(90);
                btnAprobar.setMaxWidth(Double.MAX_VALUE);
                btnRechazar.setMaxWidth(Double.MAX_VALUE);

                btnAprobar.setStyle("-fx-background-color: #2DBE8D; -fx-text-fill: white;");
                btnRechazar.setStyle("-fx-background-color: #E85D75; -fx-text-fill: white;");

                btnAprobar.setOnAction(event -> updateEstado("APROBADO"));
                btnRechazar.setOnAction(event -> updateEstado("RECHAZADO"));
            }

            private void updateEstado(String nuevoEstado) {
                EnrollmentRequest req = getTableView().getItems().get(getIndex());
                req.setStatus(nuevoEstado); // cambia el objeto
                requestData.updateStatus(req, nuevoEstado); // guarda en JSON
                showAlert(Alert.AlertType.INFORMATION, "Trámite actualizado",
                        nuevoEstado.equals("APROBADO") ? "¡La solicitud fue aprobada!" : "La solicitud fue rechazada.");
                loadPendingRequests(); // refresca tabla
            }

            @Override
            protected void updateItem(EnrollmentRequest req, boolean empty) {
                super.updateItem(req, empty);
                setGraphic(empty ? null : panel);
            }
        });

        // Que la columna sea más ancha
        colActions.setPrefWidth(210);

        tblRequests.setItems(requests);
    }

    private void loadPendingRequests() {

        requests.clear();

        var all = requestData.getAllRequests();

        try {

            PriorityLinkedQueue<EnrollmentRequest> temp = new PriorityLinkedQueue<>();

            // 1. Recorrer la cola sin romperla
            while (!all.isEmpty()) {

                EnrollmentRequest req = all.deQueue();

                String status = (req.getStatus() == null)
                        ? ""
                        : req.getStatus().toUpperCase();

                if ("PENDING".equals(status) || "PENDIENTE".equals(status)) {
                    requests.add(req);
                }

                temp.enQueue(req, req.getPriority());
            }

            // 2. Restaurar la cola original
            while (!temp.isEmpty()) {
                EnrollmentRequest req = temp.deQueue();
                all.enQueue(req, req.getPriority());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        tblRequests.refresh();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}