package projekt.delivery.archetype;

import projekt.delivery.routing.ConfirmedOrder;

import java.util.ArrayList;
import java.util.List;

public abstract class DeterministicOrderGenerator implements OrderGenerator {

    protected long currentTick = 0;

    /**
     * Generates orders for the given tick. Calling this method with the same parameter will always result in the same result
     * @param tick the tick to generate orders for
     * @return the generated orders
     */
    public abstract List<ConfirmedOrder> generateOrdersForTick(long tick);

    @Override
    public List<ConfirmedOrder> generateNextOrders() {
        return generateOrdersForTick(currentTick++);
    }

    @Override
    public long lastTick() {
        return Long.MAX_VALUE;
    }

    public List<ConfirmedOrder> getAllOrders() {
        List<ConfirmedOrder> allOrders = new ArrayList<>();

        for (int i = 0; i < lastTick(); i++) {
            List<ConfirmedOrder> ordersForTick = generateOrdersForTick(i);

            if (((long) ordersForTick.size()) + ((long) allOrders.size()) > Integer.MAX_VALUE) {
                throw new IndexOutOfBoundsException("Too many orders to store");
            }

            allOrders.addAll(ordersForTick);
        }

        return allOrders;
    }
}
