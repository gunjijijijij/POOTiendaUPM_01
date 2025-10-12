package org.example;

import java.util.Scanner;

//
public class CLI {
    private final Ticket currentTicket = new Ticket();
    private final ProductController productController = new ProductController();

    public void start() {
        init();

        Scanner sc = new Scanner(System.in);
        boolean finish = false;

        while (!finish && sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] commandUni = line.split(" ");
            String cmd = commandUni[0].toLowerCase();

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

                case "exit":
                    finish = true;
                    break;

                default:
                    System.out.println("Invalid command");
                    break;
            }
        }

        end();
    }

    private void end () {
        System.out.println("Bye");
    }

    private void init () {
        System.out.println("Welcome to the ticket module App");
        System.out.println("Ticket module. Type 'help' to see commands.");
    }

    private void help () {
        System.out.println("Commands:");
        System.out.println(" prod add <id> \"<name>\" <category> <price>");
        System.out.println(" prod list");
        System.out.println(" prod update <id> NAME|CATEGORY|PRICE <value>");
        System.out.println(" prod remove <id>");
        System.out.println(" ticket new");
        System.out.println(" ticket add <prodId><quantity>");
        System.out.println(" ticket remove <prodId>");
        System.out.println(" ticket print");
        System.out.println(" echo \"<texto>\"");
        System.out.println(" help");
        System.out.println(" exit");
        System.out.println(" \nCategories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
        System.out.println("Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%,\n" +
                "ELECTRONICS 3%.");
    }

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

    private void handleProdCommand (String[]args){
        if (requireMinArgs(args, 2, "Use: prod <add|list|update|remove> ...")) return;

        switch (args[1].toLowerCase()) {
            case "add": {
                // prod add <id> "<name>" <category> <price>
                prodAdd(args);
                break;
            }

            case "list": {
                productController.prodList();
                System.out.println("prod list: ok");
                break;
            }

            case "update": {
                // prod update <id> NAME|CATEGORY|PRICE <value>
                prodUpdate(args);
                break;
            }

            case "remove": {
                prodRemove(args);
                break;
            }

            default: {
                System.err.println("Subcommand not found. Use: add | list | update | remove");
                break;
            }
        }
    }


    private void handleTicketCommand (String[]args){
        if (requireMinArgs(args, 2, "ticket command needs two parameters \"\"ticket \"<add|list|update|remove> ...\" \"\"")) return;

        String idString = args[2];

        switch (args[1]) {
            case "new":
                currentTicket.resetTicket();
                break;

            case "add":
                if (requireMinArgs(args, 4, "Please input all the necessary arguments")) return;

                Integer addId = parsePositiveInt(idString, "The ID must be a positive integer.");
                if (addId == null) return;

                Integer quantity = parsePositiveInt(args[3], "The quantity must be a positive integer");
                if (quantity == null) return;

                Product product = productController.findProductById(addId);


                currentTicket.addProductTicket(product, quantity);

                break;

            case "remove":
                if (requireMinArgs(args, 3, "Please input all the necessary arguments")) return;

                Integer removeId = parsePositiveInt(idString, "The ID must be a positive integer.");
                if (removeId == null) return;

                currentTicket.ticketRemove(removeId);
                currentTicket.print();
                System.out.println("ticket remove: ok");
                break;


            case "print":
                currentTicket.print();
                break;
        }
    }

    private String joinQuoted(String[] args, int i0, int iN) {
        StringBuilder sb = new StringBuilder();
        for (int i = i0; i < iN; i++) sb.append(args[i]).append(" ");
        String name = sb.toString().trim();
        if (name.startsWith("\"") && name.endsWith("\"") && name.length() >= 2) {
            name = name.substring(1, name.length() - 1);
        }
        return name;
    }

    private boolean requireMinArgs(String[] args, int min, String usage) {
        if (args.length < min) { System.err.println(usage); return true; }
        return false;
    }

    private Integer parsePositiveInt(String s, String errMsg) {
        try {
            int v = Integer.parseInt(s);
            if (v <= 0) { System.err.println(errMsg); return null; }
            return v;
        } catch (NumberFormatException e) {
            System.err.println(errMsg);
            return null;
        }
    }

    private Float parseNonNegativeFloat(String s) {
        try {
            float v = Float.parseFloat(s);
            if (v < 0f) { System.err.println("Price must be a non-negative number."); return null; }
            return v;
        } catch (NumberFormatException e) {
            System.err.println("Price must be a non-negative number.");
            return null;
        }
    }

    private Category parseCategory(String s) {
        try {
            return Category.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid category. Use: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
            return null;
        }
    }

    private void prodAdd(String[] args) {
        if (requireMinArgs(args, 5, "Usage: prod add <id> \"<name>\" <category> <price>")) return;

        Integer id = parsePositiveInt(args[2], "The id must be a positive integer.");
        if (id == null) return;

        String name = joinQuoted(args, 3, args.length - 2).trim();
        if (name.isEmpty()) {System.err.println("The name is empty."); return; }

        Category cat = parseCategory(args[args.length - 2]);
        if (cat == null) return;

        Float price = parseNonNegativeFloat(args[args.length - 1]);
        if (price == null) return;

        productController.addProduct(id, name, cat, price);
        System.out.println("{class:Product, id:" + id + ", name:'" + name + "', category:" + cat + ", price:" + price + "}");
    }

    private void prodUpdate(String[] args) {
        if (requireMinArgs(args, 4, "Usage: prod update <id> NAME|CATEGORY|PRICE <value>")) return;

        Integer id = parsePositiveInt(args[2], "The ID must be a positive integer.");
        if (id == null) return;

        String field = args[3].toUpperCase();
        switch (field) {
            case "NAME":
                if (requireMinArgs(args, 5, "Usage: prod update <id> NAME \"<new name>\"")) return;
                String newName = joinQuoted(args, 4, args.length).trim();
                if (newName.isEmpty()) {System.err.println("The name is empty"); return; }
                productController.prodUpdate(id, field, newName);
                break;

            case "CATEGORY":
                if (requireMinArgs(args, 5, "Usage: prod update <id> CATEGORY <newCategory>")) return;
                Category newCat = parseCategory(args[4]);
                if (newCat == null) return;
                productController.prodUpdate(id, field, newCat.name());
                break;

            case "PRICE":
                if (requireMinArgs(args, 5, "Usage: prod update <id> PRICE <newPrice>")) return;
                Float newPrice = parseNonNegativeFloat(args[4]);
                if (newPrice == null) return;
                productController.prodUpdate(id, field, Float.toString(newPrice));
                break;

            default: System.err.println("Field not supported. Use NAME, CATEGORY, or PRICE."); break;
        }
    }

    private void prodRemove(String[] args) {
        if (requireMinArgs(args, 3, "Usage: prod remove <id>")) return;
        Integer id = parsePositiveInt(args[2], "The ID must be a positive integer.");
        if (id == null) return;

        try {
            Product removed = productController.findProductById(id);

            productController.prodRemove(id);
            currentTicket.ticketRemove(id); /
            System.out.println("{class:Product, id:" + id + ", name:'" + removed.getName()
                    + "', category:" + removed.getCategory() + ", price:" + removed.getPrice() + "}");
            System.out.println("prod remove: ok");
        } catch (IllegalArgumentException e) {
            System.err.println("prod remove: error (" + e.getMessage() + ")");
        }
    }

}
