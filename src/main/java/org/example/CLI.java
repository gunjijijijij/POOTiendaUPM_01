package org.example;

import java.util.Scanner;

public class CLI {
    private final Ticket currentTicket = new Ticket();
    private final ProductController productController = new ProductController();
    public void start() {
        init();

        Scanner sc = new Scanner(System.in);
        boolean finish = false;

        while(!finish){
            String command = sc.nextLine();
            String[] commandUni = command.split(" ");

            switch (commandUni[0]){
                case "help":
                    help();
                    break;

                case "echo":
                    if (commandUni.length < 2)
                        System.err.println("echo command needs two parameters \"\"echo \"<text>\" \"\"");

                    for (int i = 1; i < commandUni.length; i++) {
                        commandUni[i-1] = commandUni[i];
                    }
                    commandUni[commandUni.length - 1] = "";

                    String message = String.join(" ", commandUni);
                    echo(message);
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

    private void end() {
        System.out.println("Bye");
    }

    private void init() {
        System.out.println("Welcome to the ticket module App");
        System.out.println("Ticket Module. Type \"help\" to see commands");
    }

    private void help (){
        System.out.println("Commands:");
        System.out.println(" prod add <id> \"<name>\" <category> <price>");
        System.out.println(" prod list");
        System.out.println(" prod update <id> NAME|CATEGORY|PRICE <value>");
        System.out.println(" prod remove <id>");
        System.out.println(" ticket new");
        System.out.println(" ticket add <prodId> <quantity>");
        System.out.println(" ticket remove <prodId>");
        System.out.println(" ticket print");
        System.out.println(" echo \"<texto>\"");
        System.out.println(" help");
        System.out.println(" exit");
        System.out.println(" \n Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
        System.out.println("Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%,\n" +
                "ELECTRONICS 3%.");
    }

    private void echo(String message){
        if (message.isEmpty()){
            System.err.println("The echo command is empty");
            return;
        }

        System.out.println(message);
    }

    private void handleProdCommand(String[] args){
        if (args.length < 2) {
            System.err.println("prod command needs two parameters \"\"prod \"<add|list|update|remove> ...\" \"\"");
            return;
        }

        switch (args[1].toLowerCase()){
            case "add":
                if (args.length < 5) {
                    System.err.println("Please input all the necessary arguments");
                    return;
                }else{
                    if (isPositiveInteger(args[2])) {
                        System.err.println("The ID must be a positive integer.");
                        return;
                    }
                    int addId =  Integer.parseInt(args[2]);

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
                    if (price < 0){
                        System.err.println("The price can't be negative");
                        return;
                    }

                    // Nombre desde args[3] hasta args.length - 2
                    // prod add <id> "NAME" <CATEGORY> <PRICE>
                    String name = getNameInBrackets(args, 3, args.length - 2);
                    if (name.isEmpty()){
                        System.err.println("The name is empty");
                        return;
                    }

                    productController.addProduct(addId, name, category, price);
                    System.out.println("{class:Product, id:" + addId + ", name: '" + name + "', category:" + category + ", price: " + price + "}");
                }
                break;

            case "list":
                productController.prodList();
                break;

            case "update":
                if (isPositiveInteger(args[2])) {
                    System.err.println("The ID must be a positive integer.");
                    return;
                }
                int updateId = Integer.parseInt(args[2]);

                String category = args[3];
                if (category.equals("NAME")){
                    String name = getNameInBrackets(args, 4, args.length);
                    if (name.isEmpty()){
                        System.err.println("The name is empty");
                        return;
                    }

                    productController.prodUpdate(updateId, category, name);
                    break;
                }

                productController.prodUpdate(updateId, category, args[4]);
                break;

            case "remove":
                if (args.length < 3) {
                    System.out.println("Please input all the necessary arguments");
                }else{
                    String idString = args[2];
                    if (isPositiveInteger(idString)) {
                        System.err.println("The ID must be a positive integer.");
                        return;
                    }
                    int removeId =  Integer.parseInt(args[2]);
                    productController.prodRemove(removeId);
                    currentTicket.prodRemove(removeId);

                    //print del producto
                    System.out.println("prod remove: ok");
                }
                break;
        }
    }

    private void handleTicketCommand(String[] args){
        if (args.length < 2) {
            System.err.println("prod command needs two parameters \"\"prod \"<add|list|update|remove> ...\" \"\"");
            return;
        }

        switch (args[1]){
            case "new":
                currentTicket.resetTicket();
                System.out.println("The ticket reseted succesfully");
                break;

            case "add":
                if (args.length < 4) {
                    System.out.println("Please input all the necessary arguments");
                    return;
                }else{
                    String idString = args[2];
                    if (isPositiveInteger(idString)) {
                        System.err.println("The ID must be a positive integer.");
                        return;
                    }
                    int addId = Integer.parseInt(idString);

                    int quantity = Integer.parseInt(args[3]);
                    if (quantity < 0){
                        System.err.println("The quantity must be a positive integer");
                    }

                    Product product = productController.findProductById(addId);

                    currentTicket.addProductTicket(product, quantity);
                    System.out.println("Product added successfully");
                    break;
                }

            case "remove":
                if(args.length < 3){
                    System.out.println("Please input all the necessary arguments");
                }else{
                    String idString = args[3];
                    if (isPositiveInteger(idString)) {
                        System.err.println("The ID must be a positive integer.");
                        return;
                    }
                    int removeId = Integer.parseInt(idString);
                    currentTicket.prodRemove(removeId);
                    System.out.println("Product removed correctly from ticket");
                    break;
                }

            case "print":
                currentTicket.print();
                break;
        }
    }

    private String getNameInBrackets(String[] args, int i0, int iN){
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = i0; i < iN; i++) {
            nameBuilder.append(args[i]).append(" ");
        }

        String name = nameBuilder.toString().trim();

        if (name.startsWith("\"") && name.endsWith("\"")) {
            name = name.substring(1, name.length()-1);
        }

        return name;
    }

    private boolean isPositiveInteger(String args){
        try{
            int num = Integer.parseInt(args);
            return num <= 0;
        } catch (NumberFormatException e){
            return true;
        }
    }
}
