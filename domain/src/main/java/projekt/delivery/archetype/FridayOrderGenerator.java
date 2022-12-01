package projekt.delivery.archetype;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * Represents the incoming orders on an average friday evening starting at 4.00pm (tick = 0) until 12:00pm (tick = 480)
 * with one tick representing one minute. The incoming orders follow a normal distribution.
 */
public class FridayOrderGenerator implements OrderGenerator {

    private final Random random;

    FridayOrderGenerator(int orderCount, VehicleManager vehicleManager, int deliveryDifference, double maxWeight, double variance, int lastTick, int seed) {
        random = seed < 0 ? new Random() : new Random(seed);

        crash(); // TODO: H7.1 - remove if implemented
    }

    @Override
    public List<ConfirmedOrder> generateOrders(long tick) {
        return crash(); // TODO: H7.1 - remove if implemented
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
