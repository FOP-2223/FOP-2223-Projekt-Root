package projekt.delivery.archetype;

import projekt.delivery.routing.VehicleManager;

public record ProblemArchetypeImpl(
    OrderGenerator.Factory orderGeneratorFactory,
    VehicleManager vehicleManager,
    long simulationLength) implements ProblemArchetype {

}
