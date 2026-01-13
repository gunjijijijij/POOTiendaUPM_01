package org.example;

import org.example.strategy.ITicketPrinter;
import org.example.strategy.StandardPrinter;
import org.example.util.TicketIdGenerator;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;


public abstract class Ticket<Item> {
    protected String id;
    protected String type;
    protected ITicketPrinter printingStrategy;

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

    public abstract void addService(ProductService service);


    // Elimina todas las apariciones de un producto existente en el ticket
    public  void ticketRemove(String productId) {

    }


    public void closeTicket() {
        id = TicketIdGenerator.generateCloseTicketId(id);
        status = Status.CLOSE;
        if (printingStrategy != null && !printingStrategy.canClose(this)) {
            throw new IllegalStateException("Ticket cannot be closed: requirements not met for this type.");
        }
    }


    public void print() {
        printingStrategy.print(this);
    }

    // Imprime el contenido del ticket
    //Todo falta cambiarlo para que imprima dependiendo de la estrategia de impresion


    @Override
    public String toString() {
        return id + " - " + status.toString();
    }
}
