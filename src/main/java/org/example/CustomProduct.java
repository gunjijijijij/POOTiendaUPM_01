package org.example;

import java.util.ArrayList;
import java.util.List;

public class CustomProduct extends Product {
    private final int maxCustomizations;
    private List<String> customTexts;

    public CustomProduct(int id, String name, Category category, float price, int maxCustomizations) {
        super(id, name, category, price);
        this.maxCustomizations = maxCustomizations;
        if (maxCustomizations < 0) {
            throw new IllegalArgumentException("Max customizations cannot be negative.");
        }
    }

    public CustomProduct(CustomProduct customProduct, List<String> customTexts) {
        this(customProduct.getIdAsInt(), customProduct.getName(), customProduct.getCategory(), customProduct.getPrice(), customProduct.getMaxCustomizations());
        this.customTexts = customTexts;
    }

    public int getMaxCustomizations() {
        return maxCustomizations;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{class:ProductPersonalized, id:").append(id)
                .append(", name:'").append(name)
                .append("', category:").append(category)
                .append(", price:").append(this.getPrice())
                .append(", maxPersonal:").append(maxCustomizations);
        if (customTexts != null) {
            sb.append(", customizations:").append(customTexts);
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public List<Product> addToTicket(int quantity, List<String> customTexts) {
        if (customTexts != null && customTexts.size() > this.maxCustomizations) {
            throw new IllegalArgumentException("can't add more than " + this.maxCustomizations + " personalizations");
        }
        List<Product> result = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            result.add(new CustomProduct(this, customTexts));
        }
        return result;
    }

    @Override
    public float getPrice() {
        if (customTexts != null)
            return super.getPrice() * (1 + (0.10f * customTexts.size()));
        return super.getPrice();
    }
}
