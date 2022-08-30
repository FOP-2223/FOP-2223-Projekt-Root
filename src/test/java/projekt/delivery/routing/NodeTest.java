package projekt.delivery.routing;

import org.junit.jupiter.api.Test;
import projekt.base.Location;

import java.time.Duration;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @Test
    void testNodeCompare() {
        final RegionImpl r1 = new RegionImpl();
        r1.putNode(new NodeImpl(r1, "n1", new Location(100, 100), Set.of(new Location(125, 125))));
        r1.putNode(new NodeImpl(r1, "n2", new Location(125, 125), Set.of(new Location(100, 100))));
        r1.putEdge(new EdgeImpl(r1, "ea", new Location(100, 100), new Location(125, 125), Duration.ZERO));
        final RegionImpl r2 = new RegionImpl();
        r2.putNode(new NodeImpl(r2, "n3", new Location(50, 50), Set.of(new Location(75, 75))));
        r2.putNode(new NodeImpl(r2, "n4", new Location(75, 75), Set.of(new Location(50, 50))));
        r2.putEdge(new EdgeImpl(r2, "eb", new Location(50, 50), new Location(75, 75), Duration.ZERO));
        assertNotEquals(r1, r2);
        assertEquals(
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of()),
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of())
        );
        assertEquals(
            new NodeImpl(r1, "", new Location(1, 1), Set.of()),
            new NodeImpl(r1, "", new Location(1, 1), Set.of())
        );
        // region should not be used in Node#equals(Object) to prevent circular dependency in Region#equals(Object)
        assertEquals(
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of()),
            new NodeImpl(r2, "aaa", new Location(1, 1), Set.of())
        );
        assertEquals(
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of(new Location(15, 25), new Location(0, 0))),
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of(new Location(15, 25), new Location(0, 0)))
        );
        assertNotEquals(
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of()),
            new NodeImpl(r1, "aab", new Location(1, 1), Set.of())
        );
        assertNotEquals(
            new NodeImpl(r1, "", new Location(1, 1), Set.of()),
            new NodeImpl(r1, "aab", new Location(1, 1), Set.of())
        );
        assertNotEquals(
            new NodeImpl(r1, "abcdefg", new Location(1, 1), Set.of()),
            new NodeImpl(r1, "bcd", new Location(1, 1), Set.of())
        );
        assertNotEquals(
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of()),
            new NodeImpl(r1, "aaa", new Location(1, 2), Set.of())
        );
        assertNotEquals(
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of()),
            new NodeImpl(r1, "aab", new Location(1, 1), Set.of(new Location(0, 1)))
        );
        assertNotEquals(
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of()),
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of(new Location(0, 1)))
        );
        assertNotEquals(
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of(new Location(1, 0))),
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of(new Location(0, 1)))
        );
        // only compare location
        assertEquals(0,
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of()).compareTo(
                new NodeImpl(r1, "aaa", new Location(1, 1), Set.of())));
        assertEquals(0,
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of()).compareTo(
                new NodeImpl(r1, "aab", new Location(1, 1), Set.of())));
        assertEquals(0,
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of(new Location(0, 0))).compareTo(
                new NodeImpl(r1, "aab", new Location(1, 1), Set.of())));
        assertEquals(-1,
            new NodeImpl(r1, "aaa", new Location(0, 1), Set.of()).compareTo(
                new NodeImpl(r1, "aaa", new Location(1, 1), Set.of())));
        assertEquals(-1,
            new NodeImpl(r1, "aaa", new Location(1, 0), Set.of()).compareTo(
                new NodeImpl(r1, "aaa", new Location(1, 1), Set.of())));
        assertEquals(-1,
            new NodeImpl(r1, "aaa", new Location(-10, 20), Set.of()).compareTo(
                new NodeImpl(r1, "aaa", new Location(-5, -10), Set.of())));
        assertEquals(1,
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of()).compareTo(
                new NodeImpl(r1, "aaa", new Location(0, 1), Set.of())));
        assertEquals(1,
            new NodeImpl(r1, "aaa", new Location(1, 1), Set.of()).compareTo(
                new NodeImpl(r1, "aaa", new Location(1, 0), Set.of())));
        assertEquals(1,
            new NodeImpl(r1, "aaa", new Location(-5, -10), Set.of()).compareTo(
                new NodeImpl(r1, "aaa", new Location(-10, 20), Set.of())));
    }

    @Test
    void testNodeHash() {
        final RegionImpl r1 = new RegionImpl();
        r1.putNode(new NodeImpl(r1, "a", new Location(0, 0), Set.of()));
        final RegionImpl r2 = new RegionImpl();
        r2.putNode(new NodeImpl(r2, "b", new Location(1, 1), Set.of()));
        assertNotEquals(r1, r2);
        assertEquals(
            new NodeImpl(r1, "c", new Location(5, -9), Set.of()).hashCode(),
            new NodeImpl(r1, "c", new Location(5, -9), Set.of()).hashCode()
        );
        assertEquals(
            new NodeImpl(r1, "c", new Location(5, -9), Set.of()).hashCode(),
            new NodeImpl(r2, "c", new Location(5, -9), Set.of()).hashCode()
        );
        assertNotEquals(
            new NodeImpl(r1, "c", new Location(5, -9), Set.of()).hashCode(),
            new NodeImpl(r1, "de", new Location(5, -9), Set.of()).hashCode()
        );
        assertNotEquals(
            new NodeImpl(r1, "", new Location(5, -9), Set.of()).hashCode(),
            new NodeImpl(r1, "a", new Location(5, -9), Set.of()).hashCode()
        );
        assertNotEquals(
            new NodeImpl(r1, "c", new Location(5, -9), Set.of()).hashCode(),
            new NodeImpl(r2, "c", new Location(3, -9), Set.of()).hashCode()
        );
        assertEquals(
            new NodeImpl(r1, "c", new Location(5, -9), Set.of(new Location(1, 2))).hashCode(),
            new NodeImpl(r1, "c", new Location(5, -9), Set.of(new Location(1, 2))).hashCode()
        );
        assertNotEquals(
            new NodeImpl(r1, "c", new Location(5, -9), Set.of(new Location(1, 2))).hashCode(),
            new NodeImpl(r1, "c", new Location(5, -9), Set.of(new Location(2, 2))).hashCode()
        );
        assertNotEquals(
            new NodeImpl(r1, "c", new Location(5, -9), Set.of(new Location(1, 2))).hashCode(),
            new NodeImpl(r1, "e", new Location(0, -9), Set.of(new Location(2, 0))).hashCode()
        );
        assertNotEquals(
            new NodeImpl(r1, "c", new Location(5, -9), Set.of(new Location(1, 2))).hashCode(),
            new NodeImpl(r1, "c", new Location(5, -9), Set.of(new Location(1, 2), new Location(1, 0))).hashCode()
        );
    }

    @Test
    void testNodeToString() {
        RegionImpl r1 = new RegionImpl();
        assertEquals(
            "NodeImpl(name='aaa', location=(4, 0), connections=[])",
            new NodeImpl(r1, "aaa", new Location(4, 0), Set.of()).toString()
        );
        assertEquals(
            "NodeImpl(name='', location=(1, 4), connections=[])",
            new NodeImpl(r1, "", new Location(1, 4), Set.of()).toString()
        );
        assertEquals(
            "NodeImpl(name='b', location=(1, 10), connections=[(1, -1)])",
            new NodeImpl(r1, "b", new Location(1, 10), Set.of(new Location(1, -1))).toString()
        );
        assertEquals(
            "NodeImpl(name='b', location=(0, 10), connections=[(-1, 1), (5, -8)])",
            new NodeImpl(r1, "b", new Location(0, 10),
                Set.of(new Location(-1, 1), new Location(5, -8))).toString()
        );
    }
}
