package projekt.food;

import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Abstraction of a saucable food item.
 *
 * @param <F> the specific food
 * @param <C> the configuration for this food item
 */
abstract class AbstractSaucable<F extends Saucable, C extends Saucable.Config>
    extends AbstractFood<F, C>
    implements Saucable {

    private final @Nullable String sauce;

    /**
     * Constructs a new {@link AbstractSaucable} with the specified parameters.
     *
     * @param price       the price for this saucable
     * @param weight      the weight of this saucable
     * @param foodVariant the saucable variant
     * @param extras      a list of extras that are applicable to this saucable
     * @param sauce       the name of the sauce
     */
    AbstractSaucable(
        BigDecimal price,
        double weight,
        Food.Variant<F, C> foodVariant,
        List<? extends Extra<? super C>> extras,
        @Nullable String sauce
    ) {
        super(price, weight, foodVariant, extras);
        this.sauce = sauce;
    }

    @Override
    public @Nullable String getSauce() {
        return sauce;
    }
}
