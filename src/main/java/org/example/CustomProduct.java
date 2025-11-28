package org.example;

import java.util.ArrayList;
import java.util.List;

public class CustomProduct extends Product {
    private final List<String> customTexts = new ArrayList<>();

    public CustomProduct(int id, String name, Category category, float price, int maxPers) {
        super(id, name, category, price, maxPers);
        if (maxPers < 0){
            throw new IllegalArgumentException("Max customizations cannot be negative.");
        }
    }

    public void addCustomText(String text) {
        if (text == null||text.trim().isEmpty()) {
            throw new IllegalArgumentException("Custom text cannot be null or empty.");
        }
        if (customTexts.size() >= this.maxPers) {
            throw new IllegalArgumentException("Maximum custom texts reached: " + this.maxPers);
        }
        customTexts.add(text.trim());
    }
    public List<String> getCustomtexts() {
        return new ArrayList<>(customTexts);
    }

    @Override
    public float getPrice() {
        return super.getPrice() * (1 + (0.10f * customTexts.size()));
    }

    public String getType() {
        return "CustomProduct";
    }

    @Override
    public String toString() {
        return "{class: Product, id: " + id
                + ", name: '" + name
                + ", category: " + category
                + ", base price: " + super.getPrice()
                + ", final price: " + getPrice()
                + ", maxCustomtexts: " + this.maxPers + "}";
    }

    public void clearCustomTexts() {
        customTexts.clear();
    }
}
