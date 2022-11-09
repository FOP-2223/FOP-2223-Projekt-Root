package projekt.food;

import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

/**
 * A category of food items that have sauce on them.
 */
public interface Saucable extends Food {

    /**
     * Returns the sauce on this food item.
     *
     * @return the current sauce
     */
    @Nullable String getSauce();

    /**
     * Specific configuration for saucable foods.
     */
    interface Config extends Food.Config {

        /**
         * Modifies the sauce that is currently on this food item.
         *
         * @param sauceMutator the mutator
         */
        void sauce(UnaryOperator<@Nullable String> sauceMutator);

        /**
         * Returns the sauce mutator for this saucable configurator.
         *
         * @return the mutator
         */
        UnaryOperator<@Nullable String> getSauceMutator();
    }

    /**
     * Specific variant for foods with sauce on them.
     *
     * @param <F> a saucable food
     * @param <C> a configuration for the food
     */
    interface Variant<F extends Saucable, C extends Config> extends Food.Variant<F, C> {

        /**
         * Returns the default sauce for this saucable variant.
         *
         * @return the default sauce
         */
        @Nullable String getBaseSauce();
    }
}
