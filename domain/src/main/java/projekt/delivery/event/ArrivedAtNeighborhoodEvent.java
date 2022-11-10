package projekt.delivery.event;

import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;

public interface ArrivedAtNeighborhoodEvent extends ArrivedAtNodeEvent {
    static ArrivedAtNeighborhoodEvent of(
        long tick,
        Vehicle vehicle,
        Region.Neighborhood node,
        Region.Edge lastEdge
    ) {
        return new ArrivedAtNeighborhoodEventImpl(tick, vehicle, node, lastEdge);
    }

    @Override
    Region.Neighborhood getNode();
}
