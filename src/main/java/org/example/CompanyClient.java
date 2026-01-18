package org.example;

import org.example.Client;
import org.example.Cashier;
public class CompanyClient extends Client{

    public CompanyClient(String id, String name, String email, Cashier registeredBy) {
        super(id, name, email, registeredBy);
    }

    public CompanyClient() {
        super();
    }

    @Override
    public String toString() {
        return "CompanyClient{identifier='" + getId() + "', name='" + getName() + "'}";
    }

    @Override
    public Ticket<?> createTicketForType(String ticketId, String ticketType) {
        switch (ticketType) {
            case "-s":
                return new ServiceTicket(ticketId);
            case "-p":
                throw new IllegalArgumentException("Product tickets are only for individual clients.");
            case "-c":
            default:
                return new CompanyTicket(ticketId);
        }
    }
}
