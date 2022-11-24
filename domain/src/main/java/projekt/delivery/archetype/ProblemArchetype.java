package projekt.delivery.archetype;

import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;

public interface ProblemArchetype {

    OrderGenerator getOrderGenerator();

    OrderGenerator.Factory getOrderGeneratorFactory();

    VehicleManager getVehicleManager();

    RatingCriteria getRatingCriteria();

    long getSimulationLength();
}
