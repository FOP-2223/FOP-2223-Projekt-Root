package projekt.delivery.event;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

class DeliverOrderEventImpl extends EventImpl implements DeliverOrderEvent {

    private final Region.Neighborhood node;
    private final ConfirmedOrder order;

    DeliverOrderEventImpl(
        long tick,
        Vehicle vehicle,
        Region.Neighborhood node,
        ConfirmedOrder order
    ) {
        super(tick, vehicle);
        this.node = node;
        this.order = order;
    }

    @Override
    public ConfirmedOrder getOrder() {
        return order;
    }

    @Override
    public Region.Neighborhood getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "DeliverOrderEvent("
            + "time=" + getTick()
            + ", vehicle=" + getVehicle().getId()
            + ", node=" + getNode()
            + ", order=" + getOrder()
            + ')';
    }
}
