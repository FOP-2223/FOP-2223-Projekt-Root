package projekt.delivery.archetype;

import projekt.delivery.rating.RatingCriteria;

import java.util.Collections;
import java.util.List;

public record ProblemGroupImpl(List<ProblemArchetype> problems,
                               List<RatingCriteria> ratingCriteria) implements ProblemGroup {

    public ProblemGroupImpl {

        for (ProblemArchetype problem : problems) {
            if (!problem.raterFactoryMap().keySet().containsAll(ratingCriteria)) {
                throw new IllegalArgumentException("Problem %s has no rater registered for rating criterion %s"
                    .formatted(problem.toString(), ratingCriteria.stream()
                        .filter( criterion -> !problem.raterFactoryMap().containsKey(criterion))
                        .findFirst().get().toString()));
            }
        }

    }

    @Override
    public List<ProblemArchetype> problems() {
        return Collections.unmodifiableList(problems);
    }
}
