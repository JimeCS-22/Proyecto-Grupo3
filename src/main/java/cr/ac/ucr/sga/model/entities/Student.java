package cr.ac.ucr.sga.model.entities;

import cr.ac.ucr.sga.model.data.AcademicRecordData;

public class Student {

    private String id;
    private String name;
    private String carnet;
    private int age;
    private String careerId;
    private transient AcademicRecord academicRecord;
    private String username;
    private String password;

    // =========================
    // CONSTRUCTOR VACÍO (GSON)
    // =========================

    public Student() {

    }

    // =========================
    // BUILDER CONSTRUCTOR
    // =========================

    private Student(Builder builder) {

        this.id = builder.id;
        this.name = builder.name;
        this.carnet = builder.carnet;
        this.age = builder.age;

        this.username = builder.username;
        this.password = builder.password;

        // siempre inicializar academicRecord
        //this.academicRecord = new AcademicRecord(this);
    }

    // =========================
    // GETTERS
    // =========================

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getCarnet() {
        return carnet;
    }

    public int getAge() {
        return age;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public AcademicRecord getAcademicRecord() {
        if (this.academicRecord == null) {
            AcademicRecordData data = new AcademicRecordData();
            AcademicRecord record = data.findByStudentId(this.id);
            if (record != null) {
                this.academicRecord = record;
            } else {
                // Si no existe, se crea en blanco para evitar NullPointer
                this.academicRecord = new AcademicRecord(this);
            }
        }
        return this.academicRecord;
    }

    public String getCareerId() {
        return careerId;
    }

    public void setCareerId(String careerId) {
        this.careerId = careerId;
    }

    public void setAcademicRecord(AcademicRecord academicRecord) {
        this.academicRecord = academicRecord;
    }

    // =========================
    // TO STRING
    // =========================

    @Override
    public String toString() {
        return "[" + id + "] " + name + " - " + carnet + " - " + age + " - " + username;
    }

    // =========================
    // BUILDER
    // =========================

    public static class Builder {

        private String id;
        private String name;
        private String carnet;
        private int age;

        private String username;
        private String password;

        private String careerId;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }



        public Builder setCarnet(String carnet) {
            this.carnet = carnet;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setcareerId(String careerId) {
            this.careerId = careerId;
            return this;
        }

        public Student build() {

            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("El ID no puede estar vacío");
            }

            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("El nombre no puede estar vacío");
            }

            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("El usuario no puede estar vacío");
            }

            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("La contraseña no puede estar vacía");
            }

            return new Student(this);
        }

        @Override
        public String toString() {
            return name;
        }

    }
}