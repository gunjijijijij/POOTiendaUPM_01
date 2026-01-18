package org.example.strategy;

import org.example.Service;
import org.example.ServiceTicket;
import org.example.Ticket;
import org.example.CompanyTicket;

public class CompanyServicePrinter implements ITicketPrinter { //TICKET SOLO DE SERVICIOS SIN DESCUENTOS NI NADA
    @Override
    public void print(Ticket<?> ticket) {
        ServiceTicket serviceTicket = (ServiceTicket) ticket;

        for (Service service : serviceTicket.getItems()) {
            System.out.println(service);
        }
    }
    @Override
    public boolean canClose(Ticket<?> ticket) {
        ServiceTicket serviceTicket = (ServiceTicket) ticket;
        return !serviceTicket.getItems().isEmpty();
    }
}