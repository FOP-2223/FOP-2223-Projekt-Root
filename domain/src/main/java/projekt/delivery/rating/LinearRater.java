package projekt.delivery.rating;

import projekt.delivery.routing.ConfirmedOrder;

import java.util.List;

public class LinearRater implements Rater {

    @Override
    public double rate(List<ConfirmedOrder> confirmedOrders) {
        return confirmedOrders.stream()
            .mapToDouble(order ->
                Math.max(0, order.getActualDeliveryTick() - order.getTimeInterval().getStart()))
            .sum();
    }
}
