package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.EnrollmentRequestData;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.EnrollmentRequest;
import cr.ac.ucr.sga.model.entities.Student;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class RequestReviewController implements Initializable {

    @FXML private TableView<EnrollmentRequest> tblRequests;
    @FXML private TableColumn<EnrollmentRequest, String> colStudent;
    @FXML private TableColumn<EnrollmentRequest, String> colCourses;
    @FXML private TableColumn<EnrollmentRequest, String> colPriority;
    @FXML private TableColumn<EnrollmentRequest, String> colStatus;
    @FXML private TableColumn<EnrollmentRequest, EnrollmentRequest> colActions;

    private final EnrollmentRequestData requestData = new EnrollmentRequestData();
    private final ObservableList<EnrollmentRequest> requests = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        loadPendingRequests();
    }

    private void initializeTable() {
        colStudent.setCellValueFactory(data -> {
            Student student = data.getValue().getStudent();
            return new SimpleStringProperty(student != null ? student.getName() + " (" + student.getCarnet() + ")" : "");
        });

        colCourses.setCellValueFactory(data -> new SimpleStringProperty(buildCoursesText(data.getValue())));
        colPriority.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getPriority())));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(translateStatus(data.getValue().getStatus())));

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnRevisar = new Button("Revisar");
            private final HBox panel = new HBox(15, btnRevisar);
            {
                btnRevisar.setMinWidth(120);
                btnRevisar.setStyle("-fx-background-color: #2DBE8D; -fx-text-fill: white;");
                btnRevisar.setOnAction(event -> {
                    EnrollmentRequest req = getTableView().getItems().get(getIndex());
                    new DialogRevisionPreMatricula(req, requestData).showAndWait();
                    loadPendingRequests();
                });
            }
            @Override
            protected void updateItem(EnrollmentRequest req, boolean empty) {
                super.updateItem(req, empty);
                setGraphic(empty ? null : panel);
            }
        });
        colActions.setPrefWidth(150);
        tblRequests.setItems(requests);
    }

    private String buildCoursesText(EnrollmentRequest req) {
        try {
            if (req.getCourses() == null || req.getCourses().size() == 0) return "Ninguno";
            StringBuilder names = new StringBuilder();
            for (int i = 1; i <= req.getCourses().size(); i++) {
                Course course = req.getCourses().get(i);
                if (course != null) {
                    names.append(course.getName()).append(i < req.getCourses().size() ? ", " : "");
                }
            }
            return names.toString();
        } catch (ListException e) { return "Error"; }
    }

    private String translateStatus(String status) {
        if (status == null) return "";
        if (status.equalsIgnoreCase("PENDING") || status.equalsIgnoreCase("PENDIENTE")) return "PENDIENTE";
        if (status.equalsIgnoreCase("APPROVED") || status.equalsIgnoreCase("APROBADO")) return "APROBADO";
        if (status.equalsIgnoreCase("REJECTED") || status.equalsIgnoreCase("RECHAZADO")) return "RECHAZADO";
        return status;
    }

    private void loadPendingRequests() {
        requests.clear();
        for (EnrollmentRequest req : requestData.getRequests()) {
            String status = req.getStatus() == null ? "" : req.getStatus().toUpperCase();
            if (status.equals("PENDING") || status.equals("PENDIENTE")) {
                requests.add(req);
            }
        }
        tblRequests.refresh();
    }
}