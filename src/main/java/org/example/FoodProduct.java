package org.example;

import java.time.LocalDate;

public class FoodProduct extends Product {
    private LocalDate expirationDate;
    private int maxPeople;
    private float pricePerPerson;

    public FoodProduct(int id, String name, float pricePerPerson, LocalDate expirationDate, int maxPeople) {
        super(id, name, null, 0f, maxPeople);
        if (maxPeople > 100 || maxPeople <= 0) {
            throw new IllegalArgumentException("Max people MUST be between 1 and 100 ADIOOOSS");
        }
        if (!isValidCreation(expirationDate)) {
            throw new IllegalArgumentException("Food product requires AT LEAST 3 days planning");
        }
        this.expirationDate = expirationDate;
        this.maxPeople = maxPeople;
        this.pricePerPerson = pricePerPerson;
    }

    //TODO ESTE METODO SOLO COMPRUEBA QUE DENTRO DE 3 DIAS NO LLEGUE A LA FECHA DE CADUCIDAD, NO SE ME OCURRIÓ COMO COMPROBAR QUE LA PLANIFICACION SEA >=3 DIAS
    public boolean isValidCreation(LocalDate expiration) {
        return LocalDate.now().plusDays(3).isBefore(expiration);
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public String getType() {
        return "FoodProduct";
    }

    public double getDiscount() {
        return 0;
    }

    @Override
    public String toString() {
        return "{class: Product, id: " + id
                + ", name: '" + name
                + ", price per person: " + pricePerPerson
                + ", expirationdate: " + expirationDate
                + ", maxpeople: " + maxPeople + "}";
    }

    public float calculateTotalPrice(int numberOfPeople) {
        if (numberOfPeople > maxPeople || numberOfPeople <= 0) {
            throw new IllegalArgumentException("Number QUETAL of people must be between 1 and " + maxPeople);
        }
        return pricePerPerson * numberOfPeople;
    }
}
