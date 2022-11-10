package projekt.delivery.routing;

import projekt.base.Location;
import projekt.base.TickInterval;

import java.io.Serializable;
import java.util.List;

public class ConfirmedOrder implements Serializable {
    private final Location location;
    private final int orderID;
    private final TickInterval tickInterval;
    private final List<String> foodList;
    private final double weight;
    private long actualDeliveryTick;

    public ConfirmedOrder(Location location, int orderID, TickInterval tickInterval, List<String> foodList, double weigth) {
        this.location = location;
        this.orderID = orderID;
        this.tickInterval = tickInterval;
        this.foodList = foodList;
        this.weight = weigth;
    }

    public ConfirmedOrder(int x, int y, int orderID, TickInterval tickInterval, List<String> foodList, double weight) {
        location = new Location(x, y);
        this.orderID = orderID;
        this.tickInterval = tickInterval;
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

    public TickInterval getTimeInterval() {
        return tickInterval;
    }

    public List<String> getFoodList() {
        return foodList;
    }

    public double getTotalWeight() {
        return weight;
    }

    public long getActualDeliveryTick() {
        return actualDeliveryTick;
    }

    public void setActualDeliveryTick(long actualDeliveryTick) {
        this.actualDeliveryTick = actualDeliveryTick;
    }

    @Override
    public String toString() {
        return String.format(
            "%d, %d, %d, %s, %s, %s",
            getX(),
            getY(),
            orderID,
            tickInterval,
            tickInterval,
            foodList.toString()
        );
    }
}
