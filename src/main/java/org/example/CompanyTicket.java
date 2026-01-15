package org.example;
import java.util.ArrayList;
import java.util.List;

public class CompanyTicket extends Ticket<CatalogItem> {
    private static final double SERVICE_DISCOUNT = 0.15;
    private List<Product> products;
    private List<Service> services;

    public CompanyTicket(String id) {
        super(id);
    }
    @Override
    public void addService(Service service) {
        if (status == Status.CLOSE) {
            throw new IllegalStateException("ticket is closed");
        }

        if (type.equalsIgnoreCase("service")) {
            throw new IllegalArgumentException("Error: Standard tickets (Individual) cannot accept Services.");
        }
        items.add(service);
        status = Status.OPEN;
    }
    @Override
    public void addProductTicket(Product product, int quantity, List<String> customTexts) {
        if (status == Status.CLOSE) {
            throw new IllegalStateException("ticket is closed");
        }
        if (product == null) {
            throw new IllegalArgumentException("product doesn't exist");
        }
        List<Product> productsToAdd = product.addToTicket(quantity, customTexts);
        if (items.size() + productsToAdd.size() > MAX_SIZE) {
            throw new IllegalArgumentException("ticket full");
        }
        items.addAll(productsToAdd);
        status = Status.OPEN;
    }
    public List<Product> getProducts() {
        List<Product> result = new ArrayList<>();
        for (CatalogItem item : items) {
            if (item instanceof Product && !item.isService()) {
                result.add((Product) item);
            }
        }
        return result;
    }

    public List<Service> getServices() {
        List<Service> result = new ArrayList<>();
        for (CatalogItem item : items) {
            if (item instanceof Service || item.isService()) {
                result.add((Service) item);
            }
        }
        return result;
    }


    public List<CatalogItem> getItems() {
        return items;
    }


    @Override
    public void closeTicket() {
        boolean hasProducts = !getProducts().isEmpty(); //getproducts en vez de getitems
        boolean hasServices = !getServices().isEmpty();//getservices

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
        int serviceCount = getServices().size(); //getservuces
        return 1 - (SERVICE_DISCOUNT * serviceCount);
    }
}
