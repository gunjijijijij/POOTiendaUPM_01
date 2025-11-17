package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
public class MeetingProduct extends Product{
private LocalDate expirationdate;
private int maxpeople;
private float priceperperson;
public MeetingProduct(int id, String name, float priceperperson, LocalDate expirationdate, int maxpeople) {
    super(id, name, null, 0);
    if (maxpeople > 100 || maxpeople <= 0) {
        throw new IllegalArgumentException("Max people MUST be between 1 and 100");
    }
    if (!isValidCreation(expirationdate)) {
        throw new IllegalArgumentException("Meeting product requires AT LEAST 12 hours planning");
    }
    this.expirationdate = expirationdate;
    this.maxpeople = maxpeople;
    this.priceperperson = priceperperson;
}
//TODO ESTE METODO SOLO COMPRUEBA QUE DENTRO DE 12 HORAS NO LLEGUE A LA FECHA DE CADUCIDAD, NO SE ME OCURRIÓ COMO COMPROBAR QUE LA PLANIFICACION SEA >=12 HORAS
public boolean isValidCreation(LocalDate expiration){
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime minDateTime = now.plusHours(12);
    LocalDateTime expirationDateTime = expiration.atTime(23,59);
    return expirationDateTime.isAfter(minDateTime);
}
public LocalDate getExpirationdate() {return expirationdate;}
public int getMaxpeople(){return maxpeople;}
public String getType(){return "MeetingProduct";}
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
