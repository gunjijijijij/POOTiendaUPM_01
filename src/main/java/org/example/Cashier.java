package org.example;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Cashier extends User {
    private static final String PREFIX = "UW";
    private static final Random random = new Random();
    private static final List<String> usedIds = new ArrayList<>();
    public Cashier(String name, String email) {
        super(generateId(), name, email);
    }

    public Cashier(String id, String name, String email) {super(validateId(id), name, email);}
    private static String generateId() {
        String id;
        int attempts = 0;
        final int MAX_ATTEMPTS = 10000; //SIRVE PARA QUE NO SE QUEDE EN UN BUCLE INFINITO, SI DESPUES DE 10K INTENTOS NO LO CONSIGUE DA ERROR DIRECTAMENTE
        do{
            int randomNumber = random.nextInt(10000000);
            id = PREFIX + String.format("%07d", randomNumber); //RELLENA CON CEROS A LA IZQUIERDA
            attempts++;
            if(attempts>MAX_ATTEMPTS){
                throw new IllegalStateException("Cannot generate unique CASHIER ID after " + MAX_ATTEMPTS + " attempts");
            }
            }while(usedIds.contains(id));
        usedIds.add(id);
        return id;
    }
    private static String validateId(String id){
        if(!id.matches("^UW\\d{7}$")){ //VERIFICA QUE TENGA UW + 7 DIGITOS
            throw new IllegalArgumentException("Cashier ID MUST be 'UW' + 7 digits. For example: 'UW1839201'");
        }
        if(usedIds.contains(id)){
            throw new IllegalArgumentException("Cashier ID " + id + " already exists");
        }
        return id;
    }
    public static List<String> getUsedIds(){
        return new ArrayList<>(usedIds);
    }
    public static void removeId(String id){
        usedIds.remove(id);
    }
    public static boolean idExists(String id){
        return usedIds.contains(id);
    }

    @Override
    public String getType() {
        return "Cashier";
    }
}
