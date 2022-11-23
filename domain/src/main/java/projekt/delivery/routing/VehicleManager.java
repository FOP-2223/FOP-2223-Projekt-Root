package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.delivery.event.Event;
import projekt.delivery.event.EventBus;
import projekt.delivery.event.Tickable;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface VehicleManager extends Tickable {

    static Builder builder() {
        return new VehicleManagerBuilderImpl();
    }

    Region getRegion();

    PathCalculator getPathCalculator();

    Collection<Vehicle> getVehicles();

    Collection<Vehicle> getAllVehicles();

    List<OccupiedRestaurant> getRestaurants();

    // O(1)
    <C extends Region.Component<C>> Occupied<C> getOccupied(C component);

    OccupiedNeighborhood getOccupiedNeighborhood(Region.Node component);

    Collection<Occupied<? extends Region.Node>> getOccupiedNodes();

    Collection<Occupied<? extends Region.Edge>> getOccupiedEdges();

    EventBus getEventBus();

    List<Event> tick(long currentTick);

    interface Occupied<C extends Region.Component<? super C>> {

        static <RC extends Region.Component<RC>> Predicate<? super Occupied<RC>> named(String name) {
            return c -> c.getComponent().getName().equals(name);
        }

        C getComponent();

        VehicleManager getVehicleManager();

        Collection<Vehicle> getVehicles();
    }

    interface OccupiedNeighborhood extends Occupied<Region.Neighborhood> {
        void deliverOrder(Vehicle vehicle, ConfirmedOrder order, long tick);
    }

    @SuppressWarnings("unused")
    interface OccupiedRestaurant extends Occupied<Region.Node> {

        //TODO kreativeres Essen
        Factory LOS_FOPBOTS_HERMANOS = (node, vehicleManager) ->
            new OccupiedRestaurantImpl(node, vehicleManager, "Los Fopbots Hermanos", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"
            ));

        Factory JAVA_HUT = (node, vehicleManager) ->
            new OccupiedRestaurantImpl(node, vehicleManager, "Java Hut", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"
            ));

        Factory PASTAFAR = (node, vehicleManager) ->
            new OccupiedRestaurantImpl(node, vehicleManager, "Pastafar", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"
            ));

        Factory PALPAPIZZA = (node, vehicleManager) ->
            new OccupiedRestaurantImpl(node, vehicleManager, "Palpapizza", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"
            ));

        Factory ISENJAR = (node, vehicleManager) ->
            new OccupiedRestaurantImpl(node, vehicleManager, "Isenjar", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"
            ));

        Factory MIDDLE_FOP = (node, vehicleManager) ->
            new OccupiedRestaurantImpl(node, vehicleManager, "Middle Fop", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"
            ));

        Factory MOUNT_DOOM_PIZZA = (node, vehicleManager) ->
            new OccupiedRestaurantImpl(node, vehicleManager, "Mount Doom Pizza", List.of(
                "Pizza Margherita", "Spaghetti Bolognese", "Rigatoni"
            ));


        void loadOrder(Vehicle vehicle, ConfirmedOrder order, long tick);

        List<String> getAvailableFood();

        String getName();

        interface Factory {

            OccupiedRestaurant create(Region.Node node, VehicleManager vehicleManager);
        }
    }

    interface Builder {

        Builder region(Region region);

        Builder pathCalculator(PathCalculator pathCalculator);

        Builder addRestaurant(Region.Node node, OccupiedRestaurant.Factory restaurantFactory);

        Builder addVehicle(
            double capacity,
            Collection<String> compatibleFoodTypes,
            @Nullable Predicate<? super Occupied<? extends Region.Node>> nodePredicate
        );

        Builder addVehicle(
            double capacity,
            Collection<String> compatibleFoodTypes
        );
        VehicleManager build();
    }
}
