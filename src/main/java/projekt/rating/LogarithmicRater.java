package projekt.rating;

import projekt.delivery.ConfirmedOrder;

import java.time.Duration;
import java.util.List;

public class LogarithmicRater implements Rater {

    @Override
    public double rate(List<ConfirmedOrder> confirmedOrders) {
        return confirmedOrders.stream()
            .mapToDouble(order ->
                Math.log10(
                    Duration
                        .between(
                            order.getActualDeliveryTime(),
                            order.getTimeInterval().getStart()
                        )
                        .getSeconds())
            )
            .sum();
    }
}
