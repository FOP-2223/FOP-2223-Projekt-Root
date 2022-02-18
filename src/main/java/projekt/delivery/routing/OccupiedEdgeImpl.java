package projekt.delivery.routing;

import projekt.delivery.event.ArrivedAtEdgeEvent;

import java.time.LocalDateTime;

class OccupiedEdgeImpl extends AbstractOccupied<Region.Edge> {
    OccupiedEdgeImpl(Region.Edge component, VehicleManager vehicleManager) {
        super(component, vehicleManager);
    }

    @Override
    void addVehicle(VehicleImpl vehicle, Runnable callback) {
        final Region.Component<?> previous = vehicle.getOccupied().getComponent();
        if (previous instanceof Region.Edge) {
            throw new AssertionError("Cannot move directly from edge to edge");
        }
        final Region.Node previousEdge = (Region.Node) previous;
        final LocalDateTime currentTime = vehicleManager.getCurrentTime();
        vehicles.put(vehicle, new VehicleStats(currentTime));
        vehicle.setOccupied(this);
        vehicleManager.getEventBus().queuePost(ArrivedAtEdgeEvent.class, ArrivedAtEdgeEvent.of(
                currentTime,
                vehicle,
                component,
                previousEdge
            )
        );
        // TODO: Run callback when component startTime + component.duration > currentTIme
    }
}
