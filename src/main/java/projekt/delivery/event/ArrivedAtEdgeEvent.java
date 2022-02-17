package projekt.delivery.event;

import projekt.delivery.routing.Region;

public interface ArrivedAtEdgeEvent extends Event {
    Region.Edge getEdge();

    Region.Node getLastEdge();
}
