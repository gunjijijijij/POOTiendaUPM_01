package org.example;

public class Producto {
    Integer identificador;
    String nombre;
    Categoria categoria;
    float precio;

    public Producto(Integer identificador, String nombre, Categoria categoria, float precio) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        if(identificador == null || identificador <=0){
            throw new IllegalArgumentException("El identificador no puede estar vacío y debe ser un número positivo");
        }
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if(nombre== null || nombre.isBlank() || nombre.length() >=100){
            throw new IllegalArgumentException("El nombre no puede estar vacío y debe contener menos de 100 caracteres");
        }
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        if(categoria==null){
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        this.categoria = categoria;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        if(precio<=0){
            throw new IllegalArgumentException("El precio debe ser un número mayor a 0 sin límite superior");
        }
        this.precio = precio;
    }
}
