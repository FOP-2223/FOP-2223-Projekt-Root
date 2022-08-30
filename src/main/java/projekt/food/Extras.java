package projekt.food;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A collection of extras.
 */
public final class Extras {

    public static final Extra<Pizza.Config> EXTRA_HAM = new ExtraImpl<>(
        "Extra Ham",
        5,
        pizza -> {
            pizza.price(p -> p.add(BigDecimal.ONE));
            pizza.weight(w -> w + 0.1);
        }
    );
    public static final Extra<Pizza.Config> EXTRA_OLIVES = new ExtraImpl<>(
        "Extra Olives",
        5,
        pizza -> {
            pizza.price(p -> p.add(BigDecimal.valueOf(0.8)));
            pizza.weight(w -> w + 0.05);
        }
    );
    public static final Extra<Pasta.Config> EXTRA_THICK = new ExtraImpl<>(
        "Extra Thick",
        4,
        pasta -> {
            pasta.price(p -> p.add(BigDecimal.valueOf(4.0)));
            pasta.weight(w -> w * 2);
            pasta.thickness(t -> t * 2);
        }
    );
    public static final Extra<Saucable.Config> SPICY_SAUCE = new ExtraImpl<>(
        "Spicy Sauce",
        3,
        saucable -> {
            saucable.price(p -> p.add(BigDecimal.valueOf(0.5)));
            saucable.sauce(s -> "Spicy " + s);
        }
    );
    public static final Extra<Saucable.Config> EXTRA_SAUCE = new ExtraImpl<>(
        "Extra Sauce",
        4,
        saucable -> {
            saucable.price(p -> p.add(BigDecimal.valueOf(1.25)));
            saucable.weight(w -> Math.max(0, w + 0.12));
            saucable.sauce(s -> "Extra " + s);
        }
    );
    public static final Extra<Saucable.Config> NO_SAUCE = new ExtraImpl<>(
        "No Sauce",
        5,
        saucable -> {
            saucable.price(p -> p.subtract(BigDecimal.valueOf(1)).max(BigDecimal.ZERO));
            saucable.weight(w -> Math.max(w - 0.1, 0));
            saucable.sauce(s -> null);
        }
    );
    public static final Extra<IceCream.Config> RAINBOW_SPRINKLES = new ExtraImpl<>(
        "Rainbow Sprinkles",
        5,
        icecream -> {
            icecream.price(p -> p.add(BigDecimal.valueOf(0.5)));
            icecream.weight(w -> w + 0.03);
        }
    );
    public static final Extra<IceCream.Config> EXTRA_SCOOP = new ExtraImpl<>(
        "Extra Scoop",
        2,
        icecream -> {
            icecream.price(p -> p.add(BigDecimal.valueOf(3.0)));
            icecream.weight(w -> w + 0.1);
        }
    );
    public static final Map<String, Extra<?>> ALL;

    static {
        final Map<String, Extra<?>> extras = new HashMap<>();
        addExtra(extras, EXTRA_HAM);
        addExtra(extras, EXTRA_OLIVES);
        addExtra(extras, EXTRA_THICK);
        addExtra(extras, SPICY_SAUCE);
        addExtra(extras, EXTRA_SAUCE);
        addExtra(extras, NO_SAUCE);
        addExtra(extras, RAINBOW_SPRINKLES);
        addExtra(extras, EXTRA_SCOOP);
        ALL = Collections.unmodifiableMap(extras);
    }

    private Extras() {
    }

    /**
     * Adds {@code extra} to {@code extras}.
     *
     * @param extras a map of an extra's name and the extra itself
     * @param extra  the extra
     */
    private static void addExtra(Map<String, Extra<?>> extras, Extra<?> extra) {
        extras.put(extra.getName(), extra);
    }
}
