package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

public interface ArrivedAtNodeEvent extends Event {

    static ArrivedAtNodeEvent of(
        LocalDateTime time,
        Vehicle vehicle,
        Region.Node node,
        Region.Edge lastEdge
    ) {
        return new ArrivedAtNodeEventImpl(time, vehicle, node, lastEdge);
    }

    Region.Node getNode();

    Region.Edge getLastEdge();
}
