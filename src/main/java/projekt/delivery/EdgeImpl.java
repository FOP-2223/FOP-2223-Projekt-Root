package projekt.delivery;

import projekt.base.Location;

import java.util.Comparator;

class EdgeImpl implements Region.Edge {

    private final static Comparator<Region.Edge> COMPARATOR =
        Comparator.comparing(Region.Edge::getNodeA).thenComparing(Region.Edge::getNodeB);

    private final Region region;
    private final Location locationA;
    private final Location locationB;

    EdgeImpl(
        Region region,
        Location locationA,
        Location locationB
    ) {
        this.region = region;
        this.locationA = locationA;
        this.locationB = locationB;
    }

    @Override
    public Region getRegion() {
        return region;
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
    public int compareTo(Region.Edge o) {
        return COMPARATOR.compare(this, o);
    }
}
