package projekt.food;

import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

/**
 * Abstraction of a saucable configuration.
 */
abstract class AbstractSaucableConfig extends AbstractFoodConfig implements Saucable.Config {

    private UnaryOperator<@Nullable String> sauceMutator = UnaryOperator.identity();

    @Override
    public void sauce(UnaryOperator<@Nullable String> sauceMutator) {
        this.sauceMutator = unaryAndThen(this.sauceMutator, sauceMutator);
    }

    @Override
    public UnaryOperator<@Nullable String> getSauceMutator() {
        return sauceMutator;
    }
}
