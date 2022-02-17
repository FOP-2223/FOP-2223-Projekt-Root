package projekt.delivery.routing;

import projekt.base.Location;

import java.time.Duration;
import java.util.Comparator;

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
}
