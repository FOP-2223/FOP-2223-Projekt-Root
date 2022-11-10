package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;

import java.util.Comparator;
import java.util.Objects;

class EdgeImpl implements Region.Edge {

    private final static Comparator<Region.Edge> COMPARATOR =
        Comparator.comparing(Region.Edge::getNodeA).thenComparing(Region.Edge::getNodeB);

    private final Region region;
    private final String name;
    private final Location locationA;
    private final Location locationB;
    private final long duration;

    EdgeImpl(
        Region region,
        String name,
        Location locationA,
        Location locationB,
        long duration
    ) {
        this.region = region;
        this.name = name;
        // locations must be in ascending order
        if (locationA.compareTo(locationB) > 0) {
            throw new IllegalArgumentException(String.format("locationA %s must be <= locationB %s", locationA, locationB));
        }
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

    private Region.Node getNode(Location location) {
        final @Nullable Region.Node node = getRegion().getNode(location);
        if (node == null) {
            throw new IllegalStateException(String.format("node %s does not exist", location));
        }
        return node;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public Region.Node getNodeA() {
        return getNode(locationA);
    }

    @Override
    public Region.Node getNodeB() {
        return getNode(locationB);
    }

    @Override
    public int compareTo(Region.Edge o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, locationA, locationB, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EdgeImpl edge = (EdgeImpl) o;
        return Objects.equals(name, edge.name)
            && Objects.equals(locationA, edge.locationA)
            && Objects.equals(locationB, edge.locationB)
            && Objects.equals(duration, edge.duration);
    }

    @Override
    public String toString() {
        return "EdgeImpl(" +
            "name='" + name + '\'' +
            ", locationA=" + locationA +
            ", locationB=" + locationB +
            ", duration=" + duration +
            ')';
    }
}
