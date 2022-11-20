package projekt.delivery.archetype;

import projekt.delivery.deliveryService.ProblemSolverDeliveryService;
import projekt.delivery.routing.VehicleManager;

public interface ProblemArchetype {

    ProblemSolverDeliveryService createProblemSolverDeliveryService();

    DeterministicOrderGenerator getOrderGenerator();

    VehicleManager getVehicleManager();
}
