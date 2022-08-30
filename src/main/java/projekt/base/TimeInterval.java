package projekt.base;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A closed interval of a start time and an end time.
 */
public class TimeInterval {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Duration duration;

    /**
     * Constructs a new {@link TimeInterval} with start time {@code start} and end time {@code end}.
     *
     * @param start the start time
     * @param end   the end time
     */
    public TimeInterval(LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
        if (start.isAfter(end)) {
            throw new IllegalArgumentException(String.format("Start %s is after end %s", start, end));
        }
        this.start = start;
        this.end = end;
        duration = Duration.between(start, end);
    }

    /**
     * Returns the start time of this time interval.
     *
     * @return the start time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Returns the end time of this time interval.
     *
     * @return the end time
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Returns the duration between {@link #start} and {@link #end}.
     *
     * @return the duration between start and end time
     */
    public Duration getDuration() {
        return duration;
    }
}
