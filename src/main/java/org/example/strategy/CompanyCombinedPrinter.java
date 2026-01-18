package org.example.strategy;

import org.example.CompanyTicket;
import org.example.Product;
import org.example.Service;
import org.example.Ticket;

public class CompanyCombinedPrinter implements ITicketPrinter { //PRODUCTOS Y SERVICIOS COMBINADOS
    @Override
    public void print(Ticket<?> ticket) {
        if (!(ticket instanceof CompanyTicket)) {
            System.out.println("print: error (ticket is not a CompanyTicket)");
            return;
        }

        CompanyTicket companyTicket = (CompanyTicket) ticket;

        int numServices = companyTicket.getServices().size();
        double discountRate = 0.15 * numServices;

        double totalOriginal = 0.0;
        double totalFinal = 0.0;

        if (!companyTicket.getServices().isEmpty()){
            System.out.println("Services Included: ");
            for (Service s : companyTicket.getServices()) {
                System.out.println("  " + s);
            }
        }

        if (!companyTicket.getProducts().isEmpty()){
            System.out.println("Product Included:");
            for (Product p : companyTicket.getProducts()) {
                System.out.println("  " + p);
                totalOriginal += p.getPrice();
                totalFinal += p.getPrice() * (1.0 - discountRate);
            }

            double totalDiscount = totalOriginal - totalFinal;

            System.out.println("  Total price: " + totalOriginal);
            System.out.println("  Extra Discount from services:" + totalDiscount + " **discount -" + totalDiscount);
            System.out.println("  Total discount: " + totalDiscount);
            System.out.println("  Final Price: " + totalFinal);
        }
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
