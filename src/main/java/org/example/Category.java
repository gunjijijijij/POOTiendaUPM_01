package org.example;

// Categorías válidas en la tienda
public enum Category {
    MERCH(0.0),
    STATIONARY(0.05),
    CLOTHES(0.07),
    BOOK(0.1),
    ELECTRONICS(0.03);

    private final double discountRate;

    Category(double discountRate) {
        this.discountRate = discountRate;
    }

    // Devuelve el porcentaje de descuento correspondiente a una categoría
    // dependiendo de cuántas categorías distintas haya en el ticket.
    public double getDiscountRate() {
        return discountRate;
    }

    // Calcula el valor total del descuento para un producto concreto
    // teniendo en cuenta su precio, cantidad y el número de categorías en el ticket.
    public double calculateDiscount(float price) {
        return price * discountRate;
    }
}
