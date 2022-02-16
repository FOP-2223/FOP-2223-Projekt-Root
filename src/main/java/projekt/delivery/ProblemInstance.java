package projekt.delivery;

import projekt.base.DistanceCalculator;
import projekt.delivery.vehicle.Vehicle;

import java.util.List;

public class ProblemInstance {
    private final DistanceCalculator distanceCalculator;
    private final List<Vehicle> vehicles;
    private final List<ConfirmedOrder> orders;

    public ProblemInstance(DistanceCalculator distanceCalculator, List<Vehicle> vehicles, List<ConfirmedOrder> orders) {
        this.distanceCalculator = distanceCalculator;
        this.vehicles = vehicles;
        this.orders = orders;
    }

    public DistanceCalculator getDistance2D() {
        return distanceCalculator;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<ConfirmedOrder> getOrders() {
        return orders;
    }
}
