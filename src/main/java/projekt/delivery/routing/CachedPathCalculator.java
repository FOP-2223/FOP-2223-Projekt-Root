package projekt.delivery.routing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CachedPathCalculator implements PathCalculator {

    private final PathCalculator delegate;
    private final Map<StartEndTuple, List<Region.Node>> cache = new HashMap<>();
    private final int size;

    public CachedPathCalculator(PathCalculator delegate, int size) {
        this.delegate = delegate;
        this.size = size;
    }

    public CachedPathCalculator(PathCalculator delegate) {
        this(delegate, 1024);
    }

    @Override
    public List<Region.Node> getPath(Region.Node start, Region.Node end) {
        return null;
    }

    private static class StartEndTuple {
        final Region.Node start;
        final Region.Node end;

        private StartEndTuple(Region.Node start, Region.Node end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StartEndTuple that = (StartEndTuple) o;
            return Objects.equals(start, that.start) && Objects.equals(end, that.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
    }
}
