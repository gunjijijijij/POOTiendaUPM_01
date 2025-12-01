package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FoodProduct extends Product {
    private final LocalDate expirationDate;
    private final int maxPeople;
    private final float pricePerPerson;

    public FoodProduct(int id, String name, float pricePerPerson, LocalDate expirationDate, int maxPeople) {
        super(id, name, null, 0f);
        if (maxPeople > 100 || maxPeople <= 0) {
            throw new IllegalArgumentException("Max people MUST be between 1 and 100");
        }
        if (!isValidCreation(expirationDate)) {
            throw new IllegalArgumentException("Food product requires AT LEAST 3 days planning");
        }
        this.expirationDate = expirationDate;
        this.maxPeople = maxPeople;
        this.pricePerPerson = pricePerPerson;
    }

    // TODO ESTE METODO SOLO COMPRUEBA QUE DENTRO DE 3 DIAS NO LLEGUE A LA FECHA DE CADUCIDAD, NO SE ME OCURRIÓ COMO COMPROBAR QUE LA PLANIFICACION SEA >=3 DIAS
    public boolean isValidCreation(LocalDate expiration) {
        return LocalDate.now().plusDays(3).isBefore(expiration);
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public float getPricePerPerson() {
        return pricePerPerson;
    }

    public double getDiscount() {
        return 0;
    }

    @Override
    public String toString() {
        return "{class: Food, id:" + id
                + ", name:'" + name
                + ", price:" + price
                + ", date of Event:" + expirationDate
                + ", maxpeople:" + maxPeople + "}";
    }

    public float calculateTotalPrice(int numberOfPeople) {
        if (numberOfPeople > maxPeople || numberOfPeople <= 0) {
            throw new IllegalArgumentException("Number QUETAL of people must be between 1 and " + maxPeople);
        }
        return pricePerPerson * numberOfPeople;
    }

    @Override
    public List<TicketLine> createTicketLine(int quantity, List<String> customTexts) {
        if (customTexts != null ) throw new IllegalArgumentException("this product doesn't support customizations");
        List<TicketLine> result = new ArrayList<>();
        result.add(new TicketLineFoodProduct(this, quantity));
        return result;
    }
}
