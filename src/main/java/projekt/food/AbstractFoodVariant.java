package projekt.food;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

/**
 * Abstraction of a food variant.
 *
 * @param <F> the specific food
 * @param <C> the configuration for this food item
 * @param <V> the variant of this food item
 */
abstract class AbstractFoodVariant<F extends Food, C extends Food.Config, V extends Food.Variant<F, C>>
    implements Food.Variant<F, C> {

    private final String name;
    private final FoodType<F, C> foodType;
    private final BigDecimal basePrice;
    private final double baseWeight;
    private final Supplier<C> configSupplier;
    private final FoodBuilder<F, C, V> foodBuilder;

    /**
     * Constructs a new {@link AbstractFoodVariant} object with the specified parameters.
     *
     * @param name           the name of this variant
     * @param foodType       the food type of this variant
     * @param basePrice      the base price for this variant
     * @param baseWeight     the base weight of this variant
     * @param configSupplier a {@link Supplier} that returns the configuration for this variant
     * @param foodBuilder    a {@link FoodBuilder} to construct the default food this variant describes
     */
    AbstractFoodVariant(
        String name,
        FoodType<F, C> foodType,
        BigDecimal basePrice,
        double baseWeight,
        Supplier<C> configSupplier,
        FoodBuilder<F, C, V> foodBuilder
    ) {
        this.name = name;
        this.foodType = foodType;
        this.basePrice = basePrice;
        this.baseWeight = baseWeight;
        this.configSupplier = configSupplier;
        this.foodBuilder = foodBuilder;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public FoodType<F, C> getFoodType() {
        return foodType;
    }

    @Override
    public BigDecimal getBasePrice() {
        return basePrice;
    }

    @Override
    public double getBaseWeight() {
        return baseWeight;
    }

    @Override
    public C createEmptyConfig() {
        return configSupplier.get();
    }

    @Override
    public F create(List<? extends Extra<? super C>> extras) {
        final C config = createEmptyConfig();
        Extra.writeToConfig(config, extras);
        return foodBuilder.build(config, self(), extras);
    }

    /**
     * Returns this variant.
     *
     * @return this variant / instance
     */
    abstract V self();
}
