package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.delivery.event.Event;
import projekt.delivery.event.EventBus;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

public interface VehicleManager {

    static Builder builder() {
        return new VehicleManagerBuilderImpl();
    }

    Region getRegion();

    PathCalculator getPathCalculator();

    Collection<Vehicle> getVehicles();

    Collection<Vehicle> getAllVehicles();

    Warehouse getWarehouse();

    // O(1)
    <C extends Region.Component<C>> Occupied<C> getOccupied(C component);

    OccupiedNeighborhood getOccupiedNeighborhood(Region.Node component);

    Collection<Occupied<? extends Region.Node>> getOccupiedNodes();

    Collection<Occupied<? extends Region.Edge>> getOccupiedEdges();

    EventBus getEventBus();

    //long getCurrentTick();

    List<Event> tick(long currentTick);

    interface Occupied<C extends Region.Component<? super C>> {

        static <RC extends Region.Component<RC>> Predicate<? super Occupied<RC>> named(String name) {
            return c -> c.getComponent().getName().equals(name);
        }

        C getComponent();

        VehicleManager getVehicleManager();

        Collection<Vehicle> getVehicles();
    }

    interface Warehouse extends Occupied<Region.Node> {
        void loadOrder(Vehicle vehicle, ConfirmedOrder order, long tick);
    }

    interface OccupiedNeighborhood extends Occupied<Region.Neighborhood> {
        void deliverOrder(Vehicle vehicle, ConfirmedOrder order, long tick);
    }

    interface Builder {
        Builder time(long time);

        Builder region(Region region);

        Builder pathCalculator(PathCalculator pathCalculator);

        Builder warehouse(Region.Node warehouse);

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
