package projekt.delivery.archetype;

import projekt.delivery.routing.ConfirmedOrder;

import java.util.List;

public interface OrderGenerator {

    /**
     * Generates orders for the given tick
     * @param tick the tick to generate orders for
     * @return the generated orders
     */
    List<ConfirmedOrder> generateOrders(long tick);

    /**
     * Returns the last tick this orderGenerator will generate orders
     * @return Returns the last tick this orderGenerator will generate orders or -1 if it always returns orders.
     */
    long lastTick();
}
