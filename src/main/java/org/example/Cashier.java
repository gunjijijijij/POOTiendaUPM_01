package org.example;

import org.example.util.CashierIdGenerator;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Cashier extends User {
    private static final String PREFIX = "UW";
    private static final Random random = new Random();
    private static final List<Category> tickets = new ArrayList<>();

    public Cashier(String name, String email) {super(CashierIdGenerator.generateId(), name, email);}

    public Cashier(String id, String name, String email) {
        super(CashierIdGenerator.validateId(id), name, email);
    }

    @Override
    public String getType() {
        return "Cashier";
    }

    public String toString(){
        return "Cash {identifier ='" + getId() + "', name = '" + getName() + "', email = '"
                + getEmail() + "'}";
    }
}
