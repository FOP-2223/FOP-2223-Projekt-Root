package projekt.delivery.event;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

class DeliverOrderEventImpl extends EventImpl implements DeliverOrderEvent {

    private final Region.Neighborhood node;
    private final ConfirmedOrder order;

    DeliverOrderEventImpl(
        LocalDateTime time,
        Vehicle vehicle,
        Region.Neighborhood node,
        ConfirmedOrder order
    ) {
        super(time, vehicle);
        this.node = node;
        this.order = order;
    }

    @Override
    public Region.Neighborhood getNode() {
        return node;
    }

    @Override
    public ConfirmedOrder getOrder() {
        return order;
    }
}
