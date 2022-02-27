package projekt.food;

import java.util.List;
import java.util.Map;

/**
 * A collection of food types.
 */
public final class FoodTypes {

    private FoodTypes() {
    }

    public static final FoodType<Pizza, Pizza.Config> PIZZA = new FoodTypeImpl<Pizza, Pizza.Config>(
        "Pizza",
        List.of(
            Extras.EXTRA_HAM,
            Extras.EXTRA_OLIVES,
            Extras.SPICY_SAUCE,
            Extras.EXTRA_SAUCE,
            Extras.NO_SAUCE
        )
    );

    public static final FoodType<Pasta, Pasta.Config> PASTA = new FoodTypeImpl<Pasta, Pasta.Config>(
        "Pasta",
        List.of(
            Extras.EXTRA_THICK,
            Extras.SPICY_SAUCE,
            Extras.EXTRA_SAUCE,
            Extras.NO_SAUCE
        )
    );

    public static final FoodType<IceCream, IceCream.Config> ICE_CREAM = new FoodTypeImpl<>(
        "Ice Cream",
        List.of(
            Extras.RAINBOW_SPRINKLES,
            Extras.EXTRA_SCOOP
        )
    );

    public static final Map<String, FoodType<?, ?>> ALL = Map.of(
        PIZZA.getName(), PIZZA,
        PASTA.getName(), PASTA,
        ICE_CREAM.getName(), ICE_CREAM
    );

    static {
        PIZZA.addFoodVariant(Pizza.MARGHERITA);
        PASTA.addFoodVariant(Pasta.RAVIOLI);
        PASTA.addFoodVariant(Pasta.RIGATONI);
        PASTA.addFoodVariant(Pasta.SPAGHETTI);
    }
}
