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

    private final Random random = new Random();
    private final Map<Long, List<ConfirmedOrder>> orders = new HashMap<>();

    private int orderID = 0;

    FridayOrderGenerator(int orderCount, VehicleManager vehicleManager, int deliveryInterval, double maxWeight, double variance, int lastTick) {
        List<Location> possibleLocations = vehicleManager.getOccupiedNodes().stream()
            .filter(VehicleManager.OccupiedNeighborhood.class::isInstance)
            .map(VehicleManager.OccupiedNeighborhood.class::cast)
            .map(n -> n.getComponent().getLocation())
            .toList();

        for (int i = 0; i < orderCount; i++) {
            long deliveryTime;
            do {
                deliveryTime = (long) ((random.nextGaussian(0.5, variance)) * lastTick);
            } while (deliveryTime < 0.0 || deliveryTime > lastTick);

            if (orders.containsKey(deliveryTime)) {
                orders.get(deliveryTime).add(createRandomOrder(vehicleManager, deliveryTime, deliveryInterval, possibleLocations, maxWeight));
            } else {
                orders.put(deliveryTime, new ArrayList<>(List.of(createRandomOrder(vehicleManager, deliveryTime, deliveryInterval, possibleLocations, maxWeight))));
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

    private ConfirmedOrder createRandomOrder(VehicleManager vehicleManager, long deliveryTime, long deliveryInterval,List<Location> possibleLocations, double maxWeight) {
        VehicleManager.OccupiedRestaurant restaurant = vehicleManager.getRestaurants().get(random.nextInt(vehicleManager.getRestaurants().size()));
        double actualMaxWeight = random.nextDouble(maxWeight);
        int foodCount = random.nextInt(1, restaurant.getAvailableFood().size());
        List<String> foodList = new ArrayList<>();

        for (int i = 0; i < foodCount; i++) {
            foodList.add(restaurant.getAvailableFood().get(random.nextInt(restaurant.getAvailableFood().size())));
        }

        return new ConfirmedOrder(
            possibleLocations.get(random.nextInt(possibleLocations.size())),
            restaurant,
            new TickInterval(deliveryTime , deliveryTime + deliveryInterval),
            foodList,
            actualMaxWeight);
    }

    private static class FridayOrderGeneratorFactory implements Factory {

        public final int orderCount;
        public final VehicleManager vehicleManager;
        public final int deliveryInterval;
        public final double maxWeight;
        public final double variance;
        public final int lastTick;

        public FridayOrderGeneratorFactory(int orderCount, VehicleManager vehicleManager, int deliveryInterval, double maxWeight, double variance, int lastTick) {
            this.orderCount = orderCount;
            this.vehicleManager = vehicleManager;
            this.deliveryInterval = deliveryInterval;
            this.maxWeight = maxWeight;
            this.variance = variance;
            this.lastTick = lastTick;
        }

        @Override
        public OrderGenerator create() {
            return new FridayOrderGenerator(orderCount, vehicleManager, deliveryInterval, maxWeight, variance, lastTick);
        }
    }


    public static class FridayOrderGeneratorFactoryBuilder implements FactoryBuilder {

        public int orderCount = 1000;
        public VehicleManager vehicleManager = null;
        public int deliveryInterval = 15;
        public double maxWeight = 0.5;
        public double variance = 0.5;
        public int lastTick = 480;

        public FridayOrderGeneratorFactoryBuilder setOrderCount(int orderCount) {
            this.orderCount = orderCount;
            return this;
        }

        public FridayOrderGeneratorFactoryBuilder setVehicleManager(VehicleManager vehicleManager) {
            this.vehicleManager = vehicleManager;
            return this;
        }

        public FridayOrderGeneratorFactoryBuilder setDeliveryInterval(int deliveryInterval) {
            this.deliveryInterval = deliveryInterval;
            return this;
        }

        public FridayOrderGeneratorFactoryBuilder setMaxWeight(double maxWeight) {
            this.maxWeight = maxWeight;
            return this;
        }

        public FridayOrderGeneratorFactoryBuilder setVariance(double variance) {
            this.variance = variance;
            return this;
        }

        public FridayOrderGeneratorFactoryBuilder setLastTick(int lastTick) {
            this.lastTick = lastTick;
            return this;
        }

        @Override
        public Factory build() {
            Objects.requireNonNull(vehicleManager);
            return new FridayOrderGeneratorFactory(orderCount, vehicleManager, deliveryInterval, maxWeight, variance, lastTick);
        }
    }
}
