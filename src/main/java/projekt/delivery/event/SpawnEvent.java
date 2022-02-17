package projekt.delivery.event;

import projekt.delivery.routing.Region;

public interface SpawnEvent extends Event {

    Region.Node getNode();
}
