package org.example;

public class Product {
    int id;
    String name;
    Category category;
    float price;

    public Product(Integer id, String name, Category category, float price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

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

    @Override
    public String toString() {
        return "{class: Product, id: " + id
                + ", name: '" + name
                + "', category: " + category
                + ", price: " + price + "}";
    }

}
