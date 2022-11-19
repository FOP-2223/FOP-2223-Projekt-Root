package projekt.delivery.archetype;

import projekt.base.Location;
import projekt.base.TickInterval;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;

import java.util.*;

/**
 * Represents the incoming orders on an average friday evening starting at 4.00pm (tick = 0) until 12:00pm (tick = 480)
 * with one tick representing one minute. The incoming orders follow a normal distribution.
 */
public class FridayOrderGenerator extends DeterministicOrderGenerator {

    private final Random random = new Random();
    private final Map<Long, List<ConfirmedOrder>> orders = new HashMap<>();
    private final List<Location> possibleLocations;
    private final int deliveryInterval;
    private final double maxWeight;
    private final long lastTick;

    private int orderID = 0;

    public FridayOrderGenerator(int orderCount, List<VehicleManager.OccupiedNeighborhood> nodes, int deliveryInterval, double maxWeight) {
        this(orderCount, nodes, deliveryInterval, maxWeight, 0.5, 480);
    }
    public FridayOrderGenerator(int orderCount, List<VehicleManager.OccupiedNeighborhood> nodes, int deliveryInterval, double maxWeight, int lastTick) {
        this(orderCount, nodes, deliveryInterval, maxWeight, 0.5, lastTick);
    }

    public FridayOrderGenerator(int orderCount, List<VehicleManager.OccupiedNeighborhood> nodes, int deliveryInterval, double maxWeight, double variance, int lastTick) {
        this.possibleLocations = nodes.stream().map(n -> n.getComponent().getLocation()).toList();
        this.deliveryInterval = deliveryInterval;
        this.maxWeight = maxWeight;
        this.lastTick = lastTick;

        for (int i = 0; i < orderCount; i++) {
            long key;
            do {
                key = (long) ((random.nextGaussian(0.5, variance)) * lastTick);
            } while (key < 0.0 || key > lastTick);

            if (orders.containsKey(key)) {
                orders.get(key).add(createRandomOrder(key));
            } else {
                orders.put(key, new ArrayList<>(List.of(createRandomOrder(key))));
            }
        }
    }

    @Override
    public List<ConfirmedOrder> generateOrdersForTick(long tick) {
        if (tick < 0) {
            throw new IndexOutOfBoundsException(tick);
        }

        if (tick > lastTick()) {
            return null;
        }

        return orders.getOrDefault(tick, List.of());
    }

    private ConfirmedOrder createRandomOrder(long deliveryTime) {
        return new ConfirmedOrder(
            possibleLocations.get(random.nextInt(possibleLocations.size())),
            new TickInterval(deliveryTime + deliveryInterval, deliveryTime + 2L * deliveryInterval),
            List.of("food" + orderID++),
            random.nextDouble(maxWeight),
            deliveryTime);
    }

    @Override
    public long lastTick() {
        return lastTick;
    }
}
