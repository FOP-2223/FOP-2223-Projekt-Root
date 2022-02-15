package projekt.delivery;

import projekt.base.Location;

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
