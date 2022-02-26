package projekt.delivery.routing;

import projekt.base.DistanceCalculator;
import projekt.base.Location;
import projekt.delivery.event.EventBus;
import projekt.food.FoodType;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Predicate;

public interface VehicleManager {

    Region getRegion();

    DistanceCalculator getDistanceCalculator();

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

    void update();

    Factory SIMPLE = (r, d) -> new VehicleManagerImpl(r, d, n -> n.getComponent().getLocation().equals(new Location(0, 0)));

    interface Occupied<C extends Region.Component<C>> {

        C getComponent();

        VehicleManager getVehicleManager();

        Collection<Vehicle> getVehicles();

        static <RC extends Region.Component<RC>> Predicate<? super Occupied<RC>> named(String name) {
            return c -> c.getComponent().getName().equals(name);
        }
    }

    interface Factory {
        VehicleManager create(Region region, DistanceCalculator distanceCalculator);
    }
}
