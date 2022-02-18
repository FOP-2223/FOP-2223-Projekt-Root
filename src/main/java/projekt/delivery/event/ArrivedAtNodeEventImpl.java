package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

class ArrivedAtNodeEventImpl extends EventImpl implements ArrivedAtNodeEvent {

    private final Region.Node node;
    private final Region.Edge lastEdge;

    ArrivedAtNodeEventImpl(
        LocalDateTime time,
        Vehicle vehicle,
        Region.Node node,
        Region.Edge lastEdge
    ) {
        super(time, vehicle);
        this.node = node;
        this.lastEdge = lastEdge;
    }

    @Override
    public Region.Node getNode() {
        return node;
    }

    @Override
    public Region.Edge getLastEdge() {
        return lastEdge;
    }
}
