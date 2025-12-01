package org.example;

public class TicketLineMeetingProduct extends TicketLine {
    private final int numPeopleAttending;
    private final MeetingProduct meetingProduct;

    public TicketLineMeetingProduct(MeetingProduct product, int numPeopleAttending) {
        super(product);
        if (numPeopleAttending <= 0 || numPeopleAttending > product.getMaxPeople()) {
            throw new IllegalArgumentException("number of people must be between 1 and " + product.getMaxPeople());
        }
        this.numPeopleAttending = numPeopleAttending;
        this.meetingProduct = product;
    }

    @Override
    public float getPrice() {
        return meetingProduct.getPricePerPerson() * numPeopleAttending;
    }

    @Override
    public String toString() {
        return "{class:Meeting"
                + ", id:" + product.id
                + ", name:" + product.name
                + ", date of Event:" + meetingProduct.getExpirationDate()
                + ", price:" + this.getPrice()
                + ", max people allowed:" + meetingProduct.getMaxPeople()
                + ", actual people in event:" + numPeopleAttending
                + "}";
    }
}
