package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

public interface ArrivedAtEdgeEvent extends Event {
    Region.Edge getEdge();

    Region.Node getLastEdge();

    static ArrivedAtEdgeEvent of(
        LocalDateTime time,
        Vehicle vehicle,
        Region.Edge edge,
        Region.Node lastNode
    ) {
        return new ArrivedAtEdgeEventImpl(time, vehicle, edge, lastNode);
    }
}
