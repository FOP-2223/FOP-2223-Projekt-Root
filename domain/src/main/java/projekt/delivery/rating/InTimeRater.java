package projekt.delivery.rating;

import projekt.delivery.event.DeliverOrderEvent;
import projekt.delivery.event.Event;
import projekt.delivery.event.OrderReceivedEvent;
import projekt.delivery.routing.ConfirmedOrder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InTimeRater implements Rater {

    public static final RatingCriteria RATING_CRITERIA = RatingCriteria.IN_TIME;

    private long totalTicksOff = 0;
    private long ordersDelivered = 0;
    private final Set<ConfirmedOrder> pendingOrders = new HashSet<>();

    private final long ignoredTicksOff;
    private final long maxTicksOff;

    InTimeRater(long ignoredTicksOff, long maxTicksOff) {
        if (ignoredTicksOff < 0) throw new IllegalArgumentException(String.valueOf(ignoredTicksOff));
        if (maxTicksOff <= 0) throw new IllegalArgumentException(String.valueOf(maxTicksOff));

        this.ignoredTicksOff = ignoredTicksOff;
        this.maxTicksOff = maxTicksOff;
    }

    public double getScore() {
        long maxTotalTicksOff = maxTicksOff * (ordersDelivered + pendingOrders.size());
        long actualTotalTicksOff = totalTicksOff + pendingOrders.size() * maxTicksOff;

        return 1 - (((double) actualTotalTicksOff) / maxTotalTicksOff);
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

                long ticksOff = Math.max(Math.min(order.getActualDeliveryTick() - order.getDeliveryInterval().getEnd() - ignoredTicksOff, 15), 0);
                totalTicksOff += ticksOff;

                ordersDelivered++;
            });

        events.stream()
            .filter(OrderReceivedEvent.class::isInstance)
            .map(OrderReceivedEvent.class::cast)
            .map(OrderReceivedEvent::getOrder)
            .forEach(pendingOrders::add);
    }

    @Override
    public RatingCriteria getRatingCriteria() {
        return RATING_CRITERIA;
    }

    public static class Factory implements Rater.Factory {

        private final long ignoredTicksOff;
        private final long maxTicksOff;

        Factory(long ignoredTicksOff, long maxTicksOff) {
            this.ignoredTicksOff = ignoredTicksOff;
            this.maxTicksOff = maxTicksOff;
        }

        @Override
        public Rater create() {
            return new InTimeRater(ignoredTicksOff, maxTicksOff);
        }
    }

    public static class FactoryBuilder implements Rater.FactoryBuilder {

        private long ignoredTicksOff = 5;
        private long maxTicksOff = 25;


        public FactoryBuilder setIgnoredTicksOff(long ignoredTicksOff) {
            this.ignoredTicksOff = ignoredTicksOff;
            return this;
        }

        public FactoryBuilder setMaxTicksOff(long maxTicksOff) {
            this.maxTicksOff = maxTicksOff;
            return this;
        }

        @Override
        public Rater.Factory build() {
            return new Factory(ignoredTicksOff, maxTicksOff);
        }
    }
}
