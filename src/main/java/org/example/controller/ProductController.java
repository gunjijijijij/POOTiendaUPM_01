package org.example.controller;

import org.example.Category;
import org.example.CustomProduct;
import org.example.Product;
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
        boolean removed = products.removeIf(p -> p.getId() == id);

        if (!removed) {
            throw new IllegalArgumentException("The product with id " + id + " was not found");
        }
    }


    // Cambia el nombre, la categoría o el precio de un producto
    public static void prodUpdate(int id, String updateType, String newValue) {
        Product product = findProductById(id);

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

    // Encuentra un producto por su id
    public static Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
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
    public void handleProdRemove(String[] args) {
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
            System.out.println(removed.toString());
            System.out.println("prod remove: ok");
        } catch (IllegalArgumentException e) {
            System.err.println("prod remove: error (" + e.getMessage() + ")");
        }
    }

    // Procesa el comando "prod add": verifica los argumentos,
    // maneja los errores correspondientes y utiliza ProductController
    // para añadir el nuevo producto al catálogo.
    public void handleProdAdd(String[] args) {
        if (Utils.requireMinArgs(args, 5, "Usage: prod add <id> \"<name>\" <category> <price>")) return;

        Integer id = Utils.parsePositiveInt(args[2], "The ID must be a positive integer.");
        if (id == null) return;

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
            System.err.println("prod add: error (" + e.getMessage() + ")");
        }
    }

    // Procesa el comando "prod update": verifica los argumentos, validando el tipo de actualización,
    // maneja los errores correspondientes y utiliza ProductController
    // para actualizar el dato del producto.
    public void handleProdUpdate(String[] args) {
        if (Utils.requireMinArgs(args, 4, "Usage: prod update <id> NAME|CATEGORY|PRICE <value>")) return;

        Integer id = Utils.parsePositiveInt(args[2], "The ID must be a positive integer.");
        if (id == null) return;

        String field = args[3].toUpperCase();
        switch (field) {
            case "NAME":
                if (Utils.requireMinArgs(args, 5, "Usage: prod update <id> NAME \"<new name>\"")) return;
                String newName = Utils.joinQuoted(args, 4, args.length).trim();
                if (newName.isEmpty()) {System.err.println("The name is empty"); return; }
                prodUpdate(id, field, newName);
                break;

            case "CATEGORY":
                if (Utils.requireMinArgs(args, 5, "Usage: prod update <id> CATEGORY <newCategory>")) return;
                Category newCat = Utils.parseCategory(args[4]);
                if (newCat == null) return;
                prodUpdate(id, field, newCat.name());
                break;

            case "PRICE":
                if (Utils.requireMinArgs(args, 5, "Usage: prod update <id> PRICE <newPrice>")) return;
                Float newPrice = Utils.parseNonNegativeFloat(args[4]);
                if (newPrice == null) return;
                prodUpdate(id, field, Float.toString(newPrice));
                break;

            default: System.err.println("Field not supported. Use NAME, CATEGORY, or PRICE."); break;
        }
    }
}
