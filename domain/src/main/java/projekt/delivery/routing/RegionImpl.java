package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;

import java.util.*;

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

    @Override
    public Collection<Node> getNodes() {
        return unmodifiableNodes;
    }

    @Override
    public Collection<Edge> getEdges() {
        return unmodifiableEdges;
    }

    void putNode(NodeImpl node) {
        if (this != node.getRegion()) {
            throw new IllegalArgumentException("Node %s has incorrect region".formatted(node.toString()));
        }
        nodes.put(node.getLocation(), node);
    }

    void putEdge(EdgeImpl edge) {
        if (this != edge.getRegion()) {
            throw new IllegalArgumentException("Edge %s has incorrect region".formatted(edge.toString()));
        }
        if (edge.getNodeA() == null) {
            throw new IllegalArgumentException("NodeA %s has incorrect region".formatted(edge.getNodeA().toString()));
        }

        if (edge.getNodeB() == null) {
            throw new IllegalArgumentException("NodeB %s has incorrect region".formatted(edge.getNodeB().toString()));
        }

        edges.computeIfAbsent(
            edge.getNodeA().getLocation(),
            k -> new HashMap<>()
        ).put(edge.getNodeB().getLocation(), edge);
        allEdges.add(edge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes, edges);
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
}
