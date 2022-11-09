package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

public interface ArrivedAtNeighborhoodEvent extends ArrivedAtNodeEvent {
    static ArrivedAtNeighborhoodEvent of(
        LocalDateTime time,
        Vehicle vehicle,
        Region.Neighborhood node,
        Region.Edge lastEdge
    ) {
        return new ArrivedAtNeighborhoodEventImpl(time, vehicle, node, lastEdge);
    }

    @Override
    Region.Neighborhood getNode();
}
