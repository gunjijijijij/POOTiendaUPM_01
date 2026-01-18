package org.example;

import java.util.ArrayList;
import java.util.List;

public class Product extends CatalogItem{
    protected String name;
    protected Category category;
    protected float price;


    public Product(String id, String name, Category category, float price) {
        super(id, category);
        this.name = name;
        this.price = price;
    }

    public Product(Product product) {
        this(product.getId(), product.getName(), product.getCategory(), product.getPrice());
    }

    public Product() {
        super();
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

    public void setCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }
        this.category = category;
    }
    public float getPrice() { return price; }

    public void setPrice(float price) {
        if (price <= 0) {
            throw new IllegalArgumentException("The price must be a number greater than 0 with no upper limit.");
        }
        this.price = price;
    }


    public double getDiscount() {
        return this.getCategory().calculateDiscount(getPrice());
    }

    public List<Product> addToTicket(int quantity, List<String> customTexts) {
        if (customTexts != null) {
            throw new IllegalArgumentException("this product doesn't support customizations");
        }
        List<Product> result = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            result.add(new Product(this));
        }
        return result;
    }

    @Override
    public String getDisplayId() {
        return String.valueOf(getId()); // "12"
    }

    @Override
    public boolean isService(){
        return false;
    }

    @Override
    public String toString() {
        return "{class:Product, id:" + getIdAsInt()
                + ", name:'" + name
                + "', category:" + getCategory()
                + ", price:" + price
                + "}";
    }
}
