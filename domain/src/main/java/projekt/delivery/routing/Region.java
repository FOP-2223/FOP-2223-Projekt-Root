package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.DistanceCalculator;
import projekt.base.Location;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

public interface Region {

    static Builder builder() {
        // TODO: Write aufgabe
        return new RegionBuilderImpl();
    }

    // O(1)
    @Nullable Node getNode(Location location);

    // O(1)
    @Nullable Edge getEdge(Location locationA, Location locationB);

    default @Nullable Edge getEdge(Node nodeA, Node nodeB) {
        return getEdge(nodeA.getLocation(), nodeB.getLocation());
    }

    Collection<Node> getNodes();

    Collection<Edge> getEdges();

    interface Component<C extends Component<C>> extends Comparable<C> {

        static <RC extends Component<RC>> Predicate<? super RC> named(String name) {
            return c -> c.getName().equals(name);
        }

        Region getRegion();

        String getName();
    }

    interface Node extends Component<Node> {

        Location getLocation();

        @Nullable Edge getEdge(Node other);

        Set<Node> getAdjacentNodes();

        Set<Edge> getAdjacentEdges();
    }

    interface Edge extends Component<Edge> {

        Duration getDuration();

        Node getNodeA();

        Node getNodeB();
    }

    interface Neighborhood extends Node {
        double getDistance();

        default boolean checkWithin(Node node, DistanceCalculator calculator) { // TODO: Maybe static?
            return calculator.calculateDistance(getLocation(), node.getLocation()) <= getDistance();
        }
    }

    interface Builder {

        Builder distanceCalculator(DistanceCalculator distanceCalculator);

        Builder addNode(String name, Location location);

        Builder addNeighborhood(String name, Location location, double distance);

        Builder addEdge(String name, Location locationA, Location locationB);

        Region build();
    }
}
