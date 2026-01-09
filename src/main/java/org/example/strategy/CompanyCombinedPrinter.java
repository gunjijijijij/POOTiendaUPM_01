package org.example.strategy;

import org.example.Product;
import org.example.Service;
import org.example.Ticket;

public class CompanyCombinedPrinter implements ITicketPrinter { //PRODUCTOS Y SERVICIOS COMBINADOS
    @Override
    public void print(Ticket ticket) {
        System.out.println("--- TICKET EMPRESA (COMBINADO) ---");

        int numServices = ticket.getServices().size();
        double discountRate = 0.15 * numServices;
        System.out.println("Servicios contratados: " + numServices);
        System.out.printf("Bonificación aplicada: %.0f%%\n", discountRate * 100);
        double total = 0;

        for (Product p : ticket.getLines()) {
            double finalPrice = p.getPrice() * (1.0 - discountRate);
            System.out.printf("%-20s Precio Base: %.2f -> Final: %.2f\n", p.getName(), p.getPrice(), finalPrice);
            total += finalPrice;
        }

        for (Service s : ticket.getServices()) {
            System.out.println("Servicio (Ref " + s.getId() + "): Facturación post-mes. Vence: " + s.getExpirationDate());
        }

        System.out.printf("Total a pagar (Productos): %.2f\n", total);
    }
}
