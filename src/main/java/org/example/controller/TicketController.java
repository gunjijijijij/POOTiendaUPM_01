package org.example.controller;

import org.example.*;

import org.example.util.TicketIdGenerator;
import org.example.util.Utils;

import java.util.ArrayList;
import java.util.Comparator;


public class TicketController {
    public static ArrayList<Ticket> tickets = new ArrayList<>();

    public static Ticket findTicketById(String id) {
        for (Ticket ticket : tickets) {
            if (ticket.getId().equals(id)) {
                return ticket;
            }
        }
        return null;
    }

    public void handleTicketNew(String[] args) {
        if (args.length < 4) {
            System.err.println("Usage: ticket new [<id>] <cashId> <userId>");
            return;
        }

        String ticketId;
        String cashId;
        String userId;

        if (args.length == 4) {
            cashId = args[2];
            userId = args[3];

            do {
                ticketId = TicketIdGenerator.generateOpenTicketId();
            } while (TicketController.findTicketById(ticketId) != null);

        } else {
            ticketId = args[2];
            cashId = args[3];
            userId = args[4];

            if (TicketController.findTicketById(ticketId) != null) {
                System.err.println("ticket new: error (ticket id already exists)");
                return;
            }
        }

        Cashier cashier = CashierController.findCashById(cashId);
        if (cashier == null) {
            System.err.println("ticket new: error (cashier not found)");
            return;
        }

        Client client = ClientController.findClientById(userId);
        if (client == null) {
            System.err.println("ticket new: error (client not found)");
            return;
        }

        Ticket ticket = new Ticket();

        cashier.getTickets().add(ticket);
        client.getTickets().add(ticket);
        tickets.add(ticket);

        System.out.println("ticket: " + ticketId);
        ticket.print();
    }

    public void handleTicketAdd(String[] args) {
        if (Utils.requireMinArgs(args, 6,
                "Usage: ticket add <ticketId> <cashId> <prodId> <quantity> [--pTXT --pTXT]"))
            return;

        String ticketId = args[2];
        String cashId = args[3];
        String prodId = args[4];
        String quantityStr = args[5];


        Integer productId = Utils.parsePositiveInt(prodId,
                "The product ID must be a positive integer.");
        if (productId == null) return;

        Product product = ProductController.findProductById(productId);
        if (product == null) {
            System.err.println("ticket add: error (product with ID " + productId + " not found)");
            return;
        }

        Integer quantity = Utils.parsePositiveInt(quantityStr,
                "Quantity must be a positive integer.");
        if (quantity == null) return;

        String fullCommand = String.join(" ", args);
        ArrayList<String> customTexts = new ArrayList<>();
        if (product.esPersonalizable()){
            customTexts = Utils.parseCustomTexts(fullCommand);
        }

        if (!Cashier.isTicketOfCash(cashId)) {
            System.err.println("Error: This cashier cannot access the ticket");
            return;
        }

        Ticket ticket = TicketController.findTicketById(ticketId);
        if (ticket == null) {
            System.err.println("ticket add: error (ticket does not exist)");
            return;
        }

        try {
            Ticket.addProductTicket(product, quantity, customTexts);
            ticket.print();
            System.out.println("ticket add: ok");

        } catch (Exception e) {
            System.err.println("ticket add: error (" + e.getMessage() + ")");
        }
    }


    public void handleTicketRemove(String[] args) {
        if (Utils.requireMinArgs(args, 5, "Usage: ticket remove <ticketId> <cashId> <prodId>"))
            return;

        String ticketId = args[2];
        String cashId = args[3];
        String prodId = args[4];

        Integer productId = Utils.parsePositiveInt(prodId,
                "The product ID must be a positive integer.");
        if (productId == null) return;

        // Check that cashier owns this ticket
        if (!Cashier.isTicketOfCash(cashId)) {
            System.err.println("Error: This cashier cannot access the ticket");
            return;
        }

        Ticket ticket = TicketController.findTicketById(ticketId);
        if (ticket == null) {
            System.err.println("ticket remove: error (ticket does not exist)");
            return;
        }

        boolean removed = ticket.ticketRemove(productId);

        if (removed) {
            ticket.print();
            System.out.println("ticket remove: ok");
        } else {
            System.err.println("ticket remove: error (no product found with that ID)");
        }
    }

    public void handleTicketPrint(String[] args) {

        if (Utils.requireMinArgs(args, 4, "Usage: ticket print <ticketId> <cashId>"))
            return;

        String ticketId = args[2];
        String cashId = args[3];

        if (!Cashier.isTicketOfCash(cashId)) {
            System.err.println("Error: This cashier cannot access the ticket");
            return;
        }

        Ticket ticket = TicketController.findTicketById(ticketId);
        if (ticket == null) {
            System.err.println("ticket print: error (ticket does not exist)");
            return;
        }
        ticket.closeTicket();
        ticket.print();
    }

    public void handleTicketList() {

        ArrayList<Cashier> cashiers = CashierController.getCashiers();
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
                System.out.println("Cashier " + c.getId() + ":");
                for (Ticket t : tickets) {
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