package projekt.food;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

/**
 * Concrete implementation for pasta.
 */
class PastaImpl extends AbstractSaucable<Pasta, Pasta.Config> implements Pasta {

    private static final FoodBuilder<Pasta, Pasta.Config, Pasta.Variant> BUILDER =
        (config, variant, extras) -> new PastaImpl(
            config.getPriceMutator().apply(variant.getBasePrice()),
            config.getWeightMutator().applyAsDouble(variant.getBaseWeight()),
            config.getSauceMutator().apply(variant.getBaseSauce()),
            config.getThicknessMutator().applyAsDouble(variant.getBaseThickness()),
            variant,
            extras
        );

    private final double thickness;

    /**
     * Constructs a new {@link PastaImpl} object with the specified parameters.
     *
     * @param price     the price for this pasta
     * @param weight    the weight of this pasta
     * @param sauce     the default sauce on this pasta
     * @param thickness the thickness of the noodles
     * @param variant   the pasta variant this pasta object is based on
     * @param extras    a list of extras that are applicable to pasta configurations
     */
    private PastaImpl(
        BigDecimal price,
        double weight,
        String sauce,
        double thickness,
        Pasta.Variant variant,
        List<? extends Extra<? super Pasta.Config>> extras
    ) {
        super(price, weight, variant, extras, sauce);
        this.thickness = thickness;
    }

    @Override
    public double getThickness() {
        return thickness;
    }

    /**
     * A concrete implementation of {@link Pasta.Config}.
     */
    private static class Config extends AbstractSaucableConfig implements Pasta.Config {
        private DoubleUnaryOperator thicknessMutator = DoubleUnaryOperator.identity();

        @Override
        public void thickness(DoubleUnaryOperator thicknessMutator) {
            this.thicknessMutator = this.thicknessMutator.andThen(thicknessMutator);
        }

        @Override
        public DoubleUnaryOperator getThicknessMutator() {
            return thicknessMutator;
        }
    }

    /**
     * A concrete implementation of {@link Pasta.Variant}.
     */
    static class Variant extends AbstractFoodVariant<Pasta, Pasta.Config, Pasta.Variant> implements Pasta.Variant {

        private final @Nullable String baseSauce;
        private final double baseThickness;

        /**
         * Constructs a new {@link Variant} object with the specified parameters.
         *
         * @param name          the name of this pasta variant
         * @param foodType      the type of food
         * @param basePrice     the base price for this pasta variant
         * @param baseWeight    the base weight of this pasta variant
         * @param baseSauce     the default sauce on this pasta variant
         * @param baseThickness the base thickness of the noodles in this pasta variant
         */
        Variant(
            String name,
            FoodType<Pasta, Pasta.Config> foodType,
            BigDecimal basePrice,
            double baseWeight,
            @Nullable String baseSauce,
            double baseThickness
        ) {
            super(name, foodType, basePrice, baseWeight, Config::new, BUILDER);
            this.baseSauce = baseSauce;
            this.baseThickness = baseThickness;
        }

        @Override
        Pasta.Variant self() {
            return this;
        }

        @Override
        public @Nullable String getBaseSauce() {
            return baseSauce;
        }

        @Override
        public double getBaseThickness() {
            return baseThickness;
        }
    }
}
