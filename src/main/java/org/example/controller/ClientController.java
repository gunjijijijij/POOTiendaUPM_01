package org.example.controller;

import org.example.Cashier;
import org.example.*;
import org.example.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClientController {
    private final List<Client> clients = new ArrayList<>();

    public List<Client> getClients(){
        return clients;
    }

    public Client findClientById(String id){
        for (Client client : clients) {
            if (Objects.equals(client.getId(), id)) {
                return client;
            }
        }
        return null;
    }

    public void handleClientAdd(String[] args){
        if (Utils.requireMinArgs(args, 5, "Usage: client add \"<name>\" <DNI> <email> <cashId>")) return;

        String name = Utils.joinQuoted(args, 2, args.length - 3).trim();
        if (name.isEmpty()) {System.err.println("The name is empty."); return; }

        String dni = Utils.joinQuoted(args, args.length - 3, args.length - 3).trim();
        if (dni.isEmpty()) { System.out.println("The dni is empty."); return; }

        String email = Utils.joinQuoted(args, args.length - 2, args.length - 2).trim();
        if (email.isEmpty()){ System.out.println("The email is empty."); return; }

        String cashId = Utils.joinQuoted(args, args.length - 1, args.length - 1).trim();
        if (cashId.isEmpty()){ System.out.println("The cashId is empty"); return; }

        Cashier cashier = CashierController.findCashById(cashId);
        if (cashier == null) {
            System.err.println("client add: error (cashier not found)");
            return;
        }

        try {
            Client client = new Client(name, dni, email, cashier);
            clients.add(client);

            System.out.println(client);
            System.out.println("client add: ok");

        } catch (IllegalArgumentException e) {
            System.err.println("client add: error (" + e.getMessage() + ")");
        }
    }

    public void handleClientRemove(String[] args){
        if (Utils.requireMinArgs(args, 3, "Usage: client remove <id>")) return;

        String id = Utils.joinQuoted(args, 2, args.length - 1).trim();
        if (id.isEmpty()) { System.out.println("The identifier is empty."); return;}

        Client found = findClientById(id);
        if (found == null) {System.err.println("client remove: error (no client with that ID)"); return;}

        try {
            clients.remove(found);
            System.out.println(found);
            System.out.println("prod remove: ok");
        } catch (IllegalArgumentException e) {
            System.err.println("prod remove: error (" + e.getMessage() + ")");
        }
    }

    public void list(){
        System.out.println("Client:");
        System.out.println("Client:");
        for (Client client : clients){
            System.out.println(client.toString());
        }
        System.out.println("Cash list: ok");
    }
}
