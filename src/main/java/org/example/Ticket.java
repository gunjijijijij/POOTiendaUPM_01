package org.example;

import org.example.strategy.ITicketPrinter;
import org.example.strategy.StandardPrinter;
import org.example.util.TicketIdGenerator;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public abstract class Ticket<T extends CatalogItem> implements Serializable {
    protected String id;
    protected String type;
    protected ITicketPrinter printingStrategy;
    protected final List<T> items = new ArrayList<>();

    protected static final int MAX_SIZE = 100;



    public enum Status {
        OPEN,
        EMPTY,
        CLOSE
    }

    protected Status status = Status.EMPTY;

    protected Ticket(String id, ITicketPrinter strategy) {
        this.id = id;
        this.printingStrategy = strategy;
    }

    protected Ticket(String id) {
        this(id, new StandardPrinter());
    }

    protected Ticket(Ticket ticket) {
        this.id = ticket.id;
        this.status = ticket.status;
        this.printingStrategy = ticket.printingStrategy;

    }

    public String getId() {
        return id;
    }


    public Status getStatus() {
        return status;
    }

    public void setPrintingStrategy(ITicketPrinter strategy) {
        this.printingStrategy = strategy;
    }

    public abstract void addProductTicket(
            Product product, int quantity, List<String> customTexts
    );

    public abstract void addService(Service service);

    public abstract double getTotalPrice();
    public abstract double getTotalDiscount();

    public double getFinalPrice() {
        return getTotalPrice() - getTotalDiscount();
    }

    // Elimina todas las apariciones de un producto existente en el ticket
    public boolean ticketRemove(String productId) {
        if (status == Status.CLOSE) {
            System.out.println("ticket remove: error (ticket is closed)");
            return false;
        }
        boolean removed = false;
        for (int i = items.size() - 1; i >= 0; i--) {
            if (productId.equals(items.get(i).getId())) {
                items.remove(i); removed = true;
            }
        } return removed;
    }


    public void closeTicket() {
        id = TicketIdGenerator.generateCloseTicketId(id);
        status = Status.CLOSE;
        if (printingStrategy != null && !printingStrategy.canClose(this)) {
            throw new IllegalStateException("Ticket cannot be closed: requirements not met for this type.");
        }
    }

    protected int countCategory(Category category) {
        int count = 0;
        for (CatalogItem item : items) {
            if (item.getCategory() == category) {
                count++;
            }
        }
        return count;
    }


    public void print() {
        printingStrategy.print(this);
    }


    @Override
    public String toString() {
        return id + " - " + status.toString();
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

