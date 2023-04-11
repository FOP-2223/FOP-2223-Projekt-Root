package projekt.solution;

import org.jetbrains.annotations.NotNull;
import projekt.base.Location;
import projekt.delivery.routing.EdgeImpl;
import projekt.delivery.routing.Region;

import java.util.Comparator;
import java.util.Objects;

public class TutorEdge extends EdgeImpl {

    private final static Comparator<Region.Edge> COMPARATOR =
        Comparator.comparing(Region.Edge::getNodeA).thenComparing(Region.Edge::getNodeB);

    /**
     * Creates a new {@link TutorEdge} instance.
     * @param region The {@link Region} this {@link TutorEdge} belongs to.
     * @param name The name of this {@link TutorEdge}.
     * @param locationA The start of this {@link TutorEdge}.
     * @param locationB The end of this {@link TutorEdge}.
     * @param duration The length of this {@link TutorEdge}.
     */
    public TutorEdge(
        Region region,
        String name,
        Location locationA,
        Location locationB,
        long duration
    ) {
        super(region, name, locationA, locationB, duration);
    }

    /**
     * Returns the start of this {@link TutorEdge}.
     * @return The start of this {@link TutorEdge}.
     */
    @Override
    public Location getLocationA() {
        return locationA;
    }

    /**
     * Returns the end of this {@link TutorEdge}.
     * @return The end of this {@link TutorEdge}.
     */
    @Override
    public Location getLocationB() {
        return locationB;
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
    public long getDuration() {
        return duration;
    }

    @Override
    public Region.Node getNodeA() {
        return getRegion().getNode(locationA);
    }

    @Override
    public Region.Node getNodeB() {
        return getRegion().getNode(locationB);
    }

    @Override
    public int compareTo(@NotNull Region.Edge o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, locationA, locationB, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TutorEdge edge = (TutorEdge) o;
        return Objects.equals(name, edge.name)
            && Objects.equals(locationA, edge.locationA)
            && Objects.equals(locationB, edge.locationB)
            && Objects.equals(duration, edge.duration);
    }

    @Override
    public String toString() {
        return "TutorEdge(" +
            "name='" + getName() + "'"
            + ", locationA='" + getLocationA() + "'"
            + ", locationB='" + getLocationB() + "'"
            + ", duration='" + getDuration() + "'"
            + ')';
    }
}
