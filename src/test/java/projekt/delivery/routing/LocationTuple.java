package projekt.delivery.routing;

import projekt.base.Location;

import java.util.Objects;

class LocationTuple {
    final Location a;
    final Location b;

    LocationTuple(Location a, Location b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationTuple that = (LocationTuple) o;
        return Objects.equals(a, that.a) && Objects.equals(b, that.b);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
