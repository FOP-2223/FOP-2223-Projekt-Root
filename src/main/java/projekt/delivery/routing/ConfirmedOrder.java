package projekt.delivery.routing;

import projekt.base.Location;
import projekt.base.TimeInterval;
import projekt.food.Extra;
import projekt.food.Food;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ConfirmedOrder implements Serializable {
    private final Location location;
    private final int orderID;
    private final TimeInterval timeInterval;
    private LocalDateTime actualDeliveryTime;
    private final List<Food> foodList;

    public ConfirmedOrder(Location location, int orderID, TimeInterval timeInterval, List<Food> foodList) {
        this.location = location;
        this.orderID = orderID;
        this.timeInterval = timeInterval;
        this.foodList = foodList;
    }

    public ConfirmedOrder(int x, int y, int orderID, TimeInterval timeInterval, List<Food> foodList) {
        location = new Location(x, y);
        this.orderID = orderID;
        this.timeInterval = timeInterval;
        this.foodList = foodList;
    }

    public Location getLocation() {
        return location;
    }

    public int getX() {
        return location.getX();
    }

    public int getY() {
        return location.getY();
    }

    public int getOrderID() {
        return orderID;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public double getTotalWeight() {
        return foodList.stream().mapToDouble(Food::getWeight).sum();
    }

    public LocalDateTime getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    @Override
    public String toString() {
        return String.format(
            "%d, %d, %d, %s, %s, %s",
            getX(),
            getY(),
            orderID,
            timeInterval.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE),
            timeInterval.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE),
            foodList
                .stream()
                .map(food -> String.format(
                    "%s %s %s",
                    food.getFoodVariant().getFoodType().getName(),
                    food.getFoodVariant().getName(),
                    food.getExtras().stream().map(Extra::getName).collect(Collectors.joining("|"))
                ))
                .collect(Collectors.joining(", "))
        );
    }
}
