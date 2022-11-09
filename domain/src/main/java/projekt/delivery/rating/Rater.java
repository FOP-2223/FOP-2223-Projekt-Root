package projekt.delivery.rating;

import projekt.delivery.routing.ConfirmedOrder;

import java.util.List;

public interface Rater {

    // TODO: Check both optimization criteria
    double rate(List<ConfirmedOrder> confirmedOrders);
}
