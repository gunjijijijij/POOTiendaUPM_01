package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MeetingProduct extends PeopleProduct {

    public MeetingProduct(String id, String name, float pricePerPerson, LocalDate expirationDate, int maxPeople) {
        super(id, name, pricePerPerson, expirationDate, maxPeople);
    }

    public MeetingProduct(MeetingProduct meetingProduct, int quantity) {
        super(meetingProduct, quantity);
    }

    public MeetingProduct() {
        super();
    }

    @Override
    public String toString() {
        return buildToString("Meeting");
    }

    @Override
    public List<Product> addToTicket(int quantity, List<String> customTexts) {
        if (customTexts != null) throw new IllegalArgumentException("this product doesn't support customizations");
        List<Product> result = new ArrayList<>();
        result.add(new MeetingProduct(this, quantity));
        return result;
    }
}
