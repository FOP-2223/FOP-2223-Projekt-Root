package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

public interface ArrivedAtWarehouseEvent extends ArrivedAtNodeEvent {

    @Override
    Region.Node getNode();

    static ArrivedAtWarehouseEvent of(
        LocalDateTime time,
        Vehicle vehicle,
        Region.Node node,
        Region.Edge lastEdge
    ) {
        return new ArrivedAtWarehouseEventImpl(time, vehicle, node, lastEdge);
    }
}