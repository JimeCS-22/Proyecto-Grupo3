package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.entities.*;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;

public class RecordController implements Initializable {

    private User currentUser;

    @FXML
    private Label lblStudentInfo;

    @FXML
    private Label lblAvg;

    @FXML
    private Label lblAvgDesc;

    @FXML
    private Label lblCredits;

    @FXML
    private Label lblCreditosDesc;

    @FXML
    private Label lblCount;

    @FXML
    private Label lblCiclo;

    @FXML
    private ComboBox<Student> cmbStudents;

    @FXML
    private TableView<Course> tblCourses;

    @FXML
    private TableColumn<Course, String> colCode;

    @FXML
    private TableColumn<Course, String> colName;

    @FXML
    private TableColumn<Course, Integer> colCredits;

    @FXML
    private TableColumn<Course, Double> colGrade;

    @FXML
    private TableColumn<Course, String> colStatus;

    @FXML
    private TextField txtSearch;

    @FXML
    private ComboBox<String> cmbCycle;

    @FXML
    private ComboBox<String> cmbStatusFilter;

    private final ObservableList<Course> courseList =
            FXCollections.observableArrayList();

    private final ObservableList<Course> originalCourseList =
            FXCollections.observableArrayList();

    private final AcademicRecordData recordData =
            new AcademicRecordData();

    @Override
    public void initialize(
            URL url,
            ResourceBundle resourceBundle
    ) {

        colCode.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        colName.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        colCredits.setCellValueFactory(
                new PropertyValueFactory<>("credits")
        );

        colGrade.setCellValueFactory(
                new PropertyValueFactory<>("grade")
        );

        colStatus.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );

        tblCourses.setItems(courseList);

        cmbCycle.getItems().addAll(
                "Ciclo I - 2026",
                "Ciclo II - 2026"
        );

        cmbStatusFilter.getItems().addAll(
                "Activo",
                "Inactivo",
                "Aprobado",
                "Reprobado"
        );
    }

    // =========================
    // USER
    // =========================

    public void setUser(User user) {

        this.currentUser = user;

        try {

            // =========================
            // ADMIN
            // =========================

            if (user.getRole() == Role.ADMIN) {

                cmbStudents.setVisible(true);
                cmbStudents.setManaged(true);

                cmbStudents.getItems().clear();

                cmbStudents.getItems().addAll(
                        recordData.getAllStudentsFromRecords()
                );

                lblStudentInfo.setText(
                        "Administrador"
                );
            }

            // =========================
            // STUDENT
            // =========================

            else if (user.getRole() == Role.STUDENT) {

                cmbStudents.setVisible(false);
                cmbStudents.setManaged(false);

                System.out.println(
                        "LOGIN USER: "
                                + user.getUsername()
                );

                AcademicRecord record =
                        recordData.findByUsername(
                                user.getUsername()
                        );

                if (record != null) {

                    System.out.println(
                            "Expediente encontrado para: "
                                    + record.getStudent().getName()
                    );

                    cargarExpediente(record);

                } else {

                    System.out.println(
                            "NO se encontró expediente"
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =========================
    // COMBO EVENT
    // =========================

    @FXML
    private void onStudentSelected() {

        Student selected =
                cmbStudents.getSelectionModel()
                        .getSelectedItem();

        if (selected != null) {

            try {

                AcademicRecord record =
                        recordData.findByStudentId(
                                selected.getId()
                        );

                if (record != null) {

                    cargarExpediente(record);
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    // =========================
    // LOAD RECORD
    // =========================

    private void cargarExpediente(
            AcademicRecord record
    ) throws ListException {

        Student student =
                record.getStudent();

        lblStudentInfo.setText(
                "Estudiante: "
                        + student.getName()
        );

        courseList.clear();

        originalCourseList.clear();

        DoublyLinkedList<Course> cursos =
                record.getCourses();

        System.out.println(
                "Cantidad cursos: "
                        + cursos.size()
        );

        double sumGrades = 0;

        int countGrades = 0;

        int sumaCreditos = 0;

        if (!cursos.isEmpty()) {

            for (
                    int i = 1;
                    i <= cursos.size();
                    i++
            ) {

                Course c = cursos.get(i);

                System.out.println(
                        "Curso cargado: "
                                + c.getName()
                );

                courseList.add(c);

                originalCourseList.add(c);

                sumGrades += c.getGrade();

                countGrades++;

                sumaCreditos += c.getCredits();
            }
        }

        double avg =
                countGrades > 0
                        ? sumGrades / countGrades
                        : 0;

        lblAvg.setText(
                String.format("%.1f", avg)
        );

        lblAvgDesc.setText(
                avg >= 70
                        ? "Excelente rendimiento"
                        : "Bajo rendimiento"
        );

        lblCredits.setText(
                String.valueOf(sumaCreditos)
        );

        lblCreditosDesc.setText(
                "Créditos aprobados"
        );

        lblCount.setText(
                String.valueOf(courseList.size())
        );

        lblCiclo.setText(
                "Ciclo I - 2026"
        );
    }

    @FXML
    private void applyFilters() {

        String search =
                txtSearch.getText()
                        .toLowerCase()
                        .trim();

        String cycle =
                cmbCycle.getValue();

        String status =
                cmbStatusFilter.getValue();

        courseList.clear();

        for (Course c : originalCourseList) {

            boolean matchesSearch = true;

            boolean matchesCycle = true;

            boolean matchesStatus = true;

            // =========================
            // SEARCH
            // =========================

            if (!search.isEmpty()) {

                matchesSearch =
                        c.getName()
                                .toLowerCase()
                                .contains(search)
                                ||
                                c.getId()
                                        .toLowerCase()
                                        .contains(search);
            }

            // =========================
            // STATUS
            // =========================

            if (status != null) {

                matchesStatus =
                        c.getStatus()
                                .equalsIgnoreCase(status);
            }

            // =========================
            // CYCLE
            // =========================
        /*
         Como Course no tiene ciclo,
         simulamos todos en Ciclo I - 2026
        */

            if (cycle != null) {

                matchesCycle =
                        cycle.equals("Ciclo I - 2026");
            }

            // =========================
            // ADD
            // =========================

            if (
                    matchesSearch
                            &&
                            matchesCycle
                            &&
                            matchesStatus
            ) {

                courseList.add(c);
            }
        }

        lblCount.setText(
                String.valueOf(courseList.size())
        );
    }

    // =========================
// EXPORT PDF
// =========================

    @FXML
    private void exportPDF() {

        try {

            FileChooser fileChooser =
                    new FileChooser();

            fileChooser.setTitle(
                    "Guardar Expediente PDF"
            );

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "PDF Files",
                            "*.pdf"
                    )
            );

            File file =
                    fileChooser.showSaveDialog(
                            tblCourses.getScene()
                                    .getWindow()
                    );

            if (file == null) {

                return;
            }

            Document document =
                    new Document();

            PdfWriter.getInstance(
                    document,
                    new FileOutputStream(file)
            );

            document.open();

            // =========================
            // TITULO
            // =========================

            document.add(
                    new Paragraph(
                            "EXPEDIENTE ACADÉMICO"
                    )
            );

            document.add(
                    new Paragraph(" ")
            );

            document.add(
                    new Paragraph(
                            lblStudentInfo.getText()
                    )
            );

            document.add(
                    new Paragraph(
                            "Promedio: "
                                    + lblAvg.getText()
                    )
            );

            document.add(
                    new Paragraph(
                            "Créditos: "
                                    + lblCredits.getText()
                    )
            );

            document.add(
                    new Paragraph(
                            "Cursos matriculados: "
                                    + lblCount.getText()
                    )
            );

            document.add(
                    new Paragraph(" ")
            );

            // =========================
            // CURSOS
            // =========================

            document.add(
                    new Paragraph(
                            "LISTA DE CURSOS"
                    )
            );

            document.add(
                    new Paragraph(" ")
            );

            for (Course c : courseList) {

                document.add(
                        new Paragraph(
                                "Código: "
                                        + c.getId()
                        )
                );

                document.add(
                        new Paragraph(
                                "Curso: "
                                        + c.getName()
                        )
                );

                document.add(
                        new Paragraph(
                                "Créditos: "
                                        + c.getCredits()
                        )
                );

                document.add(
                        new Paragraph(
                                "Nota: "
                                        + c.getGrade()
                        )
                );

                document.add(
                        new Paragraph(
                                "Estado: "
                                        + c.getStatus()
                        )
                );

                document.add(
                        new Paragraph(
                                "-------------------------"
                        )
                );
            }

            document.close();

            Alert alert =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            alert.setTitle("PDF");

            alert.setHeaderText(null);

            alert.setContentText(
                    "PDF exportado correctamente"
            );

            alert.showAndWait();

        } catch (Exception e) {

            e.printStackTrace();

            Alert alert =
                    new Alert(
                            Alert.AlertType.ERROR
                    );

            alert.setTitle("Error");

            alert.setHeaderText(null);

            alert.setContentText(
                    "No se pudo exportar el PDF"
            );

            alert.showAndWait();
        }
    }

    public void reloadStudents() {
        if (cmbStudents.isVisible()) {
            cmbStudents.getItems().clear();
            cmbStudents.getItems().addAll(recordData.getAllStudentsFromRecords());
        }
    }
}