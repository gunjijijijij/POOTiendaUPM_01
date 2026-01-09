package org.example.util;

import org.example.Client;
import org.example.Cashier;
public class CompanyClient extends Client{
    public CompanyClient(String id, String name, String email, Cashier registeredBy) {
        super(id, name, email, registeredBy);
    }
    @Override
    public String toString() {
        return "CompanyClient{NIF='" + getId() + "', name='" + getName() + "'}";
    }
}
