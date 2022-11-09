package projekt.delivery.routing;

import projekt.delivery.event.ArrivedAtEdgeEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class OccupiedEdgeImpl extends AbstractOccupied<Region.Edge> {
    OccupiedEdgeImpl(Region.Edge component, VehicleManager vehicleManager) {
        super(component, vehicleManager);
    }

    @Override
    void tick() {
        final LocalDateTime currentTime = vehicleManager.getCurrentTime();
        // it is important to create a copy here. The move method in vehicle will probably modify this map
        for (Map.Entry<VehicleImpl, VehicleStats> entry : List.copyOf(vehicles.entrySet())) {
            if (!currentTime.isBefore(entry.getValue().arrived.plus(component.getDuration()))) {
                entry.getKey().move();
            }
        }
    }

    @Override
    void addVehicle(VehicleImpl vehicle) {
        if (vehicles.containsKey(vehicle)) {
            return;
        }
        final VehicleManager.Occupied<?> previous = vehicle.getOccupied();
        if (previous instanceof OccupiedEdgeImpl) {
            throw new AssertionError("Vehicle " + vehicle.getId() + " cannot move directly from edge to edge");
        }
        final OccupiedNodeImpl<?> previousNode = (OccupiedNodeImpl) previous;
        if (previousNode.vehicles.remove(vehicle) == null) {
            throw new AssertionError("Vehicle " + vehicle.getId() + " was not found in previous node");
        }
        final LocalDateTime currentTime = vehicleManager.getCurrentTime();
        vehicles.put(vehicle, new VehicleStats(currentTime, previous));
        vehicle.setOccupied(this);
        vehicleManager.getEventBus().queuePost(ArrivedAtEdgeEvent.of(
                currentTime,
                vehicle,
                component,
                previousNode.getComponent()
            )
        );
    }
}
