package projekt.delivery.routing;

import projekt.delivery.event.ArrivedAtRestaurantEvent;
import projekt.delivery.event.LoadOrderEvent;

class OccupiedRestaurantImpl extends OccupiedNodeImpl<Region.Restaurant> implements VehicleManager.OccupiedRestaurant {

    OccupiedRestaurantImpl(Region.Restaurant component, VehicleManager vehicleManager) {
        super(component, vehicleManager);
    }

    @Override
    public void loadOrder(Vehicle vehicle, ConfirmedOrder order, long currentTick) {
        if (vehicle.getOccupied() != this) {
            throw new IllegalArgumentException("The specified vehicle is not located on this node!");
        }

        ((VehicleImpl) vehicle).loadOrder(order);
        vehicleManager.getEventBus().queuePost(LoadOrderEvent.of(
                currentTick,
                vehicle,
                order,
                this
            )
        );
    }

    @Override
    protected void emitArrivedEvent(VehicleImpl vehicle, OccupiedEdgeImpl previousEdge, long currentTick) {
        vehicleManager.getEventBus().queuePost(ArrivedAtRestaurantEvent.of(
                currentTick,
                vehicle,
                this,
                previousEdge.getComponent()
            )
        );
    }
}
