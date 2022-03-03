package projekt.delivery.routing;

import projekt.base.DistanceCalculator;
import projekt.base.Location;

import java.time.Duration;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

class RegionBuilderImpl implements Region.Builder {
    private DistanceCalculator distanceCalc;
    private final Map<Location, NodeBuilder> nodes = new HashMap<>();
    private final Set<EdgeBuilder> edges = new TreeSet<>(
        Comparator.comparing(EdgeBuilder::getLocationA).thenComparing(EdgeBuilder::getLocationB)
    );
    private final Set<String> allNames = new HashSet<>();

    private void addName(String name) {
        if (!allNames.add(name)) {
            throw new IllegalArgumentException(String.format("Duplicate name '%s'", name));
        }
    }

    @Override
    public Region.Builder distanceCalculator(DistanceCalculator distanceCalculator) {
        this.distanceCalc = distanceCalculator;
        return this;
    }

    @Override
    public Region.Builder addNode(String name, Location location) {
        addName(name);
        if (nodes.putIfAbsent(location, new NodeBuilder(name, location)) != null) {
            allNames.remove(name);
            throw new IllegalArgumentException("Duplicate node at location " + location);
        }
        return this;
    }

    @Override
    public Region.Builder addNeighborhood(String name, Location location, double distance) {
        addName(name);
        if (nodes.putIfAbsent(location, new NeighborhoodBuilder(name, location, distance)) != null) {
            allNames.remove(name);
            throw new IllegalArgumentException("Duplicate node at location " + location);
        }
        return this;
    }

    @Override
    public Region.Builder addEdge(String name, Location locationA, Location locationB) {
        if (locationA.compareTo(locationB) < 0) {
            addSortedEdge(name, locationA, locationB);
        } else {
            addSortedEdge(name, locationB, locationA);
        }
        return this;
    }

    private void addSortedEdge(String name, Location locationA, Location locationB) {
        addName(name);
        if (!edges.add(new EdgeBuilder(name, locationA, locationB))) {
            allNames.remove(name);
            throw new IllegalArgumentException("Duplicate edge connecting %s to %s".formatted(locationA, locationB));
        }
    }

    @Override
    public Region build() {
        Objects.requireNonNull(distanceCalc, "distanceCalculator");
        RegionImpl region = new RegionImpl();
        nodes.forEach((l, n) -> region.putNode(n.build(region)));
        edges.forEach(e -> {
            nodes.get(e.locationA).connections.add(e.locationB);
            nodes.get(e.locationB).connections.add(e.locationA);
            region.putEdge(e.build(region, distanceCalc));
        });
        return region;
    }

    private static class NodeBuilder {

        protected final String name;
        protected final Location location;
        protected final Set<Location> connections = new HashSet<>();

        private NodeBuilder(String name, Location location) {
            this.name = name;
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }

        NodeImpl build(Region region) {
            // may only be used once as the backing map for connections is not copied
            return new NodeImpl(region, name, location, Collections.unmodifiableSet(connections));
        }
    }

    private static class NeighborhoodBuilder extends NodeBuilder {

        protected final double distance;

        public NeighborhoodBuilder(String name, Location location, double distance) {
            super(name, location);
            this.distance = distance;
        }

        @Override
        NeighborhoodImpl build(Region region) {
            // may only be used once as the backing map for connections is not copied
            return new NeighborhoodImpl(region, name, location, Collections.unmodifiableSet(connections), distance);
        }
    }

    static final class EdgeBuilder {
        private final String name;
        private final Location locationA;
        private final Location locationB;

        EdgeBuilder(String name, Location locationA, Location locationB) {
            this.name = name;
            this.locationA = locationA;
            this.locationB = locationB;
        }

        EdgeImpl build(Region region, DistanceCalculator distanceCalculator) {
            double distance = distanceCalculator.calculateDistance(locationA, locationB);
            Duration duration = Duration.ofMinutes((long)Math.ceil(distance));
            return new EdgeImpl(region, name, locationA, locationB, duration);
        }

        public Location getLocationA() {
            return locationA;
        }

        public Location getLocationB() {
            return locationB;
        }
    }
}
