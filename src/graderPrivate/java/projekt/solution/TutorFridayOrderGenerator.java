package projekt.solution;

import projekt.base.Location;
import projekt.base.TickInterval;
import projekt.delivery.generator.OrderGenerator;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class TutorFridayOrderGenerator implements OrderGenerator {

    private final Random random;
    private final Map<Long, List<ConfirmedOrder>> orders = new HashMap<>();

    /**
     * Creates a new {@link TutorFridayOrderGenerator} with the given parameters.
     * @param orderCount The total amount of orders this {@link OrderGenerator} will create. It is equal to the sum of
     *                   the size of the lists that are returned for every positive long value.
     * @param vehicleManager The {@link VehicleManager} this {@link OrderGenerator} will create orders for.
     * @param deliveryInterval The amount of ticks between the start and end tick of the deliveryInterval of the created orders.
     * @param maxWeight The maximum weight of a created order.
     * @param standardDeviation The standardDeviation of the normal distribution.
     * @param lastTick The last tick this {@link OrderGenerator} can return a non-empty list.
     * @param seed The seed for the used {@link Random} instance. If negative a random seed will be used.
     */
    private TutorFridayOrderGenerator(int orderCount, VehicleManager vehicleManager, int deliveryInterval, double maxWeight, double standardDeviation, long lastTick, int seed) {
        random = seed < 0 ? new Random() : new Random(seed);

        for (int i = 0; i < orderCount; i++) {
            long deliveryTime;
            do {
                deliveryTime = (long) ((random.nextGaussian(0.5, standardDeviation)) * lastTick);
            } while (deliveryTime < 0.0 || deliveryTime > lastTick);

            if (orders.containsKey(deliveryTime)) {
                orders.get(deliveryTime).add(createRandomOrder(vehicleManager, deliveryTime, deliveryInterval, maxWeight));
            } else {
                orders.put(deliveryTime, new ArrayList<>(List.of(createRandomOrder(vehicleManager, deliveryTime, deliveryInterval, maxWeight))));
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

    private ConfirmedOrder createRandomOrder(VehicleManager vehicleManager, long deliveryTime, long deliveryInterval, double maxWeight) {
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
            new TickInterval(deliveryTime , deliveryTime + deliveryInterval),
            foodList,
            actualMaxWeight);
    }

    /**
     * A {@link OrderGenerator.Factory} for creating a new {@link TutorFridayOrderGenerator}.
     */
    public static class Factory implements OrderGenerator.Factory {

        public final int orderCount;
        public final VehicleManager vehicleManager;
        public final int deliveryInterval;
        public final double maxWeight;
        public final double standardDeviation;
        public final long lastTick;
        public final int seed;

        private Factory(int orderCount, VehicleManager vehicleManager, int deliveryInterval, double maxWeight, double standardDeviation, long lastTick, int seed) {
            this.orderCount = orderCount;
            this.vehicleManager = vehicleManager;
            this.deliveryInterval = deliveryInterval;
            this.maxWeight = maxWeight;
            this.standardDeviation = standardDeviation;
            this.lastTick = lastTick;
            this.seed = seed;
        }

        @Override
        public OrderGenerator create() {
            return new TutorFridayOrderGenerator(orderCount, vehicleManager, deliveryInterval, maxWeight, standardDeviation, lastTick, seed);
        }

        /**
         * Creates a new {@link TutorFridayOrderGenerator.FactoryBuilder}.
         * @return The created {@link TutorFridayOrderGenerator.FactoryBuilder}.
         */
        public static TutorFridayOrderGenerator.FactoryBuilder builder() {
            return new TutorFridayOrderGenerator.FactoryBuilder();
        }
    }


    /**
     * A {@link OrderGenerator.FactoryBuilder} form constructing a new {@link TutorFridayOrderGenerator.Factory}.
     */
    public static class FactoryBuilder implements OrderGenerator.FactoryBuilder {

        public int orderCount = 1000;
        public VehicleManager vehicleManager = null;
        public int deliveryInterval = 15;
        public double maxWeight = 0.5;
        public double standardDeviation = 0.5;
        public long lastTick = 480;
        public int seed = -1;

        private FactoryBuilder() {}

        public TutorFridayOrderGenerator.FactoryBuilder setOrderCount(int orderCount) {
            this.orderCount = orderCount;
            return this;
        }

        public TutorFridayOrderGenerator.FactoryBuilder setVehicleManager(VehicleManager vehicleManager) {
            this.vehicleManager = vehicleManager;
            return this;
        }

        public TutorFridayOrderGenerator.FactoryBuilder setDeliveryInterval(int deliveryInterval) {
            this.deliveryInterval = deliveryInterval;
            return this;
        }

        public TutorFridayOrderGenerator.FactoryBuilder setMaxWeight(double maxWeight) {
            this.maxWeight = maxWeight;
            return this;
        }

        public TutorFridayOrderGenerator.FactoryBuilder setStandardDeviation(double standardDeviation) {
            this.standardDeviation = standardDeviation;
            return this;
        }

        public TutorFridayOrderGenerator.FactoryBuilder setLastTick(long lastTick) {
            this.lastTick = lastTick;
            return this;
        }

        public TutorFridayOrderGenerator.FactoryBuilder setSeed(int seed) {
            this.seed = seed;
            return this;
        }

        @Override
        public TutorFridayOrderGenerator.Factory build() {
            Objects.requireNonNull(vehicleManager);
            return new Factory(orderCount, vehicleManager, deliveryInterval, maxWeight, standardDeviation, lastTick, seed);
        }
    }
}
