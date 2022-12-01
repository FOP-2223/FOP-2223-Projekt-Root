package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;

import java.util.*;

import static org.tudalgo.algoutils.student.Student.crash;

class RegionImpl implements Region {

    private final Map<Location, NodeImpl> nodes = new HashMap<>();
    private final Map<Location, Map<Location, EdgeImpl>> edges = new HashMap<>();
    private final List<EdgeImpl> allEdges = new ArrayList<>();
    private final Collection<Node> unmodifiableNodes = Collections.unmodifiableCollection(nodes.values());
    private final Collection<Edge> unmodifiableEdges = Collections.unmodifiableCollection(allEdges);

    @Override
    public @Nullable Node getNode(Location location) {
        return crash(); // TODO: H2.1 - remove if implemented
    }

    @Override
    public @Nullable Edge getEdge(Location locationA, Location locationB) {
        return crash(); // TODO: H2.3 - remove if implemented
    }

    @Override
    public Collection<Node> getNodes() {
        return crash(); // TODO: H2.5 - remove if implemented
    }

    @Override
    public Collection<Edge> getEdges() {
        return crash(); // TODO: H2.5 - remove if implemented
    }

    void putNode(NodeImpl node) {
        crash(); // TODO: H2.2 - remove if implemented
    }

    void putEdge(EdgeImpl edge) {
        crash(); // TODO: H2.4 - remove if implemented
    }

    @Override
    public int hashCode() {
        return crash(); // TODO: H2.6 - remove if implemented
    }

    @Override
    public boolean equals(Object o) {
        return crash(); // TODO: H2.6 - remove if implemented
    }
}
