package projekt.delivery.rating;

import projekt.delivery.event.ArrivedAtNodeEvent;
import projekt.delivery.event.Event;
import projekt.delivery.event.OrderReceivedEvent;
import projekt.delivery.routing.PathCalculator;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;

import java.util.Deque;
import java.util.List;
import java.util.Objects;

public class TravelDistanceRater implements Rater {

    public static final RatingCriteria RATING_CRITERIA = RatingCriteria.TRAVEL_DISTANCE;

    private final Region region;
    private final PathCalculator pathCalculator;
    private final double factor;

    private long worstDistance = 0;
    private long actualDistance = 0;

    public TravelDistanceRater(VehicleManager vehicleManager, double factor) {
        region = vehicleManager.getRegion();
        pathCalculator = vehicleManager.getPathCalculator();
        this.factor = factor;
    }

    @Override
    public double getScore() {
        double actualWorstDistance = worstDistance * factor;

        if (actualDistance >= actualWorstDistance) {
            return 0;
        }

        return 1 - (actualDistance / actualWorstDistance);
    }

    @Override
    public RatingCriteria getRatingCriteria() {
        return RATING_CRITERIA;
    }

    @Override
    public void onTick(List<Event> events, long tick) {

        events.stream()
            .filter(OrderReceivedEvent.class::isInstance)
            .map(OrderReceivedEvent.class::cast)
            .forEach(orderReceivedEvent -> worstDistance += 2 * getDistance(orderReceivedEvent.getRestaurant(), region.getNode(orderReceivedEvent.getOrder().getLocation())));

        events.stream()
            .filter(ArrivedAtNodeEvent.class::isInstance)
            .map(ArrivedAtNodeEvent.class::cast)
            .forEach(arrivedAtNodeEvent -> {
                actualDistance += arrivedAtNodeEvent.getLastEdge().getDuration();
            });
    }

    private double getDistance(Region.Node node1, Region.Node node2) {
        Deque<Region.Node> path = pathCalculator.getPath(node1, node2);
        long distance = 0;
        Region.Node previousNode = node1;
        Region.Node node = path.pollFirst();

        do {
            assert node != null;
            distance += Objects.requireNonNull(region.getEdge(previousNode, node)).getDuration();
            previousNode = node;
            node = path.pollFirst();
        } while (!path.isEmpty());

        return distance;
    }

    public static class Factory implements Rater.Factory {

        private final VehicleManager vehicleManager;
        private final double factor;

        Factory(VehicleManager vehicleManager, double factor) {
            this.vehicleManager = vehicleManager;
            this.factor = factor;
        }

        @Override
        public Rater create() {
            return new TravelDistanceRater(vehicleManager, factor);
        }

    }

    public static class FactoryBuilder implements Rater.FactoryBuilder {

        private VehicleManager vehicleManager;
        private double factor = 0.5;

        @Override
        public Rater.Factory build() {
            return new Factory(vehicleManager, factor);
        }

        public FactoryBuilder setVehicleManager(VehicleManager vehicleManager) {
            this.vehicleManager = vehicleManager;
            return this;
        }

        public FactoryBuilder setFactor(double factor) {
            if (factor < 0) {
                throw new IllegalArgumentException("factor must be positiv");
            }

            this.factor = factor;
            return this;
        }
    }

}
