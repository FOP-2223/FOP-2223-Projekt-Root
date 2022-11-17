package projekt.delivery.archetype;

import projekt.delivery.routing.ConfirmedOrder;

import java.util.List;

public abstract class DeterministicOrderGenerator implements OrderGenerator {

    protected long currentTick = 0;

    /**
     * Generates orders for the given tick. Calling this method with the same parameter will always result in the same result
     * @param tick the tick to generate orders for
     * @return the generated orders
     */
    abstract List<ConfirmedOrder> generateOrders(long tick);

    @Override
    public List<ConfirmedOrder> generateNextOrders() {
        return generateOrders(currentTick++);
    }

    @Override
    public long lastTick() {
        return Long.MAX_VALUE;
    }
}
