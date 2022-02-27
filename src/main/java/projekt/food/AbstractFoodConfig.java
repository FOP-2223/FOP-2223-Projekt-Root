package projekt.food;

import java.math.BigDecimal;
import java.util.function.DoubleUnaryOperator;
import java.util.function.UnaryOperator;

/**
 * Abstraction of a food configuration.
 */
abstract class AbstractFoodConfig implements Food.Config {

    private UnaryOperator<BigDecimal> priceMutator = UnaryOperator.identity();
    private DoubleUnaryOperator weightMutator = DoubleUnaryOperator.identity();

    @Override
    public void price(UnaryOperator<BigDecimal> priceMutator) {
        this.priceMutator = unaryAndThen(this.priceMutator, priceMutator);
    }

    @Override
    public UnaryOperator<BigDecimal> getPriceMutator() {
        return priceMutator;
    }

    @Override
    public void weight(DoubleUnaryOperator weightMutator) {
        this.weightMutator = this.weightMutator.andThen(weightMutator);
    }

    @Override
    public DoubleUnaryOperator getWeightMutator() {
        return weightMutator;
    }

    /**
     * Concatenates two unary operators so that the result of the first one
     * is applied to the second one.
     *
     * @param op1 the first unary operator
     * @param op2 the second unary operator
     * @param <T> the generic type of both operators and the resulting one
     * @return a unary operator that performs the action described above
     */
    <T> UnaryOperator<T> unaryAndThen(UnaryOperator<T> op1, UnaryOperator<T> op2) {
        return t -> op2.apply(op1.apply(t));
    }
}
