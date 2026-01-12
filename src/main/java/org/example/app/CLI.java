package org.example.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.example.controller.ClientController;
import org.example.controller.ProductController;
import org.example.controller.TicketController;
import org.example.util.Utils;
import org.example.controller.CashierController;

public class CLI {

    private final ProductController productController = ProductController.getInstance();
    private final CashierController cashierController = CashierController.getInstance();
    private final ClientController clientController = ClientController.getInstance();
    private final TicketController ticketController = TicketController.getInstance();

    public void start(String inputFile) {
        init();

        Scanner sc;

        try {
            if (inputFile != null) {
                sc = new Scanner(new File(inputFile));
            } else {
                sc = new Scanner(System.in);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File '" + inputFile + "' not found.");
            return;
        }

        boolean finish = false;

        while (!finish && sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] commandUni = Utils.parseLine(line).toArray(new String[0]);
            String cmd = commandUni[0].toLowerCase();

            System.out.println("tUPM> " + line);

            switch (cmd) {
                case "help":
                    help();
                    break;

                case "echo":
                    echo(commandUni);
                    break;

                case "prod":
                    handleProdCommand(commandUni);
                    break;

                case "ticket":
                    handleTicketCommand(commandUni);
                    break;

                case "cash":
                    handleCash(commandUni);
                    break;

                case "client":
                    handleClient(commandUni);
                    break;

                case "exit":
                    finish = true;
                    break;

                default:
                    System.out.println("Invalid command");
                    break;
            }
            if (!finish) {
                System.out.println();
            }
        }

        sc.close();
        end();
    }

    private void end() {
        System.out.println("Closing application.");
        System.out.print("Goodbye!");
    }

    private void init() {
        System.out.println("Welcome to the ticket module App.");
        System.out.println("Ticket module. Type 'help' to see commands.");
    }

    // Imprime el guía de comandos
    private void help() {
        System.out.println("Commands:");
        System.out.println("  client add \"<nombre>\" <DNI> <email> <cashId>");
        System.out.println("  client remove <DNI>");
        System.out.println("  client list");
        System.out.println("  cash add [<id>] \"<nombre>\"<email>");
        System.out.println("  cash remove <id>");
        System.out.println("  cash list");
        System.out.println("  cash tickets <id>");
        System.out.println("  ticket new [<id>] <cashId> <userId>");
        System.out.println("  ticket add <ticketId><cashId> <prodId> <amount> [--p<txt> --p<txt>]");
        System.out.println("  ticket remove <ticketId><cashId> <prodId>");
        System.out.println("  ticket print <ticketId> <cashId>");
        System.out.println("  ticket list");
        System.out.println("  prod add <id> \"<name>\" <category> <price>");
        System.out.println("  prod update <id> NAME|CATEGORY|PRICE <value>");
        System.out.println("  prod addFood [<id>] \"<name>\" <price> <expiration:yyyy-MM-dd> <max_people>");
        System.out.println("  prod addMeeting [<id>] \"<name>\" <price> <expiration:yyyy-MM-dd> <max_people>");
        System.out.println("  prod list");
        System.out.println("  prod remove <id>");
        System.out.println("  help");
        System.out.println("  echo \"<text>\"");
        System.out.println("  exit");
        System.out.println();
        System.out.println("Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
        System.out.println("Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%, ELECTRONICS 3%.");
    }

    // Repite el texto entre comillas
    private void echo(String[] fullLine) {
        String message = fullLine[1].trim();
        if (message.isEmpty()) {
            System.out.println("The echo message is empty.");
            return;
        }
        System.out.println("\"" + message + "\" ");
    }

    // Maneja los subcomandos relacionados con productos
    private void handleProdCommand(String[] args) {
        if (Utils.requireMinArgs(args, 2,
                "Use: prod <add|addFood|addMeeting|list|update|remove> ...")) return;

        String sub = args[1].toLowerCase();

        switch (sub) {
            case "add":
                productController.handleProdAdd(args);
                break;
            case "addfood":
                productController.handleProdAddFood(args);
                break;
            case "addmeeting":
                productController.handleProdAddMeeting(args);
                break;
            case "list":
                productController.prodList();
                System.out.println("prod list: ok");
                break;
            case "update":
                productController.handleProdUpdate(args);
                break;
            case "remove":
                productController.handleProdRemove(args);
                break;
            default:
                System.out.println("Subcommand not found. Use: add | addFood | addMeeting | list | update | remove");
                break;
        }
    }


    // Maneja los subcomandos relacionados con tickets
    private void handleTicketCommand(String[] args) {
        if (Utils.requireMinArgs(args, 2, "ticket command needs two parameters \"\"ticket \"<add|list|update|remove> ...\" \"\""))
            return;


        switch (args[1].toLowerCase()) {
            case "new":
                ticketController.handleTicketNew(args);
                break;

            case "add":
                ticketController.handleTicketAdd(args);
                break;

            case "remove":
                ticketController.handleTicketRemove(args);
                break;

            case "print":
                ticketController.handleTicketPrint(args);
                System.out.println("ticket print: ok");
                break;

            case "list":
                ticketController.handleTicketList();
                break;
            default:
                System.out.println("Invalid command");
                break;
        }
    }

    private void handleCash(String[] args) {
        if (Utils.requireMinArgs(args, 2, "cash command needs two parameters \"\"cash \"<add|remove|list|tickets> ...\" \"\""))
            return;

        switch (args[1].toLowerCase()) {
            case "add":
                cashierController.handleCashAdd(args);
                break;

            case "remove":
                cashierController.handleCashRemove(args);
                break;

            case "list":
                cashierController.list();
                break;

            case "tickets":
                cashierController.tickets(args[2]);
                break;

            default:
                System.out.println("Invalid command");
                break;
        }
    }

    public void handleClient(String[] args) {
        if (Utils.requireMinArgs(args, 2, "cash command needs two parameters \"\"cash \"<add|remove|list|tickets> ...\" \"\""))
            return;

        switch (args[1].toLowerCase()) {
            case "add":
                clientController.handleClientAdd(args);
                break;
            case "remove":
                clientController.handleClientRemove(args);
                break;
            case "list":
                clientController.list();
                break;

            default:
                System.out.println("Invalid command");
                break;

        }
    }
}


