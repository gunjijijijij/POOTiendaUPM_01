package org.example;
import org.example.controller.ProductController;
import org.example.util.TicketIdGenerator;
import java.util.ArrayList;
import java.util.List;
import org.example.util.Utils;


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
        this.status = Status.VACIO;
    }

    // Vacía el ticket
    public void resetTicket() {
        lines = new ArrayList<>();
    }

    public String getId(){
        return id;
    }

    public Status getStatus(){
        return status;
    }

    // Añade una cantidad x de un producto al ticket mientras no estuviera lleno
    public static void addProductTicket(Product product, int quantity, ArrayList<String> customTexts) {
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
        this.status = Status.CLOSED;
    }

    // Procesa el comando "ticket remove": verifica los argumentos,
    // maneja los errores correspondientes y utiliza Ticket
    // para eliminar todas las apariciones del producto del ticket.
    public void handleTicketRemove(String[] args) {
        if (Utils.requireMinArgs(args, 3, "Usage: ticket remove <prodId>")) return;

        String idString = args[2];
        Integer removeId = Utils.parsePositiveInt(idString, "The ID must be a positive integer.");
        if (removeId == null) return;

        boolean success = ticketRemove(removeId);
        if (success) {
            print();
            System.out.println("ticket remove: ok");
        } else {
            System.err.println("ticket remove: error (no product found with that ID)");
        }
    }

    // Procesa el comando "ticket add": verifica los argumentos,
    // maneja los errores correspondientes y utiliza Ticket
    // para añadir el producto al Ticket.
    public void handleTicketAdd(String[] args) {
        if (Utils.requireMinArgs(args, 4, "Please input all the necessary arguments")) return;

        String idString = args[2];
        Integer addId = Utils.parsePositiveInt(idString, "The ID must be a positive integer.");
        if (addId == null) return;

        Integer quantity = Utils.parsePositiveInt(args[3], "The quantity must be a positive integer");
        if (quantity == null) return;

        Product product = ProductController.findProductById(addId);

        if (product == null) {
            System.err.println("ticket add: error (product with ID " + addId + " not found)");
            return;
        }

        try {
            //addProductTicket(product, quantity);
            print();
            System.out.println("ticket add: ok");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
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


}
