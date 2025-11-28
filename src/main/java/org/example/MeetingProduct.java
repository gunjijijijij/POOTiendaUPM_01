package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MeetingProduct extends Product {

    private LocalDate expirationDate;
    private int maxPeople;
    private float pricePerPerson;

    public MeetingProduct(int id, String name, float pricePerPerson, LocalDate expirationDate, int maxPeople) {
        super(id, name, null, 0f, maxPeople);

        if (maxPeople > 100 || maxPeople <= 0) {
            throw new IllegalArgumentException("Max people MUST be between 1 and 100 ME CAGO EN DIOS");
        }

        if (!isValidCreation(expirationDate)) {
            throw new IllegalArgumentException("Meeting product requires AT LEAST 12 hours planning");
        }

        this.expirationDate = expirationDate;
        this.maxPeople = maxPeople;
        this.pricePerPerson = pricePerPerson;
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

    public String getType() {
        return "MeetingProduct";
    }

    @Override
    public String toString() {
        return "{class: Product, id: " + id
                + ", name: '" + name
                + "', pricePerPerson: " + pricePerPerson
                + ", expirationDate: " + expirationDate
                + ", maxPeople: " + maxPeople + "}";
    }

    public float calculateTotalPrice(int numberOfPeople) {
        if (numberOfPeople > maxPeople || numberOfPeople <= 0) {
            throw new IllegalArgumentException(
                    "Number of people YA NO? must be between 1 and " + maxPeople);
        }
        return pricePerPerson * numberOfPeople;
    }
}
