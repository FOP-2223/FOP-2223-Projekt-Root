package projekt.delivery.rating;

import projekt.delivery.event.Event;
import projekt.delivery.routing.PathCalculator;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;

import java.util.List;

import static org.tudalgo.algoutils.student.Student.crash;

public class TravelDistanceRater implements Rater {

    public static final RatingCriteria RATING_CRITERIA = RatingCriteria.TRAVEL_DISTANCE;

    private final Region region;
    private final PathCalculator pathCalculator;
    private final double factor;

    public TravelDistanceRater(VehicleManager vehicleManager, double factor) {
        region = vehicleManager.getRegion();
        pathCalculator = vehicleManager.getPathCalculator();
        this.factor = factor;
    }

    @Override
    public double getScore() {
        return crash(); // TODO: H8.3 - remove if implemented
    }

    @Override
    public RatingCriteria getRatingCriteria() {
        return RATING_CRITERIA;
    }

    @Override
    public void onTick(List<Event> events, long tick) {
        crash(); // TODO: H5.1 - remove if implemented
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
