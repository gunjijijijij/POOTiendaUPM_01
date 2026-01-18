package org.example.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.example.Category;

public class Utils {
    // Verifica si hay suficientes argumentos
    public static boolean requireMinArgs(String[] args, int min, String usage) {
        if (args.length < min) {
            System.out.println(usage);
            return true;
        }
        return false;
    }

    // Parseo seguro de enteros positivos
    public static Integer parsePositiveInt(String s, String errMsg) {
        try {
            int v = Integer.parseInt(s);
            if (v <= 0) {
                System.out.println(errMsg);
                return null;
            }
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
            if (v < 0f) {
                System.out.println("Price must be a non-negative number.");
                return null;
            }
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

    public static List<String> parseLine(String line) {
        line = line.trim();
        boolean isBetweenQuotes = false;
        boolean followsWhitespace = false;
        List<String> result = new ArrayList<>();
        StringBuilder curr = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '"') {
                followsWhitespace = false;
                isBetweenQuotes = !isBetweenQuotes;
                if (!isBetweenQuotes) {
                    followsWhitespace = true;
                    result.add(curr.toString());
                    curr = new StringBuilder();
                }
                continue;
            }

            if (isBetweenQuotes) {
                curr.append(c);
                continue;
            }

            if (!Character.isWhitespace(c)) {
                followsWhitespace = false;
                curr.append(c);
            } else if (!followsWhitespace) {
                result.add(curr.toString());
                curr = new StringBuilder();
                followsWhitespace = true;
            }
        }
        if (curr.length() > 0) {
            result.add(curr.toString());
        }
        return result;
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

    public static boolean isValidMeetingCreation(LocalDate expiration) { //requiere al menos doce horas de planning
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minDateTime = now.plusHours(12);
        LocalDateTime expirationDateTime = expiration.atTime(23, 59); //Es la fecha expiration a la última hora posible
        return expirationDateTime.isAfter(minDateTime); //el dia expiration a las 23:59 sea al menos 12 horas despues de NOW
    }

    public static boolean isValidID(String id) {
        return isDNI(id)||isNIF(id);
    }
    public static boolean isNIF(String nif){
        if (nif.length()!=9){
            return false;
        }
        String secuenciaLetrasNIF = "TRWAGMYFPDXBNJZSQVHLCKE";
        nif = nif.toUpperCase();
        String numeroNIF = nif.substring(0, nif.length()-1);
        numeroNIF = numeroNIF.replace("X", "0").replace("Y", "1").replace("Z", "2");
        char letraNIF = nif.charAt(8);
        int i = Integer.parseInt(numeroNIF) % 23;
        return letraNIF == secuenciaLetrasNIF.charAt(i);
    }

    public static boolean isDNI(String dni) {
        if (!dni.matches("\\d{8}[A-Z]")) {
            return false;
        }

        String letter = "TRWAGMYFPDXBNJZSQVHLCKE";
        int number = Integer.parseInt(dni.substring(0, 8));
        char resultLetter = letter.charAt(number % 23);

        return dni.charAt(8) == resultLetter;
    }

    public static boolean isDateYYYYMMDD(String s) {
        return s != null && s.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    public static LocalDate parseDateYYYYMMDD(String s) {
        try {
            return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
