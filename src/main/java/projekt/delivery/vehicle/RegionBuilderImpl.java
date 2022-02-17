package projekt.delivery.vehicle;

import projekt.base.Location;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class RegionBuilderImpl implements Region.Builder {

    private final Map<Location, NodeBuilder> nodes = new HashMap<>();
    private final Set<EdgeBuilder> edges = new HashSet<>();

    @Override
    public Region.Builder addNode(String name, Location location) {
        if (nodes.putIfAbsent(location, new NodeBuilder(name, location)) != null) {
            throw new IllegalArgumentException("Duplicate node at location " + location);
        }
        return this;
    }

    @Override
    public Region.Builder addNeighborhood(String name, Location location, double distance) {
        if (nodes.putIfAbsent(location, new NeighborhoodBuilder(name, location, distance)) != null) {
            throw new IllegalArgumentException("Duplicate node at location " + location);
        }
        return this;
    }

    @Override
    public Region.Builder addEdge(String name, Location locationA, Location locationB, Duration duration) {
        if (locationA.compareTo(locationB) < 0) {
            addSortedEdge(locationA, locationB, duration);
        } else {
            addSortedEdge(locationB, locationA, duration);
        }
        return null;
    }

    private void addSortedEdge(Location locationA, Location locationB, Duration duration) {
        if (!edges.add(new EdgeBuilder(locationA, locationB, duration))) {
            throw new IllegalArgumentException("Duplicate edge connecting %s to %s".formatted(locationA, locationB));
        }
    }

    @Override
    public Region build() {
        RegionImpl region = new RegionImpl();
        edges.forEach(e -> {
            nodes.get(e.locationA).connections.add(e.locationB);
            nodes.get(e.locationB).connections.add(e.locationA);
            region.addEdge(e.build(region));
        });
        nodes.forEach((l, n) -> region.addNode(n.build(region)));
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

    record EdgeBuilder(String name, Location locationA, Location locationB, Duration duration) {
        EdgeImpl build(Region region) {
            return new EdgeImpl(region, name, locationA, locationB, duration);
        }
    }
}
