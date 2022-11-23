package projekt.delivery.archetype;

import projekt.delivery.deliveryService.ProblemSolverDeliveryService;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.solver.DeliveryProblemSolver;

public interface ProblemArchetype {

    OrderGenerator getOrderGenerator();

    VehicleManager getVehicleManager();

    RatingCriteria getRatingCriteria();
}
