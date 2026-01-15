package org.example;

import java.io.Serializable;

public abstract class CatalogItem implements Serializable{
    protected final String id;
    protected final Category category;

    protected CatalogItem(String id, Category category) {
        if (category == null) throw new IllegalArgumentException("category can't be null");
        this.id = id;
        this.category = category;
    }

    public String getId() { return id; }
    public int getIdAsInt() {
        return Integer.parseInt(String.valueOf(id));
    }
    public Category getCategory() { return category; }

    public abstract boolean isService();

    public abstract String getDisplayId();
}
