package org.example;

import org.example.util.Utils;
import org.example.exceptions.Validators;

import java.util.ArrayList;
import java.util.List;

public class ProductController {
    private static final int MAX_PRODUCTS = 200;
    private static List<Product> products = new ArrayList<>();

    // Añade un nuevo producto al catálogo si no existe otro con el mismo id
    private static void addProduct(int id, String name, Category category, float price, Integer maxPers) {
        Validators.requirePositiveInt(id, "The ID must be a positive integer");
        Validators.requireValidProductName(name);
        Validators.requireValidCategory(category);
        Validators.requireValidPrice(price);
        Validators.requireCapacityNotExceeded(products.size(), MAX_PRODUCTS);

        Product product = createProduct(id, name, category, price, maxPers);
        products.add(product);

        System.out.println(product);
        System.out.println("prod add: ok");
    }


    // Imprime los productos existentes en el catálogo
    public void prodList() {
        System.out.println("Catalog:");
        if (products.isEmpty()) {
            System.out.println("There aren't any products in the catalog.");
        } else {
            for (Product product : products) {
                System.out.println(product.toString());
            }
        }
    }

    // Elimina un producto existente del catálogo
    public static void prodRemove(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                products.remove(product);
                return;
            }
        }
        throw new IllegalArgumentException(String.format("The product with id %s was not found", id));
    }

    // Cambia el nombre, la categoría o el precio de un producto
    public void prodUpdate(int id, String updateType, String newValue) {
        Product product = this.findProductById(id);

        try {
            switch (updateType) {
                case "NAME":
                    product.setName(newValue);
                    break;

                case "CATEGORY":
                    product.setCategory(Category.valueOf(newValue));
                    break;

                case "PRICE":
                    product.setPrice(Float.parseFloat(newValue));
                    break;
            }
            System.out.println(product.toString());
            System.out.println("prod update: ok");

        } catch (IllegalArgumentException exception) {
            System.out.println("prod update: error (" + exception.getMessage() + ")");
        }
    }

    // Procesa el comando "prod add": verifica los argumentos,
    // maneja los errores correspondientes y utiliza ProductController
    // para añadir el nuevo producto al catálogo.
    public static void handleProdAdd(String[] args) {
        if (Utils.requireMinArgs(args, 5, "Usage: prod add <id> \"<name>\" <category> <price>")) return;

        Integer id = Utils.parsePositiveInt(args[2], "The id must be a positive integer.");
        Validators.requirePositiveInt(id, "The ID must be a positive integer.");

        String name = Utils.joinQuoted(args, 3, args.length - 2).trim();
        if (name.isEmpty()) {System.err.println("The name is empty."); return; }

        Category cat = Utils.parseCategory(args[args.length - 2]);
        if (cat == null) return;

        Float price = Utils.parseNonNegativeFloat(args[args.length - 1]);
        if (price == null) return;

        try {
            addProduct(id, name, cat, price, null);
        } catch (IllegalArgumentException e) {
            System.err.println( e.getMessage().trim());
        } catch (Exception e) {
            System.err.println( e.getMessage());
        }
    }

    // Encuentra un producto por su id
    public static Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    private static void validatePrice(float price) {
        if (price < 0)
            throw new IllegalArgumentException("The price must be a positive number");
    }

    private static void validateNoDuplicate(int id) {
        for (Product p : products) {
            if (p.getId() == id)
                throw new IllegalArgumentException("There can't be two products with the same id");
        }
    }

    private static void validateCapacity() {
        if (products.size() >= MAX_PRODUCTS)
            throw new IllegalStateException("No more products can be added");
    }

    private static Product createProduct(int id, String name, Category category, float price, Integer maxPers) {
        if (maxPers != null) {
            return new CustomProduct(id, name, category, price, maxPers);
        }
        return new Product(id, name, category, price, null);
    }


    // Procesa el comando "prod remove": verifica los argumentos,
    // maneja los errores correspondientes y utiliza ProductController
    // para eliminar el nuevo producto al catálogo.
    public static void handleProdRemove(String[] args) {
        if (Utils.requireMinArgs(args, 3, "Usage: prod remove <id>")) return;
        Integer id = Utils.parsePositiveInt(args[2], "The ID must be a positive integer.");
        if (id == null) return;

        Product removed = findProductById(id);
        if (removed == null) {
            System.err.println("prod remove: error (no product with that ID)");
            return;
        }
        try {
            prodRemove(id);
            //currentTicket.ticketRemove(id);
            System.out.println(removed.toString());
            System.out.println("prod remove: ok");
        } catch (IllegalArgumentException e) {
            System.err.println("prod remove: error (" + e.getMessage() + ")");
        }
    }

}
