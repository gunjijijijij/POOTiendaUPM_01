package org.example;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class ProductIdGenerator { //clase similar a cashier
    private static final Random random = new Random();
    private static final List<Integer> usedIds = new ArrayList<>();

    private static int generateId() {
        int id;
        int attempts = 0;
        final int MAX_ATTEMPTS = 1000;
        do {
            id = random.nextInt(200) + 1;
            attempts++;
            if (attempts > MAX_ATTEMPTS) {
                throw new IllegalStateException("Cannot generate unique PRODUCT ID after " + MAX_ATTEMPTS + " attempts.");
            }
        } while (usedIds.contains(id));
        usedIds.add(id);
        return id;
    }

    public static int validateId(int id) {
        if (id < 1 || id > 200) {
            throw new IllegalArgumentException("Product ID MUST be between 1 and 200.");
        }
        if (usedIds.contains(id)) {
            throw new IllegalArgumentException("Product ID " + id + " already exists.");
        }
        return id;
    }

    //TODO NO SE SI LAS TRES CLASES DE ABAJO SON NECESARIAS PORQUE HAY FUNCIONES PARECIDAS EN PRODUCTCONTROLLER
    public static List<Integer> getUsedIds() {
        return new ArrayList<>(usedIds);
    }

    public static void removeid(int id) {
        usedIds.remove((Integer) id); //para que quite el valor id en vez de la posicion
    }

    public static boolean idExists(int id) {
        return usedIds.contains(id);
    }
}
