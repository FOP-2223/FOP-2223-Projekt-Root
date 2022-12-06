package projekt.delivery.archetype;

import projekt.delivery.rating.RatingCriteria;

import java.util.List;

public interface ProblemGroup {
    List<ProblemArchetype> problems();

    List<RatingCriteria> ratingCriteria();
}
