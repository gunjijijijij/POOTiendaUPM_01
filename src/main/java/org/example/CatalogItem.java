package org.example;

public abstract class CatalogItem {
    protected String id;
    protected Category category;

    protected CatalogItem(String id, Category category) {
        this.id = id;
        this.category = category;
    }

    public CatalogItem() {}

    public String getId() { return id; }
    public int getIdAsInt() {
        return Integer.parseInt(String.valueOf(id));
    }
    public Category getCategory() { return category; }


    public abstract boolean isService();

    public abstract String getDisplayId();
}
