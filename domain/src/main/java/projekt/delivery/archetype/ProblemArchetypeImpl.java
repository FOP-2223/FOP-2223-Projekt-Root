package projekt.delivery.archetype;

import projekt.delivery.deliveryService.ProblemSolverDeliveryService;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.solver.BasicDeliveryProblemSolver;

import java.util.ArrayList;
import java.util.List;

public class ProblemArchetypeImpl implements ProblemArchetype {

    private final VehicleManager vehicleManager;
    private final DeterministicOrderGenerator orderGenerator;

    public ProblemArchetypeImpl(DeterministicOrderGenerator orderGenerator, VehicleManager vehicleManager) {
        this.vehicleManager = vehicleManager;
        this.orderGenerator = orderGenerator;
    }

    @Override
    public ProblemSolverDeliveryService createProblemSolverDeliverySolver() {
        return new ProblemSolverDeliveryService(
            vehicleManager,
            new BasicDeliveryProblemSolver(
                orderGenerator,
                new ArrayList<>(vehicleManager.getAllVehicles()),
                vehicleManager.getWarehouse().getComponent().getLocation()
            )
        );
    }

    @Override
    public DeterministicOrderGenerator getOrderGenerator() {
        return orderGenerator;
    }

    @Override
    public VehicleManager getVehicleManager() {
        return vehicleManager;
    }
}
