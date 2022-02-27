package projekt.delivery.rating;

import projekt.delivery.ConfirmedOrder;

import java.time.Duration;
import java.util.List;

public class LinearRater implements Rater {

    @Override
    public double rate(List<ConfirmedOrder> confirmedOrders) {
        return confirmedOrders.stream()
            .mapToDouble(order ->
                Duration
                    .between(
                        order.getTimeInterval().getStart(),
                        order.getActualDeliveryTime()
                    ).getSeconds())
            .sum();
    }
}
