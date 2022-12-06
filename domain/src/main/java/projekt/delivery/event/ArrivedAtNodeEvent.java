package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

public interface ArrivedAtNodeEvent extends VehicleEvent {

    static ArrivedAtNodeEvent of(
        long tick,
        Vehicle vehicle,
        Region.Node node,
        Region.Edge lastEdge
    ) {
        return new ArrivedAtNodeEventImpl(tick, vehicle, node, lastEdge);
    }

    Region.Node getNode();

    Region.Edge getLastEdge();
}
