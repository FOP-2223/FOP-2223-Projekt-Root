package projekt.delivery.runner;

import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.service.DeliveryService;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A basic {@link Runner} that only executes the simulation and returns the result.
 */
public class BasicRunner extends AbstractRunner {

    @Override
    public Map<RatingCriteria, Double> run(ProblemGroup problemGroup,
                                           SimulationConfig simulationConfig,
                                           int simulationRuns,
                                           DeliveryService.Factory deliveryServiceFactory) {

        Map<ProblemArchetype, Simulation> simulations = createSimulations(problemGroup, simulationConfig, deliveryServiceFactory);
        Map<RatingCriteria, Double> results = new HashMap<>();

        for (RatingCriteria criteria : problemGroup.ratingCriteria()) {
            results.put(criteria, 0.0);
        }

        for (int i = 0; i < simulationRuns; i++) {
            for (Map.Entry<ProblemArchetype, Simulation> entry : simulations.entrySet()) {
                entry.getValue().runSimulation(entry.getKey().simulationLength());
                results.replaceAll((criteria, rating) -> results.get(criteria) + entry.getValue().getRatingForCriterion(criteria));
            }
        }

        results.replaceAll((criteria, rating) -> (results.get(criteria) / (simulationRuns * problemGroup.problems().size())));

        return results;
    }
}
