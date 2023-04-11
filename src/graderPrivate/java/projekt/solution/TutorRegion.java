package projekt.solution;

import org.jetbrains.annotations.Nullable;
import projekt.base.DistanceCalculator;
import projekt.base.EuclideanDistanceCalculator;
import projekt.base.Location;
import projekt.delivery.routing.EdgeImpl;
import projekt.delivery.routing.NodeImpl;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.RegionImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("DuplicatedCode")
public class TutorRegion extends RegionImpl {

    public Collection<Node> unmodifiableNodes = Collections.unmodifiableCollection(nodes.values());
    public Collection<Edge> unmodifiableEdges = Collections.unmodifiableCollection(allEdges);

    /**
     * Creates a new, empty {@link TutorRegion} instance using a {@link EuclideanDistanceCalculator}.
     */
    public TutorRegion() {
        this(new EuclideanDistanceCalculator());
    }

    /**
     * Creates a new, empty {@link TutorRegion} instance using the given {@link DistanceCalculator}.
     */
    public TutorRegion(DistanceCalculator distanceCalculator) {
        super(distanceCalculator);
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
     * Adds the given {@link TutorRegion} to this {@link TutorRegion}.
     * @param node the {@link TutorRegion} to add.
     */
    @Override
    public void putNode(NodeImpl node) {
        if (this != node.getRegion()) {
            throw new IllegalArgumentException("Node %s has incorrect region".formatted(node.toString()));
        }
        nodes.put(node.getLocation(), node);
    }

    /**
     * Adds the given {@link TutorEdge} to this {@link TutorRegion}.
     * @param edge the {@link TutorEdge} to add.
     */
    @Override
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
        TutorRegion region = (TutorRegion) o;
        return Objects.equals(nodes, region.nodes) && Objects.equals(edges, region.edges);
    }
}
