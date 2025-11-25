package org.example.app;
import org.example.*;
import org.example.controller.ProductController;
import org.example.util.Utils;
import org.example.controller.CashierController;

import java.util.Scanner;

public class CLI {
    // Controladores principales
    private final ProductController productController = new ProductController();
    private final CashierController cashierController = new CashierController();

    public void start() {
        init(); // Mensaje inicial

        Scanner sc = new Scanner(System.in);
        boolean finish = false;

        // Bucle principal: se ejecuta hasta que el usuario escriba "exit"
        while (!finish && sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] commandUni = line.split(" ");
            String cmd = commandUni[0].toLowerCase(); // Primera palabra = comando tipo help, echo, prod, ticket o exit

            switch (cmd) {
                case "help":
                    help();
                    break;

                case "echo":
                    echo(line);
                    break;

                case "prod":
                    handleProdCommand(commandUni);
                    break;

                case "ticket":
                    handleTicketCommand(commandUni);
                    break;

                case "cash":
                    break;

                case "client":
                    break;

                case "exit":
                    finish = true;
                    break;

                default:
                    System.out.println("Invalid command");
                    break;
            }
        }
        end(); // Mensaje final
    }

    private void end () {
        System.out.println("Bye");
    }

    private void init () {
        System.out.println("Welcome to the ticket module App");
        System.out.println("Ticket module. Type 'help' to see commands.");
    }

    // Imprime el guía de comandos
    private void help () {
        System.out.println("Commands:");
        System.out.println(" prod add <id> \"<name>\" <category> <price>");
        System.out.println(" prod list");
        System.out.println(" prod update <id> NAME|CATEGORY|PRICE <value>");
        System.out.println(" prod remove <id>");
        System.out.println(" ticket new");
        System.out.println(" ticket add <prodId> <quantity>");
        System.out.println(" ticket remove <prodId>");
        System.out.println(" ticket print");
        System.out.println(" echo \"<text>\"");
        System.out.println(" help");
        System.out.println(" exit");
        System.out.println(" \nCategories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
        System.out.println("Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%,\n" +
                "ELECTRONICS 3%.");
    }

    // Repite el texto entre comillas
    private void echo (String fullLine) {
        int firstSpace = fullLine.indexOf(' ');
        if (firstSpace < 0 || firstSpace == fullLine.length() - 1) {
            System.err.println("Usage: echo \"<text>\"");
            return;
        }
        String message = fullLine.substring(firstSpace + 1).trim();
        if (message.startsWith("\"") && message.endsWith("\"") && message.length() >= 2) {
            message = message.substring(1, message.length() - 1);
        }
        if (message.isEmpty()) {
            System.err.println("The echo message is empty.");
            return;
        }
        System.out.println(message);
    }

    // Maneja los subcomandos relacionados con productos
    private void handleProdCommand (String[]args){
        if (Utils.requireMinArgs(args, 2, "Use: prod <add|list|update|remove> ...")) return;

        switch (args[1].toLowerCase()) {
            case "add": {
                productController.handleProdAdd(args);
                break;
            }

            case "list": {
                productController.prodList();
                System.out.println("prod list: ok");
                break;
            }

            case "update": {
                productController.handleProdUpdate(args);
                break;
            }

            case "remove": {
                productController.handleProdRemove(args);
                break;
            }

            default: {
                System.err.println("Subcommand not found. Use: add | list | update | remove");
                break;
            }
        }
    }

    // Maneja los subcomandos relacionados con tickets
    private void handleTicketCommand (String[]args){
        if (Utils.requireMinArgs(args, 2, "ticket command needs two parameters \"\"ticket \"<add|list|update|remove> ...\" \"\"")) return;

        switch (args[1].toLowerCase()) {
            case "new":
                Ticket currentTicket = new Ticket();
                break;

            case "add":
                Ticket.handleTicketAdd(args);
                break;

            case "remove":
                Ticket.handleTicketRemove(args);
                break;

            case "print":
                Ticket.print();
                System.out.println("ticket print: ok");
                break;

            default: System.err.println("Invalid command"); break;
        }
    }

    private void handleCash(String[] args){
        if (Utils.requireMinArgs(args, 2, "cash command needs two parameters \"\"cash \"<add|remove|list|tickets> ...\" \"\"")) return;

        switch (args[1].toLowerCase()) {
            case "add":
                cashierController.handleCashAdd(args);
                break;

            case "remove":

                break;

            case "list":

                break;

            case "tickets":

                break;

            default: System.err.println("Invalid command"); break;
        }
    }
}


