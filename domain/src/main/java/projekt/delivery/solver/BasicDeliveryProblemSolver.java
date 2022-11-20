package projekt.delivery.solver;

import projekt.base.Location;
import projekt.delivery.archetype.DeterministicOrderGenerator;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicDeliveryProblemSolver implements DeliveryProblemSolver {

    private final DeterministicOrderGenerator orderGenerator;
    private final List<Vehicle> vehicles;
    private final Location warehouseLocation;

    public BasicDeliveryProblemSolver(DeterministicOrderGenerator orderGenerator, List<Vehicle> vehicles, Location warehouseLocation) {
        this.orderGenerator = orderGenerator;
        this.vehicles = vehicles;
        this.warehouseLocation = warehouseLocation;
    }

    @Override
    public Map<Vehicle, Map<Long, List<ConfirmedOrder>>> solve() {
        Map<Vehicle, Map<Long, List<ConfirmedOrder>>> solution = new HashMap<>();
        List<ConfirmedOrder> allOrders = orderGenerator.getAllOrders();

        DestinationPartitioner destinationPartitioner = new AngleDestinationPartitioner(vehicles, allOrders, warehouseLocation);

        Map<Vehicle, List<ConfirmedOrder>> partitions = destinationPartitioner.partition();

        for (Map.Entry<Vehicle, List<ConfirmedOrder>> partition : partitions.entrySet()) {
            DeliveryRoutePlanner routePlanner = new SimpleDeliveryRoutePlaner(partition.getValue());
            solution.put(partition.getKey(), routePlanner.route());
        }

        return solution;
    }
}
