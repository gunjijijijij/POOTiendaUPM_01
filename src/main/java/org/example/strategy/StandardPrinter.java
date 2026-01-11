package org.example.strategy;

import org.example.Category;
import org.example.Product;
import org.example.Ticket;
import java.util.List;

public class StandardPrinter implements ITicketPrinter { //ESTRATEGIA DE IMPRIMIR TICKET NORMAL (MISMA DE LA ENTREGA ANTERIOR)
    @Override
    public void print(Ticket ticket) {
        System.out.println("--- TICKET ESTÁNDAR ---");
        List<Product> lines = ticket.getLines();
        double total = 0;
        double totalDiscount = 0;

        for (Product p : lines) {
            long count = lines.stream().filter(x -> x.getCategory() == p.getCategory()).count();
            double discount = (count > 1) ? p.getDiscount() : 0;
            System.out.printf("%-20s %.2f (Dto: %.2f)\n", p.getName(), p.getPrice(), discount);
            total += p.getPrice();
            totalDiscount += discount;
        }
        System.out.printf("Total: %.2f\n", total - totalDiscount);
    }
    @Override
    public boolean canClose(Ticket<?> ticket) {
        // Un ticket estándar siempre se puede cerrar si no está vacío (la validación básica está en Ticket)
        return true;
    }
}
