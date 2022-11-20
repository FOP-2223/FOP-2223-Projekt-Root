package projekt.delivery.solver;

import projekt.base.Location;
import projekt.delivery.archetype.DeterministicOrderGenerator;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicDeliveryProblemSolver implements DeliveryProblemSolver {
    @Override
    public Map<Vehicle, Map<Long, List<ConfirmedOrder>>> solve(ProblemArchetype problemArchetype) {

        final DeterministicOrderGenerator orderGenerator = problemArchetype.getOrderGenerator();
        final List<Vehicle> vehicles = new ArrayList<>(problemArchetype.getVehicleManager().getAllVehicles());
        final Location warehouseLocation = problemArchetype.getVehicleManager().getWarehouse().getComponent().getLocation();

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
