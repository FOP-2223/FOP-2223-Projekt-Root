package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.DistanceCalculator;
import projekt.base.Location;

import java.util.Collection;
import java.util.List;
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

        long getDuration();

        Node getNodeA();

        Node getNodeB();
    }

    interface Neighborhood extends Node {
        double getDistance();

        default boolean checkWithin(Node node, DistanceCalculator calculator) { // TODO: Maybe static?
            return calculator.calculateDistance(getLocation(), node.getLocation()) <= getDistance();
        }
    }

    interface Restaurant extends Node {

        //TODO kreativeres Essen
        Preset LOS_FOPBOTS_HERMANOS =new Preset("Los Fopbots Hermanos", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"));

        Preset JAVA_HUT = new Preset("Java Hut", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"));

        Preset PASTAFAR =  new Preset("Pastafar", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"));

        Preset PALPAPIZZA =  new Preset("Palpapizza", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"         ));

        Preset ISENJAR =  new Preset("Isenjar", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"));

        Preset MIDDLE_FOP =  new Preset("Middle Fop", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"         ));

        Preset MOUNT_DOOM_PIZZA =  new Preset("Mount Doom Pizza", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"));


        List<String> getAvailableFood();

        record Preset(String name, List<String> availableFoods) {}
    }

    interface Builder {

        Builder distanceCalculator(DistanceCalculator distanceCalculator);

        Builder addNode(String name, Location location);

        Builder addNeighborhood(String name, Location location, double distance);

        Builder addRestaurant(String name, Location location, List<String> availableFood);

        Builder addRestaurant(Location location, Region.Restaurant.Preset restaurantPreset);

        Builder addEdge(String name, Location locationA, Location locationB);

        Region build();
    }
}
