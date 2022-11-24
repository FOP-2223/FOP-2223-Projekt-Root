package projekt.delivery.archetype;

import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;

public interface ProblemArchetype {

    OrderGenerator.Factory orderGeneratorFactory();

    VehicleManager vehicleManager();

    long simulationLength();
}
