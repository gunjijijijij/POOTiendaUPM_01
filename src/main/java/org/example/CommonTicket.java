package org.example;
import java.util.ArrayList;
import java.util.List;

public class CommonTicket extends Ticket<Product>{
    private final List<Product> products = new ArrayList<>();
    public CommonTicket(String id) {
        super(id);

    }
    @Override
    public void addProductTicket(Product product, int quantity, List<String> customTexts) {
        if (status == Status.CLOSE) {
            throw new IllegalStateException("ticket is closed");
        }

        if (product == null) {
            throw new IllegalArgumentException("product doesn't exist");
        }

        List<Product> ticketLines = product.addToTicket(quantity, customTexts);
        if (products.size() + ticketLines.size() > MAX_SIZE) {
            throw new IllegalArgumentException("ticket full");
        }

        products.addAll(ticketLines);
        status = Status.OPEN;
    }
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public void addService(ProductService service) {
        throw new IllegalArgumentException(
                "Common tickets cannot accept services"
        );
    }

    @Override
    public void closeTicket() {
        if (products.isEmpty()) {
            throw new IllegalStateException("Cannot close empty ticket");
        }
        super.closeTicket();
    }
}
