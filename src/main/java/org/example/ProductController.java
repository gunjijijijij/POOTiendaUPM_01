package org.example;

import java.util.ArrayList;
import java.util.List;

public class ProductController {
    private final int MAX_PRODUCTS = 200;
    private List<Product> products = new ArrayList<>();

    // Añade un nuevo producto al catálogo si no existe otro con el mismo id
    public void addProduct(int id, String name, Category category, float prize) {
        for (Product product : products) {
            if (product.getId() == id) {
                throw new IllegalArgumentException("There can't be two products with the same id\n");
            }
        }
        if (products.size() >= MAX_PRODUCTS) {
            throw new IllegalStateException("No more products can be added, the maximum is " + MAX_PRODUCTS);
        }
        Product product = new Product(id, name, category, prize); //creamos y agregamos el producto
        products.add(product);
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
    public void prodRemove(int id) {
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

    // Encuentra un producto por su id
    public Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }
}
