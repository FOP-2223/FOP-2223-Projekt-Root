package projekt.delivery.archetype;

import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;

import java.util.Map;

public record ProblemArchetypeImpl(
    OrderGenerator.Factory orderGeneratorFactory,
    VehicleManager vehicleManager,
    Map<RatingCriteria, Rater.Factory> raterFactoryMap,
    long simulationLength) implements ProblemArchetype {

}
