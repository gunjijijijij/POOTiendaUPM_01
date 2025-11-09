package org.example;
import java.util.Scanner;

public class CLI {
    // Controladores principales
    private final Ticket currentTicket = new Ticket();
    private final ProductController productController = new ProductController();

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
        if (requireMinArgs(args, 2, "Use: prod <add|list|update|remove> ...")) return;

        switch (args[1].toLowerCase()) {
            case "add": {
                handleProdAdd(args);
                break;
            }

            case "list": {
                productController.prodList();
                System.out.println("prod list: ok");
                break;
            }

            case "update": {
                handleProdUpdate(args);
                break;
            }

            case "remove": {
                handleProdRemove(args);
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
        if (requireMinArgs(args, 2, "ticket command needs two parameters \"\"ticket \"<add|list|update|remove> ...\" \"\"")) return;

        switch (args[1].toLowerCase()) {
            case "new":
                currentTicket.resetTicket();
                break;

            case "add":
                handleTicketAdd(args);
                break;

            case "remove":
                handleTicketRemove(args);
                break;

            case "print":
                currentTicket.print();
                System.out.println("ticket print: ok");
                break;

            default: System.err.println("Invalid command"); break;
        }
    }

    // Une argumentos entre comillas (para los nombres de productos con espacios)
    private String joinQuoted(String[] args, int i0, int iN) {
        StringBuilder sb = new StringBuilder();
        for (int i = i0; i < iN; i++) sb.append(args[i]).append(" ");
        String name = sb.toString().trim();
        if (name.startsWith("\"") && name.endsWith("\"") && name.length() >= 2) {
            name = name.substring(1, name.length() - 1);
        }
        return name;
    }

    // Verifica si hay suficientes argumentos
    private boolean requireMinArgs(String[] args, int min, String usage) {
        if (args.length < min) { System.err.println(usage); return true; }
        return false;
    }

    // Parseo seguro de enteros positivos
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

    // Parseo seguro de precios (float no negativos)
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

    // Convierte un texto a una categoría válida
    private Category parseCategory(String s) {
        try {
            return Category.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid category. Use: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
            return null;
        }
    }

    // Procesa el comando "prod add": verifica los argumentos,
    // maneja los errores correspondientes y utiliza ProductController
    // para añadir el nuevo producto al catálogo.
    private void handleProdAdd(String[] args) {
        if (requireMinArgs(args, 5, "Usage: prod add <id> \"<name>\" <category> <price>")) return;

        Integer id = parsePositiveInt(args[2], "The id must be a positive integer.");
        if (id == null) return;

        String name = joinQuoted(args, 3, args.length - 2).trim();
        if (name.isEmpty()) {System.err.println("The name is empty."); return; }

        Category cat = parseCategory(args[args.length - 2]);
        if (cat == null) return;

        Float price = parseNonNegativeFloat(args[args.length - 1]);
        if (price == null) return;

        try {
            productController.addProduct(id, name, cat, price);
            Product added = productController.findProductById(id);
            System.out.println(added.toString());
            System.out.println("prod add: ok");
        } catch (IllegalArgumentException e) {
            System.err.println( e.getMessage().trim());
        } catch (Exception e) {
            System.err.println( e.getMessage());
        }
    }

    // Procesa el comando "prod update": verifica los argumentos, validando el tipo de actualización,
    // maneja los errores correspondientes y utiliza ProductController
    // para actualizar el dato del producto.
    private void handleProdUpdate(String[] args) {
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

    // Procesa el comando "prod remove": verifica los argumentos,
    // maneja los errores correspondientes y utiliza ProductController
    // para eliminar el nuevo producto al catálogo.
    private void handleProdRemove(String[] args) {
        if (requireMinArgs(args, 3, "Usage: prod remove <id>")) return;
        Integer id = parsePositiveInt(args[2], "The ID must be a positive integer.");
        if (id == null) return;

        Product removed = productController.findProductById(id);
        if (removed == null) {
            System.err.println("prod remove: error (no product with that ID)");
            return;
        }
        try {
            productController.prodRemove(id);
            currentTicket.ticketRemove(id);
            System.out.println(removed.toString());
            System.out.println("prod remove: ok");
        } catch (IllegalArgumentException e) {
            System.err.println("prod remove: error (" + e.getMessage() + ")");
        }
    }

    // Procesa el comando "ticket add": verifica los argumentos,
    // maneja los errores correspondientes y utiliza Ticket
    // para añadir el producto al Ticket.
    private void handleTicketAdd(String[] args) {
        if (requireMinArgs(args, 4, "Please input all the necessary arguments")) return;

        String idString = args[2];
        Integer addId = parsePositiveInt(idString, "The ID must be a positive integer.");
        if (addId == null) return;

        Integer quantity = parsePositiveInt(args[3], "The quantity must be a positive integer");
        if (quantity == null) return;

        Product product = productController.findProductById(addId);

        if (product == null) {
            System.err.println("ticket add: error (product with ID " + addId + " not found)");
            return;
        }

        try {
            currentTicket.addProductTicket(product, quantity);
            currentTicket.print();
            System.out.println("ticket add: ok");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    // Procesa el comando "ticket remove": verifica los argumentos,
    // maneja los errores correspondientes y utiliza Ticket
    // para eliminar todas las apariciones del producto del ticket.
    private void handleTicketRemove(String[] args) {
        if (requireMinArgs(args, 3, "Usage: ticket remove <prodId>")) return;

        String idString = args[2];
        Integer removeId = parsePositiveInt(idString, "The ID must be a positive integer.");
        if (removeId == null) return;

        boolean success = currentTicket.ticketRemove(removeId);
        if (success) {
            currentTicket.print();
            System.out.println("ticket remove: ok");
        } else {
            System.err.println("ticket remove: error (no product found with that ID)");
        }
    }
}
