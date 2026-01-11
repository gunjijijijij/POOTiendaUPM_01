package org.example.strategy;

import org.example.Service;
import org.example.Ticket;

public class CompanyServicePrinter implements ITicketPrinter { //TICKET SOLO DE SERVICIOS SIN DESCUENTOS NI NADA
    @Override
    public void print(Ticket<?> ticket) {
        System.out.println("Ticket Empresa (Solo Servicios)");

        if (ticket.getServices().isEmpty()) {
            System.out.println("  (Sin servicios añadidos)");
            return;
        }

        for (Service service : ticket.getServices()) {
            System.out.println("  Service: " + service.getId() + " -> Expira: " + service.getExpirationDate());
        }

        System.out.println("  Estado: Pendiente de facturación mensual.");
        System.out.println("  Total: 0.0 (A pagar a final de mes)");
    }
    @Override
    public boolean canClose(Ticket<?> ticket) {
        return !ticket.getServices().isEmpty();
    }
}