package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;

import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CachedPathCalculator implements PathCalculator {

    private final PathCalculator delegate;
    private final Map<StartEndTuple, Deque<Region.Node>> cache = new HashMap<>();
    private final int size;
    private final Set<StartEndTuple> accessOrder;

    public CachedPathCalculator(PathCalculator delegate, int size) {
        this.delegate = delegate;
        this.size = size;
        this.accessOrder = new LinkedHashSet<>(size);
    }

    public CachedPathCalculator(PathCalculator delegate) {
        this(delegate, 1024);
    }

    @Override
    public Deque<Region.Node> getPath(Region.Node start, Region.Node end) {
        final StartEndTuple tuple = new StartEndTuple(start, end);
        @Nullable Deque<Region.Node> path = cache.get(tuple);
        if (path != null) {
            return path;
        }
        path = delegate.getPath(start, end);

        // Limit cache size
        if (accessOrder.size() >= size) {
            Iterator<StartEndTuple> iterator = accessOrder.iterator();
            cache.remove(iterator.next());
            iterator.remove();
        }

        accessOrder.add(tuple);
        cache.put(tuple, path);
        return path;
    }

    private static class StartEndTuple {
        final Region.Node start;
        final Region.Node end;
        final int hashcode;

        private StartEndTuple(Region.Node start, Region.Node end) {
            this.start = start;
            this.end = end;
            hashcode = Objects.hash(start, end);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            StartEndTuple that = (StartEndTuple) o;
            if (hashcode != that.hashcode) {
                return false;
            }
            return Objects.equals(start, that.start)
                && Objects.equals(end, that.end);
        }

        @Override
        public int hashCode() {
            return hashcode;
        }
    }
}
