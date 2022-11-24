package projekt.delivery.archetype;

import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;

public class ProblemArchetypeImpl implements ProblemArchetype {

    private final VehicleManager vehicleManager;
    private final OrderGenerator.Factory orderGeneratorFactory;
    private final long simulationLength;

    public ProblemArchetypeImpl(OrderGenerator.Factory orderGeneratorFactory, VehicleManager vehicleManager, long simulationLength) {
        this.vehicleManager = vehicleManager;
        this.orderGeneratorFactory = orderGeneratorFactory;
        this.simulationLength = simulationLength;
    }

    @Override
    public OrderGenerator.Factory getOrderGeneratorFactory() {
        return orderGeneratorFactory;
    }

    @Override
    public VehicleManager getVehicleManager() {
        return vehicleManager;
    }

    @Override
    public long getSimulationLength() {
        return simulationLength;
    }
}
