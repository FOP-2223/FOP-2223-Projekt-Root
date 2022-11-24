package projekt.delivery.archetype;

import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;

public class ProblemArchetypeImpl implements ProblemArchetype {

    private final VehicleManager vehicleManager;
    private final OrderGenerator.Factory orderGeneratorFactory;
    private final RatingCriteria ratingCriteria;
    private final long simulationLength;

    public ProblemArchetypeImpl(OrderGenerator.Factory orderGeneratorFactory, VehicleManager vehicleManager, RatingCriteria ratingCriteria, long simulationLength) {
        this.vehicleManager = vehicleManager;
        this.orderGeneratorFactory = orderGeneratorFactory;
        this.ratingCriteria = ratingCriteria;
        this.simulationLength = simulationLength;
    }

    @Override
    public OrderGenerator getOrderGenerator() {
        return orderGeneratorFactory.create();
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
    public RatingCriteria getRatingCriteria() {
        return ratingCriteria;
    }

    @Override
    public long getSimulationLength() {
        return simulationLength;
    }
}
