package org.example;

public class Product {
    int id;
    String name;
    Categoria category;
    float price;

    public Product(Integer id, String name, Categoria category, float price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if(id == null || id <=0){
            throw new IllegalArgumentException("El identificador no puede estar vacío y debe ser un número positivo");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name== null || name.isBlank() || name.length() >=100){
            throw new IllegalArgumentException("El nombre no puede estar vacío y debe contener menos de 100 caracteres");
        }
        this.name = name;
    }

    public Categoria getCategory() {
        return category;
    }

    public void setCategory(Categoria category) {
        if(category==null){
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        if(price<=0){
            throw new IllegalArgumentException("El precio debe ser un número mayor a 0 sin límite superior");
        }
        this.price = price;
    }


}
