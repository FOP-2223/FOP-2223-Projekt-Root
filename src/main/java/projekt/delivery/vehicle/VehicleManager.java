package projekt.delivery.vehicle;

import projekt.base.DistanceCalculator;
import projekt.food.FoodType;

import java.util.Collection;

public interface VehicleManager {

    Region getRegion();

    DistanceCalculator getDistanceCalculator();

    Collection<Vehicle> getVehicles();

    Vehicle addVehicle(double capacity, Iterable<FoodType<?, ?>> compatibleFoodTypes);

    // O(1)
    <C extends Region.Component<C>> Occupied<C> getOccupied(C component);

    Collection<Occupied<Region.Node>> getOccupiedNodes();

    Collection<Occupied<Region.Edge>> getOccupiedEdges();

    interface Occupied<C extends Region.Component<C>> {

        C getComponent();

        VehicleManager getVehicleManager();

        Collection<Vehicle> getVehicles();
    }

    interface Factory {
        VehicleManager create(Region region, DistanceCalculator distanceCalculator);
    }
}
