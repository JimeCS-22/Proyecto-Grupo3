package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.entities.*;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

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

    private final ObservableList<Course> courseList =
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
}