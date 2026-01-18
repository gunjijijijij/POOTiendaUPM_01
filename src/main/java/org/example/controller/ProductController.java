package org.example.controller;

import org.example.*;
import org.example.util.ProductIdGenerator;
import org.example.util.Utils;
import org.example.exceptions.Validators;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    private static final int MAX_PRODUCTS = 200;
    private static ProductController instancia;
    private static final List<CatalogItem> products = new ArrayList<>();

    private ProductController(){}

    public static ProductController getInstance(){
        if (instancia == null){
            instancia = new ProductController();
        }
        return instancia;
    }
    public List<CatalogItem> getProducts() {
        return products;
    }

    // Imprime los productos existentes en el catálogo
    public void prodList() {
        System.out.println("Catalog:");
        if (products.isEmpty()) {
            System.out.println("There aren't any products in the catalog.");
        } else {
            for (CatalogItem product : products) {
                System.out.println("  " + product.toString());
            }
        }
    }

    // Elimina un producto existente del catálogo
    public void prodRemove(String id) {
        boolean removed = products.removeIf(p -> p.getId().equals(id));

        if (!removed) {
            throw new IllegalArgumentException("The product with id " + id + " was not found");
        }
    }

    // Cambia el nombre, la categoría o el precio de un producto
    public void prodUpdate(String id, String updateType, String newValue) {
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
            System.out.println(product);
            System.out.print("prod update: ok");
            System.out.println();

        } catch (IllegalArgumentException exception) {
            System.out.println("prod update: error (" + exception.getMessage() + ")");
        }
    }

    // Encuentra un producto por su id
    public Product findProductById(String id) {

        for (CatalogItem item : products) {
            if (item.getId().equals(id) && item instanceof Product) {
                return (Product) item;
            }
        }
        return null;
    }

    // Procesa el comando "prod remove": verifica los argumentos,
    // maneja los errores correspondientes y utiliza ProductController
    // para eliminar el nuevo producto al catálogo.
    public void handleProdRemove(String[] args) {
        if (Utils.requireMinArgs(args, 3, "Usage: prod remove <id>")) return;

        Product removed = findProductById(args[2]);
        if (removed == null) {
            System.out.println("prod remove: error (no product with that ID)");
            return;
        }

        try {
            prodRemove(args[2]);
            System.out.println(removed);
            System.out.println("prod remove: ok");
        } catch (IllegalArgumentException e) {
            System.out.println("prod remove: error (" + e.getMessage() + ")");
        }
    }

    // Procesa el comando "prod add": verifica los argumentos,
    // maneja los errores correspondientes y utiliza ProductController
    // para añadir el nuevo producto al catálogo.

    public void addService(LocalDate expirationDate, Category category) {
        // Validaciones

        if (expirationDate == null) {
            throw new IllegalArgumentException("Expiration date cannot be null");
        }

        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }

        // Crear y agregar
        Service service = new Service(expirationDate, category);
        products.add(service);
    }
    private void addProduct(String id, String name, Category category, float price, Integer maxPers) {
        Validators.requireValidProductName(name);
        Validators.requireValidCategory(category);
        Validators.requireValidPrice(price);

        if (products.size() < MAX_PRODUCTS){
            Product product = createProduct(id, name, category, price, maxPers);
            products.add(product);

            System.out.println(product);
            System.out.println("prod add: ok");
        }else{
            System.out.println("No more products can be added, the maximum is " + MAX_PRODUCTS);
        }
    }

    private Product createProduct(String id, String name, Category category, float price, Integer maxPers) {
        if (maxPers != null) {
            return new CustomProduct(id, name, category, price, maxPers);
        }
        return new Product(id, name, category, price);
    }

    public void handleProdAdd(String[] args) {
        if (args.length < 4) {
            System.out.println("Usage: prod add <id> \"<name>\" <category> <price> [<maxPers>]");
            System.out.println("   or: prod add <expiration: yyyy-MM-dd> <category>");
            return;
        }

        if (Utils.isDateYYYYMMDD(args[2])){
            System.out.println("Es dia");
            if (args.length != 4){
                System.out.println("Usage: prod add <expiration: yyyy-MM-dd> <category>");
                return;
            }

            LocalDate expiration = Utils.parseDateYYYYMMDD(args[2]);

            if (expiration == null) {
                System.out.println("prod add: error (invalid expiration format, expected yyyy-MM-dd)");
                return;
            }

            Category category = Utils.parseCategory(args[3]);
            if (category == null) return;

            try {
                addService(expiration, category);
                System.out.println("prod add: ok");
            } catch (IllegalArgumentException e) {
                System.out.println("prod add: error (" + e.getMessage() + ")");
            }
            return;
        }

        if (findProductById(args[2]) != null){
            System.out.println("prod add: error (A product with that id already exists)");
            return;
        };

        int i;
        for (i = 4; i < args.length; i++) {
            if (args[i].endsWith("\"")) {
                break;
            }
        }

        String name = args[3].trim();
        if (name.isEmpty()) {
            System.out.println("The name is empty.");
            return;
        }

        Category category = Utils.parseCategory(args[4]);
        if (category == null) return;

        Float price = Utils.parseNonNegativeFloat(args[5]);
        if (price == null) return;

        Integer maxPers = null;
        if (args.length == 7) {
            maxPers = Utils.parsePositiveInt(args[6], "The maximum number of people allowed should be a positive integer");
        }

        try {
            addProduct(args[2], name, category, price, maxPers);
        } catch (IllegalArgumentException e) {
            System.out.println("prod add: error (" + e.getMessage() + ")");
        }
    }

    // Procesa el comando "prod update": verifica los argumentos, validando el tipo de actualización,
    // maneja los errores correspondientes y utiliza ProductController
    // para actualizar el dato del producto.
    public void handleProdUpdate(String[] args) {
        if (Utils.requireMinArgs(args, 4, "Usage: prod update <id> NAME|CATEGORY|PRICE <value>")) return;

        Product updated = findProductById(args[2]);
        if (updated == null) {
            System.out.println("prod update: error (no product with that ID)");
            return;
        }

        String field = args[3].toUpperCase();
        switch (field) {
            case "NAME":
                if (Utils.requireMinArgs(args, 5, "Usage: prod update <id> NAME \"<new name>\"")) return;
                String newName = args[4].trim();
                if (newName.isEmpty()) {
                    System.out.println("The name is empty");
                    return;
                }
                prodUpdate(args[2], field, newName);
                break;

            case "CATEGORY":
                if (Utils.requireMinArgs(args, 5, "Usage: prod update <id> CATEGORY <newCategory>")) return;
                Category newCat = Utils.parseCategory(args[4]);
                if (newCat == null) return;
                prodUpdate(args[2], field, newCat.name());
                break;

            case "PRICE":
                if (Utils.requireMinArgs(args, 5, "Usage: prod update <id> PRICE <newPrice>")) return;
                Float newPrice = Utils.parseNonNegativeFloat(args[4]);
                if (newPrice == null) return;
                prodUpdate(args[2], field, Float.toString(newPrice));
                break;

            default:
                System.out.println("Field not supported. Use NAME, CATEGORY, or PRICE.");
                break;
        }
    }

    public void handleProdAddFood(String[] args) {
        if (Utils.requireMinArgs(args, 6, "Usage: prod addFood [<id>] \"<name>\" <price> <expiration:yyyy-MM-dd> <max_people>")) {
            return;
        }
        Integer id = null;
        int index = 2;
        if (args.length == 6) { //recordar que el id es opcional en food product
            id = Utils.parsePositiveInt(args[2], "The ID must be a positive integer.");
            if (id == null) return;
            index = 3;
        }
        String name = args[3];
        if (name.isEmpty()) {
            System.out.println("The name is empty.");
            return;
        }
        Float price = Utils.parseNonNegativeFloat(args[args.length - 3]);
        if (price == null) return;

        LocalDate expiration = Utils.parseExpirationDate(args[args.length - 2]);
        if (expiration == null) return;

        Integer maxPeople = Utils.parsePositiveInt(args[args.length - 1], "Max people must be a positive integer.");
        if (maxPeople == null) return;
        if (maxPeople < 1 || maxPeople > 100) {
            System.out.println("Max people must be between 1 and 100");
            return;
        }
        if (!Utils.isValidFoodCreation(expiration)) {
            System.out.println("Food product requires at least 3 days planning");
            return;
        }
        addFoodProduct(id, name, price, expiration, maxPeople);
    }

    private void addFoodProduct(Integer id, String name, float price, LocalDate expiration, int maxPeople) {
        int finalid = (id != null) ? id : ProductIdGenerator.generateId(); //si no existe id, lo genera
        if (findProductById(String.valueOf(finalid)) != null) {
            throw new IllegalArgumentException("Product ID " + finalid + " already exists");
        }
        FoodProduct product = new FoodProduct(String.valueOf(finalid), name, price, expiration, maxPeople);
        products.add(product);
        System.out.println(product);
        System.out.println("prod addFood: ok");
    }

    public void handleProdAddMeeting(String[] args) {
        if (Utils.requireMinArgs(args, 6, "Usage: prod addMeeting [<id>] \"<name>\" <price> <expiration:yyyy-MM-dd> <max_people>")) {
            return;
        }

        Integer id = 0;
        int index = 2;

        if (!args[2].startsWith("\"")) { //recordar que el id es opcional, comprobamos si hay
            id = Utils.parsePositiveInt(args[2], "The ID must be a positive integer.");
            if (id == null) return;
            index = 3;
        }

        String name = args[index];
        if (name.isEmpty()) {
            System.out.println("The name is empty.");
            return;
        }
        Float price = Utils.parseNonNegativeFloat(args[args.length - 3]);
        if (price == null) return;

        LocalDate expiration = Utils.parseExpirationDate(args[args.length - 2]);
        if (expiration == null) return;

        Integer maxPeople = Utils.parsePositiveInt(args[args.length - 1], "Max people must be a positive integer.");
        if (maxPeople == null) return;
        if (maxPeople < 1 || maxPeople > 100) {
            System.out.println("Max people must be between 1 and 100");
            return;
        }
        if (!Utils.isValidMeetingCreation(expiration)) {
            System.out.println("Meeting product requires at least 12 hours planning");
            return;
        }
        addMeetingProduct(id, name, price, expiration, maxPeople);
    }

    private void addMeetingProduct(Integer id, String name, float price, LocalDate expiration, int maxPeople) {
        int finalid = (id != null) ? id : ProductIdGenerator.generateId(); //si no existe id, lo genera
        if (findProductById(String.valueOf(finalid)) != null) {
            throw new IllegalArgumentException("Product ID " + finalid + " already exists");
        }
        MeetingProduct product = new MeetingProduct(String.valueOf(finalid), name, price, expiration, maxPeople);
        products.add(product);
        System.out.println(product);
        System.out.println("prod addMeeting: ok");
    }

    // Reemplaza la lista actual de productos con los datos cargados desde la persistencia
    public void setProducts(List<CatalogItem> loadedProducts) {
        products.clear();
        products.addAll(loadedProducts);
    }
}

