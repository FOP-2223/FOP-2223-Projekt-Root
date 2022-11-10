package projekt.delivery.routing;

import projekt.delivery.event.ArrivedAtEdgeEvent;

import java.util.List;
import java.util.Map;

class OccupiedEdgeImpl extends AbstractOccupied<Region.Edge> {
    OccupiedEdgeImpl(Region.Edge component, VehicleManager vehicleManager) {
        super(component, vehicleManager);
    }

    @Override
    void tick(long currentTick) {
        // it is important to create a copy here. The move method in vehicle will probably modify this map
        for (Map.Entry<VehicleImpl, VehicleStats> entry : List.copyOf(vehicles.entrySet())) {
            if (currentTick >= entry.getValue().arrived + component.getDuration()) {
                entry.getKey().move(currentTick);
            }
        }
    }

    @Override
    void addVehicle(VehicleImpl vehicle, long currentTick) {
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
        vehicles.put(vehicle, new VehicleStats(currentTick, previous));
        vehicle.setOccupied(this);
        vehicleManager.getEventBus().queuePost(ArrivedAtEdgeEvent.of(
                currentTick,
                vehicle,
                component,
                previousNode.getComponent()
            )
        );
    }
}
