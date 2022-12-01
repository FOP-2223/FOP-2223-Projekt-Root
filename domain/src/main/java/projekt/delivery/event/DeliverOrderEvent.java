package projekt.delivery.event;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

public interface DeliverOrderEvent extends VehicleEvent {

    static DeliverOrderEvent of(
        long tick,
        Vehicle vehicle,
        Region.Neighborhood node,
        ConfirmedOrder order
    ) {
        return new DeliverOrderEventImpl(tick, vehicle, node, order);
    }

    ConfirmedOrder getOrder();

    Region.Neighborhood getNode();
}
