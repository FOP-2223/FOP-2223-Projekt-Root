package projekt.delivery.archetype;

import projekt.delivery.deliveryService.ProblemSolverDeliveryService;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.solver.DeliveryProblemSolver;

public interface ProblemArchetype {

    ProblemSolverDeliveryService createProblemSolverDeliveryService(DeliveryProblemSolver deliveryProblemSolver);

    DeterministicOrderGenerator getOrderGenerator();

    VehicleManager getVehicleManager();
}
