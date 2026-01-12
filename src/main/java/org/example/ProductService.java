package org.example;

import org.example.util.TicketIdGenerator;

import java.time.LocalDate;

public class ProductService extends CatalogItem {
    private final LocalDate expirationDate;

    public ProductService(Integer id, Category category, LocalDate expirationDate) {
        super(id, category);
        if (expirationDate == null) throw new IllegalArgumentException("expiration can't be null");
        this.expirationDate = expirationDate;
    }

    public LocalDate getExpirationDate() { return expirationDate; }

    @Override public boolean isService() { return true; }

    @Override
    public String getDisplayId() {
        return getId() + "S"; // "1S"
    }

    @Override
    public String toString() {
        return "{class:ProductService, id:" + id +
                ", category:" + category +
                ", expiration:" + expirationDate + "}";
    }
}
