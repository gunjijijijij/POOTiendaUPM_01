package org.example.controller;

import org.example.Cashier;
import org.example.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CashierController {
    private final List<Cashier> cashiers = new ArrayList<>();


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
    }
}
