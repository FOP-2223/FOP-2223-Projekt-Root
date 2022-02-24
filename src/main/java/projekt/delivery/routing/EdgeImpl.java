package projekt.delivery.routing;

import projekt.base.Location;

import java.time.Duration;
import java.util.Comparator;
import java.util.Objects;

class EdgeImpl implements Region.Edge {

    private final static Comparator<Region.Edge> COMPARATOR =
        Comparator.comparing(Region.Edge::getNodeA).thenComparing(Region.Edge::getNodeB);

    private final Region region;
    private final String name;
    private final Location locationA;
    private final Location locationB;
    private final Duration duration;

    EdgeImpl(
        Region region,
        String name,
        Location locationA,
        Location locationB,
        Duration duration
    ) {
        this.region = region;
        this.name = name;
        this.locationA = locationA;
        this.locationB = locationB;
        this.duration = duration;
    }

    public Location getLocationA() {
        return locationA;
    }

    public Location getLocationB() {
        return locationB;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Region.Node getNodeA() {
        return getRegion().getNode(locationA);
    }

    @Override
    public Region.Node getNodeB() {
        return getRegion().getNode(locationB);
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public int compareTo(Region.Edge o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeImpl edge = (EdgeImpl) o;
        return Objects.equals(name, edge.name)
            && Objects.equals(locationA, edge.locationA)
            && Objects.equals(locationB, edge.locationB)
            && Objects.equals(duration, edge.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, locationA, locationB, duration);
    }
}
