package projekt.delivery.event;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

class OrderReceivedEventImpl extends EventImpl implements OrderReceivedEvent {

    private final Region.Node node;
    private final ConfirmedOrder order;

    public OrderReceivedEventImpl(long tick, Region.Node node, ConfirmedOrder order) {
        super(tick, null);
        this.node = node;
        this.order = order;
    }

    @Override
    public Region.Node getNode() {
        return node;
    }

    @Override
    public ConfirmedOrder getOrder() {
        return order;
    }

    @Override
    public Vehicle getVehicle() {
        return null;
    }

    @Override
    public String toString() {
        return "OrderReceivedEventImpl{" +
            "time=" + getTick() +
            ", node=" + getNode() +
            ", order=" + getOrder() +
            '}';
    }
}
