package projekt.food;

import java.util.Comparator;
import java.util.List;

/**
 * A modification that targets configurable values in a {@link Food.Config}.
 *
 * @param <C> The target {@link Food.Config} type
 */
public interface Extra<C extends Food.Config> {

    /**
     * Applies all extras in {@code extras} to {@code config}.
     * Extras are applied in ascending order of priority, or the extra's name
     * if two or more extras have the same priority.
     *
     * @param config the configuration the extras should be applied to
     * @param extras a list of extras to apply
     * @param <C>    the configuration
     */
    static <C extends Food.Config> void writeToConfig(C config, List<? extends Extra<? super C>> extras) {
        extras.stream()
            .sorted(Comparator.<Extra<?>>comparingInt(Extra::getPriority).thenComparing(Extra::getName))
            .forEach(e -> e.apply(config));
    }

    /**
     * The name of this extra.
     *
     * @return The name of this extra
     */
    String getName();

    /**
     * The priority of the extra, lower is calculated first.
     *
     * @return the priority
     */
    int getPriority();

    /**
     * Applies the modifications of this extra to the provided {@link C config}.
     *
     * @param config the configuration to which to apply the modifications
     */
    void apply(C config);
}
