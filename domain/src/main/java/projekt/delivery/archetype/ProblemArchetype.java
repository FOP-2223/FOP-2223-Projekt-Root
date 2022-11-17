package projekt.delivery.archetype;

import projekt.delivery.deliveryService.ProblemSolverDeliveryService;
import projekt.delivery.routing.ConfirmedOrder;

import java.util.List;

public interface ProblemArchetype {

    ProblemSolverDeliveryService createProblemSolverDeliverySolver();

    DeterministicOrderGenerator getOrderGenerator();
}
