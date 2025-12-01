package org.example;

import java.util.ArrayList;
import java.util.List;

public class Product {
    protected int id;
    protected String name;
    protected Category category;
    protected float price;

    public Product(Integer id, String name, Category category, float price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    // Getters y setters con validación

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank() || name.length() >= 100) {
            throw new IllegalArgumentException("The name cannot be empty and must contain less than 100 characters.");
        }
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        if (price <= 0) {
            throw new IllegalArgumentException("The price must be a number greater than 0 with no upper limit.");
        }
        this.price = price;
    }


    public List<TicketLine> createTicketLine(int quantity, List<String> customTexts) {
        if (customTexts != null) {
            throw new IllegalArgumentException("The custom text list can't be null.");
        }
        List<TicketLine> result = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            result.add(new TicketLine(this));
        }
        return result;
    }

    @Override
    public String toString() {
        return "{class:Product, id:" + id
                + ", name:'" + name
                + "', category:" + category
                + ", price:" + price + "}";
    }
}
