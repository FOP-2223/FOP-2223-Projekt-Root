package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;

import java.util.Set;

import static org.tudalgo.algoutils.student.Student.crash;

class NodeImpl implements Region.Node {

    protected final Set<Location> connections;
    private final Region region;
    private final String name;
    private final Location location;

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
        return crash(); // TODO: H3.1 - remove if implemented
    }

    @Override
    public String getName() {
        return crash(); // TODO: H3.1 - remove if implemented
    }

    @Override
    public Location getLocation() {
        return crash(); // TODO: H3.1 - remove if implemented
    }

    @Override
    public @Nullable Region.Edge getEdge(Region.Node other) {
        return crash(); // TODO: H3.2 - remove if implemented
    }

    @Override
    public Set<Region.Node> getAdjacentNodes() {
        return crash(); // TODO: H3.3 - remove if implemented
    }

    @Override
    public Set<Region.Edge> getAdjacentEdges() {
        return crash(); // TODO: H3.4 - remove if implemented
    }

    @Override
    public int compareTo(Region.Node o) {
        return crash(); // TODO: H3.5 - remove if implemented
    }

    @Override
    public int hashCode() {
        return crash(); // TODO: H3.7 - remove if implemented
    }

    @Override
    public boolean equals(Object o) {
        return crash(); // TODO: H3.6 - remove if implemented
    }

    @Override
    public String toString() {
        return crash(); // TODO: H3.8 - remove if implemented
    }
}
