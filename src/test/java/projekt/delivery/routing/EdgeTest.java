package projekt.delivery.routing;

import org.junit.jupiter.api.Test;
import projekt.base.Location;

import java.time.Duration;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {

    @Test
    void testEdgeCompare() {
        final RegionImpl r1 = new RegionImpl();
        r1.putNode(new NodeImpl(r1, "n0_1", new Location(0, 1), Set.of()));
        r1.putNode(new NodeImpl(r1, "n0_10", new Location(0, 10), Set.of()));
        r1.putNode(new NodeImpl(r1, "n1_0", new Location(1, 0), Set.of()));
        r1.putNode(new NodeImpl(r1, "n1_1", new Location(1, 1), Set.of()));
        r1.putNode(new NodeImpl(r1, "n1_3", new Location(1, 3), Set.of()));
        r1.putNode(new NodeImpl(r1, "n2_2", new Location(2, 2), Set.of()));
        r1.putNode(new NodeImpl(r1, "n2_3", new Location(2, 3), Set.of()));
        r1.putNode(new NodeImpl(r1, "n10_3", new Location(10, 3), Set.of()));
        r1.putNode(new NodeImpl(r1, "n10_10", new Location(10, 10), Set.of()));
        r1.putNode(new NodeImpl(r1, "n10_30", new Location(10, 30), Set.of()));
        r1.putNode(new NodeImpl(r1, "n11_3", new Location(11, 3), Set.of()));
        final RegionImpl r2 = new RegionImpl();
        r2.putNode(new NodeImpl(r2, "n1_1", new Location(1, 1), Set.of()));
        r2.putNode(new NodeImpl(r2, "n2_3", new Location(2, 3), Set.of()));
        r2.putNode(new NodeImpl(r2, "n50_50", new Location(50, 50), Set.of()));
        assertNotEquals(r1, r2);
        assertEquals(
            new EdgeImpl(r1, "eee", new Location(2, 2), new Location(3, 3), Duration.ofMinutes(3)),
            new EdgeImpl(r1, "eee", new Location(2, 2), new Location(3, 3), Duration.ofMinutes(3))
        );
        assertEquals(
            new EdgeImpl(r1, "", new Location(2, 2), new Location(3, 3), Duration.ofMinutes(3)),
            new EdgeImpl(r1, "", new Location(2, 2), new Location(3, 3), Duration.ofMinutes(3))
        );
        // region should not be used in Edge#equals(Object) to prevent circular dependency in Region#equals(Object)
        assertEquals(
            new EdgeImpl(r1, "eee", new Location(2, 2), new Location(3, 3), Duration.ofMinutes(3)),
            new EdgeImpl(r2, "eee", new Location(2, 2), new Location(3, 3), Duration.ofMinutes(3))
        );
        // locationA must be <= locationB
        assertEquals("locationA (23, 4) must be <= locationB (-34, 42)",
            assertThrows(IllegalArgumentException.class, () ->
                new EdgeImpl(r1, "eee", new Location(23, 4), new Location(-34, 42), Duration.ofMinutes(3))
            ).getMessage());
        // allow loops
        assertDoesNotThrow(() ->
            new EdgeImpl(r1, "eee", new Location(3, 3), new Location(3, 3), Duration.ofMinutes(3))
        );
        assertNotEquals(
            new EdgeImpl(r1, "eee", new Location(-34, 42), new Location(23, 4), Duration.ofMinutes(3)),
            new EdgeImpl(r1, "eee", new Location(-34, 42), new Location(23, 4), Duration.ofMinutes(4))
        );
        assertNotEquals(
            new EdgeImpl(r1, "eee", new Location(-34, 42), new Location(23, 4), Duration.ofMinutes(3)),
            new EdgeImpl(r1, "eea", new Location(-34, 42), new Location(23, 4), Duration.ofMinutes(3))
        );
        assertNotEquals(
            new EdgeImpl(r1, "eee", new Location(-34, 42), new Location(23, 4), Duration.ofMinutes(3)),
            new EdgeImpl(r1, "eea", new Location(-34, 42), new Location(23, 4), Duration.ofMinutes(3))
        );
        assertNotEquals(
            new EdgeImpl(r1, "", new Location(-34, 42), new Location(23, 4), Duration.ofMinutes(3)),
            new EdgeImpl(r1, "eea", new Location(-34, 42), new Location(23, 4), Duration.ofMinutes(3))
        );
        assertNotEquals(
            new EdgeImpl(r1, "hello", new Location(-34, 42), new Location(23, 4), Duration.ofMinutes(3)),
            new EdgeImpl(r1, "bye", new Location(-34, 42), new Location(23, 4), Duration.ofMinutes(3))
        );
        assertNotEquals(
            new EdgeImpl(r1, "eee", new Location(-34, 42), new Location(23, 4), Duration.ofMinutes(3)),
            new EdgeImpl(r1, "eea", new Location(-33, 42), new Location(-33, 44), Duration.ofMinutes(2))
        );
        assertEquals(0,
            new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1))));
        assertEquals(0,
            new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r2, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1))));
        assertEquals(0,
            new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(2))));
        assertEquals(-1,
            new EdgeImpl(r1, "aaa", new Location(0, 1), new Location(2, 3), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1))));
        assertEquals(-1,
            new EdgeImpl(r1, "aaa", new Location(1, 0), new Location(2, 3), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1))));
        assertEquals(-1,
            new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(1, 3), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1))));
        assertEquals(-1,
            new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 2), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1))));
        assertEquals(-1,
            new EdgeImpl(r1, "aaa", new Location(0, 10), new Location(10, 3), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1))));
        assertEquals(-1,
            new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(10, 30), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(11, 3), Duration.ofMinutes(1))));
        assertEquals(1,
            new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(0, 1), new Location(2, 3), Duration.ofMinutes(1))));
        assertEquals(1,
            new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(1, 0), new Location(2, 3), Duration.ofMinutes(1))));
        assertEquals(1,
            new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(1, 3), Duration.ofMinutes(1))));
        assertEquals(1,
            new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 2), Duration.ofMinutes(1))));
        assertEquals(1,
            new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(2, 3), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(0, 10), new Location(10, 3), Duration.ofMinutes(1))));
        assertEquals(1,
            new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(11, 3), Duration.ofMinutes(1)).compareTo(
                new EdgeImpl(r1, "aaa", new Location(1, 1), new Location(10, 30), Duration.ofMinutes(1))));
    }

    @Test
    void testEdgeHash() {
        final RegionImpl r1 = new RegionImpl();
        r1.putNode(new NodeImpl(r1, "a", new Location(0, 0), Set.of()));
        final RegionImpl r2 = new RegionImpl();
        r2.putNode(new NodeImpl(r2, "b", new Location(1, 1), Set.of()));
        assertNotEquals(r1, r2);
        assertEquals(
            new EdgeImpl(r1, "e", new Location(0, 1), new Location(1, 0), Duration.ZERO).hashCode(),
            new EdgeImpl(r1, "e", new Location(0, 1), new Location(1, 0), Duration.ZERO).hashCode()
        );
        assertEquals(
            new EdgeImpl(r1, "e", new Location(0, 1), new Location(1, 0), Duration.ZERO).hashCode(),
            new EdgeImpl(r2, "e", new Location(0, 1), new Location(1, 0), Duration.ZERO).hashCode()
        );
        assertNotEquals(
            new EdgeImpl(r1, "e", new Location(0, 1), new Location(1, 0), Duration.ZERO).hashCode(),
            new EdgeImpl(r1, "e", new Location(0, 1), new Location(1, 0), Duration.ofSeconds(1)).hashCode()
        );
        assertNotEquals(
            new EdgeImpl(r1, "e", new Location(0, 1), new Location(1, 0), Duration.ZERO).hashCode(),
            new EdgeImpl(r1, "e", new Location(0, 1), new Location(1, 1), Duration.ZERO).hashCode()
        );
        assertNotEquals(
            new EdgeImpl(r1, "e", new Location(0, 1), new Location(1, 0), Duration.ZERO).hashCode(),
            new EdgeImpl(r1, "e", new Location(0, 1), new Location(2, 0), Duration.ZERO).hashCode()
        );
        assertNotEquals(
            new EdgeImpl(r1, "e", new Location(0, 1), new Location(1, 0), Duration.ZERO).hashCode(),
            new EdgeImpl(r1, "e", new Location(0, 2), new Location(1, 0), Duration.ZERO).hashCode()
        );
        assertNotEquals(
            new EdgeImpl(r1, "e", new Location(0, 1), new Location(1, 0), Duration.ZERO).hashCode(),
            new EdgeImpl(r1, "e", new Location(1, 1), new Location(1, 2), Duration.ZERO).hashCode()
        );
        assertNotEquals(
            new EdgeImpl(r1, "e", new Location(0, 1), new Location(1, 0), Duration.ZERO).hashCode(),
            new EdgeImpl(r1, "ee", new Location(0, 1), new Location(1, 0), Duration.ZERO).hashCode()
        );
    }

    @Test
    void testEdgeThrows() {
        final RegionImpl r1 = new RegionImpl();
        assertThrowsExactly(
            IllegalArgumentException.class,
            () -> new EdgeImpl(r1, "", new Location(1, 1), new Location(0, 0), Duration.ZERO)
        );
        assertThrowsExactly(
            IllegalArgumentException.class,
            () -> new EdgeImpl(r1, "", new Location(1, 0), new Location(0, 1), Duration.ZERO)
        );
        assertDoesNotThrow(() -> new EdgeImpl(r1, "", new Location(0, 0), new Location(1, 1), Duration.ZERO));
        EdgeImpl edge = new EdgeImpl(r1, "", new Location(0, 0), new Location(1, 1), Duration.ZERO);
        assertThrowsExactly(IllegalStateException.class, edge::getNodeA);
        assertThrowsExactly(IllegalStateException.class, edge::getNodeB);
        r1.putNode(new NodeImpl(r1, "", new Location(0, 0), Set.of()));
        assertDoesNotThrow(edge::getNodeA);
        assertThrowsExactly(IllegalStateException.class, edge::getNodeB);
        r1.putNode(new NodeImpl(r1, "", new Location(1, 1), Set.of()));
        assertDoesNotThrow(edge::getNodeA);
        assertDoesNotThrow(edge::getNodeB);
    }

    @Test
    void testEdgeToString() {
        final RegionImpl r1 = new RegionImpl();
        assertEquals(
            String.format("EdgeImpl(name='e', locationA=(0, 0), locationB=(1, 1), duration=%s)", Duration.ZERO),
            new EdgeImpl(r1, "e", new Location(0, 0), new Location(1, 1), Duration.ZERO).toString()
        );
        assertEquals(
            String.format("EdgeImpl(name='e', locationA=(1, 0), locationB=(1, 1), duration=%s)", Duration.ofSeconds(42)),
            new EdgeImpl(r1, "e", new Location(1, 0), new Location(1, 1), Duration.ofSeconds(42)).toString()
        );
        assertNotEquals(
            String.format("EdgeImpl(name='e', locationA=(1, 0), locationB=(1, 1), duration=%s)", Duration.ofSeconds(1337)),
            new EdgeImpl(r1, "ee", new Location(1, 0), new Location(1, 1), Duration.ofSeconds(1337)).toString()
        );
        assertEquals(
            String.format("EdgeImpl(name='', locationA=(0, 0), locationB=(10, 0), duration=%s)", Duration.ZERO),
            new EdgeImpl(r1, "", new Location(0, 0), new Location(10, 0), Duration.ZERO).toString()
        );
    }
}
