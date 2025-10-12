package org.example;

public class Ticket {
    private final int MAX_SIZE = 100;
    private Product[] lines = new Product[MAX_SIZE];
    private int size = 0;
    private final DiscountController discountController = new DiscountController();

    public void resetTicket() {
        this.lines = new Product[MAX_SIZE];
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
                lines[size++] = product;
            } else {
                System.err.println("ticket add: error (no room for more lines)");
                return;
            }
        }

        print();
        System.out.println("ticket add: ok");
    }

    public void ticketRemove(int productId) {
        int newSize = 0;
        Product[] newLines = new Product[MAX_SIZE];
        for (int i = 0; i < size; i++) {
            if (lines[i].getId() != productId) {
                newLines[i] = lines[i];
                newSize++;
            }
        }
        size = newSize;
        lines = newLines;
    }

    private int countCategory(Category category) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (lines[i].getCategory() == category) {
                count++;
            }
        }
        return count;
    }

    public double getTotalPrice() {
        double total = 0;
        for (int i = 0; i < size; i++) {
            total += lines[i].getPrice();
        }
        return total;
    }

    public double getTotalDiscount() {
        double totalDiscount = 0;
        for (int i = 0; i < size; i++) {
            Product p = lines[i];
            int catCount = countCategory(p.getCategory());
            totalDiscount += discountController.calculateDiscount(p, 1, catCount);
        }
        return totalDiscount;
    }

    public double getFinalPrice() {
        return getTotalPrice() - getTotalDiscount();
    }

    public void print() {
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (lines[i].getName().compareToIgnoreCase(lines[j].getName()) > 0) {
                    Product tmp = lines[i];
                    lines[i] = lines[j];
                    lines[j] = tmp;
                }
            }
        }

        for (int i = 0; i < size; i++) {
            Product p = lines[i];
            int catCount = countCategory(p.getCategory());
            double discount = discountController.calculateDiscount(p, 1, catCount);
            System.out.printf(
                    "{class:Product, id:%d, name:'%s', category:%s, price:%.1f}%s\n",
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    p.getPrice(),
                    (catCount > 1 ? " **discount -" + String.format("%.1f", discount) : "")
            );
        }

        System.out.printf("Total price: %.1f\n", getTotalPrice());
        System.out.printf("Total discount: %.1f\n", getTotalDiscount());
        System.out.printf("Final Price: %.1f\n", getFinalPrice());
    }
}
