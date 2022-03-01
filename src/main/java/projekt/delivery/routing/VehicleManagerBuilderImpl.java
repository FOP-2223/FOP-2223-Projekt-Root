package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.DistanceCalculator;
import projekt.food.FoodType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

class VehicleManagerBuilderImpl implements VehicleManager.Builder {

    private LocalDateTime time;
    private Region region;
    private DistanceCalculator distanceCalculator;
    private PathCalculator pathCalculator;
    private Region.Node warehouse;
    private final List<VehicleBuilder> vehicles = new ArrayList<>();

    @Override
    public VehicleManager.Builder time(LocalDateTime time) {
        this.time = time;
        return this;
    }

    @Override
    public VehicleManager.Builder region(Region region) {
        this.region = region;
        return this;
    }

    @Override
    public VehicleManager.Builder distanceCalculator(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
        return this;
    }

    @Override
    public VehicleManager.Builder pathCalculator(PathCalculator pathCalculator) {
        this.pathCalculator = pathCalculator;
        return this;
    }

    @Override
    public VehicleManager.Builder warehouse(Region.Node warehouse) {
        this.warehouse = warehouse;
        return this;
    }

    @Override
    public VehicleManager.Builder addVehicle(
        double capacity,
        Collection<FoodType<?, ?>> compatibleFoodTypes,
        @Nullable Predicate<? super VehicleManager.Occupied<? extends Region.Node>> nodePredicate
    ) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        vehicles.add(new VehicleBuilder(capacity, compatibleFoodTypes, nodePredicate));
        return this;
    }

    @Override
    public VehicleManager.Builder addVehicle(double capacity, Collection<FoodType<?, ?>> compatibleFoodTypes) {
        return addVehicle(capacity, compatibleFoodTypes, null);
    }

    @Override
    public VehicleManager build() {
        Objects.requireNonNull(time, "time");
        Objects.requireNonNull(region, "region");
        Objects.requireNonNull(distanceCalculator, "distanceCalculator");
        Objects.requireNonNull(pathCalculator, "pathCalculator");
        Objects.requireNonNull(warehouse, "warehouse");
        if (!warehouse.getRegion().equals(region)) {
            throw new IllegalArgumentException(String.format("Warehouse %s is not in region %s", warehouse, region));
        }
        VehicleManagerImpl vehicleManager = new VehicleManagerImpl(time, region, distanceCalculator, pathCalculator, warehouse);
        for (VehicleBuilder vehicleBuilder : vehicles) {
            vehicleManager.addVehicle(vehicleBuilder.capacity, vehicleBuilder.compatibleFoodTypes, vehicleBuilder.nodePredicate);
        }
        return vehicleManager;
    }

    private static class VehicleBuilder {
        private double capacity;
        private Collection<FoodType<?, ?>> compatibleFoodTypes;
        private @Nullable Predicate<? super VehicleManager.Occupied<? extends Region.Node>> nodePredicate;

        private VehicleBuilder(
            double capacity,
            Collection<FoodType<?, ?>> compatibleFoodTypes,
            @Nullable Predicate<? super VehicleManager.Occupied<? extends Region.Node>> nodePredicate
        ) {
            this.capacity = capacity;
            this.compatibleFoodTypes = compatibleFoodTypes;
            this.nodePredicate = nodePredicate;
        }
    }
}
