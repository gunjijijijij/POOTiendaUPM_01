package org.example;
import org.example.util.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Ticket {
    private String id;
    private final int MAX_SIZE = 100;
    private List<Product> lines = new ArrayList<>();


    public enum Status {
        OPEN,
        VACIO,
        CLOSED
    }

    private Status status;

    public Ticket() {
        this.id = Utils.getCurrentDateTime() + ThreadLocalRandom.current().nextInt(10000, 100000);
        this.status = Status.VACIO;

    }

    // Vacía el ticket
    public void resetTicket() {
        this.lines = new ArrayList<>();
    }

    // Añade una cantidad x de un producto al ticket mientras no estuviera lleno
    public void addProductTicket(Product product, int quantity) {
        if (product == null) {
            System.err.println("ticket add: error (product doesn't exist)");
            return;
        }

        for (int i = 0; i < quantity; i++) {
            if (lines.size() < MAX_SIZE) {
                lines.add(product);
            } else {
                System.err.println("ticket add: error (no room for more lines)");
                return;
            }
        }
        this.status = Status.OPEN;
    }

    // Elimina todas las apariciones de un producto existente en el ticket
    public boolean ticketRemove(int productId) {
        boolean found = false;
        for (int i = lines.size() - 1; i >= 0; i--) {
            if (lines.get(i).getId() == productId) {
                lines.remove(i);
                found = true;
            }
        }
        return found;
    }

    // Cuenta cuantos productos hay de una categoría en el ticket
    private int countCategory(Category category) {
        int count = 0;
        for (Product product : lines) {
            if (product.getCategory() == category) {
                count++;
            }
        }
        return count;
    }

    // Getter del total del precio de todos los productos del ticket sin descuento
    public double getTotalPrice() {
        double total = 0;
        for (Product product : lines) {
            total += product.getPrice();
        }
        return total;
    }

    // Getter del descuento total del ticket
    public double getTotalDiscount() {
        double totalDiscount = 0;
        for (Product product : lines) {
            int catCount = countCategory(product.getCategory());
            if (catCount > 1) {
                totalDiscount += product.getCategory().calculateDiscount(product.getPrice());
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
        lines.sort((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName())); //ordena lines alfabeticamente

        for (Product product : lines) {
            int catCount = countCategory(product.getCategory());
            double discount = product.getCategory().calculateDiscount(product.getPrice());
            System.out.printf(
                    "{class:Product, id:%d, name:'%s', category:%s, price:%.2f}%s\n",
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    (catCount > 1 ? " **discount -" + String.format("%.2f", discount) : "")
            );
        }

        System.out.printf("Total price: %.1f\n", getTotalPrice());
        System.out.printf("Total discount: %.1f\n", getTotalDiscount());
        System.out.printf("Final Price: %.1f\n", getFinalPrice());
    }

    public void closeTicket(){

    }
}
