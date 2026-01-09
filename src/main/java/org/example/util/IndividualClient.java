package org.example.util;

import org.example.Cashier;
import org.example.Client;
public class IndividualClient extends Client{
    public IndividualClient(String id, String name, String email, Cashier registeredBy) {
        super(id, name, email, registeredBy);
    }
    public String toString() {
        return "IndividualClient{DNI/NIE='" + getId() + "', name='" + getName() + "', email='" + getEmail() + "'}";
    }
}
