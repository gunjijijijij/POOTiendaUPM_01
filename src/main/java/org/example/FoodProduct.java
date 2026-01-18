package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FoodProduct extends PeopleProduct {
    public FoodProduct(String id, String name, float pricePerPerson, LocalDate expirationDate, int maxPeople) {
        super(id, name, pricePerPerson, expirationDate, maxPeople);
    }

    public FoodProduct(FoodProduct foodProduct, int quantity) {
        super(foodProduct, quantity);
    }

    public FoodProduct() {
        super();
    }

    @Override
    public String toString() {
        return buildToString("Food");
    }

    @Override
    public List<Product> addToTicket(int quantity, List<String> customTexts) {
        if (customTexts != null) throw new IllegalArgumentException("this product doesn't support customizations");
        List<Product> result = new ArrayList<>();
        result.add(new FoodProduct(this, quantity));
        return result;
    }
}
