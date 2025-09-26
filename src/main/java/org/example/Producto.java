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
        this.identificador = identificador;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public float getPrecio() {

        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
}
