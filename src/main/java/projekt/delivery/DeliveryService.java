package projekt.delivery;

import projekt.base.DistanceCalculator;

import java.util.List;

public interface DeliveryService {

    void deliver(List<ConfirmedOrder> confirmedOrders);

    interface Factory {

        DeliveryService create(DistanceCalculator distanceCalculator);
    }

    Factory SIMPLE = SimpleDeliveryService::new;
}
