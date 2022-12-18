package projekt.delivery.rating;

import projekt.delivery.event.DeliverOrderEvent;
import projekt.delivery.event.Event;
import projekt.delivery.event.OrderReceivedEvent;
import projekt.delivery.routing.ConfirmedOrder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AmountDeliveredRater implements Rater {

    public static final RatingCriteria RATING_CRITERIA = RatingCriteria.AMOUNT_DELIVERED;

    private long ordersCount = 0;
    private final Set<ConfirmedOrder> pendingOrders = new HashSet<>();

    private final double factor;

    public AmountDeliveredRater(double factor) {
        this.factor = factor;
    }

    @Override
    public double getScore() {
        long undeliveredOrders = pendingOrders.size();
        double maxUndeliveredOrders = ordersCount * (1 - factor);

        if (undeliveredOrders > maxUndeliveredOrders) {
            return 0;
        }

        return 1 - (undeliveredOrders / maxUndeliveredOrders);
    }

    @Override
    public RatingCriteria getRatingCriteria() {
        return RATING_CRITERIA;
    }

    @Override
    public void onTick(List<Event> events, long tick) {
        events.stream()
            .filter(DeliverOrderEvent.class::isInstance)
            .map(DeliverOrderEvent.class::cast)
            .forEach(deliverOrderEvent -> {
                ConfirmedOrder order = deliverOrderEvent.getOrder();

                if (!pendingOrders.remove(order)) {
                    throw new AssertionError("DeliverOrderEvent before OrderReceivedEvent");
                }
            });

        events.stream()
            .filter(OrderReceivedEvent.class::isInstance)
            .map(OrderReceivedEvent.class::cast)
            .map(OrderReceivedEvent::getOrder)
            .forEach(order -> {
                pendingOrders.add(order);
                ordersCount++;
            });
    }

    public static class Factory implements Rater.Factory {

        public final double factor;

        Factory(double factor) {
            this.factor = factor;
        }

        @Override
        public Rater create() {
            return new AmountDeliveredRater(factor);
        }

    }

    public static class FactoryBuilder implements Rater.FactoryBuilder {

        public double factor = 0.99;

        @Override
        public Rater.Factory build() {
            return new Factory(factor);
        }

        public FactoryBuilder setFactor(double factor) {
            if (factor < 0 || factor > 1) {
                throw new IllegalArgumentException("factor must be between 0 and 1");
            }

            this.factor = factor;
            return this;
        }
    }
}
