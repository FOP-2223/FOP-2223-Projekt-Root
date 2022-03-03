package projekt.delivery.routing;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import projekt.base.EuclideanDistanceCalculator;
import projekt.base.Location;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class RegionTest {

    @Test
    void testRegionSimple() {
        final RegionImpl region = new RegionImpl();
        final NodeImpl nodeA = new NodeImpl(
            region,
            "nodeA",
            new Location(2, 3),
            Set.of(
                new Location(5, 6),
                new Location(7, 8)
            )
        );
        final NodeImpl nodeB = new NodeImpl(
            region,
            "nodeB",
            new Location(5, 6),
            Set.of(
                new Location(2, 3),
                new Location(7, 8)
            )
        );
        final NodeImpl nodeC = new NodeImpl(
            region,
            "nodeC",
            new Location(7, 8),
            Set.of(
                new Location(2, 3),
                new Location(5, 6)
            )
        );
        final EdgeImpl edge1 = new EdgeImpl(
            region,
            "edge1",
            new Location(2, 3),
            new Location(5, 6),
            Duration.ofMinutes(5)
        );
        final EdgeImpl edge2 = new EdgeImpl(
            region,
            "edge2",
            new Location(2, 3),
            new Location(7, 8),
            Duration.ofMinutes(3)
        );
        final EdgeImpl edge3 = new EdgeImpl(
            region,
            "edge3",
            new Location(5, 6),
            new Location(7, 8),
            Duration.ofMinutes(4)
        );
        region.putNode(nodeA);
        region.putNode(nodeB);
        region.putNode(nodeC);
        region.putEdge(edge1);
        region.putEdge(edge2);
        region.putEdge(edge3);
        assertEquals("nodeA", nodeA.getName());
        assertEquals("nodeB", nodeB.getName());
        assertEquals("nodeC", nodeC.getName());
        assertEquals("edge1", edge1.getName());
        assertEquals("edge2", edge2.getName());
        assertEquals("edge3", edge3.getName());
        assertEquals(nodeA, region.getNode(new Location(2, 3)));
        assertEquals(nodeB, region.getNode(new Location(5, 6)));
        assertEquals(nodeC, region.getNode(new Location(7, 8)));
        assertEquals(edge1, region.getEdge(new Location(2, 3), new Location(5, 6)));
        assertEquals(edge1, region.getEdge(new Location(5, 6), new Location(2, 3)));
        assertEquals(edge2, region.getEdge(new Location(2, 3), new Location(7, 8)));
        assertEquals(edge2, region.getEdge(new Location(7, 8), new Location(2, 3)));
        assertEquals(edge3, region.getEdge(new Location(5, 6), new Location(7, 8)));
        assertEquals(edge3, region.getEdge(new Location(7, 8), new Location(5, 6)));
        assertEquals(List.of(nodeA, nodeB, nodeC), region.getNodes().stream().sorted().toList());
        assertEquals(List.of(edge1, edge2, edge3), region.getEdges().stream().sorted().toList());
        assertThrows(UnsupportedOperationException.class, () -> region.getNodes().clear());
        assertThrows(UnsupportedOperationException.class, () -> region.getNodes().add(
            new NodeImpl(region, "gude", new Location(1, 1), Set.of())
        ));
        assertThrows(UnsupportedOperationException.class, () -> region.getEdges().clear());
        assertThrows(UnsupportedOperationException.class, () -> region.getEdges().add(
            new EdgeImpl(region, "moin", new Location(1, 1), new Location(1, 2), Duration.ZERO)
        ));
    }

    @Test
    void testRegionBuilderBasic() {
        final Region.Builder builder = Region.builder();
        assertNotNull(builder);
        assertEquals(builder, builder.addNode("a", new Location(2, 3)));
        assertEquals(builder, builder.addNeighborhood("b", new Location(5, 6), 5.0));
        assertEquals(builder, builder.addEdge("c", new Location(2, 3), new Location(4, 5)));
        // loops allowed
        assertEquals(builder, builder.addEdge("d", new Location(1, 2), new Location(1, 2)));
    }

    @Test
    void testRegionBuilderNodeThrows() {
        final Region.Builder builder = Region.builder();
        builder.addNode("a", new Location(2, 3));
        assertEquals("Duplicate name 'a'",
            assertThrows(IllegalArgumentException.class,
                () -> builder.addNode("a", new Location(2, 3))).getMessage()
        );
        assertEquals("Duplicate node at location (2, 3)",
            assertThrows(IllegalArgumentException.class,
                () -> builder.addNode("b", new Location(2, 3))).getMessage()
        );
        assertEquals("Duplicate node at location (2, 3)",
            assertThrows(IllegalArgumentException.class,
                () -> builder.addNeighborhood("b", new Location(2, 3), 0.0)).getMessage()
        );
    }

    @Test
    void testRegionBuilderEdgeThrows() {
        final Region.Builder builder = Region.builder();
        builder.addEdge("a", new Location(2, 3), new Location(4, 5));
        assertEquals("Duplicate name 'a'",
            assertThrows(IllegalArgumentException.class,
                () -> builder.addEdge("a", new Location(2, 3), new Location(4, 5))).getMessage()
        );
        assertEquals("Duplicate edge connecting (2, 3) to (4, 5)",
            assertThrows(IllegalArgumentException.class,
                () -> builder.addEdge("b", new Location(2, 3), new Location(4, 5))).getMessage()
        );
        assertEquals("Duplicate edge connecting (2, 3) to (4, 5)",
            assertThrows(IllegalArgumentException.class,
                () -> builder.addEdge("b", new Location(4, 5), new Location(2, 3))).getMessage()
        );
    }

    /**
     * Tests that multiple calls to builder.build() work correctly.
     */
    @Test
    void testRegionBuilderMultiple() {
        final Region.Builder builder = Region.builder()
            .addNode("a", new Location(1, 1))
            .addNeighborhood("b", new Location(2, 2), 0.5)
            .addEdge("e1", new Location(1, 1), new Location(2, 2))
            .distanceCalculator(new EuclideanDistanceCalculator());
        Region r1 = assertDoesNotThrow(builder::build);
        Region r2 = assertDoesNotThrow(builder::build);
        assertArrayEquals(r1.getNodes().toArray(), r2.getNodes().toArray());
        assertArrayEquals(r1.getEdges().toArray(), r2.getEdges().toArray());
        assertEquals(r1, r2);
    }

    @Test
    void testRegionBuilderMissingNodeThrows() {
        final Region.Builder builder = Region.builder();
    }

    @Test
    void testRegionBuilderComplex() {
        final Map<String, Location> nodes = Map.of(
            "nodeA", new Location(2, 3),
            "nodeB", new Location(4, 5),
            "nodeC", new Location(6, 7),
            "nodeD", new Location(8, 9)
        );
        final Map<String, LocationTuple> edges = Map.of(
            "edgeA-B", new LocationTuple(new Location(2, 3), new Location(4, 5)),
            "edgeB-C", new LocationTuple(new Location(4, 5), new Location(6, 7)),
            "edgeC-D", new LocationTuple(new Location(6, 7), new Location(8, 9)),
            "edgeD-A", new LocationTuple(new Location(8, 9), new Location(2, 3))
        );
        final Region.Builder builder = Region.builder();
        nodes.forEach(builder::addNode);
        int i = 0;
        for (Map.Entry<String, LocationTuple> entry : edges.entrySet()) {
            final LocationTuple loc = entry.getValue();
            builder.addEdge(entry.getKey(), loc.a, loc.b);
        }
        // build region
        builder.distanceCalculator(new EuclideanDistanceCalculator());
        final Region region = builder.build();
        for (Map.Entry<String, Location> entry : nodes.entrySet()) {
            final Region.Node node = region.getNodes().stream().filter(Region.Component.named(entry.getKey()))
                .findAny().orElseThrow(AssertionFailedError::new);
            assertEquals(entry.getKey(), node.getName());
            assertEquals(node, region.getNode(entry.getValue()));
        }
        int j = 0;
        for (Map.Entry<String, LocationTuple> entry : edges.entrySet()) {
            final Region.Edge edge = region.getEdges().stream().filter(Region.Component.named(entry.getKey()))
                .findAny().orElseThrow(AssertionFailedError::new);
            assertEquals(entry.getKey(), edge.getName());
            final LocationTuple loc = entry.getValue();
            assertEquals(edge, region.getEdge(loc.a, loc.b));
            assertEquals(edge, region.getEdge(loc.b, loc.a));
        }
    }
}
