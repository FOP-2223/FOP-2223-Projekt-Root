package projekt.food;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of a food type.
 *
 * @param <F> the type of food
 * @param <C> the config type that is compatible with this food type
 */
class FoodTypeImpl<F extends Food, C extends Food.Config> implements FoodType<F, C> {

    private final String name;
    private final List<? extends Extra<? super C>> compatibleExtras;
    private final List<Food.Variant<F, C>> foodVariants = new ArrayList<>();

    /**
     * Constructs a new {@link FoodTypeImpl} object with the specified parameters.
     *
     * @param name             the name of this food type
     * @param compatibleExtras a list of extras that are compatible with this food type
     */
    FoodTypeImpl(
        String name,
        List<? extends Extra<? super C>> compatibleExtras
    ) {
        this.name = name;
        this.compatibleExtras = compatibleExtras;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<? extends Extra<? super C>> getCompatibleExtras() {
        return Collections.unmodifiableList(compatibleExtras);
    }

    @Override
    public void addFoodVariant(Food.Variant<F, C> variant) {
        foodVariants.add(variant);
    }

    @Override
    public List<? extends Food.Variant<F, C>> getFoodVariants() {
        return Collections.unmodifiableList(foodVariants);
    }
}
