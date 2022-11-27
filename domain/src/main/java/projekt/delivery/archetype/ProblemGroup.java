package projekt.delivery.archetype;

import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;

import java.util.List;
import java.util.Map;

public interface ProblemGroup {
    List<ProblemArchetype> problems();

    List<RatingCriteria> ratingCriteria();
}
