package projekt.delivery.routing;

import java.util.Deque;

public interface PathCalculator {

    // TODO: Throw exception if region mismatch

    /**
     * Calculates the shortest path from {@code start} to {@code end}.
     *
     * @param start The start {@link Region.Node}
     * @param end   The end {@link Region.Node}
     * @return A list of nodes (including start and end) that represent a path from start to end
     */
    Deque<Region.Node> getPath(Region.Node start, Region.Node end);
}
