package org.example;

public class DiscountController {
    public double getDiscountRate(Category category, int categoryCount) {
        if (categoryCount <= 1) return 0.0;

        switch (category) {
            case MERCH:
                return 0.0;
            case STATIONARY:
                return 0.05;
            case CLOTHES:
                return 0.07;
            case BOOK:
                return 0.10;
            case ELECTRONICS:
                return 0.03;
            default:
                return 0.0;
        }

    }

    public double calculateDiscount(Product product, int quantity, int categoryCount) {
        double rate = getDiscountRate(product.getCategory(), categoryCount);
        return (categoryCount > 1) ? product.getPrice() * quantity * rate : 0.0;
    }
}