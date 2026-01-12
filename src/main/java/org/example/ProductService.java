package org.example;

import org.example.util.TicketIdGenerator;

import java.time.LocalDate;

public class ProductService extends CatalogItem {
    private final LocalDate expirationDate;
    private static int serviceCounter = 1;

    public ProductService(LocalDate expirationDate, Category category) {
        super(generateServiceId(), category);
        if (expirationDate == null) throw new IllegalArgumentException("expiration can't be null");
        this.expirationDate = expirationDate;
    }

    public LocalDate getExpirationDate() { return expirationDate; }

    @Override public boolean isService() { return true; }

    @Override
    public String getDisplayId() {
        return getId() + "S"; // "1S"
    }

    private static String generateServiceId() {
        return serviceCounter++ + "S";
    }

    @Override
    public String toString() {
        return "{class:ProductService, id:" + getDisplayId() +
                ", category:" + category +
                ", expiration:" + expirationDate + "}";
    }
}
