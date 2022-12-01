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
public class FridayOrderGenerator implements OrderGenerator {

    private final Random random;
    private final Map<Long, List<ConfirmedOrder>> orders = new HashMap<>();

    FridayOrderGenerator(int orderCount, VehicleManager vehicleManager, int deliveryDifference, double maxWeight, double variance, int lastTick, int seed) {
        random = seed < 0 ? new Random() : new Random(seed);

        for (int i = 0; i < orderCount; i++) {
            long deliveryTime;
            do {
                deliveryTime = (long) ((random.nextGaussian(0.5, variance)) * lastTick);
            } while (deliveryTime < 0.0 || deliveryTime > lastTick);

            if (orders.containsKey(deliveryTime)) {
                orders.get(deliveryTime).add(createRandomOrder(vehicleManager, deliveryTime, deliveryDifference, maxWeight));
            } else {
                orders.put(deliveryTime, new ArrayList<>(List.of(createRandomOrder(vehicleManager, deliveryTime, deliveryDifference, maxWeight))));
            }
        }
    }

    @Override
    public List<ConfirmedOrder> generateOrders(long tick) {
        if (tick < 0) {
            throw new IndexOutOfBoundsException(tick);
        }

        return orders.getOrDefault(tick, List.of());
    }

    private ConfirmedOrder createRandomOrder(VehicleManager vehicleManager, long deliveryTime, long deliveryDifference, double maxWeight) {
        VehicleManager.OccupiedRestaurant restaurant = new ArrayList<>(vehicleManager.getOccupiedRestaurants())
            .get(random.nextInt(vehicleManager.getOccupiedRestaurants().size()));
        Location location = new ArrayList<>(vehicleManager.getOccupiedNeighborhoods())
            .get(random.nextInt(vehicleManager.getOccupiedNeighborhoods().size())).getComponent().getLocation();
        double actualMaxWeight = random.nextDouble(maxWeight);
        int foodCount = random.nextInt(1, 10);
        List<String> foodList = new ArrayList<>();

        for (int i = 0; i < foodCount; i++) {
            foodList.add(restaurant.getComponent().getAvailableFood().get(random.nextInt(restaurant.getComponent().getAvailableFood().size())));
        }

        return new ConfirmedOrder(
            location,
            restaurant,
            new TickInterval(deliveryTime , deliveryTime + deliveryDifference),
            foodList,
            actualMaxWeight);
    }

    private static class Factory implements OrderGenerator.Factory {

        public final int orderCount;
        public final VehicleManager vehicleManager;
        public final int deliveryInterval;
        public final double maxWeight;
        public final double variance;
        public final int lastTick;
        public final int seed;

        public Factory(int orderCount, VehicleManager vehicleManager, int deliveryInterval, double maxWeight, double variance, int lastTick, int seed) {
            this.orderCount = orderCount;
            this.vehicleManager = vehicleManager;
            this.deliveryInterval = deliveryInterval;
            this.maxWeight = maxWeight;
            this.variance = variance;
            this.lastTick = lastTick;
            this.seed = seed;
        }

        @Override
        public OrderGenerator create() {
            return new FridayOrderGenerator(orderCount, vehicleManager, deliveryInterval, maxWeight, variance, lastTick, seed);
        }
    }


    public static class FactoryBuilder implements OrderGenerator.FactoryBuilder {

        public int orderCount = 1000;
        public VehicleManager vehicleManager = null;
        public int deliveryInterval = 15;
        public double maxWeight = 0.5;
        public double variance = 0.5;
        public int lastTick = 480;
        public int seed = -1;

        public FactoryBuilder setOrderCount(int orderCount) {
            this.orderCount = orderCount;
            return this;
        }

        public FactoryBuilder setVehicleManager(VehicleManager vehicleManager) {
            this.vehicleManager = vehicleManager;
            return this;
        }

        public FactoryBuilder setDeliveryInterval(int deliveryInterval) {
            this.deliveryInterval = deliveryInterval;
            return this;
        }

        public FactoryBuilder setMaxWeight(double maxWeight) {
            this.maxWeight = maxWeight;
            return this;
        }

        public FactoryBuilder setVariance(double variance) {
            this.variance = variance;
            return this;
        }

        public FactoryBuilder setLastTick(int lastTick) {
            this.lastTick = lastTick;
            return this;
        }

        public FactoryBuilder setSeed(int seed) {
            this.seed = seed;
            return this;
        }

        @Override
        public Factory build() {
            Objects.requireNonNull(vehicleManager);
            return new Factory(orderCount, vehicleManager, deliveryInterval, maxWeight, variance, lastTick, seed);
        }
    }
}
