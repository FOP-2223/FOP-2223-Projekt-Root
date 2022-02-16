package projekt.delivery.event;

import projekt.delivery.vehicle.Region;

public interface SpawnEvent extends Event {

    Region.Node getNode();
}
