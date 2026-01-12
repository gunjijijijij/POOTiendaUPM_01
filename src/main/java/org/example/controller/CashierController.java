package org.example.controller;

import org.example.Cashier;
import org.example.util.Utils;
import org.example.Ticket;

import java.util.ArrayList;
import java.util.List;

public class CashierController {
    private static final ArrayList<Cashier> cashiers = new ArrayList<>();
    private static CashierController instance;

    private CashierController(){}

    public static CashierController getInstance(){
        if (instance == null){
            instance = new CashierController();
        }
        return instance;
    }

    public ArrayList<Cashier> getCashiers() {
        return cashiers;
    }


    public Cashier findCashById(String id) {
        for (Cashier cash : cashiers) {
            if (cash.getId().equals(id)) {
                return cash;
            }
        }
        return null;
    }

    public void handleCashAdd(String[] args) {
        if (Utils.requireMinArgs(args, 4, "Usage: cash add [<id>] \"<name>\" <email>")) return;

        Cashier cashier;
        if (args.length == 4) {
            String name = args[2].trim();
            if (name.isEmpty()) {
                System.out.println("The name is empty.");
                return;
            }
            cashier = new Cashier(name, args[args.length - 1]);
        } else {
            String name = args[3].trim();
            if (name.isEmpty()) {
                System.out.println("The name is empty.");
                return;
            }
            cashier = new Cashier(args[2], name, args[4]);
        }

        cashiers.add(cashier);
        System.out.println(cashier);
        System.out.println("cash add: ok");
    }

    public void handleCashRemove(String[] args) {
        if (Utils.requireMinArgs(args, 3, "Usage: cash remove <id>")) return;

        String id = args[2];
        if (id == null) return;

        Cashier found = findCashById(id);
        if (found == null) {
            System.out.println("cash remove: error (id not found)");
            return;
        }

        cashiers.remove(found);
        System.out.println("cash remove: ok");
    }

    public void list() {
        System.out.println("Cash:");
        for (Cashier cash : cashiers) {
            System.out.println("  " + cash.toString());
        }
        System.out.println("cash list: ok");
    }

    public void tickets(String id) {
        Cashier cash = findCashById(id);
        if (cash == null) {
            System.out.println("cash tickets: error (cashier not found)");
            return;
        }

        System.out.println("Tickets: ");
        List<Ticket> tickets = cash.getTickets();
        for (Ticket ticket : tickets) {
            System.out.println("  " + ticket.getId() + "->" + ticket.getStatus());
        }
        System.out.println("cash tickets: ok");
    }
}
