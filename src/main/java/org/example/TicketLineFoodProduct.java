package org.example;

import java.util.List;

public class TicketLineFoodProduct extends TicketLine {
    private final int numPeopleAttending;
    private final FoodProduct foodProduct;

    public TicketLineFoodProduct(FoodProduct product, int numPeopleAttending) {
        super(product);
        if (numPeopleAttending <= 0 || numPeopleAttending > product.getMaxPeople()) {
            throw new IllegalArgumentException("Number of people must be between 1 and " + product.getMaxPeople());
        }
        this.numPeopleAttending = numPeopleAttending;
        this.foodProduct = product;
    }

    @Override
    public float getPrice() {
        return foodProduct.getPricePerPerson() * numPeopleAttending;
    }

    public double getDiscount() {
        return 0;
    }

    @Override
    public String toString() {
        return "{class:Food"
                + ", id:" + product.id
                + ", name:" + product.name
                + ", category:" + product.category
                + ", price:" + this.getPrice()
                + ", max people allowed:" + foodProduct.getMaxPeople()
                + ", actual people in event:" + numPeopleAttending
                + "}";
    }
}
