package projekt.delivery.routing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import projekt.ObjectUnitTests;
import projekt.base.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;


public class RegionImplUnitTests {

    private static ObjectUnitTests<Region> objectUnitTests;

    @BeforeAll
    public static void initialize() {

        Function<Integer, Region> testObjectFactory = i -> {
            RegionImpl region = new RegionImpl();

            NodeImpl A;
            NodeImpl B;
            EdgeImpl AB;

            if (i == 0) {
                A = new NodeImpl(region, "node: " + i, new Location(i,i), Set.of(new Location(-1, -1)));
                B = new NodeImpl(region, "node: " + i, new Location(-1, -1), Set.of(new Location(i,i)));

                AB = new EdgeImpl(region, "edge: " + i, B.getLocation(), A.getLocation(), 1);
            } else {
                A = new NodeImpl(region, "node: " + i, new Location(i,i), Set.of(new Location(2 * i, 2 * i)));
                B = new NodeImpl(region, "node: " + i, new Location(2 * i, 2 * i), Set.of(new Location(i,i)));

                AB = new EdgeImpl(region, "edge: " + i, A.getLocation(), B.getLocation(), 1);
            }


            region.putNode(A);
            region.putNode(B);
            region.putEdge(AB);

            return region;
        };

        objectUnitTests = new ObjectUnitTests<>(testObjectFactory, null);

        objectUnitTests.initialize(10);
    }

    @Test
    public void testEquals() {
        objectUnitTests.testEquals();
    }

    @Test
    public void testHashCode() {
        objectUnitTests.testHashCode();
    }

    @Test
    public void testNodes() {
        RegionImpl region = new RegionImpl();

        NodeImpl A = new NodeImpl(region, "A", new Location(0,0), new HashSet<>());
        NodeImpl B = new NodeImpl(region, "B", new Location(1,0), new HashSet<>());
        NodeImpl C = new NodeImpl(region, "C", new Location(2,0), new HashSet<>());

        region.putNode(A);
        region.putNode(B);
        region.putNode(C);

        Assertions.assertTrue(region.getNodes().contains(A));
        Assertions.assertTrue(region.getNodes().contains(B));
        Assertions.assertTrue(region.getNodes().contains(C));

        Assertions.assertSame(region.getNode(new Location(0, 0)), A);
        Assertions.assertSame(region.getNode(new Location(1, 0)), B);
        Assertions.assertSame(region.getNode(new Location(2, 0)), C);

        Assertions.assertNull(region.getNode(new Location(4, 0)));

        RegionImpl region2 = new RegionImpl();

        NodeImpl D = new NodeImpl(region2, "D", new Location(4,0), new HashSet<>());

        Assertions.assertThrows(IllegalArgumentException.class, () -> region.putNode(D), "Node D has incorrect region");
    }

    @Test
    public void testEdges() {

        RegionImpl region = new RegionImpl();

        NodeImpl A = new NodeImpl(region, "A", new Location(0,0), Set.of(new Location(0, 0), new Location(1, 0)));
        NodeImpl B = new NodeImpl(region, "B", new Location(1,0), Set.of(new Location(0, 0), new Location(2, 0)));
        NodeImpl C = new NodeImpl(region, "C", new Location(2,0), Set.of(new Location(1, 0)));

        region.putNode(A);
        region.putNode(B);
        region.putNode(C);

        EdgeImpl AA = new EdgeImpl(region, "AA", new Location(0,0), new Location(0, 0), 1);
        EdgeImpl AB = new EdgeImpl(region, "AB", new Location(0,0), new Location(1, 0), 1);
        EdgeImpl BC = new EdgeImpl(region, "BC", new Location(1,0), new Location(2, 0), 1);

        region.putEdge(AA);
        region.putEdge(AB);
        region.putEdge(BC);

        Assertions.assertTrue(region.getEdges().contains(AA));
        Assertions.assertTrue(region.getEdges().contains(AB));
        Assertions.assertTrue(region.getEdges().contains(BC));

        Assertions.assertSame(region.getEdge(A, A), AA);
        Assertions.assertSame(region.getEdge(A, B), AB);
        Assertions.assertSame(region.getEdge(B, C), BC);

        Assertions.assertNull(region.getEdge(A,C));

        RegionImpl region2 = new RegionImpl();

        EdgeImpl AD = new EdgeImpl(region, "AD", new Location(0,0), new Location(4, 0), 1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> region.putEdge(AD), "NodeB (4, 0) has incorrect region");

        EdgeImpl EA = new EdgeImpl(region, "EA", new Location(-1,0), new Location(0, 0), 1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> region.putEdge(EA), "NodeA (-1, 0) has incorrect region");

        EdgeImpl AC = new EdgeImpl(region2, "AC", new Location(0, 0), new Location(2, 0), 1);

        Assertions.assertThrows(IllegalArgumentException.class, () -> region.putEdge(AC), "Edge AC has incorrect region");
    }
}
