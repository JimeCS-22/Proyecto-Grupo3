package cr.ac.ucr.sga.model.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Font;
import cr.ac.ucr.sga.model.data.AcademicRecordData;
import cr.ac.ucr.sga.model.data.CareerData;
import cr.ac.ucr.sga.model.data.CourseData;
import cr.ac.ucr.sga.model.data.StudentData;
import cr.ac.ucr.sga.model.entities.AcademicRecord;
import cr.ac.ucr.sga.model.entities.Course;
import cr.ac.ucr.sga.model.entities.ReportRow;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cr.ac.ucr.sga.model.entities.Student;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class ReportService {
    private final StudentData studentData = new StudentData();
    private final CourseData courseData = new CourseData();
    private final AcademicRecordData recordData = new AcademicRecordData();
    private final CareerData careerData = new CareerData();
    //=========================
    // PATHS JSON
    //=========================

    private static final String COURSES_JSON =
            "src/main/resources/data/courses.json";

    private static final String STUDENTS_JSON =
            "src/main/resources/data/students.json";

    private static final String PROFESSORS_JSON =
            "src/main/resources/data/professors.json";



    //=========================
    // DATOS
    //=========================

    private List<ReportRow> reportRows;

    public ReportService() {

        reportRows = new ArrayList<>();
    }

    public void exportReport(String format, File file) {

        List<ReportRow> rows = loadReportRows();

        switch (format.toUpperCase()) {

            case "PDF":
                exportPDF(rows, file);
                break;

            case "EXCEL":
                exportExcel(rows, file);
                break;

            case "CSV":
                exportCSV(rows, file);
                break;

            default:
                throw new IllegalArgumentException("Formato no soportado");
        }

    }
    public ObservableList<ReportRow> getObservableRows() {

        return FXCollections.observableArrayList(loadReportRows());

    }


    //=====================================================
    // CARGAR TODA LA INFORMACIÓN
    //=====================================================

    public List<ReportRow> loadReportRows() {

        reportRows = new ArrayList<>();

        try {

            var students = studentData.getAllStudents().toList();
            var courses = courseData.getAllCourses().toList();

            for (Course course : courses) {

                int matriculados = 0;
                int aprobados = 0;
                int reprobados = 0;
                double suma = 0;

                for (Student student : students) {

                    AcademicRecord record =
                            recordData.findByStudentId(student.getId());

                    if (record == null)
                        continue;

                    for (Course c : record.getCoursesAsList()) {

                        if (c.getId().equals(course.getId())) {

                            matriculados++;
                            suma += c.getGrade();

                            if (c.getGrade() >= 70)
                                aprobados++;
                            else
                                reprobados++;

                            break;
                        }
                    }
                }

                double promedio =
                        matriculados == 0 ? 0 : suma / matriculados;

                reportRows.add(
                        new ReportRow(
                                course.getName(),
                                "Profesor",
                                "Informática Empresarial",
                                matriculados,
                                promedio,
                                aprobados,
                                reprobados
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reportRows;
    }
    //=====================================================
    // MÉTRICAS
    //=====================================================
    public ReportMetrics getMetrics() {

        return calculateMetrics();

    }
    public ReportMetrics calculateMetrics() {

        if (reportRows == null || reportRows.isEmpty()) {
            loadReportRows();
        }

        ReportMetrics metrics = new ReportMetrics();

        //--------------------------
        // Estudiantes
        //--------------------------

        int estudiantes = reportRows.stream()
                .mapToInt(ReportRow::getEstudiantes)
                .sum();

        metrics.totalStudents = estudiantes;

        //--------------------------
        // Cursos
        //--------------------------

        metrics.totalCourses = reportRows.size();

        //--------------------------
        // Matrículas
        //--------------------------

        metrics.totalEnrollments = estudiantes;

        //--------------------------
        // Promedio institucional
        //--------------------------

        metrics.average = reportRows.stream()
                .mapToDouble(ReportRow::getPromedio)
                .average()
                .orElse(0);

        //--------------------------
        // Nota máxima
        //--------------------------

        metrics.maxAverage = reportRows.stream()
                .mapToDouble(ReportRow::getPromedio)
                .max()
                .orElse(0);

        //--------------------------
        // Nota mínima
        //--------------------------

        metrics.minAverage = reportRows.stream()
                .mapToDouble(ReportRow::getPromedio)
                .min()
                .orElse(0);

        //--------------------------
        // Aprobados
        //--------------------------

        metrics.approved = reportRows.stream()
                .mapToInt(ReportRow::getAprobados)
                .sum();

        //--------------------------
        // Reprobados
        //--------------------------

        metrics.failed = reportRows.stream()
                .mapToInt(ReportRow::getReprobados)
                .sum();

        //--------------------------
        // Curso con mayor matrícula
        //--------------------------

        ReportRow max = Collections.max(
                reportRows,
                Comparator.comparingInt(ReportRow::getEstudiantes)
        );

        metrics.courseMax = max.getCurso();
        metrics.maxEnrollment = max.getEstudiantes();

        //--------------------------
        // Curso con menor matrícula
        //--------------------------

        ReportRow min = Collections.min(
                reportRows,
                Comparator.comparingInt(ReportRow::getEstudiantes)
        );

        metrics.courseMin = min.getCurso();
        metrics.minEnrollment = min.getEstudiantes();

        //--------------------------
        // Profesor con mayor carga
        //--------------------------

        Map<String,Integer> carga = new HashMap<>();

        for(ReportRow r : reportRows){

            carga.put(
                    r.getProfesor(),
                    carga.getOrDefault(r.getProfesor(),0)+1
            );

        }

        metrics.teacherMostCourses =
                carga.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse("N/D");

        metrics.activeTeachers = carga.size();

        return metrics;
    }

    //=====================================================
    // FILTRAR
    //=====================================================

    public List<ReportRow> filterRows(String carrera,
                                      String curso,
                                      String profesor){

        if(reportRows==null || reportRows.isEmpty()){
            loadReportRows();
        }

        return reportRows.stream()

                .filter(r-> carrera==null
                        || carrera.equals("Todas")
                        || r.getCarrera().equals(carrera))

                .filter(r-> curso==null
                        || curso.equals("Todos")
                        || r.getCurso().equals(curso))

                .filter(r-> profesor==null
                        || profesor.equals("Todos")
                        || r.getProfesor().equals(profesor))

                .collect(Collectors.toList());

    }

    //=====================================================
    // GETTERS
    //=====================================================

    public List<ReportRow> getReportRows(){

        if(reportRows==null || reportRows.isEmpty()){
            loadReportRows();
        }

        return reportRows;
    }

    //=====================================================
    // CLASE INTERNA DE MÉTRICAS
    //=====================================================

    public static class ReportMetrics{

        public int totalStudents;

        public int totalCourses;

        public int totalEnrollments;

        public double average;

        public double maxAverage;

        public double minAverage;

        public int approved;

        public int failed;

        public String courseMax;

        public int maxEnrollment;

        public String courseMin;

        public int minEnrollment;

        public int activeTeachers;

        public String teacherMostCourses;

    }
    public void exportPDF(List<ReportRow> rows, File outputFile) {

        try {

            if (rows == null || rows.isEmpty()) {
                rows = loadReportRows();
            }

            ReportMetrics metrics = calculateMetrics();

            Document document = new Document(PageSize.A4, 45, 45, 40, 40);

            PdfWriter.getInstance(document, new FileOutputStream(outputFile));

            document.open();

            //-------------------------
            // FUENTES
            //-------------------------

            Font title =
                    new Font(Font.FontFamily.HELVETICA,18,Font.BOLD);

            Font section =
                    new Font(Font.FontFamily.HELVETICA,13,Font.BOLD);

            Font normal =
                    new Font(Font.FontFamily.HELVETICA,11);

            Font bold =
                    new Font(Font.FontFamily.HELVETICA,11,Font.BOLD);

            //-------------------------
            // TÍTULO
            //-------------------------

            Paragraph p = new Paragraph(
                    "INFORME DE MÉTRICAS DEL SISTEMA ACADÉMICO",
                    title);

            p.setAlignment(Element.ALIGN_CENTER);

            document.add(p);

            document.add(new Paragraph(" "));

            document.add(new Paragraph(
                    "Fecha: "
                            + new SimpleDateFormat("dd/MM/yyyy HH:mm")
                            .format(new Date()),
                    normal));

            document.add(new Paragraph("Período: I Ciclo 2026", normal));

            document.add(new Paragraph(" "));

            //----------------------------------------------------
            // ESTUDIANTES
            //----------------------------------------------------

            addSection(document,"ESTUDIANTES",section);

            document.add(new Paragraph(
                    "Registrados: " + metrics.totalStudents,
                    normal));

            document.add(new Paragraph(
                    "Activos: " + metrics.totalStudents,
                    normal));

            document.add(new Paragraph(
                    "Inactivos: 0",
                    normal));

            document.add(new Paragraph(" "));

            //----------------------------------------------------
            // CURSOS
            //----------------------------------------------------

            addSection(document,"CURSOS",section);

            document.add(new Paragraph(
                    "Cursos registrados: "
                            + metrics.totalCourses,
                    normal));

            document.add(new Paragraph(
                    "Curso con mayor matrícula:",
                    bold));

            document.add(new Paragraph(
                    metrics.courseMax
                            + " ("
                            + metrics.maxEnrollment
                            + ")",
                    normal));

            document.add(new Paragraph(" "));

            document.add(new Paragraph(
                    "Curso con menor matrícula:",
                    bold));

            document.add(new Paragraph(
                    metrics.courseMin
                            + " ("
                            + metrics.minEnrollment
                            + ")",
                    normal));

            document.add(new Paragraph(" "));

            //----------------------------------------------------
            // CALIFICACIONES
            //----------------------------------------------------

            addSection(document,"CALIFICACIONES",section);

            document.add(new Paragraph(
                    String.format(
                            "Promedio institucional: %.2f",
                            metrics.average),
                    normal));

            document.add(new Paragraph(
                    String.format(
                            "Nota máxima: %.2f",
                            metrics.maxAverage),
                    normal));

            document.add(new Paragraph(
                    String.format(
                            "Nota mínima: %.2f",
                            metrics.minAverage),
                    normal));

            document.add(new Paragraph(
                    "Aprobados: "
                            + metrics.approved,
                    normal));

            document.add(new Paragraph(
                    "Reprobados: "
                            + metrics.failed,
                    normal));

            document.add(new Paragraph(" "));

            //----------------------------------------------------
            // PROFESORES
            //----------------------------------------------------

            addSection(document,"PROFESORES",section);

            document.add(new Paragraph(
                    "Profesores activos: "
                            + metrics.activeTeachers,
                    normal));

            document.add(new Paragraph(
                    "Profesor con mayor carga:",
                    bold));

            document.add(new Paragraph(
                    metrics.teacherMostCourses,
                    normal));

            document.add(new Paragraph(" "));

            //----------------------------------------------------
            // TABLA
            //----------------------------------------------------

            PdfPTable table = new PdfPTable(7);

            table.setWidthPercentage(100);

            table.setSpacingBefore(15);

            table.setWidths(new float[]{
                    3f,2.3f,2.5f,1.3f,1.5f,1.5f,1.5f
            });

            addHeader(table,"Curso");
            addHeader(table,"Profesor");
            addHeader(table,"Carrera");
            addHeader(table,"Est.");
            addHeader(table,"Prom.");
            addHeader(table,"Apr.");
            addHeader(table,"Rep.");

            for(ReportRow r : rows){

                table.addCell(r.getCurso());
                table.addCell(r.getProfesor());
                table.addCell(r.getCarrera());

                table.addCell(
                        String.valueOf(r.getEstudiantes()));

                table.addCell(
                        String.format("%.2f",
                                r.getPromedio()));

                table.addCell(
                        String.valueOf(r.getAprobados()));

                table.addCell(
                        String.valueOf(r.getReprobados()));

            }

            document.add(table);

            document.close();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error generando PDF: "
                            + e.getMessage(),e);

        }

    }

    private void addSection(Document document,
                            String title,
                            Font font) throws Exception {

        Paragraph p = new Paragraph(title,font);

        p.setSpacingBefore(10);
        p.setSpacingAfter(8);

        document.add(p);

    }
    private void addHeader(PdfPTable table,
                           String text){

        PdfPCell cell = new PdfPCell(
                new Phrase(text,
                        new Font(
                                Font.FontFamily.HELVETICA,
                                10,
                                Font.BOLD)));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        cell.setPadding(6);

        table.addCell(cell);

    }

    public void exportExcel(List<ReportRow> rows, File outputFile) {

        try {

            if (rows == null || rows.isEmpty()) {
                rows = loadReportRows();
            }

            ReportMetrics metrics = calculateMetrics();

            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("Reporte");

            int rowIndex = 0;

            //--------------------------
            // TÍTULO
            //--------------------------

            Row title = sheet.createRow(rowIndex++);

            Cell cell = title.createCell(0);

            cell.setCellValue("INFORME DE MÉTRICAS DEL SISTEMA ACADÉMICO");

            CellStyle titleStyle = workbook.createCellStyle();

            org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();

            titleFont.setBold(true);

            titleFont.setFontHeightInPoints((short)16);

            titleStyle.setFont(titleFont);

            cell.setCellStyle(titleStyle);

            rowIndex++;

            //--------------------------
            // MÉTRICAS
            //--------------------------

            rowIndex = metric(sheet,rowIndex,"Fecha",
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));

            rowIndex = metric(sheet,rowIndex,"Período",
                    "I Ciclo 2026");

            rowIndex++;

            rowIndex = metric(sheet,rowIndex,
                    "Estudiantes registrados",
                    String.valueOf(metrics.totalStudents));

            rowIndex = metric(sheet,rowIndex,
                    "Cursos registrados",
                    String.valueOf(metrics.totalCourses));

            rowIndex = metric(sheet,rowIndex,
                    "Promedio institucional",
                    String.format("%.2f",metrics.average));

            rowIndex = metric(sheet,rowIndex,
                    "Aprobados",
                    String.valueOf(metrics.approved));

            rowIndex = metric(sheet,rowIndex,
                    "Reprobados",
                    String.valueOf(metrics.failed));

            rowIndex++;

            //--------------------------
            // TABLA
            //--------------------------

            Row header = sheet.createRow(rowIndex++);

            String[] cols = {

                    "Curso",
                    "Profesor",
                    "Carrera",
                    "Estudiantes",
                    "Promedio",
                    "Aprobados",
                    "Reprobados"

            };

            CellStyle headerStyle = workbook.createCellStyle();

            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();

            headerFont.setBold(true);

            headerStyle.setFont(headerFont);

            for(int i=0;i<cols.length;i++){

                Cell h = header.createCell(i);

                h.setCellValue(cols[i]);

                h.setCellStyle(headerStyle);

            }

            //--------------------------
            // DATOS
            //--------------------------

            for(ReportRow r : rows){

                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(r.getCurso());

                row.createCell(1).setCellValue(r.getProfesor());

                row.createCell(2).setCellValue(r.getCarrera());

                row.createCell(3).setCellValue(r.getEstudiantes());

                row.createCell(4).setCellValue(r.getPromedio());

                row.createCell(5).setCellValue(r.getAprobados());

                row.createCell(6).setCellValue(r.getReprobados());

            }

            //--------------------------
            // AJUSTAR COLUMNAS
            //--------------------------

            for(int i=0;i<7;i++){

                sheet.autoSizeColumn(i);

            }

            FileOutputStream fos =
                    new FileOutputStream(outputFile);

            workbook.write(fos);

            fos.close();

            workbook.close();

        }

        catch (Exception e){

            throw new RuntimeException(
                    "Error exportando Excel",
                    e);

        }

    }

    public void exportCSV(List<ReportRow> rows,
                          File outputFile){

        try{

            if(rows==null || rows.isEmpty()){

                rows=loadReportRows();

            }

            ReportMetrics metrics=
                    calculateMetrics();

            FileWriter writer=
                    new FileWriter(outputFile);

            writer.write("INFORME DEL SISTEMA ACADÉMICO\n\n");

            writer.write("Fecha,"
                    +new java.text.SimpleDateFormat("dd/MM/yyyy")
                    .format(new java.util.Date())
                    +"\n");

            writer.write("Periodo,I Ciclo 2026\n");

            writer.write("Estudiantes,"
                    +metrics.totalStudents+"\n");

            writer.write("Cursos,"
                    +metrics.totalCourses+"\n");

            writer.write("Promedio,"
                    +String.format("%.2f",
                    metrics.average)
                    +"\n");

            writer.write("Aprobados,"
                    +metrics.approved+"\n");

            writer.write("Reprobados,"
                    +metrics.failed+"\n\n");

            writer.write(
                    "Curso,Profesor,Carrera,Estudiantes,Promedio,Aprobados,Reprobados\n"
            );

            for(ReportRow r:rows){

                writer.write(

                        escape(r.getCurso())+","

                                +escape(r.getProfesor())+","

                                +escape(r.getCarrera())+","

                                +r.getEstudiantes()+","

                                +String.format("%.2f",
                                r.getPromedio())+","

                                +r.getAprobados()+","

                                +r.getReprobados()

                                +"\n"

                );

            }

            writer.flush();

            writer.close();

        }

        catch(Exception e){

            throw new RuntimeException(
                    "Error exportando CSV",
                    e);

        }

    }

    private int metric(Sheet sheet,
                       int rowIndex,
                       String label,
                       String value){

        Row row=
                sheet.createRow(rowIndex++);

        row.createCell(0).setCellValue(label);

        row.createCell(1).setCellValue(value);

        return rowIndex;

    }

    private String escape(String text){

        if(text==null){

            return "";

        }

        if(text.contains(",")
                || text.contains("\"")
                || text.contains("\n")){

            text=text.replace("\"","\"\"");

            return "\""+text+"\"";

        }

        return text;

    }
}