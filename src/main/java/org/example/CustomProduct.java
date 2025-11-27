package org.example;

import java.util.ArrayList;
import java.util.List;

public class CustomProduct extends Product {
    private static ArrayList<String> customtexts = new ArrayList<>();

    public CustomProduct(int id, String name, Category category, float price, Integer maxPers) {
        super(id, name, category, price, maxPers);

    }

    public static void addCustomText(String text) {
        if (customtexts.size() >= maxPers) {
            throw new IllegalArgumentException("Maximum custom texts reached: " + maxPers);
        }

        customtexts.add(text.trim());
    }

    public List<String> getCustomtexts() {
        return new ArrayList<>(customtexts);
    }

    @Override
    public float getPrice() {
        return super.getPrice() * (1 + (0.10f * customtexts.size()));
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
                + ", maxCustomtexts: " + maxPers + "}";
    }

    public void clearCustomTexts() {
        customtexts.clear();
    }
}
