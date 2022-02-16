package projekt.delivery.event;

import projekt.delivery.vehicle.Region;

public interface ArrivedAtEdgeEvent extends Event {
    Region.Edge getEdge();

    Region.Node getLastEdge();
}
