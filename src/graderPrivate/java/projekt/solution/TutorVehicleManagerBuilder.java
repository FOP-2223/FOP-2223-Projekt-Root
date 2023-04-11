package projekt.solution;

import projekt.base.Location;
import projekt.delivery.routing.PathCalculator;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.routing.VehicleManagerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TutorVehicleManagerBuilder implements VehicleManager.Builder {

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
        double capacity
    ) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        vehicles.add(new VehicleBuilder(startingLocation, capacity));
        return this;
    }

    @Override
    public VehicleManager.Builder removeVehicle(Location startingLocation) {
        vehicles.removeIf(vehicleBuilder -> vehicleBuilder.startingLocation.equals(startingLocation));
        return this;
    }

    @Override
    public VehicleManager build() {
        Objects.requireNonNull(region, "region");
        Objects.requireNonNull(pathCalculator, "pathCalculator");
        TutorVehicleManager vehicleManager = new TutorVehicleManager(region, pathCalculator);
        for (VehicleBuilder vehicleBuilder : vehicles) {
            vehicleManager.addVehicle(vehicleBuilder.startingLocation, vehicleBuilder.capacity);
        }
        return vehicleManager;
    }

    private record VehicleBuilder(Location startingLocation, double capacity) { }

}
