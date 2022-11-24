package projekt.delivery.runner;

import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.simulation.SimulationConfig;

import java.util.Map;

public interface Runner {

    Map<RatingCriteria, Double> run(
        ProblemGroup problemGroup,
        Map<RatingCriteria, Rater.Factory> raterFactory,
        SimulationConfig config,
        int simulationRuns);

}
