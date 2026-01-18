package org.example;
import org.example.controller.ProductController;
import org.example.controller.TicketController;
import org.example.strategy.CompanyCombinedPrinter;
import org.example.strategy.CompanyServicePrinter;

import java.util.ArrayList;
import java.util.List;

public class CompanyTicket extends Ticket<CatalogItem> {

    public CompanyTicket(String id) {
        super(id, new CompanyCombinedPrinter());
    }

    public CompanyTicket(){}

    @Override
    public void addItem(String itemId, int quantity, List<String> customTexts) {
        if (itemId.endsWith("S")) {
            Service service = TicketController.getInstance().findServiceById(itemId);
            if (service == null) {
                System.out.println("ticket add: error (service " + itemId + " not found)");
                return;
            }
            this.items.add(service);
        } else {
            Product product = ProductController.getInstance().findProductById(itemId);

            if (product == null) {
                throw new IllegalArgumentException("product doesn't exist");
            }

            List<Product> ticketContent = product.addToTicket(quantity, customTexts);
            if (items.size() + ticketContent.size() > MAX_SIZE) {
                throw new IllegalArgumentException("ticket full");
            }

            items.addAll(ticketContent);
        }
        status = Status.OPEN;
    }

    public void addService(Service service) {
        if (status == Status.CLOSE) {
            throw new IllegalStateException("ticket is closed");
        }
        // SOLO SI es servicio o combinado puede añadir servicios
        items.add(service);
        status = Status.OPEN;
    }

    public void addProductTicket(List<Product> productsToAdd) {
        if (status == Status.CLOSE) {
            throw new IllegalStateException("ticket is closed");
        }
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
        double productDiscount = 0;
        double serviceDiscount = 0;
        double cantServices = getServices().size();

        for (Product product : getProducts()) {
            Category category = product.getCategory();
            int catCount = category != null ? countCategory(category) : 0;
            if (catCount > 1) {
                productDiscount += product.getDiscount();
            }
        }
        for (Service service : getServices()) {
            serviceDiscount += service.getCategory().getDiscountRate();
        }

        return (cantServices * serviceDiscount) + productDiscount;
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
