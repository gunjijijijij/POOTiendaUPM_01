package org.example;

public class ProductController {
    private static final int MAX_PRODUCTS = 200;
    private Product[] products;
    private int productcount;

    public ProductController(Product[] products, int productcount){
        this.products = new Product[MAX_PRODUCTS];
        this.productcount = 0;
    }
    private void addproduct (String nombre, int id, Categoria categoria, float precio){
        for(int i=0; i<productcount; i++){
            if(products[i].getId().equals(id)){ //comprobamos que no existan dos productos con el mismo id
                throw new IllegalArgumentException("Existen dos productos con el mismo id");
            }
        }
        if(productcount >= MAX_PRODUCTS){ //verificamos el límite de productos
            throw new IllegalStateException("No se pueden agregar más productos, el máximo es " + MAX_PRODUCTS);
        }
        Product product = new Product(id, nombre, categoria,precio); //creamos y agregamos el producto
        products[productcount] = product;
        productcount++; //aumentamos el contador

    }

    private void prodlist(String[] listaproductos){
        System.out.println("Catalog:");
        if(productcount == 0){
            System.out.println("(empty)"); //No sé si hay que poner algo cuando esté vacío (?)
        }else{
            for(int i=0; i<productcount;i++){
                Product product = products[i];
                System.out.println("(class:Product, id:" + product.getId() + ", name: '" + product.getName() + "', category:" + product.getCategory() + ", price: " + product.getPrice() + ")");
            }
        }
        System.out.println("prod list: ok");


    }
}
