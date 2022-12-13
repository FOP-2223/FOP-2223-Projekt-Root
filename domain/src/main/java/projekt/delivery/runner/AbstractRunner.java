package projekt.delivery.runner;

import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.service.DeliveryService;
import projekt.delivery.simulation.BasicDeliverySimulation;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;

import java.util.List;
import java.util.function.Function;

public abstract class AbstractRunner implements Runner {

    protected List<Simulation> createSimulations(ProblemGroup problemGroup,
                                                 SimulationConfig simulationConfig,
                                                 Function<VehicleManager, DeliveryService> deliveryServiceFactory) {

        return problemGroup.problems().stream().<Simulation>map(problem -> new BasicDeliverySimulation(
            simulationConfig,
            problem.raterFactoryMap(),
            deliveryServiceFactory.apply(problem.vehicleManager()),
            problem.orderGeneratorFactory(),
            problem.simulationLength())
        ).toList();
    }

}
