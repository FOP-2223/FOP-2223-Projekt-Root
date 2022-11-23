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

    private static int nextOrderID;

    public ConfirmedOrder(Location location, TickInterval tickInterval, List<String> foodList, double weight) {
        this.location = location;
        this.tickInterval = tickInterval;
        this.foodList = foodList;
        this.weight = weight;
        orderID = nextOrderID++;
    }

    public ConfirmedOrder(int x, int y, TickInterval tickInterval, List<String> foodList, double weight) {
        this(new Location(x,y), tickInterval, foodList, weight);
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
