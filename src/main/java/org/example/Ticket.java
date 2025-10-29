package org.example;

public class Ticket {
    private final int MAX_SIZE = 100;
    private Product[] lines = new Product[MAX_SIZE];
    private int size = 0;

    // Vacía el ticket
    public void resetTicket() {
        this.lines = new Product[MAX_SIZE];
        this.size = 0;

    }

    // Añade una cantidad x de un producto al ticket mientras no estuviera lleno
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
    }

    // Elimina todas las apariciones de un producto existente en el ticket
    public boolean ticketRemove(int productId) {
        boolean found = false;
        int newSize = 0;
        Product[] newLines = new Product[MAX_SIZE];

        for (int i = 0; i < size; i++) {
            if (lines[i].getId() != productId) {
                newLines[newSize++] = lines[i];
            } else {
                found = true;
            }
        }

        if (found) {
            lines = newLines;
            size = newSize;
        }

        return found;
    }

    // Cuenta cuantos productos hay de una categoría en el ticket
    private int countCategory(Category category) {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (lines[i].getCategory() == category) {
                count++;
            }
        }
        return count;
    }

    // Getter del total del precio de todos los productos del ticket sin descuento
    public double getTotalPrice() {
        double total = 0;
        for (int i = 0; i < size; i++) {
            total += lines[i].getPrice();
        }
        return total;
    }

    // Getter del descuento total del ticket
    public double getTotalDiscount() {
        double totalDiscount = 0;
        for (int i = 0; i < size; i++) {
            Product p = lines[i];
            int catCount = countCategory(p.getCategory());
            if (catCount > 1){
                totalDiscount += p.getCategory().calculateDiscount(p.getPrice());
            }
        }
        return totalDiscount;
    }

    // Getter del precio total con descuento
    public double getFinalPrice() {
        return getTotalPrice() - getTotalDiscount();
    }

    // Imprime el contenido del ticket
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
            double discount = p.getCategory().calculateDiscount(p.getPrice());
            System.out.printf(
                    "{class:Product, id:%d, name:'%s', category:%s, price:%.2f}%s\n",
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    p.getPrice(),
                    (catCount > 1 ? " **discount -" + String.format("%.2f", discount) : "")
            );
        }

        System.out.printf("Total price: %.1f\n", getTotalPrice());
        System.out.printf("Total discount: %.1f\n", getTotalDiscount());
        System.out.printf("Final Price: %.1f\n", getFinalPrice());
    }
}
