package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class NodeImpl implements Region.Node {

    private final Region region;
    private final String name;
    private final Location location;
    protected final Set<Location> connections;

    NodeImpl(
        Region region,
        String name,
        Location location,
        Set<Location> connections
    ) {
        this.region = region;
        this.name = name;
        this.location = location;
        this.connections = connections;
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
    public Location getLocation() {
        return location;
    }

    @Override
    public @Nullable Region.Edge getEdge(Region.Node other) {
        return region.getEdge(getLocation(), other.getLocation());
    }

    @Override
    public Set<Region.Node> getAdjacentNodes() {
        return connections.stream().map(region::getNode).collect(Collectors.toSet());
    }

    @Override
    public Set<Region.Edge> getAdjacentEdges() {
        return connections.stream().map(c -> region.getEdge(getLocation(), c)).collect(Collectors.toSet());
    }

    @Override
    public int compareTo(Region.Node o) {
        return location.compareTo(o.getLocation());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NodeImpl node = (NodeImpl) o;
        if (hashCode() != node.hashCode()) {
            return false;
        }
        return Objects.equals(name, node.name)
            && Objects.equals(location, node.location)
            && Objects.equals(connections, node.connections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, connections);
    }

    @Override
    public String toString() {
        return "NodeImpl(name='" + getName() + "'"
            + ", location=" + getLocation()
            + ", connections=" + connections
            + ')';
    }
}
