package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.DistanceCalculator;
import projekt.delivery.ConfirmedOrder;
import projekt.delivery.event.EventBus;
import projekt.food.FoodType;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.function.Predicate;

public interface VehicleManager {

    static Builder builder() {
        return new VehicleManagerBuilderImpl();
    }

    Region getRegion();

    DistanceCalculator getDistanceCalculator();

    PathCalculator getPathCalculator();

    Collection<Vehicle> getVehicles();

    Warehouse getWarehouse();

    // O(1)
    <C extends Region.Component<C>> Occupied<C> getOccupied(C component);

    Collection<Occupied<Region.Node>> getOccupiedNodes();

    Collection<Occupied<Region.Edge>> getOccupiedEdges();

    EventBus getEventBus();

    LocalDateTime getCurrentTime();

    void tick();

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

    interface Builder {
        Builder time(LocalDateTime time);

        Builder region(Region region);

        Builder distanceCalculator(DistanceCalculator distanceCalculator);

        Builder pathCalculator(PathCalculator pathCalculator);

        Builder warehouse(Region.Node warehouse);

        Builder addVehicle(
            double capacity,
            Collection<FoodType<?, ?>> compatibleFoodTypes,
            @Nullable Predicate<? super Occupied<Region.Node>> nodePredicate
        );

        Builder addVehicle(
            double capacity,
            Collection<FoodType<?, ?>> compatibleFoodTypes
        );

        VehicleManager build();
    }
}
