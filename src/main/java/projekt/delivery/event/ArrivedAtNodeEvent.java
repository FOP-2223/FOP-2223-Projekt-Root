package projekt.delivery.event;

import projekt.delivery.routing.Region;

public interface ArrivedAtNodeEvent extends Event {

    Region.Node getNode();

    Region.Edge getLastEdge();
}
