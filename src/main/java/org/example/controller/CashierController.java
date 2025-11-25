package org.example.controller;

import org.example.Cashier;
import org.example.Product;
import org.example.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CashierController {
    private final List<Cashier> cashiers = new ArrayList<>();

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    private Cashier findCashById(String id) {
        for (Cashier cash : cashiers) {
            if (cash.getId().equals(id)) {
                return cash;
            }
        }
        return null;
    }

    public void handleCashAdd(String[] args){
        if (Utils.requireMinArgs(args, 4, "Usage: cash add [<id>] \"<name>\" <email>")) return;

        Cashier cashier;
        if (args[2].startsWith("\"")) {
            String name = Utils.joinQuoted(args, 2, args.length - 1).trim();
            if (name.isEmpty()) {System.err.println("The name is empty."); return; }
            cashier = new Cashier(name, args[args.length - 1]);
        } else  {
            String name = Utils.joinQuoted(args, 3, args.length - 1).trim();
            if (name.isEmpty()) {System.err.println("The name is empty."); return; }
            cashier = new Cashier(args[2], name, args[args.length - 1]);
        }

        cashiers.add(cashier);
        System.out.println(cashier);
        System.out.println("cash add: ok");
    }

    public void handleCashRemove(String[] args){
        if (Utils.requireMinArgs(args, 3, "Usage: cash remove <id>")) return;

        String id = args[2];
        if (id == null) return;

        Cashier found = findCashById(id);
        if (found == null) {
            System.err.println("cash remove: error (id not found)");
            return;
        }

        cashiers.remove(found);
    }

    public void list(){
        System.out.println("Cash:");
        for (Cashier cash : cashiers){
            System.out.println(cash.toString());
        }
        System.out.println("Cash list: ok");
    }

    public void tickets(){

    }

}
