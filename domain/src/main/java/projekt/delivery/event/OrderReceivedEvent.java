package projekt.delivery.event;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Region;

public interface OrderReceivedEvent extends Event{

    static OrderReceivedEvent of(
        long tick,
        Region.Node node,
        ConfirmedOrder order
    ) {
        return new OrderReceivedEventImpl(tick, node, order);
    }

    ConfirmedOrder getOrder();

    Region.Node getNode();

}
