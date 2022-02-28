package projekt.delivery.routing;

import projekt.base.DistanceCalculator;
import projekt.base.Location;
import projekt.delivery.ConfirmedOrder;
import projekt.delivery.event.EventBus;
import projekt.food.FoodType;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Predicate;

public interface VehicleManager {

    Region getRegion();

    DistanceCalculator getDistanceCalculator();

    PathCalculator getPathCalculator();

    Collection<Vehicle> getVehicles();

    Vehicle addVehicle(
        double capacity,
        Collection<FoodType<?, ?>> compatibleFoodTypes,
        Predicate<? super Occupied<Region.Node>> nodePredicate
    );

    Vehicle addVehicle(
        double capacity,
        Collection<FoodType<?, ?>> compatibleFoodTypes
    );

    // O(1)
    <C extends Region.Component<C>> Occupied<C> getOccupied(C component);

    Collection<Occupied<Region.Node>> getOccupiedNodes();

    Collection<Occupied<Region.Edge>> getOccupiedEdges();

    EventBus getEventBus();

    LocalDateTime getCurrentTime();

    void tick();

    Factory SIMPLE = (r, d, pathCalculator) -> new VehicleManagerImpl(r, d, pathCalculator, n ->
        n.getComponent().getLocation().equals(new Location(0, 0)));

    interface Occupied<C extends Region.Component<C>> {

        C getComponent();

        VehicleManager getVehicleManager();

        Collection<Vehicle> getVehicles();

        static <RC extends Region.Component<RC>> Predicate<? super Occupied<RC>> named(String name) {
            return c -> c.getComponent().getName().equals(name);
        }
    }

    interface Warehouse extends Occupied<Region.Node> {
        void loadOrder(Vehicle vehicle, ConfirmedOrder order);
    }

    interface Factory {
        VehicleManager create(Region region, DistanceCalculator distanceCalculator, PathCalculator pathCalculator);
    }
}
