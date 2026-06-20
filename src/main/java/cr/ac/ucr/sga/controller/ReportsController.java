package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.entities.ReportRow;
import cr.ac.ucr.sga.model.services.ReportService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ReportsController implements Initializable {

    @FXML
    private TableColumn<ReportRow, String> colCurso;

    @FXML
    private TableColumn<ReportRow, String> colProfesor;

    @FXML
    private TableColumn<ReportRow, String> colCarrera;

    @FXML
    private TableColumn<ReportRow, Integer> colEstudiantes;

    @FXML
    private TableColumn<ReportRow, Double> colPromedio;

    @FXML
    private TableColumn<ReportRow, Integer> colAprobados;

    @FXML
    private TableColumn<ReportRow, Integer> colReprobados;

    @FXML
    private TableView<ReportRow> tblReportes;

    @FXML
    private ComboBox<String> cbPeriodo;

    @FXML
    private ComboBox<String> cbCarrera;

    @FXML
    private ComboBox<String> cbCurso;

    @FXML
    private ComboBox<String> cbProfesor;

    @FXML
    private Label lblEstudiantes;

    @FXML
    private Label lblCursos;

    @FXML
    private Label lblMatriculas;

    @FXML
    private Label lblPromedio;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnGenerar;

    @FXML
    private Button btnDescargar;

    @FXML
    private RadioButton rbPDF= new RadioButton();

    @FXML
    private RadioButton rbExcel= new RadioButton();;

    @FXML
    private RadioButton rbCSV= new RadioButton();;

    @FXML
    private ToggleGroup formatoGroup = new ToggleGroup();

    private MainController mainController;
    
    private final ReportService reportService = new ReportService();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        configurarTabla();
        cargarCombos();
        inicializarMetricas();

        tblReportes.setItems(reportService.getObservableRows());

        loadMetrics();

        btnBuscar.setOnAction(e -> buscar());
        btnGenerar.setOnAction(e -> generarReporte());
        btnDescargar.setOnAction(e -> descargarReporte());
        rbPDF.setToggleGroup(formatoGroup);
        rbExcel.setToggleGroup(formatoGroup);
        rbCSV.setToggleGroup(formatoGroup);


        rbPDF.setSelected(true); // opción por defecto

    }
    private void loadMetrics(){

        ReportService.ReportMetrics m =
                reportService.getMetrics();

        lblEstudiantes.setText(
                String.valueOf(m.totalStudents));

        lblCursos.setText(
                String.valueOf(m.totalCourses));

        lblMatriculas.setText(
                String.valueOf(m.totalEnrollments));

        lblPromedio.setText(
                String.format("%.2f",m.average));

    }

    private String getFormatoSeleccionado() {
        Toggle toggle = formatoGroup.getSelectedToggle();

            if (toggle == null)
                return null;


            return String.valueOf(toggle.getUserData());
        }

    // SET MAIN CONTROLLER
    // =========================
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    // =========================
    // CONFIG TABLE
    // =========================
    private void configurarTabla() {

        colCurso.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCurso()));
        colProfesor.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProfesor()));
        colCarrera.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCarrera()));

        colEstudiantes.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEstudiantes()));
        colPromedio.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPromedio()));
        colAprobados.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAprobados()));
        colReprobados.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getReprobados()));
    }

    // =========================
    // COMBOS
    // =========================
    private void cargarCombos() {

        cbPeriodo.setItems(FXCollections.observableArrayList("2025-I", "2025-II", "2026-I"));

        cbCarrera.setItems(FXCollections.observableArrayList(
                "Todas",
                "Informática Empresarial",
                "Computación",
                "Ingeniería de Software"
        ));

        cbCurso.setItems(FXCollections.observableArrayList(
                "Todos",
                "Programación I",
                "Programación II",
                "Estructuras de Datos",
                "Bases de Datos"
        ));

        cbProfesor.setItems(FXCollections.observableArrayList(
                "Todos",
                "Juan Pérez",
                "María López",
                "Carlos Rojas"
        ));

        cbCarrera.getSelectionModel().selectFirst();
        cbCurso.getSelectionModel().selectFirst();
        cbProfesor.getSelectionModel().selectFirst();
    }

    // =========================
    // METRICAS
    // =========================
    private void inicializarMetricas() {
        lblEstudiantes.setText("0");
        lblCursos.setText("0");
        lblMatriculas.setText("0");
        lblPromedio.setText("0.0");
    }

    private void actualizarMetricas(List<ReportRow> data) {

        int estudiantes = data.stream().mapToInt(ReportRow::getEstudiantes).sum();
        int cursos = (int) data.stream().map(ReportRow::getCurso).distinct().count();
        int matriculas = data.size();

        double promedio = data.stream()
                .mapToDouble(ReportRow::getPromedio)
                .average()
                .orElse(0.0);

        lblEstudiantes.setText(String.valueOf(estudiantes));
        lblCursos.setText(String.valueOf(cursos));
        lblMatriculas.setText(String.valueOf(matriculas));
        lblPromedio.setText(String.format("%.2f", promedio));
    }

    // =========================
    // BUSCAR
    // =========================
    @FXML
    private void buscar(){

        ObservableList<ReportRow> rows=

                reportService.getObservableRows();

        String carrera=cbCarrera.getValue();

        String curso=cbCurso.getValue();

        String profesor=cbProfesor.getValue();

        rows.removeIf(r->

                (!carrera.equals("Todas")
                        &&!r.getCarrera().equals(carrera))

                        ||

                        (!curso.equals("Todos")
                                &&!r.getCurso().equals(curso))

                        ||

                        (!profesor.equals("Todos")
                                &&!r.getProfesor().equals(profesor))

        );

        tblReportes.setItems(rows);

    }

    // =========================
    // GENERAR
    // =========================
    @FXML
    private void generarReporte(){

        String formato=getFormatoSeleccionado();

        if(formato==null){

            mostrarMensaje(
                    "Formato",
                    "Seleccione un formato.");

            return;

        }

        FileChooser chooser=
                new FileChooser();

        chooser.setTitle("Guardar reporte");

        switch(formato){

            case "PDF":

                chooser.getExtensionFilters().add(

                        new FileChooser.ExtensionFilter(

                                "PDF",

                                "*.pdf"

                        )

                );

                break;

            case "Excel":

                chooser.getExtensionFilters().add(

                        new FileChooser.ExtensionFilter(

                                "Excel",

                                "*.xlsx"

                        )

                );

                break;

            case "CSV":

                chooser.getExtensionFilters().add(

                        new FileChooser.ExtensionFilter(

                                "CSV",

                                "*.csv"

                        )

                );

                break;

        }

        File file=

                chooser.showSaveDialog(

                        btnGenerar

                                .getScene()

                                .getWindow()

                );

        if(file==null)

            return;

        reportService.exportReport(

                formato,

                file

        );

        mostrarMensaje(

                "Éxito",

                "Reporte generado correctamente."

        );

    }

    // =========================
    // DESCARGAR
    // =========================
    @FXML
    private void descargarReporte() {

        generarReporte();

    }



    // =========================
    // ALERTA
    // =========================
    private void mostrarMensaje(String titulo, String mensaje) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}



