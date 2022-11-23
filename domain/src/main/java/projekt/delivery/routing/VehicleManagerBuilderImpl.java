package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

class VehicleManagerBuilderImpl implements VehicleManager.Builder {

    private final List<VehicleBuilder> vehicles = new ArrayList<>();
    private Region region;
    private PathCalculator pathCalculator;

    @Override
    public VehicleManager.Builder region(Region region) {
        this.region = region;
        return this;
    }

    @Override
    public VehicleManager.Builder pathCalculator(PathCalculator pathCalculator) {
        this.pathCalculator = pathCalculator;
        return this;
    }

    @Override
    public VehicleManager.Builder addVehicle(
        double capacity,
        Collection<String> compatibleFoodTypes,
        @Nullable Predicate<? super VehicleManager.Occupied<? extends Region.Node>> nodePredicate
    ) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        vehicles.add(new VehicleBuilder(capacity, compatibleFoodTypes, nodePredicate));
        return this;
    }

    @Override
    public VehicleManager.Builder addVehicle(double capacity, Collection<String> compatibleFoodTypes) {
        return addVehicle(capacity, compatibleFoodTypes, null);
    }

    @Override
    public VehicleManager build() {
        Objects.requireNonNull(region, "region");
        Objects.requireNonNull(pathCalculator, "pathCalculator");
        VehicleManagerImpl vehicleManager = new VehicleManagerImpl(region, pathCalculator);
        for (VehicleBuilder vehicleBuilder : vehicles) {
            vehicleManager.addVehicle(vehicleBuilder.capacity, vehicleBuilder.compatibleFoodTypes, vehicleBuilder.nodePredicate);
        }
        return vehicleManager;
    }

    private static class VehicleBuilder {
        private final double capacity;
        private final Collection<String> compatibleFoodTypes;
        private final @Nullable Predicate<? super VehicleManager.Occupied<? extends Region.Node>> nodePredicate;

        private VehicleBuilder(
            double capacity,
            Collection<String> compatibleFoodTypes,
            @Nullable Predicate<? super VehicleManager.Occupied<? extends Region.Node>> nodePredicate
        ) {
            this.capacity = capacity;
            this.compatibleFoodTypes = compatibleFoodTypes;
            this.nodePredicate = nodePredicate;
        }
    }
}
