package projekt.delivery.archetype;

import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record ProblemGroupImpl(List<ProblemArchetype> problems,
                               Map<RatingCriteria, Rater.Factory> raterFactoryMap) implements ProblemGroup {

    @Override
    public List<ProblemArchetype> problems() {
        return Collections.unmodifiableList(problems);
    }
}
