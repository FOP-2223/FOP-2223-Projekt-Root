package projekt.food;

import java.math.BigDecimal;
import java.util.function.DoubleUnaryOperator;

/**
 * A specific food item, i.e. pasta.
 */
public interface Pasta extends Saucable {

    Variant SPAGHETTI = new PastaImpl.Variant(
        "Spaghetti",
        FoodTypes.PASTA,
        BigDecimal.valueOf(12.5),
        0.2,
        null,
        2
    );
    Variant RIGATONI = new PastaImpl.Variant(
        "Rigatoni",
        FoodTypes.PASTA,
        BigDecimal.valueOf(11.5),
        0.2,
        null,
        10
    );
    Variant RAVIOLI = new PastaImpl.Variant(
        "Ravioli",
        FoodTypes.PASTA,
        BigDecimal.valueOf(11.5),
        0.2,
        null,
        40
    );
    Variant FUSILLI = new PastaImpl.Variant(
        "Fusilli",
        FoodTypes.PASTA,
        BigDecimal.valueOf(11.5),
        0.2,
        null,
        15
    );

    /**
     * The thickness of this pasta in mm.
     */
    double getThickness();

    /**
     * Configurations for {@link Pasta}.
     */
    interface Config extends Saucable.Config {

        /**
         * Option for configuring the thickness of a {@link Pasta} object.
         *
         * @param thicknessMutator the mutator to apply
         */
        void thickness(DoubleUnaryOperator thicknessMutator);

        /**
         * Returns the thickness mutator.
         *
         * @return the thickness mutator
         */
        DoubleUnaryOperator getThicknessMutator();
    }

    /**
     * Variations of {@link Pasta}.
     */
    interface Variant extends Saucable.Variant<Pasta, Config> {

        /**
         * Returns the base thickness of the noodles in a pasta dish.
         *
         * @return the base thickness
         */
        double getBaseThickness();
    }
}
