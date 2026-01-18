package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class PeopleProduct extends Product {

    private final LocalDate expirationDate;
    private final int maxPeople;
    private int numPeopleAttending = 0;

    public PeopleProduct(String id, String name, float pricePerPerson, LocalDate expirationDate, int maxPeople) {
        super(id, name, null, pricePerPerson);

        if (maxPeople > 100 || maxPeople <= 0) {
            throw new IllegalArgumentException("Max people MUST be between 1 and 100");
        }

        if (!isValidCreation(expirationDate)) {
            throw new IllegalArgumentException("This kind of product requires AT LEAST 12 hours planning");
        }

        this.expirationDate = expirationDate;
        this.maxPeople = maxPeople;
    }
    
    public PeopleProduct(PeopleProduct peopleProduct, int numPeopleAttending) {
        this(peopleProduct.getId(), peopleProduct.getName(), peopleProduct.getPricePerPerson(), peopleProduct.getExpirationDate(), peopleProduct.getMaxPeople());
        this.numPeopleAttending = numPeopleAttending;
    }

    // Comprueba que falten al menos 12 horas para la fecha de caducidad
    public boolean isValidCreation(LocalDate expiration) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minDateTime = now.plusHours(12);
        LocalDateTime expirationDateTime = expiration.atTime(23, 59);
        return expirationDateTime.isAfter(minDateTime);
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public double getDiscount() {
        return 0;
    }

    protected String buildToString(String className) {
        StringBuilder sb = new StringBuilder();
        sb.append("{class:").append(className).append(", id:").append(getId())
                .append(", name:'").append(name)
                .append("', price:").append(price * numPeopleAttending)
                .append(", date of Event:").append(expirationDate)
                .append(", max people allowed:").append(maxPeople);
        if (numPeopleAttending != 0) {
            sb.append(", people attending:").append(numPeopleAttending);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public abstract List<Product> addToTicket(int quantity, List<String> customTexts);

    public float getPricePerPerson() {
        return price;
    }

    public float getPrice() {
        return price * numPeopleAttending;
    }
}

