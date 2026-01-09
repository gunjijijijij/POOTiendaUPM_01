package org.example;

import org.example.util.TicketIdGenerator;

import java.util.Date;

public class Service {
    String id;
    Date expirationDate;

    public Service(Date expirationDate){
        this.id = TicketIdGenerator.generateServiceTicketId();
        this.expirationDate = expirationDate;
    }

    public Date getExpirationDate(){
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate){
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "expirationDate=" + expirationDate + "}";
    }

    public String getId() {
        return id;
    }
}
