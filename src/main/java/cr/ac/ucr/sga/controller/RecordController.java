package cr.ac.ucr.sga.controller;

import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.Role;
import cr.ac.ucr.sga.model.entities.User;
import cr.ac.ucr.sga.model.structures.lists.DoublyLinkedList;
import cr.ac.ucr.sga.model.structures.lists.ListException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    private final ObservableList<Course> courseList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCode.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tblCourses.setItems(courseList);
    }

    public void setUser(User user) throws ListException {
        this.currentUser = user;
        //Saber el estudiante actual
        lblStudentInfo.setText("Estudiante: " + user.getUsername());
        cargarExpediente();
    }

    private void cargarExpediente() throws ListException {
        if (currentUser != null && currentUser.getRole() == Role.STUDENT) {
            AcademicRecordData recordData = new AcademicRecordData();
            String studentId = currentUser.getUsername();
            AcademicRecord record = recordData.findByStudentId(studentId);
            if (record != null) {
                courseList.clear();
                DoublyLinkedList<Course> cursos = record.getCourses();
                double sumGrades = 0;
                int countGrades = 0;
                int sumaCreditos = 0;
                for (int i = 1; i <= cursos.size(); i++) {
                    Course c = cursos.get(i);
                    courseList.add(c);
                    sumGrades += c.getGrade();
                    countGrades++;
                    sumaCreditos += c.getCredits();
                }
                // Actualiza los labels superiores
                double avg = countGrades > 0 ? sumGrades / countGrades : 0;
                lblAvg.setText(String.format("%.1f", avg));
                lblAvgDesc.setText(avg >= 70 ? "Excelente rendimiento" : "Bajo rendimiento");
                lblCredits.setText(String.valueOf(sumaCreditos));
                lblCreditosDesc.setText(String.format("%d%% completado", sumaCreditos));
                lblCount.setText(String.valueOf(courseList.size()));
                lblCiclo.setText("Ciclo I - 2026"); // Solo si se tiene información, si no deja vacío
            } else {
                lblStudentInfo.setText("Expediente no encontrado.");
                courseList.clear();
                lblAvg.setText("0.0");
                lblAvgDesc.setText("");
                lblCredits.setText("0");
                lblCreditosDesc.setText("");
                lblCount.setText("0");
                lblCiclo.setText("");
            }
        }
    }


}