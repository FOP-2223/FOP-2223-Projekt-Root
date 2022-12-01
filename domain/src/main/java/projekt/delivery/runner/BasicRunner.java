package projekt.delivery.runner;

import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;

import java.util.List;
import java.util.Map;

import static org.tudalgo.algoutils.student.Student.crash;

public class BasicRunner implements Runner {

    @Override
    public Map<RatingCriteria, Double> run(
        ProblemGroup problemGroup,
        SimulationConfig simulationConfig,
        int simulationRuns) {

        return crash(); // TODO: H10.1 - remove if implemented
    }

    private List<Simulation> createSimulations(
        ProblemGroup problemGroup,
        SimulationConfig simulationConfig) {

        return crash(); // TODO: H10.1 - remove if implemented
    }
}
