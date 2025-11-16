package org.example;

public class Utils {
    // Verifica si hay suficientes argumentos
    public static boolean requireMinArgs(String[] args, int min, String usage) {
        if (args.length < min) { System.err.println(usage); return true; }
        return false;
    }

    // Parseo seguro de enteros positivos
    public static Integer parsePositiveInt(String s, String errMsg) {
        try {
            int v = Integer.parseInt(s);
            if (v <= 0) { System.err.println(errMsg); return null; }
            return v;
        } catch (NumberFormatException e) {
            System.err.println(errMsg);
            return null;
        }
    }

    // Parseo seguro de precios (float no negativos)
    public static Float parseNonNegativeFloat(String s) {
        try {
            float v = Float.parseFloat(s);
            if (v < 0f) { System.err.println("Price must be a non-negative number."); return null; }
            return v;
        } catch (NumberFormatException e) {
            System.err.println("Price must be a non-negative number.");
            return null;
        }
    }

    // Convierte un texto a una categoría válida
    public static Category parseCategory(String s) {
        try {
            return Category.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid category. Use: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
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
}
