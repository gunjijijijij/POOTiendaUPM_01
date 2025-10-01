package org.example;

import java.util.Scanner;

public class CLI {
    private static final int MAX_PRODUCTS = 200;
    private Product[] products;
    private int productcount;

    public CLI(){
        this.products = new Product[MAX_PRODUCTS];
        this.productcount = 0;
    }

    public void start() {
        init();

        Scanner sc = new Scanner(System.in);
        boolean finish = false;
       /* Product[] arrayProduct = new Product[100];*/

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
                    handleprodcommand(commandUni);
                    break;
                case "ticket":
                    handleticketcommand(commandUni);
                    break;
                case "exit":
                    finish = true;
                    break;
            }
        }
        end();
    }

    private void end() {
        System.out.println("Bye");
    }

    private void init() {
        System.out.println("Welcome to my extraordinary app");
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
        System.out.println("Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
        System.out.println("Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%,\n" +
                "ELECTRONICS 3%.");
    }

    private void echo(String mensaje){
        System.out.println(mensaje);
    }

    private void handleprodcommand(String[] args){
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

    private void handleticketcommand(String[] args){
        if (args.length < 2) {
            System.err.println("prod command needs two parameters \"\"prod \"<add|list|update|remove> ...\" \"\"");
            return;
        }

        switch (args[1]){
            case "new":
                break;
            case "add":
                break;
            case "remove":
                break;
            case "print":
                break;
        }
    }

    private void addproduct (String nombre, int id, Categoria categoria, float precio){
        for(int i=0; i<productcount; i++){
            if(products[i].getId().equals(id)){ //comprobamos que no existan dos productos con el mismo id
                throw new IllegalArgumentException("Existen dos productos con el mismo id");
            }
        }
        if(productcount >= MAX_PRODUCTS){ //verificamos el límite de productos
            throw new IllegalStateException("No se pueden agregar más productos, el máximo es " + MAX_PRODUCTS);
        }
        Product product = new Product(id, nombre, categoria,precio); //creamos y agregamos el producto
        products[productcount] = product;
        productcount++; //aumentamos el contador

    }
    private void prodlist(String[] listaproductos){
        System.out.println("Catalog:");
        if(productcount == 0){
            System.out.println("(empty)"); //No sé si hay que poner algo cuando esté vacío (?)
        }else{
            for(int i=0; i<productcount;i++){
                Product product = products[i];
                System.out.println("(class:Product, id:" + product.getId() + ", name: '" + product.getName() + "', category:" + product.getCategory() + ", price: " + product.getPrice() + ")");
            }
        }
        System.out.println("prod list: ok");


    }
}
