package org.example.util;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.example.Category;

public class Utils {
    // Verifica si hay suficientes argumentos
    public static boolean requireMinArgs(String[] args, int min, String usage) {
        if (args.length < min) { System.out.println(usage); return true; }
        return false;
    }

    // Parseo seguro de enteros positivos
    public static Integer parsePositiveInt(String s, String errMsg) {
        try {
            int v = Integer.parseInt(s);
            if (v <= 0) { System.out.println(errMsg); return null; }
            return v;
        } catch (NumberFormatException e) {
            System.out.println(errMsg);
            return null;
        }
    }

    // Parseo seguro de precios (float no negativos)
    public static Float parseNonNegativeFloat(String s) {
        try {
            float v = Float.parseFloat(s);
            if (v < 0f) { System.out.println("Price must be a non-negative number."); return null; }
            return v;
        } catch (NumberFormatException e) {
            System.out.println("Price must be a non-negative number.");
            return null;
        }
    }

    // Convierte un texto a una categoría válida
    public static Category parseCategory(String s) {
        try {
            return Category.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid category. Use: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
            return null;
        }
    }

    // Une argumentos entre comillas (para los nombres de productos con espacios)
    public static String joinQuoted(String[] args, int i0, int iN) {
        StringBuilder sb = new StringBuilder();
        for (int i = i0; i < iN; i++) sb.append(args[i]).append(" ");
        String name = sb.toString().trim();
        if (name.startsWith("\"") && name.endsWith("\"") && name.length() >= 2) {
            name = name.substring(1, name.length() - 1);
        }
        return name;
    }

    public static String parseIntToString(int number) {
        return Integer.toString(number);
    }


    public static ArrayList<String> parseCustomTexts(String[] args) {
        ArrayList<String> customTexts = new ArrayList<>();

        for (int i = 6; i < args.length; i++) {
            if (args[i].startsWith("--p")) {
                try {
                    String text = args[i].substring(3);
                    customTexts.add(text);
                } catch (IndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("there should be text following the --p flag");
                }
            }
        }
        return customTexts.isEmpty() ? null : customTexts;
    }

    public static LocalDate parseExpirationDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            System.out.println("Invalid date format. Use yyyy-MM-dd");
            return null;
        }
    }
    public static boolean isValidFoodCreation(LocalDate expiration) {
        return LocalDate.now().plusDays(3).isBefore(expiration);
    }
    public static boolean isValidMeetingCreation(LocalDate expiration){ //requiere al menos doce horas de planning
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minDateTime = now.plusHours(12);
        LocalDateTime expirationDateTime = expiration.atTime(23,59); //Es la fecha expiration a la última hora posible
        return expirationDateTime.isAfter(minDateTime); //el dia expiration a las 23:59 sea al menos 12 horas despues de NOW
    }
}
