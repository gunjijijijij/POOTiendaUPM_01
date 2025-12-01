package org.example.util;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class CashierIdGenerator {
    private static final Random random = new Random();
    private static final List<String> usedIds = new ArrayList<>();

    public static String generateId() {
        String id;
        do {
            id = createRandomId();
        } while (usedIds.contains(id));

        usedIds.add(id);
        return id;
    }

    private static String createRandomId() {
        StringBuilder sb = new StringBuilder("UW");

        for (int i = 0; i < 7; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

    public static String validateId(String id) {
        if (id.length() != 9) {
            throw new IllegalArgumentException("Cashier ID must have format: UWxxxxxxx");
        }

        if (!id.startsWith("UW")) {
            throw new IllegalArgumentException("Cashier ID must start with 'UW'");
        }

        String numbers = id.substring(2);
        if (!numbers.matches("\\d{7}")) {
            throw new IllegalArgumentException("Cashier ID must have 7 digits after 'UW'");
        }

        if (usedIds.contains(id)) {
            throw new IllegalArgumentException("Cashier ID " + id + " is already in use.");
        }

        usedIds.add(id);

        return id;
    }

    public static void removeId(String id) {
        usedIds.remove(id);
    }

    public static boolean idExist(String id) {
        return usedIds.contains(id);
    }
}
