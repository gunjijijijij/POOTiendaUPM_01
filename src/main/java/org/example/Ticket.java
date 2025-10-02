package org.example;

public class Ticket {
    private static final int MAX_SIZE = 100;
    private TicketLine[] lines;
    private int size;

    public Ticket() {
        this.lines = new TicketLine[MAX_SIZE];
        this.size = 0;
    }

    public void resetTicket(){
        this.lines = new TicketLine[MAX_SIZE];
        this.size = 0;
    }

    public void addProduct(Product p, int quantity) {
        for (int i = 0; i < size; i++) {
            if (lines[i].getProduct().getId() == p.getId()) {
                lines[i].addQuantity(quantity);
                return;
            }
        }

        if (size < MAX_SIZE) {
            lines[size++] = new TicketLine(p, quantity);
        } else {
            System.out.println("ticket add: error (no caben más líneas)");
        }
    }

    public void removeProduct(int productId) {
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

        // imprimir
        for (int i = 0; i < size; i++) {
            System.out.println(sorted[i]);
        }
        System.out.println("Total price: " + getTotalPrice());
        System.out.println("Total discount: " + getTotalDiscount());
        System.out.println("Final Price: " + getFinalPrice());
    }
}
