package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

public interface ArrivedAtEdgeEvent extends VehicleEvent {
    static ArrivedAtEdgeEvent of(
        long tick,
        Vehicle vehicle,
        Region.Edge edge,
        Region.Node lastNode
    ) {
        return new ArrivedAtEdgeEventImpl(tick, vehicle, edge, lastNode);
    }

    Region.Edge getEdge();

    Region.Node getLastNode();
}
