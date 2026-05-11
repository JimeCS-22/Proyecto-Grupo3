package ucr.algoritmos.proyecto.model;

import java.time.LocalDate;

public class Employee extends Person{

    private String jobPosition;
    private LocalDate hireDate;


    public Employee(String name, String id, int age, double height, double weight, String jobPosition) {
        super(name, id, age, height, weight);
        this.jobPosition = jobPosition;
        this.hireDate = null;
    }

    public Employee(String name, String id, int age, double height, double weight, String jobPosition, LocalDate hireDate) {
        super(name, id, age, height, weight);
        this.jobPosition = jobPosition;
        this.hireDate = hireDate;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    @Override
    public String getRoleDescription() {
        return "Empleado: " + getName() + " puesto " + jobPosition;
    }
    @Override
    public String toString() {
        return super.toString() + ". Empleado, puesto: " + jobPosition
                + (hireDate != null ? ", fecha ingreso: " + hireDate : "");
    }
}
