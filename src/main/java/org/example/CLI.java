package org.example;

import java.sql.SQLOutput;
import java.util.Scanner;

public class CLI {
    private Ticket currentTicket = new Ticket();

    public CLI(){}

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
                        commandUni[i] = "";
                    }

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

    private void echo(String mensaje){
        System.out.println(mensaje);
    }

    private void handleProdCommand(String[] args){
        if (args.length < 2) {
            System.err.println("prod command needs two parameters \"\"prod \"<add|list|update|remove> ...\" \"\"");
            return;
        }

        switch (args[1]){
            case "add":

                break;
            case "list":

                break;
            case "update":

                break;
            case "remove":

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
                int addId = Integer.parseInt(args[3]);
                int quantity = Integer.parseInt(args[4]);
                currentTicket.addProduct(addId,quantity);
                System.out.println("Product added successfully");
                break;
                }
            case "remove":
                if(args.length < 3){
                    System.out.println("Please input all the necessary arguments");
                    return;
                }else{
                    int removeIid = Integer.parseInt(args[3]);
                    currentTicket.removeProduct(removeIid);
                    System.out.println("Product removed correctly from ticket");
                    break;
                }

            case "print":
                currentTicket.print();
                break;
        }
    }


}
