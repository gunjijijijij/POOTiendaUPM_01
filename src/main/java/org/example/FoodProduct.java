package org.example;

import java.time.LocalDate;
public class FoodProduct extends Product {
private LocalDate expirationdate;
private int maxpeople;
private float priceperperson;
public FoodProduct(int id, String name, float priceperperson, LocalDate expirationdate, int maxpeople) {
    super(id, name, null, 0, maxpeople);
    if (maxpeople > 100 || maxpeople <= 0) {
        throw new IllegalArgumentException("Max people MUST be between 1 and 100");
    }
    if (!isValidCreation(expirationdate)) {
        throw new IllegalArgumentException("Food product requires AT LEAST 3 days planning");
    }
    this.expirationdate = expirationdate;
    this.maxpeople = maxpeople;
    this.priceperperson = priceperperson;
}
//TODO ESTE METODO SOLO COMPRUEBA QUE DENTRO DE 3 DIAS NO LLEGUE A LA FECHA DE CADUCIDAD, NO SE ME OCURRIÓ COMO COMPROBAR QUE LA PLANIFICACION SEA >=3 DIAS
public boolean isValidCreation(LocalDate expiration){
    return LocalDate.now().plusDays(3).isBefore(expiration);
}
public LocalDate getExpirationdate() {return expirationdate;}
    public int getMaxpeople(){return maxpeople;}
    public String getType(){return "FoodProduct";}
    @Override
    public String toString(){
    return "{class: Product, id: " + id
            + ", name: '" + name
            + ", price per person: " + priceperperson
            + ", expirationdate: " + expirationdate
            + ", maxpeople: " + maxpeople + "}";
    }
    public float calculateTotalPrice(int numberofpeople){
    if(numberofpeople > maxpeople || numberofpeople <=0){
        throw new IllegalArgumentException("Number of people must be between 1 and " + maxpeople);
    }
    return priceperperson * numberofpeople;
    }
}
