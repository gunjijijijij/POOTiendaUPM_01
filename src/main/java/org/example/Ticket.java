package org.example;
import org.example.util.TicketIdGenerator;
import java.util.ArrayList;
import java.util.List;


public class Ticket {
    private String id;
    private static final int MAX_SIZE = 100;
    private static List<Product> lines = new ArrayList<>();

    public enum Status {
        OPEN,
        VACIO,
        CLOSED
    }

    private static Status status;

    public Ticket() {
        this.id = TicketIdGenerator.generateOpenTicketId();
        status = Status.VACIO;
    }

    public String getId(){
        return id;
    }

    public Status getStatus(){
        return status;
    }

    // Añade una cantidad x de un producto al ticket mientras no estuviera lleno
    public static void addProductTicket(Product product, int quantity, ArrayList<String> customTexts) {
        if (status == Status.CLOSED) {
            throw new IllegalStateException("ticket add: error (ticket is closed)");
        }

        if (product == null) {
            System.err.println("ticket add: error (product doesn't exist)");
            return;
        }
        if (product instanceof MeetingProduct || product instanceof FoodProduct) {
            for (Product p : lines) {
                if (p instanceof MeetingProduct || p instanceof FoodProduct) {
                    System.err.println("ticket add: error (meeting/food product already in ticket)");
                    return;
                }
            }
        }
        if (product instanceof CustomProduct ) {
            CustomProduct cp = (CustomProduct) product;
            for (String txt : customTexts) {
                try {
                    cp.addCustomText(txt);
                } catch (IllegalArgumentException e) {
                    System.err.println("ticket add: error (" + e.getMessage() + ")");
                    return;
                }
            }
        }
        else if (!customTexts.isEmpty()) {
            System.err.println("ticket add: error (product not personalizable)");
            return;
        }

        for (int i = 0; i < quantity; i++) {
            if (lines.size() >= MAX_SIZE) {
                System.err.println("ticket add: error (no room for more lines)");
                return;
            }
            lines.add(product);
        }

       status = Status.OPEN;
    }

    // Elimina todas las apariciones de un producto existente en el ticket
    public static boolean ticketRemove(int productId) {
        boolean found = false;
        if (status == Status.CLOSED) {
            System.err.println("ticket remove: error (ticket is closed)");
            return false;
        }


        for (int i = lines.size() - 1; i >= 0; i--) {
            if (lines.get(i).getId() == productId) {
                lines.remove(i);
                found = true;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("no product found with that ID");
        }
        return true;
    }

    // Cuenta cuantos productos hay de una categoría en el ticket
    private static int countCategory(Category category) {
        int count = 0;
        for (Product product : lines) {
            if (product.getCategory() == category) {
                count++;
            }
        }
        return count;
    }

    // Getter del total del precio de todos los productos del ticket sin descuento
    private static double getTotalPrice() {
        double total = 0;
        for (Product product : lines) {
            total += product.getPrice();
        }
        return total;
    }

    // Getter del descuento total del ticket
    private static double getTotalDiscount() {
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
    private static double getFinalPrice() {
        return getTotalPrice() - getTotalDiscount();
    }

    public void closeTicket(){
        id = TicketIdGenerator.generateCloseTicketId(id);
        status = Status.CLOSED;
    }

    // Procesa el comando "ticket remove": verifica los argumentos,
    // maneja los errores correspondientes y utiliza Ticket
    // para eliminar todas las apariciones del producto del ticket.


    // Procesa el comando "ticket add": verifica los argumentos,
    // maneja los errores correspondientes y utiliza Ticket
    // para añadir el producto al Ticket.


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


}
