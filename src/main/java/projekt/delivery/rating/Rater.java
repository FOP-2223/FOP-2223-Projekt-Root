package projekt.delivery.rating;

import projekt.delivery.ConfirmedOrder;

import java.util.List;

public interface Rater {

    // TODO: Check both optimization criteria
    double rate(List<ConfirmedOrder> confirmedOrders);
}
