package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

public interface SpawnEvent extends VehicleEvent {

    static SpawnEvent of(
        long tick,
        Vehicle vehicle,
        Region.Node node
    ) {
        return new SpawnEventImpl(tick, vehicle, node);
    }

    Region.Node getNode();
}
