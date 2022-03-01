package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

class ArrivedAtWarehouseEventImpl extends ArrivedAtNodeEventImpl implements ArrivedAtWarehouseEvent {

    ArrivedAtWarehouseEventImpl(
        LocalDateTime time,
        Vehicle vehicle,
        Region.Node node,
        Region.Edge lastEdge
    ) {
        super(time, vehicle, node, lastEdge);
    }

    @Override
    public String toString() {
        return "ArrivedAtWarehouseEvent("
            + "time= " + getTime()
            + ", vehicle= " + getVehicle()
            + ", node=" + getNode()
            + ", lastEdge=" + getLastEdge()
            + ')';
    }
}
