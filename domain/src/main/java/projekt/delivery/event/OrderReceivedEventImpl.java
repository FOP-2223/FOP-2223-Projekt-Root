package projekt.delivery.event;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

class OrderReceivedEventImpl extends EventImpl implements OrderReceivedEvent {

    private final ConfirmedOrder order;

    public OrderReceivedEventImpl(long tick, ConfirmedOrder order) {
        super(tick, null);
        this.order = order;
    }

    public Region.Restaurant getRestaurant() {
        return order.getRestaurant().getComponent();
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
            ", node=" + this.getOrder() +
            ", order=" + this.getOrder() +
            '}';
    }
}
