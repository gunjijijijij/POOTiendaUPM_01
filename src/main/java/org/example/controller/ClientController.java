package org.example.controller;

import org.example.Cashier;
import org.example.*;
import org.example.util.CompanyClient;
import org.example.util.IndividualClient;
import org.example.util.Utils;
import java.util.ArrayList;

public class ClientController {
    private static final ArrayList<Client> clients = new ArrayList<>();

    public ArrayList<Client> getClients() {
        return clients;
    }

    public static Client findClientById(String id) {
        for (Client client : clients) {
            if (client.getId().equals(id)) {
                return client;
            }
        }
        return null;
    }

    public void handleClientAdd(String[] args) {
        if (Utils.requireMinArgs(args, 5, "Usage: client add \"<name>\" <DNI> <email> <cashId>")) return;

        String name = args[2].trim();
        if (name.isEmpty()) {
            System.out.println("The name is empty.");
            return;
        }

        String id = args[3];
        if (id.isEmpty() || !isValidID(id)) {
            System.out.println("The identifier is empty or invalid.");
            return;
        }

        String email = args[4];
        if (email.isEmpty()) {
            System.out.println("The email is empty.");
            return;
        }

        String cashId = args[5];
        if (cashId.isEmpty()) {
            System.out.println("The cashId is empty");
            return;
        }

        Cashier cashier = CashierController.findCashById(cashId);
        if (cashier == null) {
            System.out.println("client add: error (cashier not found)");
            return;
        }

        try {
            Client client;
            if(isNIF(id)) {
                client = new CompanyClient(id, name, email, cashier);
            }else{
                client = new IndividualClient(id,name,email,cashier);
            }
            clients.add(client);
            System.out.println(client);
            System.out.println("client add: ok");

        } catch (IllegalArgumentException e) {
            System.out.println("client add: error (" + e.getMessage() + ")");
        }
    }

    public void handleClientRemove(String[] args) {
        if (Utils.requireMinArgs(args, 3, "Usage: client remove <id>")) return;

        String id = args[2];
        if (id.isEmpty()) {
            System.out.println("The identifier is empty.");
            return;
        }

        Client found = findClientById(id);
        if (found == null) {
            System.out.println("client remove: error (no client with that ID)");
            return;
        }

        try {
            clients.remove(found);
            System.out.println("client remove: ok");
        } catch (IllegalArgumentException e) {
            System.out.println("client remove: error (" + e.getMessage() + ")");
        }
    }

    public void list() {
        System.out.println("Client:");
        for (Client client : clients) {
            System.out.println(client.toString());
        }
        System.out.println("client list: ok");
    }

    public boolean isValidID(String id) {
        return id.matches("[A-HJ-NP-S]?[0-9]{7}[A-Z]") || id.matches("\\d{8}[A-Z]");
    }
    private boolean isNIF(String id){
        return id.matches("[A-HJ-NP-S]?[0-9]{7}[A-Z]");
    }

}
