package org.example;

public class TicketLine {
    protected Product product;

    public TicketLine(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public float getPrice() {
        return product.getPrice();
    }

    @Override
    public String toString() {
        return product.toString();
    }
}
