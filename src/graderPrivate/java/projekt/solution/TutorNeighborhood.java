package projekt.solution;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;
import projekt.delivery.routing.NeighborhoodImpl;
import projekt.delivery.routing.Region;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TutorNeighborhood extends NeighborhoodImpl {

    /**
     * Creates a new {@link TutorNeighborhood} instance.
     * @param region The {@link Region} this {@link TutorNeighborhood} belongs to.
     * @param name The name of this {@link TutorNeighborhood}.
     * @param location The {@link Location} of this {@link TutorNeighborhood}.
     * @param connections All {@link Location}s this {@link TutorNeighborhood} has an {@link Region.Edge} to.
     */
    public TutorNeighborhood(
        Region region,
        String name,
        Location location,
        Set<Location> connections
    ) {
        super(region, name, location, connections);
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public @Nullable Region.Edge getEdge(Region.Node other) {
        return region.getEdge(getLocation(), other.getLocation());
    }

    @Override
    public Set<Region.Node> getAdjacentNodes() {
        return connections.stream().map(region::getNode).collect(Collectors.toSet());
    }

    @Override
    public Set<Region.Edge> getAdjacentEdges() {
        return connections.stream().map(c -> region.getEdge(getLocation(), c)).collect(Collectors.toSet());
    }

    @Override
    public Set<Location> getConnections() {
        return connections;
    }

    @Override
    public int compareTo(Region.Node o) {
        return location.compareTo(o.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, connections);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NeighborhoodImpl that = (NeighborhoodImpl) o;
        if (hashCode() != that.hashCode()) {
            return false;
        }

        return Objects.equals(name, that.name)
            && Objects.equals(location, that.location)
            && Objects.equals(connections, that.connections);
    }

    @Override
    public String toString() {
        return "NeighborhoodImpl(name='" + getName()
            + ", location=" + getLocation()
            + ", connections=" + connections
            + ')';
    }

}
