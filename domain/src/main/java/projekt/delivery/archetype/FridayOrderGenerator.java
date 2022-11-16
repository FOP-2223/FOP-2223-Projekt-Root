package projekt.delivery.archetype;

import projekt.base.TickInterval;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Region;

import java.util.*;

/**
 * Represents the incoming orders on an average friday evening starting at 4.00pm (tick = 0) until 12:00pm (tick = 480)
 * with one tick representing one minute. The incoming orders follow a normal distribution.
 */
public class FridayOrderGenerator implements OrderGenerator {

    private static final long LAST_TICK = 480;

    Map<Long, List<ConfirmedOrder>> orders = new HashMap<>();
    private final Random random = new Random();
    private final int deliveryInterval;
    private final List<Region.Node> nodes;
    private final int maxWeight;
    private int orderID = 0;

    public FridayOrderGenerator(int orderCount, List<Region.Node> nodes, int deliveryInterval, int maxWeight) {
        this(orderCount, nodes, deliveryInterval, maxWeight, 0.5);
    }

    public FridayOrderGenerator(int orderCount, List<Region.Node> nodes, int deliveryInterval, int maxWeight, double variance) {
        this.nodes = nodes;
        this.deliveryInterval = deliveryInterval;
        this.maxWeight = maxWeight;

        for (int i = 0; i < orderCount; i++) {
            long key;
            do {
                key = (long) ((random.nextGaussian(0.5, variance)) * LAST_TICK);
            } while (key < 0.0 || key > LAST_TICK);

            if (orders.containsKey(key)) {
                orders.get(key).add(createRandomOrder(key));
            } else {
                orders.put(key, new ArrayList<>(List.of(createRandomOrder(key))));
            }
        }
    }

    @Override
    public List<ConfirmedOrder> generateOrders(long tick) {
        return orders.getOrDefault(tick, List.of());
    }

    private ConfirmedOrder createRandomOrder(long deliveryTime) {
        return new ConfirmedOrder(nodes.get(
            random.nextInt(nodes.size())).getLocation(),
            orderID++,
            new TickInterval(deliveryTime, deliveryTime + deliveryInterval),
            List.of("food" + orderID),
            random.nextDouble(maxWeight));
    }

    @Override
    public long lastTick() {
        return LAST_TICK;
    }
}
