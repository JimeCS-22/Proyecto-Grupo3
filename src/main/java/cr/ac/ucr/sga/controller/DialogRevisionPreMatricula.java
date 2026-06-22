package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.EnrollmentApprovedData;
import cr.ac.ucr.sga.model.data.EnrollmentRequestData;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.Enrollment;
import cr.ac.ucr.sga.model.entities.EnrollmentRequest;
import cr.ac.ucr.sga.model.entities.MatriculaAprobada;
import cr.ac.ucr.sga.model.structures.lists.LinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import cr.ac.ucr.sga.model.services.NotificationService;
import java.util.UUID;

public class DialogRevisionPreMatricula extends Dialog<Void> {
    private final EnrollmentRequest request;
    private final EnrollmentRequestData requestData;
    private final EnrollmentApprovedData matriculaData;
    private final ObservableList<Course> cursosEnSolicitud = FXCollections.observableArrayList();
    private TableView<Course> tblCursos;

    public DialogRevisionPreMatricula(EnrollmentRequest req, EnrollmentRequestData reqData) {
        this.request = req;
        this.requestData = reqData;
        this.matriculaData = new EnrollmentApprovedData();
        initializeDialog();
    }

    private void initializeDialog() {
        this.setTitle("Revisar Pre-Matrícula");
        this.setResizable(true);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(0));

        HBox header = new HBox();
        header.setStyle("-fx-background-color: -fx-header-bg, linear-gradient(#ffffff44, #ffffff22); -fx-padding: 18 24;");
        header.setAlignment(Pos.CENTER_LEFT);
        Label badge = new Label("SGA");
        badge.getStyleClass().add("header-badge");
        badge.setStyle("-fx-background-color: #2DBE8D; -fx-text-fill: white; -fx-padding: 6 10; -fx-font-weight: bold; -fx-border-radius: 4; -fx-background-radius: 4;");
        VBox headerTexts = new VBox(2);
        Label title = new Label("Revisión de Pre-Matrícula");
        title.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        Label subtitle = new Label("Revisa y modifica la solicitud del estudiante");
        subtitle.setStyle("-fx-font-size: 11; -fx-text-fill: -fx-secondary-text;");
        headerTexts.getChildren().addAll(title, subtitle);
        header.getChildren().addAll(badge, headerTexts);
        HBox.setMargin(headerTexts, new Insets(0, 0, 0, 12));
        root.setTop(header);

        VBox content = new VBox(14);
        content.setPadding(new Insets(18));
        content.setStyle("-fx-background-color: -fx-card-bg;");

        Label lblStudent = new Label("Estudiante: " + (request.getStudent() != null ? request.getStudent().getName() + " (" + request.getStudent().getCarnet() + ")" : "N/A"));
        lblStudent.setStyle("-fx-font-size: 13; -fx-font-weight: bold;");
        content.getChildren().add(lblStudent);

        VBox card = new VBox(10);
        card.setPadding(new Insets(12));
        card.setStyle("-fx-background-color: white; -fx-border-color: rgba(0,0,0,0.06); -fx-border-radius: 6; -fx-background-radius: 6;");
        Label lblCursos = new Label("Cursos Solicitados");
        lblCursos.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        card.getChildren().add(lblCursos);

        tblCursos = new TableView<>();
        tblCursos.setPrefHeight(320);

        TableColumn<Course, String> colCode = new TableColumn<>("Código");
        colCode.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCode.setPrefWidth(100);

        TableColumn<Course, String> colName = new TableColumn<>("Nombre");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setPrefWidth(360);

        TableColumn<Course, Integer> colCredits = new TableColumn<>("Créditos");
        colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));
        colCredits.setPrefWidth(80);

        TableColumn<Course, Course> colDelete = new TableColumn<>("Acción");
        colDelete.setPrefWidth(110);
        colDelete.setCellFactory(param -> new TableCell<Course, Course>() {
            private final Button btnEliminar = new Button("Eliminar");
            {
                btnEliminar.setStyle("-fx-background-color: #E85D75; -fx-text-fill: white; -fx-padding: 6 12; -fx-font-size: 12;");
                btnEliminar.setOnAction(event -> {
                    Course course = getTableView().getItems().get(getIndex());
                    cursosEnSolicitud.remove(course);
                });
            }
            @Override
            protected void updateItem(Course c, boolean empty) {
                super.updateItem(c, empty);
                setGraphic(empty ? null : btnEliminar);
            }
        });

        tblCursos.getColumns().addAll(colCode, colName, colCredits, colDelete);
        tblCursos.setItems(cursosEnSolicitud);
        VBox.setVgrow(tblCursos, Priority.ALWAYS);
        card.getChildren().add(tblCursos);
        content.getChildren().add(card);

        HBox actions = new HBox(12);
        actions.setAlignment(Pos.CENTER_RIGHT);

        Button btnAceptar = new Button("Aceptar Pre-Matrícula");
        btnAceptar.setStyle("-fx-background-color: #2DBE8D; -fx-text-fill: white; -fx-padding: 10 24; -fx-font-size: 13;");
        btnAceptar.setOnAction(e -> {
            try {
                aceptarPreMatricula();
            } catch (ListException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }
        });

        Button btnRechazar = new Button("Rechazar Pre-Matrícula");
        btnRechazar.setStyle("-fx-background-color: #E85D75; -fx-text-fill: white; -fx-padding: 10 24; -fx-font-size: 13;");
        btnRechazar.setOnAction(e -> rechazarPreMatricula());

        actions.getChildren().addAll(btnRechazar, btnAceptar);
        content.getChildren().add(actions);

        root.setCenter(content);

        cargarCursosEnTabla();
        this.getDialogPane().setContent(root);
        this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        try {
            this.getDialogPane().getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        } catch (Exception ignored) {}
    }

    private void cargarCursosEnTabla() {
        try {
            cursosEnSolicitud.clear();
            if (request.getCourses() != null && request.getCourses().size() > 0) {
                for (int i = 1; i <= request.getCourses().size(); i++) {
                    cursosEnSolicitud.add(request.getCourses().get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void aceptarPreMatricula() throws ListException {
        LinkedList<Course> cursosFinales = new LinkedList<>();
        for (Course c : cursosEnSolicitud) {
            cursosFinales.add(c);
        }
        request.setCourses(cursosFinales);
        request.setStatus("APPROVED");
        requestData.updateStatus(request, "APPROVED");

        LinkedList<Enrollment> enrollments = new LinkedList<>();
        for (int i = 1; i <= cursosFinales.size(); i++) {
            Course c = cursosFinales.get(i);
            Enrollment e = new Enrollment();
            e.setStudentId(request.getStudent().getId());
            e.setCourseId(c.getId());
            e.setStatus("APPROVED");
            enrollments.add(e);
        }

        MatriculaAprobada matricula = new MatriculaAprobada(UUID.randomUUID().toString(), request.getStudent(), enrollments);
        matriculaData.addOrUpdate(matricula);

        NotificationService.getInstance().notifyObservers(
                request.getStudent().getId(),
                "✓ PRE-MATRÍCULA APROBADA\nTu solicitud fue aprobada correctamente."
        );

        showAlert(Alert.AlertType.INFORMATION, "Éxito", "Pre-matrícula aceptada correctamente");
        this.close();
    }

    private void rechazarPreMatricula() {
        requestData.deleteRequest(request);
        NotificationService.getInstance().notifyObservers(
                request.getStudent().getId(),
                "✗ PRE-MATRÍCULA RECHAZADA\nTu solicitud fue rechazada."
        );
        showAlert(Alert.AlertType.INFORMATION, "Rechazado", "Pre-matrícula rechazada y eliminada");
        this.close();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}