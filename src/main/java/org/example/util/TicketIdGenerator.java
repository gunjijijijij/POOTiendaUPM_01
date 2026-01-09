package org.example.util;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TicketIdGenerator {
    private static final Random random = new Random();
    private static int serviceTicketCounter = 0;

    public static String generateOpenTicketId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm-");

        String formatDate = now.format(formatter);

        return formatDate + generateRandomNumber();
    }

    public static String generateCloseTicketId(String ticketId) {
        if (ticketId == null) return null;

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("-yy-MM-dd-HH:mm");

        String formatDate = now.format(formatter);

        return ticketId + formatDate;
    }

    private static String generateRandomNumber() {
        StringBuilder randomString = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            int id = random.nextInt(10);
            randomString.append(id);
        }

        return randomString.toString();
    }

    public static String generateServiceTicketId(){
        serviceTicketCounter++;
        return serviceTicketCounter + "S";
    }

}
