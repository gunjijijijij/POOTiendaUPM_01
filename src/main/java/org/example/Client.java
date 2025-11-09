package org.example;

public class Client extends User {
    private Cashier registeredBy;
    private String dni;

    public Client(String dni, String name, String email, Cashier registeredBy) {
        super(dni, name, email);
        this.dni = dni;
        this.registeredBy = registeredBy;
    }
    public Cashier getRegisteredBy() {
        return registeredBy;
    }

    public String getDni() {
        return dni;
    }

    @Override
    public String getType() {
        return "Client";
    }

    @Override
    public String toString() {
        return  "{class: "+getType()+", dni: '"+dni+"', name: '"+name+"', email: '"+email+"', registeredBy: '"+(registeredBy != null ? registeredBy.getId() : "null")+"'}";
    }
}
