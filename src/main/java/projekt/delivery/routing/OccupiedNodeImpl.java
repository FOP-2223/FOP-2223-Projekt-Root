package projekt.delivery.routing;

import projekt.delivery.event.ArrivedAtNodeEvent;

import java.time.LocalDateTime;

class OccupiedNodeImpl extends AbstractOccupied<Region.Node> {
    OccupiedNodeImpl(Region.Node component, VehicleManager vehicleManager) {
        super(component, vehicleManager);
    }

    @Override
    void addVehicle(VehicleImpl vehicle, Runnable callback) {
        final Region.Component<?> previous = vehicle.getOccupied().getComponent();
        if (previous instanceof Region.Node) {
            throw new AssertionError("Cannot move directly from node to node");
        }
        final Region.Edge previousEdge = (Region.Edge) previous;
        final LocalDateTime currentTime = vehicleManager.getCurrentTime();
        vehicles.put(vehicle, new VehicleStats(currentTime));
        vehicle.setOccupied(this);
        vehicleManager.getEventBus().queuePost(ArrivedAtNodeEvent.class, ArrivedAtNodeEvent.of(
                currentTime,
                vehicle,
                component,
                previousEdge
            )
        );
        callback.run();
    }
}
