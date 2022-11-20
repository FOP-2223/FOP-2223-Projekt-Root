package projekt.delivery.solver;

import projekt.base.Location;
import projekt.delivery.routing.ConfirmedOrder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SimpleDeliveryRoutePlaner implements DeliveryRoutePlanner {

    private final List<ConfirmedOrder> orders;
    private final Location warehouseLocation;

    public SimpleDeliveryRoutePlaner(List<ConfirmedOrder> orders, Location warehouseLocation) {
        this.orders = orders;
        this.warehouseLocation = warehouseLocation;
    }

    @Override
    public HashMap<Long, List<ConfirmedOrder>> route() {
        HashMap<Long, List<ConfirmedOrder>> routes = new HashMap<>();

        orders.forEach(order -> {
            if (!routes.containsKey(order.getReadyToDeliverAt())) {
                routes.put(order.getReadyToDeliverAt(), new ArrayList<>());
            }

            routes.get(order.getReadyToDeliverAt()).add(order);
        });

        routes.forEach((tick, orders) -> orders.sort(Comparator.comparingDouble(order -> getDistance(order.getLocation(), warehouseLocation))));

        return routes;
    }

    private double getDistance(Location loc1, Location loc2) {
        return Math.sqrt(Math.pow(loc1.getX() - loc2.getX(), 2) + Math.pow(loc1.getY() - loc2.getY(), 2));
    }
}
