package projekt.delivery.routing;

import projekt.base.Location;

import static org.tudalgo.algoutils.student.Student.crash;

class EdgeImpl implements Region.Edge {

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
        return crash(); // TODO: H4.1 - remove if implemented
    }

    public Location getLocationB() {
        return crash(); // TODO: H4.1 - remove if implemented
    }

    @Override
    public Region getRegion() {
        return crash(); // TODO: H4.1 - remove if implemented
    }

    @Override
    public String getName() {
        return crash(); // TODO: H4.1 - remove if implemented
    }

    @Override
    public long getDuration() {
        return crash(); // TODO: H4.1 - remove if implemented
    }

    @Override
    public Region.Node getNodeA() {
        return crash(); // TODO: H4.2 - remove if implemented
    }

    @Override
    public Region.Node getNodeB() {
        return crash(); // TODO: H4.2 - remove if implemented
    }

    @Override
    public int compareTo(Region.Edge o) {
        return crash(); // TODO: H4.3 - remove if implemented
    }

    @Override
    public int hashCode() {
        return crash(); // TODO: H4.5 - remove if implemented
    }

    @Override
    public boolean equals(Object o) {
        return crash(); // TODO: H4.4 - remove if implemented
    }

    @Override
    public String toString() {
        return crash(); // TODO: H4.6 - remove if implemented
    }
}
