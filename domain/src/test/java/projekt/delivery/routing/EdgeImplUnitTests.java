package projekt.delivery.routing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import projekt.ComparableUnitTests;
import projekt.ObjectUnitTests;
import projekt.base.Location;

import java.util.Set;
import java.util.function.Function;

public class EdgeImplUnitTests {

    private static ComparableUnitTests<Region.Edge> comparableUnitTests;
    private static ObjectUnitTests<Region.Edge> objectUnitTests;
    private static NodeImpl nodeA;
    private static NodeImpl nodeB;
    private static NodeImpl nodeC;

    private static EdgeImpl edgeAA;
    private static EdgeImpl edgeAB;
    private static EdgeImpl edgeBC;

    @BeforeAll
    public static void initialize() {

        RegionImpl region = new RegionImpl();

        nodeA = new NodeImpl(region, "A", new Location(0, 0), Set.of(new Location(0,0), new Location(1,0)));
        nodeB = new NodeImpl(region, "B", new Location(1, 0), Set.of(new Location(2,0), new Location(0, 0)));
        nodeC = new NodeImpl(region, "C", new Location(2, 0), Set.of(new Location(1, 0)));

        region.putNode(nodeA);
        region.putNode(nodeB);
        region.putNode(nodeC);

        edgeAA = new EdgeImpl(region, "AA", new Location(0,0), new Location(0, 0), 1);
        edgeAB = new EdgeImpl(region, "AB", new Location(0,0), new Location(1, 0), 1);
        edgeBC = new EdgeImpl(region, "BC", new Location(1,0), new Location(2, 0), 1);

        region.putEdge(edgeAA);
        region.putEdge(edgeAB);
        region.putEdge(edgeBC);


        Function<Integer, Region.Edge> testObjectFactory = i -> {
            RegionImpl region2 = new RegionImpl();
            EdgeImpl edge;
            if (i < 5) {
                region2.putNode(new NodeImpl(region2, "A", new Location(0, i), Set.of(new Location(0, i + 1))));
                region2.putNode(new NodeImpl(region2, "B", new Location(0, i + 1), Set.of(new Location(0, i))));
                edge = new EdgeImpl(region2, "edge: " + i, new Location(0, i), new Location(0, i + 1), 1);

            } else if (i < 10){
                region2.putNode(new NodeImpl(region2, "A", new Location(i - 5, i), Set.of(new Location(i - 5, i + 1))));
                region2.putNode(new NodeImpl(region2, "B", new Location(i - 5, i + 1), Set.of(new Location(i - 5, i))));
                edge = new EdgeImpl(region2, "edge: " + i, new Location(i - 5, i), new Location(i - 5, i + 1), 1);
            } else {
                region2.putNode(new NodeImpl(region2, "A", new Location(i, 0), Set.of(new Location(i, 1))));
                region2.putNode(new NodeImpl(region2, "B", new Location(i, 1), Set.of(new Location(i, 0))));
                edge = new EdgeImpl(region2, "edge: " + i, new Location(i, 0), new Location(i, 1), 1);
            }

            region2.putEdge(edge);
            return edge;
        };

        objectUnitTests = new ObjectUnitTests<>(testObjectFactory, o ->
            "EdgeImpl(" +
                "name='" + o.getName() + '\'' +
                ", locationA='" + o.getNodeA().getLocation() + "'" +
                ", locationB='" + o.getNodeB().getLocation() + "'" +
                ", duration='" + o.getDuration() + "'" +
                ')');
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
    public void testGetNode() {
        Assertions.assertEquals(edgeAA.getNodeA(), nodeA);
        Assertions.assertEquals(edgeAA.getNodeB(), nodeA);

        Assertions.assertEquals(edgeAB.getNodeA(), nodeA);
        Assertions.assertEquals(edgeAB.getNodeB(), nodeB);

        Assertions.assertEquals(edgeBC.getNodeA(), nodeB);
        Assertions.assertEquals(edgeBC.getNodeB(), nodeC);
    }
}
