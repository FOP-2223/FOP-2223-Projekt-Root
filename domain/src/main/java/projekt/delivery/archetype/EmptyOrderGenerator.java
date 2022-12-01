package projekt.delivery.archetype;

import projekt.delivery.routing.ConfirmedOrder;

import java.util.List;

public class EmptyOrderGenerator implements OrderGenerator {

    @Override
    public List<ConfirmedOrder> generateOrders(long tick) {
        return List.of();
    }

    public static class Factory implements OrderGenerator.Factory {
        @Override
        public OrderGenerator create() {
            return new EmptyOrderGenerator();
        }
    }

    public static class FactoryBuilder implements OrderGenerator.FactoryBuilder {
        @Override
        public Factory build() {
            return new Factory();
        }
    }
}
