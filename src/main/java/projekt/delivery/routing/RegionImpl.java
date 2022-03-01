package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class RegionImpl implements Region {

    private final Map<Location, NodeImpl> nodes = new HashMap<>();
    private final Map<Location, Map<Location, EdgeImpl>> edges = new HashMap<>();
    private final List<EdgeImpl> allEdges = new ArrayList<>();
    private final Collection<Node> unmodifiableNodes = Collections.unmodifiableCollection(nodes.values());
    private final Collection<Edge> unmodifiableEdges = Collections.unmodifiableCollection(allEdges);

    @Override
    public @Nullable Node getNode(Location location) {
        return nodes.get(location);
    }

    void putNode(NodeImpl node) {
        // TODO: Test exception
        if (this != node.getRegion()) {
            throw new IllegalArgumentException("Node " + node + " has incorrect region");
        }
        nodes.put(node.getLocation(), node);
    }

    @Override
    public @Nullable Edge getEdge(Location locationA, Location locationB) {
        if (locationA.compareTo(locationB) > 0) {
            Location temp = locationA;
            locationA = locationB;
            locationB = temp;
        }
        final @Nullable Map<Location, EdgeImpl> firstDim = edges.get(locationA);
        if (firstDim == null) {
            return null;
        }
        return firstDim.get(locationB);
    }

    void putEdge(EdgeImpl edge) {
        // TODO: Write tests for exceptions
        if (this != edge.getRegion()) {
            throw new IllegalArgumentException("edge has incorrect region");
        }
        Objects.requireNonNull(edge.getNodeA(), "node " + edge.getLocationA() + " not present in region");
        Objects.requireNonNull(edge.getNodeB(), "node" + edge.getLocationB() + " not present in region");
        edges.computeIfAbsent(
            edge.getNodeA().getLocation(),
            k -> new HashMap<>()
        ).put(edge.getNodeB().getLocation(), edge);
        allEdges.add(edge);
    }

    @Override
    public Collection<Node> getNodes() {
        return unmodifiableNodes;
    }

    @Override
    public Collection<Edge> getEdges() {
        return unmodifiableEdges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegionImpl region = (RegionImpl) o;
        return Objects.equals(nodes, region.nodes) && Objects.equals(edges, region.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges);
    }
}
