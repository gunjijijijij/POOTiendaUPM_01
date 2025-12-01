package org.example;

import java.util.List;

public class TicketLineCustomProduct extends TicketLine {
    private final List<String> customTexts;
    private final CustomProduct customProduct;

    public TicketLineCustomProduct(CustomProduct product, List<String> customTexts) {
        super(product);
        this.customTexts = customTexts;
        this.customProduct = product;
    }

    @Override
    public float getPrice() {
        if (customTexts != null)
            return product.getPrice() * (1 + (0.10f * customTexts.size()));
        return product.getPrice();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{class:ProductPersonalized")
                .append(", id:").append(product.id)
                .append(", name:'").append(product.name)
                .append("', category:").append(product.category)
                .append(", price:").append(this.getPrice())
                .append(", maxPersonal:").append(customProduct.getMaxCustomizations());

        if (customTexts != null) {
            sb.append(", personalizationList:").append(customTexts);
        }

        sb.append("}");
        return sb.toString();
    }
}
