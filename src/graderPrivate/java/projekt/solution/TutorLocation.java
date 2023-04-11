package projekt.solution;

import org.jetbrains.annotations.NotNull;
import projekt.base.Location;

import java.util.Comparator;

public class TutorLocation extends Location {

    private final static Comparator<Location> COMPARATOR =
        Comparator.comparing(Location::getX).thenComparing(Location::getY);

    private final int hashcode;

    /**
     * Instantiates a new {@link TutorLocation} object using {@code x} and {@code y} as coordinates.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public TutorLocation(int x, int y) {
        super(x, y);
        // Equivalent: hashcode = (x << 16) | ((y << 16) >>> 16);
        hashcode = (x << 16) | (0xFFFF & y);
    }

    /**
     * Returns the x-coordinate of this location.
     *
     * @return the x-coordinate
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of this location.
     *
     * @return the y-coordinate
     */

    @Override
    public int getY() {
        return y;
    }

    /**
     * Adds the coordinates of this location and the other location and returns a new
     * {@link TutorLocation} object with the resulting coordinates.
     *
     * @param other the other {@link TutorLocation} object to get the second set of coordinates from
     * @return a new {@link TutorLocation} object with the sum of coordinates from both TutorLocations
     */

    @Override
    public Location add(Location other) {
        return new TutorLocation(x + other.getX(), y + other.getY());
    }

    /**
     * Subtracts the coordinates of this TutorLocation from the other TutorLocation and returns a new
     * {@link TutorLocation} object with the resulting coordinates.
     *
     * @param other the other {@link TutorLocation} object to get the second set of coordinates from
     * @return a new {@link TutorLocation} object with the difference of coordinates from both TutorLocations
     */
    @Override
    public Location subtract(Location other) {
        return new TutorLocation(x - other.getX(), y - other.getY());
    }

    @Override
    public int compareTo(@NotNull Location o) {
        return COMPARATOR.compare(this, o);
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location location)) {
            return false;
        }
        return x == location.getX() && y == location.getY();
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
    }

}
