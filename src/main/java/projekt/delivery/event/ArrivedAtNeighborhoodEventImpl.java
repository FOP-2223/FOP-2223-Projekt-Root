package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

class ArrivedAtNeighborhoodEventImpl extends ArrivedAtNodeEventImpl implements ArrivedAtNeighborhoodEvent {

    ArrivedAtNeighborhoodEventImpl(
        LocalDateTime time,
        Vehicle vehicle,
        Region.Neighborhood node,
        Region.Edge lastEdge
    ) {
        super(time, vehicle, node, lastEdge);
    }

    @Override
    public Region.Neighborhood getNode() {
        return (Region.Neighborhood) super.getNode();
    }

    @Override
    public String toString() {
        return "ArrivedAtNeighborhoodEvent("
            + "time=" + getTime()
            + ", vehicle=" + getVehicle().getId()
            + ", node=" + getNode()
            + ", lastEdge=" + getLastEdge()
            + ')';
    }
}
