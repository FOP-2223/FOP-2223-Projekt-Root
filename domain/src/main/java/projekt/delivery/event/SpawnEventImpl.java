package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

class SpawnEventImpl extends EventImpl implements SpawnEvent {

    private final Region.Node node;

    SpawnEventImpl(
        LocalDateTime time,
        Vehicle vehicle,
        Region.Node node
    ) {
        super(time, vehicle);
        this.node = node;
    }

    @Override
    public Region.Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "SpawnEvent("
            + "time=" + getTime()
            + ", vehicle=" + getVehicle().getId()
            + ", node=" + getNode()
            + ')';
    }
}
