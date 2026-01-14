package org.example.strategy;

import org.example.CompanyTicket;
import org.example.Product;
import org.example.Service;
import org.example.Ticket;

public class CompanyCombinedPrinter implements ITicketPrinter { //PRODUCTOS Y SERVICIOS COMBINADOS
    @Override
    public void print(Ticket<?> ticket) {
        CompanyTicket companyTicket = (CompanyTicket) ticket;
        System.out.println("--- TICKET EMPRESA (COMBINADO) ---");

        int numServices = companyTicket.getServices().size();
        double discountRate = 0.15 * numServices;
        System.out.println("Servicios contratados: " + numServices);
        System.out.printf("Bonificación aplicada: %.0f%%\n", discountRate * 100);
        double total = 0;

        for (Product p : companyTicket.getProducts()) {
            double finalPrice = p.getPrice() * (1.0 - discountRate);
            System.out.printf("%-20s Precio Base: %.2f -> Final: %.2f\n", p.getName(), p.getPrice(), finalPrice);
            total += finalPrice;
        }

        for (Service s : companyTicket.getServices()) {
            System.out.println("Servicio (Ref " + s.getId() + "): Facturación post-mes. Vence: " + s.getExpirationDate());
        }

        System.out.printf("Total a pagar (Productos): %.2f\n", total);
    }
    @Override
    public boolean canClose(Ticket<?> ticket) {//Solo se cierra si hay al menos 1 producto y 1 servicio
        CompanyTicket companyTicket = (CompanyTicket) ticket;
        if (companyTicket.getProducts().isEmpty() & companyTicket.getServices().isEmpty()) {
            System.out.println("Error: Combined tickets need at least 1 Product AND 1 Service to close.");
            return false;
        }
        return true;
    }
}
