package cr.ac.ucr.sga.model.entities;

public class Professor {

    private String id;
    private String name;
    private String careerId;
    private String username;
    private String password;

    public Professor() {
    }

    private Professor(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.careerId = builder.careerId;
        this.username = builder.username;
        this.password = builder.password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCareerId() {
        return careerId;
    }

    public void setCareerId(String careerId) {
        this.careerId = careerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Builder{

        private String id;
        private String name;
        private String careerId;
        private String username;
        private String password;

        public Builder setId(String id){
            this.id=id;
            return this;
        }

        public Builder setName(String name){
            this.name=name;
            return this;
        }

        public Builder setCareerId(String careerId){
            this.careerId=careerId;
            return this;
        }
        public Builder setUsername(String username) {
            this.username=username;
            return this;
        }
        public Builder setPassword(String password) {
            this.password=password;
            return this;
        }

        public Professor build() {

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

            return new Professor(this);
        }

        @Override
        public String toString() {
            return name;
        }


    }

    @Override
    public String toString() {
        return name;
    }
}
