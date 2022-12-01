package projekt.delivery.event;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;

public interface LoadOrderEvent extends VehicleEvent {

    static LoadOrderEvent of(
        long tick,
        Vehicle vehicle,
        ConfirmedOrder order,
        VehicleManager.OccupiedRestaurant restaurant
    ) {
        return new LoadOrderEventImpl(tick, vehicle, order, restaurant);
    }

    ConfirmedOrder getOrder();

    VehicleManager.OccupiedRestaurant getRestaurant();
}
