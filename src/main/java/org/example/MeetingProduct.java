package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MeetingProduct extends Product {

    private final LocalDate expirationDate;
    private final int maxPeople;
    private final float pricePerPerson;

    public MeetingProduct(int id, String name, float pricePerPerson, LocalDate expirationDate, int maxPeople) {
        super(id, name, null, 0f);

        if (maxPeople > 100 || maxPeople <= 0) {
            throw new IllegalArgumentException("Max people MUST be between 1 and 100");
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

    public double getDiscount() {
        return 0;
    }

    @Override
    public String toString() {
        return "{class:Meeting, id:" + id
                + ", name:'" + name
                + "', price:" + price
                + ", date of Event:" + expirationDate
                + ", max people allowed:" + maxPeople + "}";
    }

    @Override
    public List<TicketLine> createTicketLine(int quantity, List<String> customTexts) {
        if (customTexts != null ) throw new IllegalArgumentException("this product doesn't support customizations");
        List<TicketLine> result = new ArrayList<>();
        result.add(new TicketLineMeetingProduct(this, quantity));
        return result;
    }

    public float getPricePerPerson() {
        return pricePerPerson;
    }
}
