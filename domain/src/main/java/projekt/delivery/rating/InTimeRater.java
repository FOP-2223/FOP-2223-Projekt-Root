package projekt.delivery.rating;

import projekt.delivery.event.DeliverOrderEvent;
import projekt.delivery.event.Event;
import projekt.delivery.event.OrderReceivedEvent;
import projekt.delivery.routing.ConfirmedOrder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InTimeRater implements Rater {

    private double totalTicksOff = 0;
    private long ordersDelivered = 0;
    private final Set<ConfirmedOrder> pendingOrders = new HashSet<>();

    private static final long IGNORED_TICKS_OFF = 5;

    @Override
    public double getScore() {



        return 0;
    }

    @Override
    public void onTick(List<Event> events, long tick) {
        events.stream()
            .filter(DeliverOrderEvent.class::isInstance)
            .map(DeliverOrderEvent.class::cast)
            .forEach(deliverOrderEvent -> {
                pendingOrders.remove(deliverOrderEvent);

                ConfirmedOrder order = deliverOrderEvent.getOrder();
                long ticksOff = Math.max(order.getActualDeliveryTick() - order.getTimeInterval().getEnd() - IGNORED_TICKS_OFF, 0);
                totalTicksOff += ticksOff;

                ordersDelivered++;
            });

        events.stream()
            .filter(OrderReceivedEvent.class::isInstance)
            .map(OrderReceivedEvent.class::cast)
            .map(OrderReceivedEvent::getOrder)
            .forEach(pendingOrders::add);
    }
}
