package projekt.delivery.archetype;

import projekt.delivery.routing.ConfirmedOrder;

import java.util.List;

public interface OrderGenerator {

    /**
     * Generates orders for the next tick
     * @return the generated orders
     */
    List<ConfirmedOrder> generateNextOrders();

    /**
     * Returns the amount of ticks this orderGenerator will generate orders
     * @return Returns the amount of tick this orderGenerator will generate orders or -1 if it always returns orders.
     */
    long lastTick();
}
