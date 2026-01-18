package org.example.strategy;

import org.example.Service;
import org.example.ServiceTicket;
import org.example.Ticket;
import org.example.CompanyTicket;

public class CompanyServicePrinter implements ITicketPrinter { //TICKET SOLO DE SERVICIOS SIN DESCUENTOS NI NADA
    @Override
    public void print(Ticket<?> ticket) {
        ServiceTicket serviceTicket = (ServiceTicket) ticket;
        System.out.println("Ticket Empresa (Solo Servicios)");

        if (serviceTicket.getItems().isEmpty()) {
            System.out.println("  (Sin servicios añadidos)");
            return;
        }

        for (Service service : serviceTicket.getItems()) {
            System.out.println("  Service: " + service.getId() + " -> Expira: " + service.getExpirationDate());
        }

        System.out.println("  Estado: Pendiente de facturación mensual.");
        System.out.println("  Total: 0.0 (A pagar a final de mes)");
    }
    @Override
    public boolean canClose(Ticket<?> ticket) {
        ServiceTicket serviceTicket = (ServiceTicket) ticket;
        return !serviceTicket.getItems().isEmpty();
    }
}