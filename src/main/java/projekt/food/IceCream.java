package projekt.food;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;

/**
 * A specific food item, i.e. ice cream.
 */
public interface IceCream extends Food {

    /**
     * Returns the flavor name.
     *
     * @return the flavor name.
     */
    @Nullable String getFlavor();

    /**
     * Specific configuration for ice cream.
     */
    interface Config extends Food.Config {

        /**
         * Modifies the flavor of this ice cream.
         *
         * @param flavorMutator the mutator
         */
        void flavor(UnaryOperator<@Nullable String> flavorMutator);

        /**
         * Returns the flavor mutator for this ice cream configurator.
         *
         * @return the mutator
         */
        UnaryOperator<@Nullable String> getFlavorMutator();
    }

    /**
     * Specific variant for ice cream.
     */
    interface Variant extends Food.Variant<IceCream, Config> {

        /**
         * Returns the default flavor for this ice cream variant.
         *
         * @return the default flavor
         */
        @Nullable String getBaseFlavor();
    }

    Variant VANILLA = new IceCreamImpl.Variant(
        "Vanilla",
        FoodTypes.ICE_CREAM,
        BigDecimal.valueOf(1.5),
        0.2,
        "Vanilla"
    );

    Variant STRAWBERRY = new IceCreamImpl.Variant(
        "Strawberry",
        FoodTypes.ICE_CREAM,
        BigDecimal.valueOf(1.5),
        0.2,
        "Strawberry"
    );

    Variant CHOCOLATE = new IceCreamImpl.Variant(
        "Chocolate",
        FoodTypes.ICE_CREAM,
        BigDecimal.valueOf(1.5),
        0.2,
        "Chocolate"
    );

    Variant STRACCIATELLA = new IceCreamImpl.Variant(
        "Stracciatella",
        FoodTypes.ICE_CREAM,
        BigDecimal.valueOf(1.5),
        0.2,
        "Stracciatella"
    );
}
