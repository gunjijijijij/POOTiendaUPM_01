package org.example;

public abstract class User {
    protected String id;
    protected String name;
    protected String email;


    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setDni(String dni) {
        this.id = dni;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return "{class: " + getType() + ", id: '" + id + "', name: '" + name + "', email: '" + email + "'}";
    }
}
