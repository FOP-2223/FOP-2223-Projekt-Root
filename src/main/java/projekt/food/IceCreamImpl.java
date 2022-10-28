package projekt.food;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Concrete implementation for ice cream.
 */
class IceCreamImpl extends AbstractFood<IceCream, IceCream.Config> implements IceCream {

    private static final FoodBuilder<IceCream, IceCream.Config, IceCream.Variant> BUILDER =
        (config, variant, extras) -> new IceCreamImpl(
            config.getPriceMutator().apply(variant.getBasePrice()),
            config.getWeightMutator().applyAsDouble(variant.getBaseWeight()),
            config.getFlavorMutator().apply(variant.getBaseFlavor()),
            variant,
            extras
        );

    private final @Nullable String flavor;

    /**
     * Constructs a new {@link IceCreamImpl} object with the specified parameters.
     *
     * @param price   the price for this ice cream
     * @param weight  the weight of this ice cream
     * @param flavor  the flavor of this ice cream
     * @param variant the ice cream variant this ice cream object is based on
     * @param extras  a list of extras that are applicable to ice cream configurations
     */
    private IceCreamImpl(
        BigDecimal price,
        double weight,
        @Nullable String flavor,
        IceCream.Variant variant,
        List<? extends Extra<? super IceCream.Config>> extras
    ) {
        super(price, weight, variant, extras);
        this.flavor = flavor;
    }

    @Override
    public @Nullable String getFlavor() {
        return flavor;
    }

    /**
     * A concrete implementation of {@link IceCream.Config}.
     */
    private static class Config extends AbstractFoodConfig implements IceCream.Config {

        private UnaryOperator<@Nullable String> flavorMutator = UnaryOperator.identity();

        @Override
        public void flavor(UnaryOperator<@Nullable String> flavorMutator) {
            this.flavorMutator = unaryAndThen(this.flavorMutator, flavorMutator);
        }

        @Override
        public UnaryOperator<@Nullable String> getFlavorMutator() {
            return flavorMutator;
        }
    }

    /**
     * A concrete implementation of {@link IceCream.Variant}.
     */
    static class Variant extends AbstractFoodVariant<IceCream, IceCream.Config, IceCream.Variant> implements IceCream.Variant {

        private final @Nullable String baseFlavor;

        /**
         * Constructs a new {@link Variant} object with the specified parameters.
         *
         * @param name       the name of this ice cream variant
         * @param foodType   the type of food
         * @param basePrice  the base price for this ice cream variant
         * @param baseWeight the base weight of this ice cream variant
         * @param baseFlavor the base flavor for this ice cream variant
         */
        Variant(
            String name,
            FoodType<IceCream, IceCream.Config> foodType,
            BigDecimal basePrice,
            double baseWeight,
            @Nullable String baseFlavor
        ) {
            super(name, foodType, basePrice, baseWeight, Config::new, BUILDER);
            this.baseFlavor = baseFlavor;
        }

        @Override
        IceCream.Variant self() {
            return this;
        }

        @Override
        public @Nullable String getBaseFlavor() {
            return baseFlavor;
        }
    }
}
