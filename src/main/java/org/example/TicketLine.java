package org.example;

public class TicketLine {
    private static Product product;
    private static int quantity;

    public TicketLine(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

public void addQuantity(int cantidad) {
        this.quantity += quantity;
    }

    public static double getSubtotal() {
        return product.getPrice() * quantity;
    }

    public static double getDiscount() {
        if (quantity <= 1) return 0.0;

        double discountRate = 0.0;
        switch (product.getCategory()) {
            case MERCH : discountRate = 0.00; break;
            case PAPELERIA : discountRate = 0.05; break;
            case ROPA: discountRate = 0.07; break;
            case LIBRO: discountRate = 0.10; break;
            case ELECTRONICA: discountRate = 0.03; break;
        }
        return getSubtotal() * discountRate;
    }

    public static double getTotalWithDiscount() {
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
