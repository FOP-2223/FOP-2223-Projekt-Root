package projekt.delivery.rating;

import projekt.delivery.event.Event;

import java.util.List;

import static org.tudalgo.algoutils.student.Student.crash;

public class AmountDeliveredRater implements Rater {

    public static final RatingCriteria RATING_CRITERIA = RatingCriteria.AMOUNT_DELIVERED;

    private final double factor;

    public AmountDeliveredRater(double factor) {
        this.factor = factor;
    }

    @Override
    public double getScore() {
        return crash(); // TODO: H8.1 - remove if implemented
    }

    @Override
    public RatingCriteria getRatingCriteria() {
        return RATING_CRITERIA;
    }

    @Override
    public void onTick(List<Event> events, long tick) {
        crash(); // TODO: H8.1 - remove if implemented
    }

    public static class Factory implements Rater.Factory {

        private final double factor;

        Factory(double factor) {
            this.factor = factor;
        }

        @Override
        public Rater create() {
            return new AmountDeliveredRater(factor);
        }

    }

    public static class FactoryBuilder implements Rater.FactoryBuilder {

        private double factor = 0.99;

        @Override
        public Rater.Factory build() {
            return new Factory(factor);
        }

        public FactoryBuilder setFactor(double factor) {
            if (factor < 0 || factor > 1) {
                throw new IllegalArgumentException("factor must be between 0 and 1");
            }

            this.factor = factor;
            return this;
        }
    }
}
