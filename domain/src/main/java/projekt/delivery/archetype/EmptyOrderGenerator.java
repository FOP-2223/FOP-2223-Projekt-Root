package projekt.delivery.archetype;

import projekt.delivery.routing.ConfirmedOrder;

import java.util.List;

public class EmptyOrderGenerator implements OrderGenerator {

    @Override
    public List<ConfirmedOrder> generateOrders(long tick) {
        return List.of();
    }

    public static class EmptyOrderGeneratorFactory implements Factory {
        @Override
        public OrderGenerator create() {
            return new EmptyOrderGenerator();
        }
    }

    public static class EmptyOrderGeneratorFactoryBuilder implements FactoryBuilder {
        @Override
        public Factory build() {
            return new EmptyOrderGeneratorFactory();
        }
    }
}
