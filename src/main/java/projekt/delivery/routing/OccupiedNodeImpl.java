package projekt.delivery.routing;

import projekt.delivery.event.ArrivedAtNodeEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class OccupiedNodeImpl<C extends Region.Node> extends AbstractOccupied<C> {
    OccupiedNodeImpl(C component, VehicleManager vehicleManager) {
        super(component, vehicleManager);
    }

    @Override
    void tick() {
        // it is important to create a copy here. The move method in vehicle will probably modify this map
        // TODO: Only move things that can be moved
        for (Map.Entry<VehicleImpl, VehicleStats> entry : List.copyOf(vehicles.entrySet())) {
            entry.getKey().move();
        }
    }

    @Override
    void addVehicle(VehicleImpl vehicle) {
        if (vehicles.containsKey(vehicle)) {
            return;
        }
        final VehicleManager.Occupied<?> previous = vehicle.getOccupied();
        if (previous instanceof OccupiedNodeImpl) {
            throw new AssertionError("Vehicle " + vehicle.getId() + " cannot move directly from node to node");
        }
        final OccupiedEdgeImpl previousEdge = (OccupiedEdgeImpl) previous;
        if (previousEdge.vehicles.remove(vehicle) == null) {
            throw new AssertionError("Vehicle " + vehicle.getId() + " was not found in previous edge");
        }
        final LocalDateTime currentTime = vehicleManager.getCurrentTime();
        vehicles.put(vehicle, new VehicleStats(currentTime, previous));
        vehicle.setOccupied(this);
        vehicleManager.getEventBus().queuePost(ArrivedAtNodeEvent.of(
                currentTime,
                vehicle,
                component,
                previousEdge.getComponent()
            )
        );
    }
}
