package org.example;

import org.example.strategy.ITicketPrinter;
import org.example.strategy.StandardPrinter;
import org.example.util.TicketIdGenerator;

import java.util.ArrayList;
import java.util.List;


public class Ticket<T> {
    private String id;
    private String type;
    private ITicketPrinter printingStrategy;

    private static final int MAX_SIZE = 100;
    private final List<Product> lines = new ArrayList<>();
    private final List<ProductService> services = new ArrayList<>();

    public enum Status {
        OPEN,
        EMPTY,
        CLOSE
    }

    private Status status = Status.EMPTY;

    public Ticket(String id, String ticketType, ITicketPrinter strategy) {
        this.id = id;
        this.type = ticketType;
    }
    public Ticket(String id, String ticketType) {
        this(id, ticketType, new StandardPrinter());
    }

    public String getId() {
        return id;
    }

    public String getType() { return type;}

    public Status getStatus() {
        return status;
    }

    public List<Product> getLines() {
        return lines;
    }
    public List<ProductService> getServices() {
        return services;
    }

    public void setPrintingStrategy(ITicketPrinter strategy) {
        this.printingStrategy = strategy;
    }

    // Añade una cantidad x de un producto al ticket mientras no estuviera lleno
    public void addProductTicket(Product product, int quantity, List<String> customTexts) {
        if (status == Status.CLOSE) {
            throw new IllegalStateException("ticket is closed");
        }

        if (type.equalsIgnoreCase("service")) {
            throw new IllegalArgumentException("Service Tickets cannot accept Products.");
        }

        if (product == null) {
            throw new IllegalStateException("product doesn't exist");
        }

        List<Product> ticketLines = product.addToTicket(quantity, customTexts);
        if (lines.size() + ticketLines.size() > MAX_SIZE) {
            throw new IllegalArgumentException("invalid quantity, ticket would exceed max size");
        }
        lines.addAll(ticketLines);

        status = Status.OPEN;
    }

    public void addService(ProductService service) {
        if (status == Status.CLOSE) {
            throw new IllegalStateException("ticket is closed");
        }

        if (type.equalsIgnoreCase("service")) {
            throw new IllegalArgumentException("Error: Standard tickets (Individual) cannot accept Services.");
        }
        services.add(service);
        status = Status.OPEN;
    }

    // Elimina todas las apariciones de un producto existente en el ticket
    public boolean ticketRemove(String productId) {
        if (status == Status.CLOSE) {
            System.out.println("ticket remove: error (ticket is closed)");
            return false;
        }

        boolean removed = false;
        for (int i = lines.size() - 1; i >= 0; i--) {
            if (productId.equals(lines.get(i).getId())) {
                lines.remove(i);
                removed = true;
            }
        }

        return removed;
    }

    // Cuenta cuantos productos hay de una categoría en el ticket
    private int countCategory(Category category) {
        int count = 0;
        for (Product product : lines) {
            if (product.getCategory() == category) {
                count++;
            }
        }
        return count;
    }

    // Getter del total del precio de todos los productos del ticket sin descuento
    private double getTotalPrice() {
        double total = 0;
        for (Product product : lines) {
            total += product.getPrice();
        }
        return total;
    }

    // Getter del descuento total del ticket
    private double getTotalDiscount() {
        double totalDiscount = 0;
        for (Product product : lines) {
            Category category = product.getCategory();
            int catCount = category != null ? countCategory(category) : 0;
            if (catCount > 1) {
                totalDiscount += product.getDiscount();
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
        if (printingStrategy != null && !printingStrategy.canClose(this)) {
            throw new IllegalStateException("Ticket cannot be closed: requirements not met for this type.");
        }
    }

    // Imprime el contenido del ticket
    //Todo falta cambiarlo para que imprima dependiendo de la estrategia de impresion
    public void print() {
        lines.sort((l1, l2) -> l1.getName().compareToIgnoreCase(l2.getName())); //ordena lines alfabeticamente

        for (Product product : lines) {
            Category category = product.getCategory();
            int catCount = category != null ? countCategory(category) : 0;
            double discount = product.getDiscount();
            System.out.printf(
                    "  %s%s\n",
                    product,
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
