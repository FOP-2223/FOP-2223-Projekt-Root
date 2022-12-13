package projekt.delivery.runner;

import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.service.DeliveryService;
import projekt.delivery.simulation.SimulationConfig;

import java.util.Map;
import java.util.function.Function;

public interface Runner {

    Map<RatingCriteria, Double> run(
        ProblemGroup problemGroup,
        SimulationConfig simulationConfig,
        int simulationRuns,
        Function<VehicleManager, DeliveryService> deliveryServiceFactory);

}
