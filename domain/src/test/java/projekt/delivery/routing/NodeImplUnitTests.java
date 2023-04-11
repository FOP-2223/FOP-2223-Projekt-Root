package projekt.delivery.routing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import projekt.ComparableUnitTests;
import projekt.ObjectUnitTests;
import projekt.base.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class NodeImplUnitTests {

    public static ComparableUnitTests<NodeImpl> comparableUnitTests;
    public static ObjectUnitTests<NodeImpl> objectUnitTests;
    public static NodeImpl nodeA;
    public static NodeImpl nodeB;
    public static NodeImpl nodeC;
    public static NodeImpl nodeD;

    public static EdgeImpl edgeAA;
    public static EdgeImpl edgeAB;
    public static EdgeImpl edgeBC;

    @BeforeAll
    public static void initialize() {

        RegionImpl region = new RegionImpl();

        nodeA = new NodeImpl(region, "A", new Location(0, 0), Set.of(new Location(0,0), new Location(1,0)));
        nodeB = new NodeImpl(region, "B", new Location(1, 0), Set.of(new Location(2,0), new Location(0, 0)));
        nodeC = new NodeImpl(region, "C", new Location(2, 0), Set.of(new Location(1, 0)));
        nodeD = new NodeImpl(region, "D", new Location(3, 0), Set.of());

        region.putNode(nodeA);
        region.putNode(nodeB);
        region.putNode(nodeC);
        region.putNode(nodeD);

        edgeAA = new EdgeImpl(region, "AA", new Location(0,0), new Location(0, 0), 1);
        edgeAB = new EdgeImpl(region, "AB", new Location(0,0), new Location(1, 0), 1);
        edgeBC = new EdgeImpl(region, "BC", new Location(1,0), new Location(2, 0), 1);

        region.putEdge(edgeAA);
        region.putEdge(edgeAB);
        region.putEdge(edgeBC);

        Function<Integer, NodeImpl> testObjectFactory = i -> {
            RegionImpl region2 = new RegionImpl();
            NodeImpl node;
            if (i < 5) {
                node = new NodeImpl(region2, "node: " + i, new Location(0, i), new HashSet<>());
            } else if (i < 10){
                node = new NodeImpl(region2, "node: " + i, new Location(i - 5, i), new HashSet<>());
            } else {
                node = new NodeImpl(region2, "node: " + i, new Location(i, 0), new HashSet<>());
            }

            region2.putNode(node);
            return node;
        };

        objectUnitTests = new ObjectUnitTests<>(testObjectFactory, o ->
            "NodeImpl(name='" + o.getName() + "'"
                + ", location='" + o.getLocation() + "'"
                + ", connections='" + o.getConnections() + "'"
                + ')');
        comparableUnitTests = new ComparableUnitTests<>(testObjectFactory);

        objectUnitTests.initialize(10);
        comparableUnitTests.initialize(10);
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
    public void testToString() {
        objectUnitTests.testToString();
    }

    @Test
    public void testBiggerThen() {
        comparableUnitTests.testBiggerThen();
    }

    @Test
    public void testAsBigAs() {
        comparableUnitTests.testAsBigAs();
    }

    @Test
    public void testLessThen() {
        comparableUnitTests.testLessThen();
    }

    @Test
    public void testGetEdge() {
        Assertions.assertEquals(edgeAA, nodeA.getEdge(nodeA));
        Assertions.assertEquals(edgeAB, nodeA.getEdge(nodeB));
        Assertions.assertNull(nodeA.getEdge(nodeC));
        Assertions.assertNull(nodeA.getEdge(nodeD));
    }

    @Test
    public void testAdjacentNodes() {
        Assertions.assertEquals(Set.of(nodeA, nodeB), nodeA.getAdjacentNodes());
        Assertions.assertEquals(Set.of(nodeA, nodeC), nodeB.getAdjacentNodes());
        Assertions.assertEquals(Set.of(nodeB), nodeC.getAdjacentNodes());
        Assertions.assertEquals(Set.of(), nodeD.getAdjacentNodes());
    }

    @Test
    public void testAdjacentEdges() {
        Assertions.assertEquals(Set.of(edgeAA, edgeAB), nodeA.getAdjacentEdges());
        Assertions.assertEquals(Set.of(edgeAB, edgeBC), nodeB.getAdjacentEdges());
        Assertions.assertEquals(Set.of(edgeBC), nodeC.getAdjacentEdges());
        Assertions.assertEquals(Set.of(), nodeD.getAdjacentEdges());
    }
}
