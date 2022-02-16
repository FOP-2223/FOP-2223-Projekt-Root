package projekt.delivery.event;

import projekt.delivery.vehicle.Region;

public interface ArrivedAtNodeEvent extends Event {

    Region.Node getNode();

    Region.Edge getLastEdge();
}
