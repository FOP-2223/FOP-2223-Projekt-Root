package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

class ArrivedAtWarehouseEventImpl extends ArrivedAtNodeEventImpl implements ArrivedAtWarehouseEvent {

    ArrivedAtWarehouseEventImpl(
        long tick,
        Vehicle vehicle,
        Region.Node node,
        Region.Edge lastEdge
    ) {
        super(tick, vehicle, node, lastEdge);
    }

    @Override
    public String toString() {
        return "ArrivedAtWarehouseEvent("
            + "time=" + getTick()
            + ", vehicle=" + getVehicle().getId()
            + ", node=" + getNode()
            + ", lastEdge=" + getLastEdge()
            + ')';
    }
}
