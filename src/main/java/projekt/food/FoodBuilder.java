package projekt.food;

import java.util.List;

/**
 * Builder for a food item.
 *
 * @param <F> the type of food to build
 * @param <C> the type of configuration for this food
 * @param <V> the type of variation for this food
 */
@FunctionalInterface
public interface FoodBuilder<F extends Food, C extends Food.Config, V extends Food.Variant<F, C>> {

    /**
     * Builds a {@link Food} object with the given parameters.
     *
     * @param config  the config to use
     * @param variant the variant to use
     * @param extras  a list of extras to apply to {@code config}
     * @return a new configured {@link Food} object
     */
    F build(C config, V variant, List<? extends Extra<? super C>> extras);
}
