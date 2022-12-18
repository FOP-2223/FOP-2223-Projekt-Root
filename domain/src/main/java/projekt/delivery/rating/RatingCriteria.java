package projekt.delivery.rating;

public enum RatingCriteria {

    IN_TIME("In Time"),
    AMOUNT_DELIVERED("Amount Delivered"),
    TRAVEL_DISTANCE("Travel Distance");

    String name;

    RatingCriteria(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
