package org.example;

public class Ticket {
    private static final int MAX_SIZE = 100;
    private static TicketLine[] lines;
    private static int size;

    public Ticket() {
        this.lines = new TicketLine[MAX_SIZE];
        this.size = 0;
    }

    public void resetTicket(){
        this.lines = new TicketLine[MAX_SIZE];
        this.size = 0;
    }

    public void addProduct(int id , int quantity) {
        Product p = ProductController.findProductById(id);

        if (p == null) {
            System.out.println("ticket add: error (product doesn't exist)");
            return;
        }

        for (int i = 0; i < size; i++) {
            if (lines[i].getProduct().getId() == p.getId()) {
                lines[i].addQuantity(quantity);
                return;
            }
        }

        if (size < MAX_SIZE) {
            lines[size++] = new TicketLine(p, quantity);
        } else {
            System.out.println("ticket add: error (there is no room for more lines)");
        }
    }

    public static void removeProduct(int productId) {
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

    public static double getTotalPrice() {
        double total = 0;
        for (int i = 0; i < size; i++) {
            total += lines[i].getSubtotal();
        }
        return total;
    }

    public static double getTotalDiscount() {
        double discount = 0;
        for (int i = 0; i < size; i++) {
            discount += lines[i].getDiscount();
        }
        return discount;
    }

    public static double getFinalPrice() {
        return getTotalPrice() - getTotalDiscount();
    }

    public static void print() {
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
