package org.example;
import java.util.ArrayList;
import java.util.List;

public class CompanyTicket extends Ticket<CatalogItem> {

    public CompanyTicket(String id) {
        super(id);
    }
    @Override
    public void addService(Service service) {
        if (status == Status.CLOSE) {
            throw new IllegalStateException("ticket is closed");
        }
        // SOLO SI es servicio o combinado puede añadir servicios
        items.add(service);
        status = Status.OPEN;
    }
    @Override
    public void addProductTicket(Product product, int quantity, List<String> customTexts) {
        if (status == Status.CLOSE) {
            throw new IllegalStateException("ticket is closed");
        }
        //AQUI SE VALIDA QUE SI ES SOLO SERVICIO NO SE AÑADAN PRODUCTOS
        if (type != null && type.equalsIgnoreCase("SERVICE")) {
            throw new IllegalArgumentException("Error: Service tickets cannot accept Products.");
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
    public double getTotalPrice() {

        if (getProducts().isEmpty()) {
            return 0; // solo servicios
        }

        double total = 0;
        for (Product product : getProducts()) {
            total += product.getPrice();
        }
        return total;
    }

    @Override
    public double getTotalDiscount() {

        if (getProducts().isEmpty()) {
            return 0;
        }

        double productsTotal = 0;
        for (Product product : getProducts()) {
            productsTotal += product.getPrice();
        }

        double discountRate = 0;

        for (Service service : getServices()) {
            discountRate += service.getCategory().getDiscountRate();
        }

        return productsTotal * discountRate;
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

}
