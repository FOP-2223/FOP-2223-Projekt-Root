package projekt.delivery.rating;

import projekt.delivery.event.Event;

import java.util.List;

import static org.tudalgo.algoutils.student.Student.crash;

public class InTimeRater implements Rater {

    public static final RatingCriteria RATING_CRITERIA = RatingCriteria.IN_TIME;

    private final long ignoredTicksOff;
    private final long maxTicksOff;

    InTimeRater(long ignoredTicksOff, long maxTicksOff) {
        if (ignoredTicksOff < 0) throw new IllegalArgumentException(String.valueOf(ignoredTicksOff));
        if (maxTicksOff <= 0) throw new IllegalArgumentException(String.valueOf(maxTicksOff));

        this.ignoredTicksOff = ignoredTicksOff;
        this.maxTicksOff = maxTicksOff;
    }

    public double getScore() {
        return crash(); // TODO: H8.2 - remove if implemented
    }

    @Override
    public void onTick(List<Event> events, long tick) {
        crash(); // TODO: H8.2 - remove if implemented
    }

    @Override
    public RatingCriteria getRatingCriteria() {
        return RATING_CRITERIA;
    }

    public static class Factory implements Rater.Factory {

        private final long ignoredTicksOff;
        private final long maxTicksOff;

        Factory(long ignoredTicksOff, long maxTicksOff) {
            this.ignoredTicksOff = ignoredTicksOff;
            this.maxTicksOff = maxTicksOff;
        }

        @Override
        public Rater create() {
            return new InTimeRater(ignoredTicksOff, maxTicksOff);
        }
    }

    public static class FactoryBuilder implements Rater.FactoryBuilder {

        private long ignoredTicksOff = 5;
        private long maxTicksOff = 25;


        public FactoryBuilder setIgnoredTicksOff(long ignoredTicksOff) {
            this.ignoredTicksOff = ignoredTicksOff;
            return this;
        }

        public FactoryBuilder setMaxTicksOff(long maxTicksOff) {
            this.maxTicksOff = maxTicksOff;
            return this;
        }

        @Override
        public Rater.Factory build() {
            return new Factory(ignoredTicksOff, maxTicksOff);
        }
    }
}
