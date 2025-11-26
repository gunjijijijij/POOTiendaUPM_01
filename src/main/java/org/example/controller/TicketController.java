package org.example.controller;

import org.example.*;

import org.example.util.Utils;

import java.util.ArrayList;


public class TicketController {
    public static ArrayList<Ticket> tickets = new ArrayList<>();

    public static Ticket findTicketById(String id) {
        for (Ticket ticket : tickets) {
            if (ticket.getId().equals(id)) {
                return ticket;
            }
        }
        return null;
    }

    public void ticketNew(String cashId, String userId){
        Ticket ticket = new Ticket();
        Cashier currentCash = CashierController.findCashById(cashId);
        currentCash.getTickets().add(ticket);
        Client currentClient = ClientController.findClientById(userId);
        currentClient.getTickets().add(ticket);
    }

    public void ticketAdd(String ticketId, String cashId, int prodId, int amount, String customT) {
        Product currentProd = ProductController.findProductById(prodId);
        ArrayList<String> customTexts = Utils.parseCustomTexts(customT);
        if (currentProd.esPersonalizable()) {
            for (String customText : customTexts) {
                CustomProduct.addCustomText(customText);
            }
        }
        //no entiedno porque se pasa cashid como argumento si en teoria el cash ya deberia tener constancia de
        //sus propios tickets
        Cashier currentCash = CashierController.findCashById(cashId);
        Ticket currentTicket = TicketController.findTicketById(ticketId);
        currentTicket.addProductTicket(currentProd, amount, customTexts);
    }

    public void ticketDelete(String ticketId, String cashId, int prodId) {
        Cashier currentCash = CashierController.findCashById(cashId);
        Ticket currentTicket = TicketController.findTicketById(ticketId);
        if(Ticket.ticketRemove(prodId)){
            System.out.println(
            );

        }
    }


}
