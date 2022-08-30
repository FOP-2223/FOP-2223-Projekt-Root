package projekt.food;

import java.math.BigDecimal;
import java.util.function.DoubleUnaryOperator;

/**
 * A specific food item, i.e. pizza.
 */
public interface Pizza extends Saucable {

    Variant MARGHERITA = new PizzaImpl.Variant(
        "Margherita",
        FoodTypes.PIZZA,
        BigDecimal.valueOf(9.75),
        0.8,
        "Tomato",
        30.0
    );
    Variant HAWAII = new PizzaImpl.Variant(
        "Hawaii",
        FoodTypes.PIZZA,
        BigDecimal.valueOf(13.75),
        1.0,
        "Tomato",
        30.0
    );
    Variant RUCOLA = new PizzaImpl.Variant(
        "Rucola",
        FoodTypes.PIZZA,
        BigDecimal.valueOf(14.5),
        0.9,
        "Tomato",
        30.0
    );
    Variant BBQ = new PizzaImpl.Variant(
        "BBQ",
        FoodTypes.PIZZA,
        BigDecimal.valueOf(14.5),
        1.1,
        "BBQ",
        30.0
    );

    /**
     * Returns the diameter of this pizza.
     *
     * @return the diameter
     */
    double getDiameter();

    /**
     * Specific configuration for pizza.
     */
    interface Config extends Saucable.Config {

        /**
         * Modifies the diameter of a pizza using the supplied unary operator.
         *
         * @param diameter the operator to apply
         */
        void diameter(DoubleUnaryOperator diameter);

        /**
         * Returns the diameter mutator for this pizza configurator.
         *
         * @return the mutator
         */
        DoubleUnaryOperator getDiameterMutator();
    }

    /**
     * Specific variant for pizza.
     */
    interface Variant extends Saucable.Variant<Pizza, Pizza.Config> {

        /**
         * Returns the default diameter for this pizza variant.
         *
         * @return the default diameter
         */
        double getBaseDiameter();
    }
}
