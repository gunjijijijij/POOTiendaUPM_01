package org.example;

import java.util.ArrayList;

public abstract class Client extends User {
    private Cashier registeredBy;

    public Client(String id, String name, String email, Cashier registeredBy) {
        super(id, name, email);
        this.registeredBy = registeredBy;
    }

    public Client() {}

    @Override
    public String toString() {
        return "Client{identifier='" + getId() + "', name='" + name + "', email='" + email + "', cash=" + (registeredBy != null ? registeredBy.getId() : "null") + "}";
    }

    public abstract Ticket<?> createTicketForType(String ticketId, String ticketType);
}
