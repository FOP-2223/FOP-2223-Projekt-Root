package projekt.delivery.solver;

import projekt.delivery.routing.ConfirmedOrder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SimpleDeliveryRoutePlaner implements DeliveryRoutePlanner {

    private final List<ConfirmedOrder> orders;

    public SimpleDeliveryRoutePlaner(List<ConfirmedOrder> orders) {
        this.orders = orders;
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

        routes.forEach((tick, orders) -> orders.sort(Comparator.comparingLong(ConfirmedOrder::getReadyToDeliverAt)));

        return routes;
    }
}
