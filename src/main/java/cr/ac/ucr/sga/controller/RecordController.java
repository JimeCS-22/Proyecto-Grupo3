package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.data.StudentData;
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

    private final StudentData studentData =
            new StudentData();

    @Override
    public void initialize(
            URL url,
            ResourceBundle resourceBundle
    ) {

        colCode.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        colName.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        colCredits.setCellValueFactory(
                new PropertyValueFactory<>("credits"));

        colGrade.setCellValueFactory(
                new PropertyValueFactory<>("grade"));

        colStatus.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        tblCourses.setItems(courseList);
    }

    // =========================
    // USER
    // =========================

    public void setUser(User user)
            throws ListException {

        this.currentUser = user;

        // ADMIN
        if (user.getRole() == Role.ADMIN) {

            cmbStudents.setVisible(true);

            cmbStudents.getItems().addAll(
                    studentData.getAllStudents()
            );

            lblStudentInfo.setText(
                    "Administrador"
            );

        }

        // STUDENT
        else if (user.getRole() == Role.STUDENT) {

            cmbStudents.setVisible(false);

            Student student =
                    studentData.findByUsername(
                            user.getUsername()
                    );

            if (student != null) {

                cargarExpediente(student);
            }
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

                cargarExpediente(selected);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    // =========================
    // LOAD RECORD
    // =========================

    private void cargarExpediente(Student student)
            throws ListException {

        lblStudentInfo.setText(
                "Estudiante: "
                        + student.getName()
        );

        AcademicRecordData recordData =
                new AcademicRecordData();

        AcademicRecord record =
                recordData.findByStudentId(
                        student.getId()
                );

        if (record != null) {

            courseList.clear();

            DoublyLinkedList<Course> cursos =
                    record.getCourses();

            double sumGrades = 0;

            int countGrades = 0;

            int sumaCreditos = 0;

            for (
                    int i = 1;
                    i <= cursos.size();
                    i++
            ) {

                Course c = cursos.get(i);

                courseList.add(c);

                sumGrades += c.getGrade();

                countGrades++;

                sumaCreditos += c.getCredits();
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

        } else {

            courseList.clear();

            lblAvg.setText("0");

            lblAvgDesc.setText("");

            lblCredits.setText("0");

            lblCreditosDesc.setText("");

            lblCount.setText("0");

            lblCiclo.setText("");

            lblStudentInfo.setText(
                    "Sin expediente"
            );
        }
    }
}