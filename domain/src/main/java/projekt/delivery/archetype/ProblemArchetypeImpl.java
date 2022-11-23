package projekt.delivery.archetype;

import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;

public class ProblemArchetypeImpl implements ProblemArchetype {

    private final VehicleManager vehicleManager;
    private final OrderGenerator orderGenerator;

    private final RatingCriteria ratingCriteria;

    public ProblemArchetypeImpl(OrderGenerator orderGenerator, VehicleManager vehicleManager, RatingCriteria ratingCriteria) {
        this.vehicleManager = vehicleManager;
        this.orderGenerator = orderGenerator;
        this.ratingCriteria = ratingCriteria;
    }

    @Override
    public OrderGenerator getOrderGenerator() {
        return orderGenerator;
    }

    @Override
    public VehicleManager getVehicleManager() {
        return vehicleManager;
    }

    @Override
    public RatingCriteria getRatingCriteria() {
        return ratingCriteria;
    }
}
