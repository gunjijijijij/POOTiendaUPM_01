package org.example;
import java.util.ArrayList;
import java.util.List;

public class CompanyTicket extends Ticket<CatalogItem> {
    private static final double SERVICE_DISCOUNT = 0.15;
    private final List<ProductService> services = new ArrayList<>();
    private final List<Product> products = new ArrayList<>();

    public CompanyTicket(String id) {
        super(id);
    }
    @Override
    public void addService(ProductService service) {
        if (status == Status.CLOSE) {
            throw new IllegalStateException("ticket is closed");
        }

        if (type.equalsIgnoreCase("service")) {
            throw new IllegalArgumentException("Error: Standard tickets (Individual) cannot accept Services.");
        }
        services.add(service);
        status = Status.OPEN;
    }
    @Override
    public void addProductTicket(Product product, int quantity, List<String> customTexts) {

    }

    public List<Product> getProducts() {
        return products;
    }

    public List<ProductService> getServices() {
        return services;
    }



    @Override
    public void closeTicket() {
        boolean hasProducts = !getProducts().isEmpty();
        boolean hasServices = !getServices().isEmpty();

        if (!hasProducts && !hasServices) {
            throw new IllegalStateException("Company ticket is empty");
        }

        if (hasProducts && !hasServices) {
            throw new IllegalStateException(
                    "Company ticket with products must have at least one service"
            );
        }

        super.closeTicket();
    }
    public double getServiceDiscountMultiplier() {
        int serviceCount = getServices().size();
        return 1 - (SERVICE_DISCOUNT * serviceCount);
    }
}
