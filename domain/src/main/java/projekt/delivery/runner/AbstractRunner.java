package projekt.delivery.runner;

import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.service.DeliveryService;
import projekt.delivery.simulation.BasicDeliverySimulation;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractRunner implements Runner {

    /**
     * Creates a {@link Map} that maps each {@link ProblemArchetype} of the given {@link ProblemGroup} to a
     * {@link BasicDeliverySimulation} that simulates the {@link ProblemArchetype}.
     *
     * @param problemGroup The {@link ProblemGroup} to create {@link BasicDeliverySimulation}s for.
     * @param simulationConfig The {@link SimulationConfig} used to create the {@link BasicDeliverySimulation}s.
     * @param deliveryServiceFactory The {@link Function} used to create the {@link DeliveryService}s for the {@link BasicDeliverySimulation}s.
     * @return The created {@link Map} from {@link ProblemArchetype} to {@link BasicDeliverySimulation}.
     */
    protected Map<ProblemArchetype, Simulation> createSimulations(ProblemGroup problemGroup,
                                                      SimulationConfig simulationConfig,
                                                      Function<VehicleManager, DeliveryService> deliveryServiceFactory) {

        return problemGroup.problems().stream().collect(Collectors.toMap(
            problem -> problem,
            problem ->new BasicDeliverySimulation(
                simulationConfig,
                problem.raterFactoryMap(),
                deliveryServiceFactory.apply(problem.vehicleManager()),
                problem.orderGeneratorFactory())
        ));
    }

}
