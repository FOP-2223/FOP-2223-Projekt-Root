package projekt.delivery.archetype;

import projekt.delivery.deliveryService.ProblemSolverDeliveryService;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;

import java.util.List;

public interface ProblemArchetype {

    ProblemSolverDeliveryService createProblemSolverDeliverySolver();

    DeterministicOrderGenerator getOrderGenerator();

    VehicleManager getVehicleManager();
}
