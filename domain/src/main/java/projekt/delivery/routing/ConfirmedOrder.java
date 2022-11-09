package projekt.delivery.routing;

import projekt.base.Location;
import projekt.base.TimeInterval;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConfirmedOrder implements Serializable {
    private final Location location;
    private final int orderID;
    private final TimeInterval timeInterval;
    private final List<String> foodList;
    private final double weight;
    private LocalDateTime actualDeliveryTime;

    public ConfirmedOrder(Location location, int orderID, TimeInterval timeInterval, List<String> foodList, double weigth) {
        this.location = location;
        this.orderID = orderID;
        this.timeInterval = timeInterval;
        this.foodList = foodList;
        this.weight = weigth;
    }

    public ConfirmedOrder(int x, int y, int orderID, TimeInterval timeInterval, List<String> foodList, double weight) {
        location = new Location(x, y);
        this.orderID = orderID;
        this.timeInterval = timeInterval;
        this.foodList = foodList;
        this.weight = weight;
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

    public List<String> getFoodList() {
        return foodList;
    }

    public double getTotalWeight() {
        return weight;
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
            foodList.toString()
        );
    }
}
