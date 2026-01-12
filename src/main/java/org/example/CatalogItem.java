package org.example;

public abstract class CatalogItem {
    protected final Integer id;
    protected final Category category;

    protected CatalogItem(Integer id, Category category) {
        if (category == null) throw new IllegalArgumentException("category can't be null");
        this.id = id;
        this.category = category;
    }

    public int getId() { return id; }
    public Category getCategory() { return category; }

    public abstract boolean isService();

    public abstract String getDisplayId();
}
