package projekt.delivery.routing;

import org.junit.jupiter.api.Test;
import projekt.base.Location;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
    @Test
    void testLocationCompare() {
        assertEquals(new Location(0, 0), new Location(0, 0));
        assertEquals(new Location(1, 5), new Location(1, 5));
        assertEquals(new Location(-1000, -300), new Location(-1000, -300));
        assertNotEquals(new Location(-1001, -300), new Location(-1000, -300));
        assertNotEquals(new Location(0, -300), new Location(0, 400));
        assertNotEquals(new Location(2, -300), new Location(0, 400));
        assertEquals(0, new Location(0, 0).compareTo(new Location(0, 0)));
        assertEquals(0, new Location(2, 2).compareTo(new Location(2, 2)));
        assertEquals(0, new Location(-100, 2).compareTo(new Location(-100, 2)));
        assertEquals(-1, new Location(0, 0).compareTo(new Location(0, 1)));
        assertEquals(-1, new Location(0, 0).compareTo(new Location(1, 0)));
        assertEquals(-1, new Location(0, 0).compareTo(new Location(1, 1)));
        assertEquals(-1, new Location(-5, 0).compareTo(new Location(-4, 200)));
        assertEquals(-1, new Location(-30, 300).compareTo(new Location(10, -30)));
        assertEquals(1, new Location(0, 1).compareTo(new Location(0, 0)));
        assertEquals(1, new Location(1, 0).compareTo(new Location(0, 0)));
        assertEquals(1, new Location(1, 1).compareTo(new Location(0, 0)));
        assertEquals(1, new Location(-4, 200).compareTo(new Location(-5, 0)));
        assertEquals(1, new Location(10, -30).compareTo(new Location(-30, 300)));
    }

    @Test
    void testLocationHash() {
        assertEquals(new Location(0, 0).hashCode(), new Location(0, 0).hashCode());
        assertEquals(new Location(-10, 20).hashCode(), new Location(-10, 20).hashCode());
        assertNotEquals(new Location(-9, 20).hashCode(), new Location(-10, 20).hashCode());
        assertNotEquals(new Location(0, 20).hashCode(), new Location(0, 17).hashCode());
    }
}
