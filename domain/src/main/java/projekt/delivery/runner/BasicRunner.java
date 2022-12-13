package projekt.delivery.runner;

import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.service.DeliveryService;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BasicRunner extends AbstractRunner {

    @Override
    public Map<RatingCriteria, Double> run(ProblemGroup problemGroup,
                                           SimulationConfig simulationConfig,
                                           int simulationRuns,
                                           Function<VehicleManager, DeliveryService> deliveryServiceFactory) {

        List<Simulation> simulations = createSimulations(problemGroup, simulationConfig, deliveryServiceFactory);
        Map<RatingCriteria, Double> results = new HashMap<>();

        for (RatingCriteria criteria : problemGroup.ratingCriteria()) {
            results.put(criteria, 0.0);
        }

        for (int i = 0; i < simulationRuns; i++) {
            for (Simulation simulation : simulations) {
                simulation.runSimulation();
                results.replaceAll((criteria, rating) -> results.get(criteria) + simulation.getRatingForCriterion(criteria));
            }
        }

        results.replaceAll((criteria, rating) -> (results.get(criteria) / (simulationRuns * problemGroup.problems().size())));

        return results;
    }
}
