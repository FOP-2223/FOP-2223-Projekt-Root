package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

class ArrivedAtNeighborhoodEventImpl extends EventImpl implements ArrivedAtNeighborhoodEvent {

    private final Region.Neighborhood node;
    private final Region.Edge lastEdge;

    ArrivedAtNeighborhoodEventImpl(
        LocalDateTime time,
        Vehicle vehicle,
        Region.Neighborhood node,
        Region.Edge lastEdge
    ) {
        super(time, vehicle);
        this.node = node;
        this.lastEdge = lastEdge;
    }

    @Override
    public Region.Neighborhood getNode() {
        return node;
    }

    @Override
    public Region.Edge getLastEdge() {
        return lastEdge;
    }
}
