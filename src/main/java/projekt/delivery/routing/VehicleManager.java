package projekt.delivery.routing;

import projekt.base.DistanceCalculator;
import projekt.food.FoodType;

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
