package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;

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
        Location startingLocation,
        double capacity,
        Collection<String> compatibleFoodTypes
    ) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        vehicles.add(new VehicleBuilder(startingLocation, capacity, compatibleFoodTypes));
        return this;
    }

    @Override
    public VehicleManager build() {
        Objects.requireNonNull(region, "region");
        Objects.requireNonNull(pathCalculator, "pathCalculator");
        VehicleManagerImpl vehicleManager = new VehicleManagerImpl(region, pathCalculator);
        for (VehicleBuilder vehicleBuilder : vehicles) {
            vehicleManager.addVehicle(vehicleBuilder.startingLocation, vehicleBuilder.capacity, vehicleBuilder.compatibleFoodTypes);
        }
        return vehicleManager;
    }

    private record VehicleBuilder(Location startingLocation, double capacity, Collection<String> compatibleFoodTypes) { }
}
