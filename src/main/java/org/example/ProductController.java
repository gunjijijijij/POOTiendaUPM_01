package org.example;

public class ProductController {
    private static final int MAX_PRODUCTS = 200;
    private static Product[] products;
    private static int productCount;

    public ProductController(Product[] products, int productCount){
        this.products = new Product[MAX_PRODUCTS];
        this.productCount = 0;
    }
    private void addproduct (String name, int id, Category category, float prize){
        for(int i = 0; i< productCount; i++){
            if(products[i].getId().equals(id)){ //comprobamos que no existan dos productos con el mismo id
                throw new IllegalArgumentException("There are two products with the same id\n");
            }
        }
        if(productCount >= MAX_PRODUCTS){ //verificamos el límite de productos
            throw new IllegalStateException("No more products can be added, the maximum is " + MAX_PRODUCTS);
        }
        Product product = new Product(id, name, category, prize); //creamos y agregamos el producto
        products[productCount] = product;
        productCount++; //aumentamos el contador

    }

    private void prodlist(String[] listaproductos){
        System.out.println("Catalog:");
        if(productCount == 0){
            System.out.println("(empty)"); //No sé si hay que poner algo cuando esté vacío (?)
        }else{
            for(int i = 0; i< productCount; i++){
                Product product = products[i];
                System.out.println("(class:Product, id:" + product.getId() + ", name: '" + product.getName() + "', category:" + product.getCategory() + ", price: " + product.getPrice() + ")");
            }
        }
        System.out.println("prod list: ok");
    }

    public void prodRemove (int id){
        for (int i = 0; i < productCount; i++){
            if(products[i].getId().equals(id)){
                Ticket.removeProduct(i);
                for (int j = i; j<productCount-1; j++){ //Reordena los productos
                    products[j] = products[j+1];
                }
                products[productCount] =null;
                productCount--;
                return;
            }
        }
        throw new IllegalArgumentException(String.format("The product with id %s was not found", id));
    }

    public void prodUpdate (int id, String action){
        for (int i = 0; i < productCount; i++){
            if(products[i].getId().equals(id)){
                Product product = products[i];
                switch (action.toUpperCase()){
                    case "NAME":
                        break;
                    case "PRICE":
                        break;
                    case "CATEGORY":
                        break;
                }
            }
        }
    }

    public static Product findProductById(int id) {
        for (int i = 0; i < productCount; i++) {
            if (products[i].getId() == id) {
                return products[i];
            }
        }
        return null;
    }
}
