package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.DistanceCalculator;
import projekt.base.EuclideanDistanceCalculator;
import projekt.base.Location;

import java.util.*;

public class RegionImpl implements Region {

    public final Map<Location, NodeImpl> nodes = new HashMap<>();
    public final Map<Location, Map<Location, EdgeImpl>> edges = new HashMap<>();
    public final List<EdgeImpl> allEdges = new ArrayList<>();
    public final Collection<Node> unmodifiableNodes = Collections.unmodifiableCollection(nodes.values());
    public final Collection<Edge> unmodifiableEdges = Collections.unmodifiableCollection(allEdges);
    public final DistanceCalculator distanceCalculator;

    /**
     * Creates a new, empty {@link RegionImpl} instance using a {@link EuclideanDistanceCalculator}.
     */
    public RegionImpl() {
        this(new EuclideanDistanceCalculator());
    }

    /**
     * Creates a new, empty {@link RegionImpl} instance using the given {@link DistanceCalculator}.
     */
    public RegionImpl(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

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

    @Override
    public DistanceCalculator getDistanceCalculator() {
        return distanceCalculator;
    }

    /**
     * Adds the given {@link NodeImpl} to this {@link RegionImpl}.
     * @param node the {@link NodeImpl} to add.
     */
    public void putNode(NodeImpl node) {
        if (this != node.getRegion()) {
            throw new IllegalArgumentException("Node %s has incorrect region".formatted(node.toString()));
        }
        nodes.put(node.getLocation(), node);
    }

    /**
     * Adds the given {@link EdgeImpl} to this {@link RegionImpl}.
     * @param edge the {@link EdgeImpl} to add.
     */
    public void putEdge(EdgeImpl edge) {
        if (this != edge.getRegion()) {
            throw new IllegalArgumentException("Edge %s has incorrect region".formatted(edge.toString()));
        }
        if (edge.getNodeA() == null) {
            throw new IllegalArgumentException("NodeA %s is not part of the region".formatted(edge.getLocationA().toString()));
        }
        if (edge.getNodeB() == null) {
            throw new IllegalArgumentException("NodeB %s is not part of the region".formatted(edge.getLocationB().toString()));
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
