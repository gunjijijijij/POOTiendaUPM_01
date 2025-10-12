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
                if (requireMinArgs(args, 5, "Use: prod add <id> \"<name>\" <category> <price>")) return;

                Integer addId = parsePositiveInt(args[2], "The id must be a positive integer.");
                if (addId == null) return;

                // nombre entre [3, args.length-2)
                String name = getNameInBrackets(args, 3, args.length - 2).trim();
                if (name.isEmpty()) {
                    System.err.println("The name is empty");
                    return;
                }

                Category category;
                try {
                    category = Category.valueOf(args[args.length - 2].toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid category. Use: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
                    return;
                }

                float price;
                try {
                    price = Float.parseFloat(args[args.length - 1]);
                } catch (NumberFormatException e) {
                    System.err.println("Price must be a valid number.");
                    return;
                }
                if (price < 0) {
                    System.err.println("The price can't be negative");
                    return;
                }
                try {
                    productController.addProduct(addId, name, category, price);
                    System.out.println("{class:Product, id:" + addId + ", name:'" + name + "', category:" + category + ", price:" + price + "}");
                    System.out.println("prod add: ok");
                } catch (IllegalArgumentException | IllegalStateException exception) {
                    System.out.println("prod add: error (" + exception.getMessage() + ")");
                }
                break;
            }

            case "list": {
                productController.prodList();
                System.out.println("prod list: ok");
                break;
            }

            case "update": {
                // prod update <id> NAME|CATEGORY|PRICE <value>
                if (requireMinArgs(args, 4, "Use: prod update <id> NAME|CATEGORY|PRICE <value>")) return;

                Integer updateId = parsePositiveInt(args[2], "The ID must be a positive integer.");
                if (updateId == null) return;

                String field = args[3].toUpperCase();

                switch (field) {
                    case "NAME": {
                        if (requireMinArgs(args, 5, "Use: prod update <id> NAME \"<new name>\"")) return;

                        String newName = getNameInBrackets(args, 4, args.length).trim();
                        if (newName.isEmpty()) {
                            System.err.println("The name is empty");
                            return;
                        }
                        productController.prodUpdate(updateId, field, newName);
                        break;
                    }
                    case "CATEGORY": {
                        if (requireMinArgs(args, 5, "Use: prod update <id> CATEGORY <newCategory>")) return;

                        try {
                            Category newCat = Category.valueOf(args[4].toUpperCase());
                            productController.prodUpdate(updateId, field, newCat.name());
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid category. Use: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
                        }
                        break;
                    }
                    case "PRICE": {
                        if (requireMinArgs(args, 5, "Use: prod update <id> PRICE <newPrice>")) return;

                        try {
                            float newPrice = Float.parseFloat(args[4]);
                            if (newPrice < 0) {
                                System.err.println("The price can't be negative");
                                return;
                            }
                            productController.prodUpdate(updateId, field, Float.toString(newPrice));
                        } catch (NumberFormatException e) {
                            System.err.println("Price must be a valid number.");
                        }
                        break;
                    }
                    default:
                        System.err.println("Field not supported. Use NAME, CATEGORY, or PRICE.");
                        break;
                }
                break;
            }

            case "remove": {
                if (requireMinArgs(args, 3, "Use: prod remove <id>")) return;

                Integer removeId = parsePositiveInt(args[2], "The ID must be a positive integer.");
                if (removeId == null) return;

                try {
                    productController.prodRemove(removeId);
                    currentTicket.ticketRemove(removeId);
                    Product product = productController.findProductById(removeId);
                    System.out.println("{class:Product, id:" + removeId + ", name:'" + product.getName() + "', category:" + product.getCategory() + ", price:" + product.getPrice() + "}");
                    System.out.println("prod remove: ok");
                } catch (IllegalArgumentException exception) {
                    System.out.println("prod remove: error (" + exception.getMessage() + ")");
                }
                break;
            }

            default:
                System.err.println("Subcommand not found. Use: add | list | update | remove");
                break;
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

                int quantity = Integer.parseInt(args[3]);
                if (quantity < 0) {
                    System.err.println("The quantity must be a positive integer");
                }

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

    private String getNameInBrackets (String[]args,int i0, int iN){
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = i0; i < iN; i++) {
            nameBuilder.append(args[i]).append(" ");
        }

        String name = nameBuilder.toString().trim();

        if (name.startsWith("\"") && name.endsWith("\"")) {
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
}
