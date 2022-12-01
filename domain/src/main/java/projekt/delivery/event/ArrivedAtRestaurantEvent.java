package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;

public interface ArrivedAtRestaurantEvent extends ArrivedAtNodeEvent {

    static ArrivedAtRestaurantEvent of(
        long tick,
        Vehicle vehicle,
        VehicleManager.OccupiedRestaurant restaurant,
        Region.Edge lastEdge
    ) {
        return new ArrivedAtRestaurantEventImpl(tick, vehicle, restaurant, lastEdge);
    }

    public VehicleManager.OccupiedRestaurant getRestaurant();

}
