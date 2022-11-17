package projekt.delivery.solver;

import projekt.base.Location;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AngleDestinationPartitioner implements DestinationPartitioner {

    private final List<Vehicle> vehicles;
    private final List<ConfirmedOrder> orders;
    private final Location warehouseLocation;
    final double partitionSize;

    public AngleDestinationPartitioner(List<Vehicle> vehicles, List<ConfirmedOrder> orders, Location warehouseLocation) {
        this.vehicles = vehicles;
        this.orders = orders;
        this.warehouseLocation = warehouseLocation;
        partitionSize = (2 * Math.PI) / vehicles.size();
    }

    @Override
    public Map<Vehicle, List<ConfirmedOrder>> partition() {
        Map<Vehicle, List<ConfirmedOrder>> partition = new HashMap<>();

        for (Vehicle vehicle : vehicles) {
            partition.put(vehicle, new ArrayList<>());
        }

        for (ConfirmedOrder order : orders) {
            double angle = getAngle(warehouseLocation, order.getLocation());
            Vehicle assignedVehicle = vehicles.get(angle == 2 * Math.PI ? vehicles.size() -1 : (int) (angle / partitionSize));
            partition.get(assignedVehicle).add(order);
        }

        return partition;
    }

    private double getAngle(Location origin, Location point) {
        return Math.atan2(point.getX() - origin.getX(), point.getY() - origin.getY()) + Math.PI;
    }
}
