package projekt.food;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

/**
 * Concrete implementation for pizza.
 */
class PizzaImpl extends AbstractSaucable<Pizza, Pizza.Config> implements Pizza {

    private static final FoodBuilder<Pizza, Pizza.Config, Pizza.Variant> BUILDER =
        (config, variant, extras) -> new PizzaImpl(
            config.getPriceMutator().apply(variant.getBasePrice()),
            config.getWeightMutator().applyAsDouble(variant.getBaseWeight()),
            config.getSauceMutator().apply(variant.getBaseSauce()),
            config.getDiameterMutator().applyAsDouble(variant.getBaseDiameter()),
            variant,
            extras
        );

    private final double diameter;

    /**
     * Constructs a new {@link PastaImpl} object with the specified parameters.
     *
     * @param price    the price for this pizza
     * @param weight   the weight of this pizza
     * @param sauce    the sauce on this pizza
     * @param diameter the diameter of this pizza
     * @param variant  the pizza variant this pizza is based on
     * @param extras   a list of extras that are applicable to pizza configurations
     */
    private PizzaImpl(
        BigDecimal price,
        double weight,
        String sauce,
        double diameter,
        Pizza.Variant variant,
        List<? extends Extra<? super Pizza.Config>> extras
    ) {
        super(price, weight, variant, extras, sauce);
        this.diameter = diameter;
    }

    @Override
    public double getDiameter() {
        return diameter;
    }

    /**
     * A concrete implementation of {@link Pizza.Config}.
     */
    private static class Config extends AbstractSaucableConfig implements Pizza.Config {

        private DoubleUnaryOperator diameterMutator = DoubleUnaryOperator.identity();

        @Override
        public void diameter(DoubleUnaryOperator diameterMutator) {
            this.diameterMutator = this.diameterMutator.andThen(diameterMutator);
        }

        @Override
        public DoubleUnaryOperator getDiameterMutator() {
            return diameterMutator;
        }
    }

    /**
     * A concrete implementation of {@link Pizza.Variant}.
     */
    static class Variant extends AbstractFoodVariant<Pizza, Pizza.Config, Pizza.Variant> implements Pizza.Variant {

        private final @Nullable String baseSauce;
        private final double baseDiameter;

        /**
         * Constructs a new {@link Variant} object with the specified parameters.
         *
         * @param name         the name tof this pizza variant
         * @param foodType     the type of food
         * @param basePrice    the base price for this pizza variant
         * @param baseWeight   the base weight of this pizza variant
         * @param baseSauce    the base sauce on pizzas of this variant
         * @param baseDiameter the base diameter for this pizza variant
         */
        Variant(
            String name,
            FoodType<Pizza, Pizza.Config> foodType,
            BigDecimal basePrice,
            double baseWeight,
            @Nullable String baseSauce,
            double baseDiameter
        ) {
            super(name, foodType, basePrice, baseWeight, Config::new, BUILDER);
            this.baseSauce = baseSauce;
            this.baseDiameter = baseDiameter;
        }

        @Override
        Pizza.Variant self() {
            return this;
        }

        @Override
        public @Nullable String getBaseSauce() {
            return baseSauce;
        }

        @Override
        public double getBaseDiameter() {
            return baseDiameter;
        }
    }
}
