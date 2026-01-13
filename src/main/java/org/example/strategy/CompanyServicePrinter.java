package org.example.strategy;

import org.example.ProductService;
import org.example.Ticket;
import org.example.CompanyTicket;

public class CompanyServicePrinter implements ITicketPrinter { //TICKET SOLO DE SERVICIOS SIN DESCUENTOS NI NADA
    @Override
    public void print(Ticket<?> ticket) {
        CompanyTicket companyTicket = (CompanyTicket) ticket;
        System.out.println("Ticket Empresa (Solo Servicios)");

        if (companyTicket.getServices().isEmpty()) {
            System.out.println("  (Sin servicios añadidos)");
            return;
        }

        for (ProductService service : companyTicket.getServices()) {
            System.out.println("  Service: " + service.getId() + " -> Expira: " + service.getExpirationDate());
        }

        System.out.println("  Estado: Pendiente de facturación mensual.");
        System.out.println("  Total: 0.0 (A pagar a final de mes)");
    }
    @Override
    public boolean canClose(Ticket<?> ticket) {
        CompanyTicket companyTicket = (CompanyTicket) ticket;
        return !companyTicket.getServices().isEmpty();
    }
}