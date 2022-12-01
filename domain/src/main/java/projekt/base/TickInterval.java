package projekt.base;

/**
 * A closed interval of a start time and an end time.
 */
public class TickInterval {
    private final long start;
    private final long end;

    /**
     * Constructs a new {@link TickInterval} with start time {@code start} and end time {@code end}.
     *
     * @param start the start time
     * @param end   the end time
     */
    public TickInterval(long start, long end) {
        if (start < 0) {
            throw new IllegalArgumentException(String.format("Start tick is negative: %d", start));
        }
        if (end < 0) {
            throw new IllegalArgumentException(String.format("End tick is negative: %d", end));
        }
        if (start > end) {
            throw new IllegalArgumentException(String.format("Start %s is after end %s", start, end));
        }
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the start time of this time interval.
     *
     * @return the start time
     */
    public long getStart() {
        return start;
    }

    /**
     * Returns the end time of this time interval.
     *
     * @return the end time
     */
    public long getEnd() {
        return end;
    }

    /**
     * Returns the duration between {@link #start} and {@link #end}.
     * The duration is represented as the amount of ticks between the start and end tick.
     *
     * @return the duration between start and end time
     */
    public long getDuration() {
        return end - start;
    }

    @Override
    public String toString() {
        return "TickInterval{" +
            "start=" + start +
            ", end=" + end +
            '}';
    }
}
