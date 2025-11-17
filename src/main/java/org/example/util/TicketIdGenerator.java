package org.example.util;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TicketIdGenerator {
    private static String generateOpenTicketId(){
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm-");

        String fechaFormateada = ahora.format(formatter);

        return fechaFormateada;
    }

    private static String generateCloseTicketId(){
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("-yy-MM-dd-HH:mm");

        String fechaFormateada = ahora.format(formatter);

        return fechaFormateada;
    }



}
