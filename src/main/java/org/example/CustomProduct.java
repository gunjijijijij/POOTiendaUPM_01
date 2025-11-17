package org.example;

import java.util.ArrayList;
import java.util.List;

public class CustomProduct extends Product{
    private int maxCustomtexts;
    private List<String> customtexts;
    public CustomProduct(int id, String name, Category category, float price, int maxCustomtexts){
        super(id,name,category,price);
        this.maxCustomtexts = maxCustomtexts;
        this.customtexts = new ArrayList<>();
    }
    public void addCustomText(String text){
        if(customtexts.size() >= maxCustomtexts){
            throw new IllegalArgumentException("Maximum custom texts reached: " + maxCustomtexts);
        }
        customtexts.add(text);
    }
    public List<String> getCustomtexts() {return new ArrayList<>(customtexts);}
    public int getMaxCustomtexts(){return maxCustomtexts;}
    @Override
    public float getPrice(){
        return super.getPrice() * (1 + (0.10f * customtexts.size()));
    }
    public String getType(){
        return "CustomProduct";
    }
    @Override
    public String toString(){
        return "{class: Product, id: " + id
                + ", name: '" + name
                + ", category: " + category
                + ", base price: " + super.getPrice()
                + ", final price: " + getPrice()
                + ", maxCustomtexts: " + maxCustomtexts + "}";
    }
    public void clearCustomTexts(){
        customtexts.clear();
    }
}
