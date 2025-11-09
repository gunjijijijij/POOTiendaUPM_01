package org.example;

import java.util.Random;

public class Cashier extends User {
    private static final String PREFIX = "UW";

    public Cashier(String name, String email) {
        super(generateId(), name, email);
    }

    public Cashier(String id, String name, String email) {
        super(id, name, email);
    }

    //Todo falta la parte de generar el id aleatorio, es PREFIX + Lo aleatorio
    private static String generateId() {
        Random random = new Random();
        String id;
    }


    @Override
    public String getType() {
        return "Cashier";
    }
}
