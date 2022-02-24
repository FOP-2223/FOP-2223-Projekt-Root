package projekt.delivery.routing;

import projekt.base.Location;

import java.util.Objects;
import java.util.Set;

class NeighborhoodImpl extends NodeImpl implements Region.Neighborhood {

    private final double distance;

    NeighborhoodImpl(
        Region region,
        String name,
        Location location,
        Set<Location> connections,
        double distance
    ) {
        super(region, name, location, connections);
        this.distance = distance;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NeighborhoodImpl that = (NeighborhoodImpl) o;
        return Double.compare(that.distance, distance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), distance);
    }

    @Override
    public String toString() {
        return "NeighborhoodImpl(name='" + getName()
            + ", location=" + getLocation()
            + ", distance=" + getDistance()
            + ", connections=" + connections
            + ')';
    }
}
