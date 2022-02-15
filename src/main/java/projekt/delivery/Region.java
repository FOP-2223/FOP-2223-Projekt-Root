package projekt.delivery;

import org.jetbrains.annotations.Nullable;
import projekt.base.DistanceCalculator;
import projekt.base.Location;

import java.util.Collection;
import java.util.Set;

public interface Region {

    static Builder builder() {
        // TODO: Write aufgabe
        return new RegionBuilderImpl();
    }

    @Nullable Node getNode(Location location);

    @Nullable Edge getEdge(Location locationA, Location locationB);

    Collection<? extends Node> getNodes();

    Collection<? extends Edge> getEdges();

    interface Component<C extends Component<C>> extends Comparable<C> {

        Region getRegion();
    }

    interface Node extends Component<Node> {

        Set<Node> getAdjacentNodes();

        Set<Edge> getAdjacentEdges();

        @Nullable Edge getEdge(Node other);

        Location getLocation();
    }

    interface Edge extends Component<Edge> {
        Node getNodeA();

        Node getNodeB();
    }

    interface Neighborhood extends Node {
        double getDistance();

        default boolean checkWithin(Region.Node node, DistanceCalculator calculator) { // TODO: Maybe static?
            return calculator.calculateDistance(getLocation(), node.getLocation()) <= getDistance();
        }
    }

    interface Builder {

        Builder addNode(Location location);

        Builder addNeighborhood(Location location, double distance);

        Builder addEdge(Location locationA, Location locationB);

        Region build();
    }
}
