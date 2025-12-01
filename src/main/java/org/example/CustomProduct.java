package org.example;

import java.util.ArrayList;
import java.util.List;

public class CustomProduct extends Product {
    private final int maxCustomizations;

    public CustomProduct(int id, String name, Category category, float price, int maxCustomizations) {
        super(id, name, category, price);
        this.maxCustomizations = maxCustomizations;
        if (maxCustomizations < 0){
            throw new IllegalArgumentException("Max customizations cannot be negative.");
        }
    }

    public int getMaxCustomizations() {
        return maxCustomizations;
    }

    @Override
    public String toString() {
        return "{class:ProductPersonalized, id:" + id
                + ", name:'" + name
                + "', category:" + category
                + ", price:" + getPrice()
                + ", maxPersonal:" + this.maxCustomizations + "}";
    }

    @Override
    public List<TicketLine> createTicketLine(int quantity, List<String> customTexts) {
        if (customTexts != null && customTexts.size() > this.maxCustomizations) {
            throw new IllegalArgumentException("can't add more than " +  this.maxCustomizations + " personalizations");
        }
        List<TicketLine> result = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            result.add(new TicketLineCustomProduct(this, customTexts));
        }
        return result;
    }
}
