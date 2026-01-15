package org.example.controller;

import org.example.*;

import org.example.strategy.CompanyCombinedPrinter;
import org.example.strategy.CompanyServicePrinter;
import org.example.util.TicketIdGenerator;
import org.example.util.Utils;

import java.util.ArrayList;
import java.util.Comparator;


public class TicketController {
    public static ArrayList<Ticket<?>> tickets = new ArrayList<>();

    private static TicketController instance;

    private TicketController(){}

    public static TicketController getInstance(){
        if (instance == null){
            instance = new TicketController();
        }
        return instance;
    }

    public Ticket<?> findTicketById(String id) {
        for (Ticket<?> ticket : tickets) {
            if (ticket.getId().equals(id)) {
                return ticket;
            }
        }
        return null;
    }

    public void handleTicketNew(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: ticket new [<id>] <cashId> <userId> [-c|-s|-p]");
            return;
        }

        Ticket<?> ticket;
        String ticketId;
        String cashId;
        String userId;
        String ticketType= "-p";

        if (args[2].startsWith("UW")) {
            ticketId = TicketIdGenerator.generateOpenTicketId();
            cashId = args[2];
            userId = args[3];
            if (args.length > 4)
                ticketType = args[4];
        } else {
            ticketId = args[2]; // ID Manual
            cashId = args[3];
            userId = args[4];
            if (args.length > 5) ticketType = args[5];
        }

        if (findTicketById(ticketId) != null) {
            System.out.println("ticket new: error (ticket id already exists)");
            return;
        }
        Cashier cashier = CashierController.getInstance().findCashById(cashId);
        if (cashier == null) {
            System.out.println("ticket new: error (cashier not found)");
            return;
        }
        Client client = ClientController.getInstance().findClientById(userId);
        if (client == null) {
            System.out.println("ticket new: error (client not found)");
            return;
        }
        try {
            switch (ticketType) {
                case "-c":
                    if (!(client instanceof org.example.util.CompanyClient)) {
                        System.out.println("Error: Combined tickets are only for Company Clients.");
                        return;
                    }
                    ticket = new CompanyTicket(ticketId);
                    ticket.setPrintingStrategy(new CompanyCombinedPrinter());
                    break;
                case "-s":
                    if (!(client instanceof org.example.util.CompanyClient)) {
                        System.out.println("Error: Service tickets are only for Company Clients.");
                        return;
                    }
                    ticket = new CompanyTicket(ticketId);
                    ticket.setPrintingStrategy(new CompanyServicePrinter());
                    break;
                default:
                    if (client instanceof org.example.util.CompanyClient) {
                        ticket = new CompanyTicket(ticketId);
                    } else {
                        ticket = new CommonTicket(ticketId);
                    }
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ticket new: error (" + e.getMessage() + ")");
            return;
        }

        cashier.getTickets().add(ticket);
        client.getTickets().add(ticket);
        tickets.add(ticket);

        System.out.println("Ticket : " + ticket.getId());
        ticket.print();
        System.out.println("ticket new: ok");
    }

    //Todo falta cambiarlo para que tenga en cuenta el tipo de ticket

    public void handleTicketAdd(String[] args) {
        String ticketId;
        String cashId;
        String itemId;
        String quantityStr;

        if (Utils.requireMinArgs(args, 6,
                "Usage: ticket add <ticketId> <cashId> <prodId> <quantity> [--pTXT --pTXT]"))
            return;

        ticketId = args[2];
        if (findTicketById(ticketId) == null){
            System.out.println("ticket add: error (ticket does not exist)");
            return;
        }

        cashId = args[3];
        itemId = args[4];
        quantityStr = args[5];

        Cashier cashier = CashierController.getInstance().findCashById(cashId);
        if (cashier == null) {
            System.out.println("ticket add: error (cashier with ID " + cashId + " not found)");
            return;
        }

        Integer productId = Utils.parsePositiveInt(itemId,
                "The product ID must be a positive integer.");
        if (productId == null) return;

        Product product = ProductController.getInstance().findProductById(productId);
        if (product == null) {
            System.out.println("ticket add: error (product with ID " + productId + " not found)");
            return;
        }

        Integer quantity = Utils.parsePositiveInt(quantityStr,
                "Quantity must be a positive integer.");
        if (quantity == null) return;

        if (!cashier.isTicketOfCash(ticketId)) {
            System.out.println("Error: This cashier cannot access the ticket");
            return;
        }

        Ticket ticket = findTicketById(ticketId);
        if (ticket == null) {
            System.out.println("ticket add: error (ticket does not exist)");
            return;
        }

        try {
            ArrayList<String> customTexts = Utils.parseCustomTexts(args);
            ticket.addProductTicket(product, quantity, customTexts);
            System.out.println("Ticket : " + ticket.getId());
            ticket.print();
            System.out.println("ticket add: ok");
        } catch (Exception e) {
            System.out.println("ticket add: error (" + e.getMessage() + ")");
        }
    }

    public void handleTicketRemove(String[] args) {
        if (Utils.requireMinArgs(args, 5, "Usage: ticket remove <ticketId> <cashId> <prodId>"))
            return;

        String ticketId = args[2];
        Ticket<?> ticket = findTicketById(ticketId);
        if (ticket == null) {
            System.out.println("ticket remove: error (ticket does not exist)");
            return;
        }
        String cashId = args[3];
        String itemId = args[4];

        Cashier cashier = CashierController.getInstance().findCashById(cashId);
        if (cashier == null) {
            System.out.println("ticket add: error (cashier with ID " + cashId + " not found)");
            return;
        }

        // Check that cashier owns this ticket
        if (!cashier.isTicketOfCash(ticketId)) {
            System.out.println("Error: This cashier cannot access the ticket");
            return;
        }

        boolean removed = ticket.ticketRemove(itemId);

        if (removed) {
            System.out.println("Ticket : " + ticket.getId());
            ticket.print();
            System.out.println("ticket remove: ok");
        } else {
            System.out.println("ticket remove: error (no product found with that ID)");
        }
    }

    //Todo falta cambiarlo para que tenga en cuenta el tipo de ticket

    public void handleTicketPrint(String[] args) {
        if (Utils.requireMinArgs(args, 4, "Usage: ticket print <ticketId> <cashId>"))
            return;

        String ticketId = args[2];
        String cashId = args[3];

        Cashier cashier = CashierController.getInstance().findCashById(cashId);
        if (cashier == null) {
            System.out.println("ticket add: error (cashier with ID " + cashId + " not found)");
            return;
        }

        if (!cashier.isTicketOfCash(ticketId)) {
            System.out.println("Error: This cashier cannot access the ticket");
            return;
        }

        Ticket ticket = findTicketById(ticketId);
        if (ticket == null) {
            System.out.println("ticket print: error (ticket does not exist)");
            return;
        }

        ticket.closeTicket();
        System.out.println("Ticket : " + ticket.getId());
        ticket.print();
    }

    public void handleTicketList() {
        ArrayList<Cashier> cashiers = CashierController.getInstance().getCashiers();
        if (cashiers.isEmpty()) {
            System.out.println("ticket list: (no cashiers available)");
            return;
        }

        // Ordenar los cajeros por su id
        cashiers.sort(Comparator.comparing(Cashier::getId));

        boolean anyTicket = false;

        for (Cashier c : cashiers) {
            ArrayList<Ticket> tickets = c.getTickets();

            if (!tickets.isEmpty()) {
                anyTicket = true;
                System.out.println("Ticket List:");
                for (Ticket<?> t : tickets) {
                    System.out.println("  " + t);
                }
            }
        }

        if (!anyTicket) {
            System.out.println("ticket list: (no tickets available)");
            return;
        }

        System.out.println("ticket list: ok");
    }
}