package org.example;

import java.util.ArrayList;

public class Client extends User {
    private Cashier registeredBy;
    ArrayList<Ticket> tickets = new ArrayList<>();

    public Client(String id, String name, String email, Cashier registeredBy) {
        super(id, name, email);
        this.registeredBy = registeredBy;
    }

    public Cashier getRegisteredBy() {
        return registeredBy;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    @Override
    public String toString() {
        return "Client{identifier='" + getId() + "', name='" + name + "', email='" + email + "', cash=" + (registeredBy != null ? registeredBy.getId() : "null") + "}";
    }


}
