package org.example;

public class TicketLine {
    private Product product;
    private int quantity;

    public TicketLine(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int quantity) {this.quantity += quantity;}

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    public double getDiscount() {
        if (quantity <= 1) return 0.0;

        double discountRate = 0.0;
        switch (product.getCategory()) {
            case MERCH : discountRate = 0.00; break;
            case STATIONARY: discountRate = 0.05; break;
            case CLOTHES: discountRate = 0.07; break;
            case BOOK: discountRate = 0.10; break;
            case ELECTRONICS: discountRate = 0.03; break;
        }
        return getSubtotal() * discountRate;
    }

    public double getTotalWithDiscount() {
        return getSubtotal() - getDiscount();
    }

    @Override
    public String toString() {
        return product.getName() + " x" + quantity +
                " | Subtotal: " + getSubtotal() +
                " | Discount: " + getDiscount() +
                " | Total: " + getTotalWithDiscount();
    }
}
