package org.example;

import org.example.controller.TicketController;
import org.example.strategy.CompanyServicePrinter;

import java.util.List;

public class ServiceTicket extends Ticket<Service>{
    public ServiceTicket(String id) {
        super(id, new CompanyServicePrinter());
    }

    public ServiceTicket() {}

    @Override
    public void addItem(String itemId, int quantity, List<String> customTexts) {
        if (!itemId.endsWith("S")) {
            throw new IllegalArgumentException("Invalid item id: " + itemId);
        }
        if (items.size() >= MAX_SIZE) {
            throw new IllegalArgumentException("ticket full");
        }
        Service service = TicketController.getInstance().findServiceById(itemId);
        if (service == null) {
            System.out.println("ticket add: error (service " + itemId + " not found)");
            return;
        }
        this.items.add(service);
    }

    @Override
    public double getTotalPrice() {
        return 0;
    }

    @Override
    public double getTotalDiscount() {
        return 0;
    }
}
