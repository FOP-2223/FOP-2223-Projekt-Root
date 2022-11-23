package projekt.delivery.archetype;

import projekt.delivery.routing.ConfirmedOrder;

import java.util.List;

public interface OrderGenerator {

    /**
     * Generates orders for the given tick. Calling this method with the same parameter will always result in the same result
     * @param tick the tick to generate orders for
     * @return the generated orders
     */
    List<ConfirmedOrder> generateOrders(long tick);

    interface Factory {

        OrderGenerator create();
    }

    interface FactoryBuilder {

        Factory build();
    }
}
