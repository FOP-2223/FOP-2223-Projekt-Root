package projekt.delivery.vehicle;

import org.jetbrains.annotations.Nullable;
import projekt.base.DistanceCalculator;
import projekt.base.Location;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;

public interface Region {

    static Builder builder() {
        // TODO: Write aufgabe
        return new RegionBuilderImpl();
    }

    // O(1)
    @Nullable Node getNode(Location location);

    // O(1)
    @Nullable Edge getEdge(Location locationA, Location locationB);

    Collection<Node> getNodes();

    Collection<Edge> getEdges();

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

        Duration getDuration();
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

        Builder addEdge(Location locationA, Location locationB, Duration duration);

        Region build();
    }
}
