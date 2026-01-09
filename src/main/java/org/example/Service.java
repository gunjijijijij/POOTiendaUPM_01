package org.example;

import java.util.Date;

public class Service {
    Date expirationDate;

    public Service(Date expirationDate){
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
}
