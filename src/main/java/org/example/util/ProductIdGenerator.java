package org.example.util;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class ProductIdGenerator { //clase similar a cashier
    private static final Random random = new Random();
    private static final List<Integer> available = new ArrayList<>();

    static {
        for (int i = 1; i <= 200; i++) {
            available.add(i);
        }
    }

    public static int generateId() {
        if (available.isEmpty()) {
            throw new IllegalStateException("No more product IDs available!");
        }

        int id = random.nextInt(available.size());

        return available.remove(id);
    }

    public static int validateId(int id) {
        if (id < 1 || id > 200) {
            throw new IllegalArgumentException("Product ID MUST be between 1 and 200.");
        }

        if (!available.contains(id)) {
            throw new IllegalArgumentException("Product ID " + id + " already exists.");
        }

        available.remove((Integer) id);

        return id;
    }

    public static void removeid(int id) {
        available.add((Integer) id); //para que quite el valor id en vez de la posicion
    }

    public static boolean idExists(int id) {
        return !available.contains(id);
    }
}
