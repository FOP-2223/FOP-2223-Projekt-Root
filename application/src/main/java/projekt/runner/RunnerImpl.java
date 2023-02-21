package projekt.runner;

import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.service.DeliveryService;
import projekt.delivery.simulation.BasicDeliverySimulation;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;
import projekt.runner.handler.ResultHandler;
import projekt.runner.handler.SimulationFinishedHandler;
import projekt.runner.handler.SimulationSetupHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RunnerImpl implements Runner {

    @Override
    public void run(ProblemGroup problemGroup,
                    SimulationConfig simulationConfig,
                    int simulationRuns,
                    DeliveryService.Factory deliveryServiceFactory,
                    SimulationSetupHandler simulationSetupHandler,
                    SimulationFinishedHandler simulationFinishedHandler,
                    ResultHandler resultHandler) {

        Map<ProblemArchetype, Simulation> simulations = createSimulations(problemGroup, simulationConfig, deliveryServiceFactory);
        Map<RatingCriteria, Double> result = new HashMap<>();

        for (RatingCriteria criteria : problemGroup.ratingCriteria()) {
            result.put(criteria, 0.0);
        }

        for (int i = 0; i < simulationRuns; i++) {

            for (Map.Entry<ProblemArchetype, Simulation> entry : simulations.entrySet()) {
                Simulation simulation = entry.getValue();
                ProblemArchetype problem = entry.getKey();

                simulationSetupHandler.accept(simulation, problem, i);

                //run the simulation
                simulation.runSimulation(problem.simulationLength());

                if (simulationFinishedHandler.accept(simulation, problem)) {
                    return;
                }

                result.replaceAll((criteria, rating) -> result.get(criteria) + simulation.getRatingForCriterion(criteria));
            }
        }

        result.replaceAll((criteria, rating) -> (result.get(criteria) / (simulationRuns * problemGroup.problems().size())));

        resultHandler.accept(result);
    }

    /**
     * Creates a {@link Map} that maps each {@link ProblemArchetype} of the given {@link ProblemGroup} to a
     * {@link BasicDeliverySimulation} that simulates the {@link ProblemArchetype}.
     *
     * @param problemGroup           The {@link ProblemGroup} to create {@link BasicDeliverySimulation}s for.
     * @param simulationConfig       The {@link SimulationConfig} used to create the {@link BasicDeliverySimulation}s.
     * @param deliveryServiceFactory The {@link DeliveryService.Factory} used to create the {@link DeliveryService}s for the {@link BasicDeliverySimulation}s.
     * @return The created {@link Map} from {@link ProblemArchetype} to {@link BasicDeliverySimulation}.
     */
    public Map<ProblemArchetype, Simulation> createSimulations(ProblemGroup problemGroup,
                                                                  SimulationConfig simulationConfig,
                                                                  DeliveryService.Factory deliveryServiceFactory) {

        return problemGroup.problems().stream().collect(Collectors.toMap(
            problem -> problem,
            problem -> new BasicDeliverySimulation(
                simulationConfig,
                problem.raterFactoryMap(),
                deliveryServiceFactory.create(problem.vehicleManager()),
                problem.orderGeneratorFactory())
        ));
    }

}
