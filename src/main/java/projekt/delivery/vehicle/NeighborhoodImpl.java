package projekt.delivery.vehicle;

import projekt.base.Location;
import projekt.delivery.vehicle.NodeImpl;
import projekt.delivery.vehicle.Region;

import java.util.Set;

class NeighborhoodImpl extends NodeImpl implements Region.Neighborhood {

    private final double distance;

    NeighborhoodImpl(
        Region region,
        Location location,
        Set<Location> connections,
        double distance
    ) {
        super(region, location, connections);
        this.distance = distance;
    }

    @Override
    public double getDistance() {
        return distance;
    }
}
