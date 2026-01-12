package org.example;

import org.example.util.TicketIdGenerator;

import java.util.Date;

public class ProductService {
    String id;
    Date expirationDate;
    Category category;

    public ProductService(Date expirationDate, Category category){
        this.id = TicketIdGenerator.generateServiceTicketId();
        this.expirationDate = expirationDate;
        this.category = category;
    }

    public Date getExpirationDate(){
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate){
        this.expirationDate = expirationDate;
    }

    public Category getCategory(){
        return category;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{class:ProductService, id:" + getId() +
                ", category:" + getCategory() +
                ", expiration:" + expirationDate + "}";
    }
}
