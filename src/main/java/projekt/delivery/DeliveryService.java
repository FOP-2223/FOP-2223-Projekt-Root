package projekt.delivery;

import projekt.delivery.routing.VehicleManager;
import projekt.rating.Rater;

import java.util.List;

public interface DeliveryService {

    void deliver(List<ConfirmedOrder> confirmedOrders);

    interface Factory {

        DeliveryService create(VehicleManager vehicleManager, Rater rater);
    }

    Factory SIMPLE = SimpleDeliveryService::new;
}
