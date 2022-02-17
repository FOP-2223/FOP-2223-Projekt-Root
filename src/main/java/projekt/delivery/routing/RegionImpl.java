package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    void addNode(NodeImpl node) {
        nodes.put(node.getLocation(), node);
    }

    @Override
    public @Nullable Edge getEdge(Location locationA, Location locationB) {
        if (locationA.compareTo(locationB) < 0) {
            return edges.get(locationA).get(locationB);
        } else {
            return edges.get(locationB).get(locationA);
        }
    }

    void addEdge(EdgeImpl edge) {
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
}
