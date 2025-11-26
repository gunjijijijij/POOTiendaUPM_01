package org.example;

import java.util.ArrayList;
import java.util.List;

public class CustomProduct extends Product {
    private List<String> customtexts;

    public CustomProduct(int id, String name, Category category, float price, Integer maxPers) {
        super(id, name, category, price, maxPers);
        this.customtexts = new ArrayList<>();
    }

    public void addCustomText(String text) {

        if (customtexts.size() >= maxPers) {
            throw new IllegalArgumentException("Maximum custom texts reached: " + maxPers);
        }
        customtexts.add(text.trim());
        calculatePrice();
    }

    private void calculatePrice() {
        this.price = super.price + (super.price * (float) 0.10 * customtexts.size());
    }

    public List<String> getCustomtexts() {
        return new ArrayList<>(customtexts);
    }

    public int getMaxCustomtexts() {
        return maxPers;
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
