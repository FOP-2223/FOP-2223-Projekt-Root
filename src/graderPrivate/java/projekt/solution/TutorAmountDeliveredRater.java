package projekt.solution;

import projekt.delivery.event.DeliverOrderEvent;
import projekt.delivery.event.Event;
import projekt.delivery.event.OrderReceivedEvent;
import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.ConfirmedOrder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TutorAmountDeliveredRater implements Rater {

    public static final RatingCriteria RATING_CRITERIA = RatingCriteria.AMOUNT_DELIVERED;

    private long ordersCount = 0;
    private final Set<ConfirmedOrder> pendingOrders = new HashSet<>();

    private final double factor;

    private TutorAmountDeliveredRater(double factor) {
        this.factor = factor;
    }

    @Override
    public double getScore() {
        long undeliveredOrders = pendingOrders.size();
        double maxUndeliveredOrders = ordersCount * (1 - factor);

        if (undeliveredOrders > maxUndeliveredOrders || maxUndeliveredOrders == 0) {
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

    /**
     * A {@link Rater.Factory} for creating a new {@link TutorAmountDeliveredRater}.
     */
    public static class Factory implements Rater.Factory {

        public final double factor;

        private Factory(double factor) {
            this.factor = factor;
        }

        @Override
        public TutorAmountDeliveredRater create() {
            return new TutorAmountDeliveredRater(factor);
        }

        /**
         * Creates a new {@link TutorAmountDeliveredRater.FactoryBuilder}.
         * @return The created {@link TutorAmountDeliveredRater.FactoryBuilder}.
         */
        public static FactoryBuilder builder() {
            return new FactoryBuilder();
        }
    }

    /**
     * A {@link Rater.FactoryBuilder} for constructing a new {@link TutorAmountDeliveredRater.Factory}.
     */
    public static class FactoryBuilder implements Rater.FactoryBuilder {

        public double factor = 0.99;

        private FactoryBuilder() {}

        @Override
        public Factory build() {
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

