package projekt.delivery.service;

import projekt.delivery.event.Event;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;

import java.util.List;

public interface DeliveryService {

    Factory SIMPLE = BasicDeliveryService::new;
    Factory BOGO = BogoDeliveryService::new;

    void deliver(List<ConfirmedOrder> confirmedOrders);

    List<Event> tick(long currentTick);

    VehicleManager getVehicleManager();

    List<ConfirmedOrder> getPendingOrders();

    void reset();

    interface Factory {

        DeliveryService create(VehicleManager vehicleManager);
    }
}
