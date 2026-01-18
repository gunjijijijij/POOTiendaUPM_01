package org.example;
import org.example.controller.ProductController;
import org.example.controller.TicketController;

import java.util.List;

public class CommonTicket extends Ticket<Product>{
    public CommonTicket(String id) {
        super(id);
    }

    @Override
    public void addItem(String itemId, int quantity, List<String> customTexts) {
        if (status == Status.CLOSE) {
            throw new IllegalStateException("ticket is closed");
        }
        if (itemId.endsWith("S")) {
            throw new IllegalArgumentException("Invalid item id: " + itemId);
        }
        Product product = ProductController.getInstance().findProductById(itemId);

        if (product == null) {
            throw new IllegalArgumentException("product doesn't exist");
        }

        List<Product> ticketContent = product.addToTicket(quantity, customTexts);
        if (items.size() + ticketContent.size() > MAX_SIZE) {
            throw new IllegalArgumentException("ticket full");
        }

        items.addAll(ticketContent);
        status = Status.OPEN;
    }

    public List<Product> getProducts() {
        return items;
    }

    @Override
    public double getTotalPrice() {
        double total = 0;
        for (Product product : items) {
            total += product.getPrice();
        }
        return total;
    }

    @Override
    public double getTotalDiscount() {
        double totalDiscount = 0;
        for (Product product : items) {
            Category category = product.getCategory();
            int catCount = category != null ? countCategory(category) : 0;
            if (catCount > 1) {
                totalDiscount += product.getDiscount();
            }
        }
        return totalDiscount;
    }



    @Override
    public void closeTicket() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot close empty ticket");
        }
        super.closeTicket();
    }
}
