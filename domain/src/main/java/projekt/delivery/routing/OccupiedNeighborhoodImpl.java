package projekt.delivery.routing;

import projekt.delivery.event.ArrivedAtNeighborhoodEvent;
import projekt.delivery.event.DeliverOrderEvent;

class OccupiedNeighborhoodImpl extends OccupiedNodeImpl<Region.Neighborhood> implements VehicleManager.OccupiedNeighborhood {
    OccupiedNeighborhoodImpl(Region.Neighborhood component, VehicleManager vehicleManager) {
        super(component, vehicleManager);
    }

    @Override
    public void deliverOrder(Vehicle vehicle, ConfirmedOrder order) {
        if (vehicle.getOccupied() != this) {
            throw new IllegalArgumentException("The specified vehicle is not located on this node!");
        }

        ((VehicleImpl) vehicle).unloadOrder(order);
        vehicleManager.getEventBus().queuePost(DeliverOrderEvent.of(
                vehicleManager.getCurrentTime(),
                vehicle,
                component,
                order
            )
        );
    }

    @Override
    protected void emitArrivedEvent(VehicleImpl vehicle, OccupiedEdgeImpl previousEdge) {
        vehicleManager.getEventBus().queuePost(ArrivedAtNeighborhoodEvent.of(
                vehicleManager.getCurrentTime(),
                vehicle,
                component,
                previousEdge.getComponent()
            )
        );
    }
}
