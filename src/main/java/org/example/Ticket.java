package org.example;

import org.example.util.TicketIdGenerator;

import java.util.ArrayList;
import java.util.List;


public class Ticket {
    private String id;
    private static final int MAX_SIZE = 100;
    private final List<TicketLine> lines = new ArrayList<>();

    public enum Status {
        OPEN,
        EMPTY,
        CLOSE
    }

    private Status status = Status.EMPTY;

    public Ticket() {
        this.id = TicketIdGenerator.generateOpenTicketId();
    }

    public Ticket(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    // Añade una cantidad x de un producto al ticket mientras no estuviera lleno
    public void addProductTicket(Product product, int quantity, List<String> customTexts) {
        if (status == Status.CLOSE) {
            throw new IllegalStateException("ticket is closed");
        }

        if (product == null) {
            throw new IllegalStateException("product doesn't exist");
        }

        List<TicketLine> ticketLines = product.createTicketLine(quantity, customTexts);
        if (lines.size() + ticketLines.size() > MAX_SIZE) {
            throw new IllegalArgumentException("invalid quantity, ticket would exceed max size");
        }
        lines.addAll(ticketLines);

        status = Status.OPEN;
    }

    // Elimina todas las apariciones de un producto existente en el ticket
    public boolean ticketRemove(int productId) {
        boolean found = false;
        if (status == Status.CLOSE) {
            System.out.println("ticket remove: error (ticket is closed)");
            return false;
        }

        for (int i = lines.size() - 1; i >= 0; i--) {
            if (lines.get(i).getProduct().getId() == productId) {
                lines.remove(i);
                found = true;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("no product found with that ID");
        }
        return true;
    }

    // Cuenta cuantos productos hay de una categoría en el ticket
    private int countCategory(Category category) {
        int count = 0;
        for (TicketLine line : lines) {
            if (line.getProduct().getCategory() == category) {
                count++;
            }
        }
        return count;
    }

    // Getter del total del precio de todos los productos del ticket sin descuento
    private double getTotalPrice() {
        double total = 0;
        for (TicketLine line : lines) {
            total += line.getPrice();
        }
        return total;
    }

    // Getter del descuento total del ticket
    private double getTotalDiscount() {
        double totalDiscount = 0;
        for (TicketLine line : lines) {
            Product product = line.getProduct();
            Category category = product.getCategory();
            int catCount = category != null ? countCategory(category) : 0;
            if (catCount > 1) {
                totalDiscount += line.getDiscount();
            }
        }
        return totalDiscount;
    }

    // Getter del precio total con descuento
    private double getFinalPrice() {
        return getTotalPrice() - getTotalDiscount();
    }

    public void closeTicket() {
        id = TicketIdGenerator.generateCloseTicketId(id);
        status = Status.CLOSE;
    }

    // Imprime el contenido del ticket
    public void print() {
        lines.sort((l1, l2) -> l1.getProduct().getName().compareToIgnoreCase(l2.getProduct().getName())); //ordena lines alfabeticamente

        for (TicketLine line : lines) {
            Product product = line.getProduct();
            Category category = product.getCategory();
            int catCount = category != null ? countCategory(category) : 0;
            double discount = line.getDiscount();
            System.out.printf(
                    "  %s%s\n",
                    line,
                    catCount > 1 && discount > 0 ? " **discount -" + String.format("%.2f", discount) : ""
            );
        }

        System.out.printf("  Total price: %.1f\n", getTotalPrice());
        System.out.printf("  Total discount: %.1f\n", getTotalDiscount());
        System.out.printf("  Final Price: %.1f\n", getFinalPrice());
    }

    @Override
    public String toString() {
        return id + " - " + status.toString();
    }
}
