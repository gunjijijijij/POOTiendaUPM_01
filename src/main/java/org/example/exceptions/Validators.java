package org.example.exceptions;

import org.example.Category;


public class Validators {
    public static void requirePositiveInt(int value, String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireValidProductName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The name cannot be empty.");
        }
    }

    public static void requireValidCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("The category cannot be null.");
        }
    }

    public static void requireValidPrice(float price) {
        if (price < 0) {
            throw new IllegalArgumentException("The price must be a non-negative value.");
        }
    }

}
