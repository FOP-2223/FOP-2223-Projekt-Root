package projekt.delivery.archetype;

import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.simulation.Simulation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ProblemGroup {

    private final List<ProblemArchetype> problems;
    private final Map<RatingCriteria, Rater.Factory> raterFactoryMap;

    public ProblemGroup(List<ProblemArchetype> problems, Map<RatingCriteria, Rater.Factory> raterFactoryMap) {
        this.problems = problems;
        this.raterFactoryMap = raterFactoryMap;
    }

    public List<ProblemArchetype> getProblems() {
        return Collections.unmodifiableList(problems);
    }

    public Map<RatingCriteria, Rater.Factory> getRaterFactoryMap() {
        return raterFactoryMap;
    }

    public int getSize() {
        return problems.size();
    }
}
