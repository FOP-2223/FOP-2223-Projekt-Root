package projekt.delivery.archetype;

import projekt.delivery.DeliveryService;
import projekt.delivery.simulation.SimulationConfig;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;

public interface ProblemArchetype {

    Region getRegion();

    VehicleManager getVehicleManager();

    SimulationConfig getSimulationConfig();

    void runSimulation(DeliveryService.Factory deliveryServiceFactory);
}
