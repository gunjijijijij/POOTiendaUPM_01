package org.example;

import java.util.Objects;

public class Ticket {
    private final int MAX_SIZE = 100;
    private TicketLine[] lines = new TicketLine[MAX_SIZE];
    private int size = 0;

    public void resetTicket(){
        this.lines = new TicketLine[MAX_SIZE];
        this.size = 0;
        System.out.println("ticket new: ok");
    }

    public void addProductTicket(Product product, int quantity) {
        if (product == null) {
            System.err.println("ticket add: error (product doesn't exist)");
            return;
        }

        for (int i = 0; i < quantity; i++) {
            if (size < MAX_SIZE) {
                lines[size++] = new TicketLine(product, quantity);
            } else {
                System.err.println("ticket add: error (there is no room for more lines)");
                return;
            }
        }
        print();
        System.out.println("ticket add: ok");
    }

    public void prodRemove(int productId) {
        for (int i = 0; i < size; i++) {
            if (lines[i].getProduct().getId() == productId) {
                for (int j = i; j < size - 1; j++) {
                    lines[j] = lines[j + 1];
                }
                lines[size - 1] = null;
                size--;
                return;
            }
        }
    }

    public double getTotalPrice() {
        double total = 0;
        for (int i = 0; i < size; i++) {
            total += lines[i].getSubtotal();
        }
        return total;
    }

    public double getTotalDiscount() {
        double discount = 0;
        for (int i = 0; i < size; i++) {
            discount += lines[i].getDiscount();
        }
        return discount;
    }

    public double getFinalPrice() {
        return getTotalPrice() - getTotalDiscount();
    }

    public void print() {
        TicketLine[] sorted = new TicketLine[size];
        for (int i = 0; i < size; i++) sorted[i] = lines[i];

        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (sorted[i].getProduct().getName()
                        .compareToIgnoreCase(sorted[j].getProduct().getName()) > 0) {
                    TicketLine tmp = sorted[i];
                    sorted[i] = sorted[j];
                    sorted[j] = tmp;
                }
            }
        }

        for (int i = 0; i < size; i++) {
            System.out.println(sorted[i]);
        }
        System.out.println("Total price: " + getTotalPrice());
        System.out.println("Total discount: " + getTotalDiscount());
        System.out.println("Final Price: " + getFinalPrice());
    }
}
