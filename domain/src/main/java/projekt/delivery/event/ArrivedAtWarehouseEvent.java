package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

public interface ArrivedAtWarehouseEvent extends ArrivedAtNodeEvent {

    static ArrivedAtWarehouseEvent of(
        long tick,
        Vehicle vehicle,
        Region.Node node,
        Region.Edge lastEdge
    ) {
        return new ArrivedAtWarehouseEventImpl(tick, vehicle, node, lastEdge);
    }

    @Override
    Region.Node getNode();
}
