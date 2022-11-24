package projekt.delivery.runner;

import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.service.ProblemSolverDeliveryService;
import projekt.delivery.simulation.BasicDeliverySimulation;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicRunner implements Runner {

    @Override
    public Map<RatingCriteria, Double> run(
        ProblemGroup problemGroup,
        SimulationConfig simulationConfig,
        int simulationRuns) {

        List<Simulation> simulations = createSimulations(problemGroup, simulationConfig);
        Map<RatingCriteria, Double> results = new HashMap<>();

        for (RatingCriteria criteria : problemGroup.raterFactoryMap().keySet()) {
            results.put(criteria, 0.0);
        }

        for (int i = 0; i < simulationRuns; i++) {
            for (Simulation simulation : simulations) {
                simulation.runSimulation();
                results.replaceAll((criteria, rating) -> results.get(criteria) + simulation.getRatingForCriterion(criteria));
            }
        }

        results.replaceAll((criteria, rating) -> results.get(criteria) / simulationRuns);

        return results;
    }

    private List<Simulation> createSimulations(
        ProblemGroup problemGroup,
        SimulationConfig simulationConfig) {

        List<Simulation> simulations = new ArrayList<>();

        problemGroup.problems().forEach(problem -> simulations.add(new BasicDeliverySimulation(
            simulationConfig,
            problemGroup.raterFactoryMap(),
            new ProblemSolverDeliveryService(problem),
            problem.orderGeneratorFactory(),
            problem.simulationLength())));

        return simulations;
    }
}
