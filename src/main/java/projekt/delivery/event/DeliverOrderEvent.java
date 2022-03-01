package projekt.delivery.event;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

public interface DeliverOrderEvent extends Event {

    ConfirmedOrder getOrder();

    Region.Neighborhood getNode();

    static DeliverOrderEvent of(
        LocalDateTime time,
        Vehicle vehicle,
        Region.Neighborhood node,
        ConfirmedOrder order
    ) {
        return new DeliverOrderEventImpl(time, vehicle, node, order);
    }
}
