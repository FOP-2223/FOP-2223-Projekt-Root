package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

class ArrivedAtEdgeEventImpl extends EventImpl implements ArrivedAtEdgeEvent {

    private final Region.Edge edge;
    private final Region.Node lastNode;

    ArrivedAtEdgeEventImpl(
        LocalDateTime time,
        Vehicle vehicle,
        Region.Edge edge,
        Region.Node lastNode
    ) {
        super(time, vehicle);
        this.edge = edge;
        this.lastNode = lastNode;
    }

    @Override
    public Region.Edge getEdge() {
        return edge;
    }

    @Override
    public Region.Node getLastEdge() {
        return lastNode;
    }
}
