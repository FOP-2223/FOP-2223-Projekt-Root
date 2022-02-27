package projekt.food;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Abstraction of a food item.
 *
 * @param <F> the specific food
 * @param <C> the configuration for this food item
 */
abstract class AbstractFood<F extends Food, C extends Food.Config> implements Food {
    private final BigDecimal price;
    private final double weight;
    private final Variant<F, C> variant;
    private final List<? extends Extra<? super C>> extras;

    /**
     * Constructs a new {@link AbstractFood} with the specified parameters.
     *
     * @param price   the price for this food
     * @param weight  the weight of this food
     * @param variant the food variant
     * @param extras  a list of extras that are applicable to this food
     */
    AbstractFood(
        BigDecimal price,
        double weight,
        Variant<F, C> variant,
        List<? extends Extra<? super C>> extras
    ) {
        this.price = price;
        this.weight = weight;
        this.variant = variant;
        this.extras = extras;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public Variant<F, C> getFoodVariant() {
        return variant;
    }

    @Override
    public List<? extends Extra<?>> getExtras() {
        return Collections.unmodifiableList(extras);
    }
}
