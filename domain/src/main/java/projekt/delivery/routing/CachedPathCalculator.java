package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CachedPathCalculator implements PathCalculator {

    private final PathCalculator delegate;
    private final Map<Region.Node, Map<Region.Node, Deque<Region.Node>>> cache = new HashMap<>();
    private final int size;
    private final Set<Region.Node> accessOrder;

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
        return getAllPathsTo(end).get(start);
    }

    @Override
    public Map<Region.Node, Deque<Region.Node>> getAllPathsTo(Region.Node end) {
        @Nullable Map<Region.Node, Deque<Region.Node>> path = cache.get(end);
        if (path != null) {
            return path;
        }

        path = delegate.getAllPathsTo(end);

        // Limit cache size
        if (accessOrder.size() >= size) {
            Iterator<Region.Node> iterator = accessOrder.iterator();
            cache.remove(iterator.next());
            iterator.remove();
        }

        // Update access order if the element already exists
        accessOrder.remove(end);
        accessOrder.add(end);
        cache.put(end, path);

        return path;
    }

}
