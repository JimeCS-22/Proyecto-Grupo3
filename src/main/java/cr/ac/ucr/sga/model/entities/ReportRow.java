package cr.ac.ucr.sga.model.entities;

public class ReportRow {

    private String curso;
    private String profesor;
    private String carrera;
    private int estudiantes;
    private double promedio;
    private int aprobados;
    private int reprobados;

    public ReportRow(String curso,
                     String profesor,
                     String carrera,
                     int estudiantes,
                     double promedio,
                     int aprobados,
                     int reprobados) {

        this.curso = curso;
        this.profesor = profesor;
        this.carrera = carrera;
        this.estudiantes = estudiantes;
        this.promedio = promedio;
        this.aprobados = aprobados;
        this.reprobados = reprobados;
    }

    public String getCurso() {
        return curso;
    }

    public String getProfesor() {
        return profesor;
    }

    public String getCarrera() {
        return carrera;
    }

    public int getEstudiantes() {
        return estudiantes;
    }

    public double getPromedio() {
        return promedio;
    }

    public int getAprobados() {
        return aprobados;
    }

    public int getReprobados() {
        return reprobados;
    }
}
