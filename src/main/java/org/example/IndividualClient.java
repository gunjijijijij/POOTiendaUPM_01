package org.example;

import org.example.Cashier;
import org.example.Client;
public class IndividualClient extends Client{
    public IndividualClient(String id, String name, String email, Cashier registeredBy) {
        super(id, name, email, registeredBy);
    }

    public IndividualClient() {
        super();
    }

    public String toString() {
        return "IndividualClient{identifier='" + getId() + "', name='" + getName() + "', email='" + getEmail() + "'}";
    }

    public Ticket<?> createTicketForType(String ticketId, String ticketType) {
        switch (ticketType) {
            case "-c":
            case "-s":
                throw new IllegalArgumentException("Combined and Service tickets are only for Company Clients.");
            case "-p":
            default:
                return new CommonTicket(ticketId);
        }
    }
}
