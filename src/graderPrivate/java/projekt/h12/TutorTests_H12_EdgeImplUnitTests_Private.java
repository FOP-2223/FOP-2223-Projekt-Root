package projekt.h12;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.exceptions.base.MockitoException;
import org.opentest4j.AssertionFailedError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;
import projekt.ComparableUnitTests;
import projekt.ObjectUnitTests;
import projekt.base.Location;
import projekt.delivery.routing.EdgeImpl;
import projekt.delivery.routing.EdgeImplUnitTests;
import projekt.delivery.routing.NodeImpl;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.RegionImpl;
import projekt.solution.TutorComparableUnitTests;
import projekt.solution.TutorEdge;
import projekt.solution.TutorLocation;
import projekt.solution.TutorNode;
import projekt.solution.TutorObjectUnitTests;
import projekt.solution.TutorRegion;
import spoon.reflect.declaration.CtMethod;

import java.lang.reflect.Field;
import java.util.Set;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertNotNull;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertSame;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers.identical;
import static projekt.h12.TutorTests_H12_Private.countInvocations;

@TestForSubmission
@SuppressWarnings("DuplicatedCode")
public class TutorTests_H12_EdgeImplUnitTests_Private {

    @BeforeEach
    public void setUp() {
        TutorObjectUnitTests.reset();
        TutorComparableUnitTests.reset();
    }

    @Test
    public void testRegion() {
        Context context = contextBuilder().subject("EdgeImplUnitTests#initialize").build();

        EdgeImplUnitTests.initialize();

        Region region = EdgeImplUnitTests.nodeA.region;

        assertSame(region, EdgeImplUnitTests.nodeB.region, context,
            TR -> "The region of node B is not the same as the region of node A.");
        assertSame(region, EdgeImplUnitTests.nodeC.region, context,
            TR -> "The region of node C is not the same as the region of node A.");

        assertEquals(3, region.getNodes().size(), context,
            TR -> "The region does not contain four nodes.");
        assertTrue(region.getNodes().contains(EdgeImplUnitTests.nodeA), context,
            TR -> "The region does not contain node A.");
        assertTrue(region.getNodes().contains(EdgeImplUnitTests.nodeB), context,
            TR -> "The region does not contain node B.");
        assertTrue(region.getNodes().contains(EdgeImplUnitTests.nodeC), context,
            TR -> "The region does not contain node C.");

        assertEquals(2, EdgeImplUnitTests.nodeA.connections.size(), context,
            TR -> "The node A does not have two connections.");
        assertTrue(EdgeImplUnitTests.nodeA.connections.contains(EdgeImplUnitTests.nodeA.location), context,
            TR -> "The node A does not have a connection to node A.");
        assertTrue(EdgeImplUnitTests.nodeA.connections.contains(EdgeImplUnitTests.nodeB.location), context,
            TR -> "The node A does not have a connection to node B.");

        assertEquals(2, EdgeImplUnitTests.nodeB.connections.size(), context,
            TR -> "The node B does not have two connections.");
        assertTrue(EdgeImplUnitTests.nodeB.connections.contains(EdgeImplUnitTests.nodeA.location), context,
            TR -> "The node B does not have a connection to node A.");
        assertTrue(EdgeImplUnitTests.nodeB.connections.contains(EdgeImplUnitTests.nodeC.location), context,
            TR -> "The node B does not have a connection to node C.");

        assertEquals(1, EdgeImplUnitTests.nodeC.connections.size(), context,
            TR -> "The node C does not have one connection.");
        assertTrue(EdgeImplUnitTests.nodeC.connections.contains(EdgeImplUnitTests.nodeB.location), context,
            TR -> "The node C does not have a connection to node B.");

        assertNotNull(EdgeImplUnitTests.nodeA.name, context,
            TR -> "The name of node A is null.");
        assertNotNull(EdgeImplUnitTests.nodeB.name, context,
            TR -> "The name of node B is null.");
        assertNotNull(EdgeImplUnitTests.nodeC.name, context,
            TR -> "The name of node C is null.");

        assertNotNull(EdgeImplUnitTests.nodeA.location, context,
            TR -> "The location of node A is null.");
        assertNotNull(EdgeImplUnitTests.nodeB.location, context,
            TR -> "The location of node B is null.");
        assertNotNull(EdgeImplUnitTests.nodeC.location, context,
            TR -> "The location of node C is null.");

        assertSame(region, EdgeImplUnitTests.edgeAA.region, context,
            TR -> "The region of edge AA is not the same as the region of node A.");
        assertSame(region, EdgeImplUnitTests.edgeAB.region, context,
            TR -> "The region of edge AB is not the same as the region of node A.");
        assertSame(region, EdgeImplUnitTests.edgeBC.region, context,
            TR -> "The region of edge BC is not the same as the region of node B.");

        assertEquals(3, region.getEdges().size(), context,
            TR -> "The region does not contain three edges.");
        assertTrue(region.getEdges().contains(EdgeImplUnitTests.edgeAA), context,
            TR -> "The region does not contain edge AA.");
        assertTrue(region.getEdges().contains(EdgeImplUnitTests.edgeAB), context,
            TR -> "The region does not contain edge AB.");
        assertTrue(region.getEdges().contains(EdgeImplUnitTests.edgeBC), context,
            TR -> "The region does not contain edge BC.");

        assertEquals(EdgeImplUnitTests.edgeAA.locationA, EdgeImplUnitTests.nodeA.location, context,
            TR -> "The location A of edge AA is not the same as the location of node A.");
        assertEquals(EdgeImplUnitTests.edgeAA.locationB, EdgeImplUnitTests.nodeA.location, context,
            TR -> "The location B of edge AA is not the same as the location of node A.");
        assertTrue((EdgeImplUnitTests.edgeAB.locationA.equals(EdgeImplUnitTests.nodeA.location) && EdgeImplUnitTests.edgeAB.locationB.equals(EdgeImplUnitTests.nodeB.location)) ||
                (EdgeImplUnitTests.edgeAB.locationB.equals(EdgeImplUnitTests.nodeA.location) && EdgeImplUnitTests.edgeAB.locationA.equals(EdgeImplUnitTests.nodeB.location)), context,
            TR -> "The location A and B of edge AB are not the same as the location of node A and B.");
        assertTrue((EdgeImplUnitTests.edgeBC.locationA.equals(EdgeImplUnitTests.nodeB.location) && EdgeImplUnitTests.edgeBC.locationB.equals(EdgeImplUnitTests.nodeC.location)) ||
                (EdgeImplUnitTests.edgeBC.locationB.equals(EdgeImplUnitTests.nodeB.location) && EdgeImplUnitTests.edgeBC.locationA.equals(EdgeImplUnitTests.nodeC.location)), context,
            TR -> "The location A and B of edge BC are not the same as the location of node B and C.");
    }

    @Test
    public void testInitializeObjectUnitTests() throws ReflectiveOperationException {

        Context context = contextBuilder().subject("EdgeImplUnitTests#initialize").build();

        try (MockedConstruction<Location> mocked = mockConstruction(Location.class, TutorTests_H12_Private::prepareLocation);
             MockedConstruction<RegionImpl> mocked2 = mockConstruction(RegionImpl.class, TutorTests_H12_Private::prepareRegion);
             MockedConstruction<NodeImpl> mocked3 = mockConstruction(NodeImpl.class, TutorTests_H12_Private::prepareNode);
             MockedConstruction<EdgeImpl> mocked4 = mockConstruction(EdgeImpl.class, TutorTests_H12_Private::prepareEdge)) {

            EdgeImplUnitTests edgeImplUnitTests = new EdgeImplUnitTests();
            EdgeImplUnitTests.initialize();

            assertNotNull(TutorObjectUnitTests.lastInstance, context,
                TR -> "No ObjectUnitTest Instance got created.");

            assertSame(TutorObjectUnitTests.lastInstance, getObjectUnitTests(edgeImplUnitTests), context,
                TR -> "The objectUnitTests Attribute is not the last create ObjectUnitTests instance.");

            assertNotNull(TutorObjectUnitTests.testObjectFactory, context,
                TR -> "The testObjectFactory is null.");
            assertNotNull(TutorObjectUnitTests.toString, context,
                TR -> "The toString function is not null.");

            assertEquals(10, TutorObjectUnitTests.testObjectCount, context,
                TR -> "The testObjectCount is not 10.");

            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    int finalI = i;
                    int finalJ = j;

                    TutorEdge edgeI = TutorTests_H12_Private.edgeMap.get((EdgeImpl) TutorObjectUnitTests.testObjectFactory.apply(i));
                    TutorEdge edgeJ = TutorTests_H12_Private.edgeMap.get((EdgeImpl) TutorObjectUnitTests.testObjectFactory.apply(j));

                    if (i == j) {
                        assertEquals(edgeI, edgeJ, context,
                            TR -> "testObjectFactory.apply(i).equals(testObjectFactory.apply(j)) returned false for i = " + finalI + " and j = " + finalJ + "");
                    } else {
                        assertNotEquals(edgeI, edgeJ, context,
                            TR -> "testObjectFactory.apply(i).equals(testObjectFactory.apply(j)) returned true for i = " + finalI + " and j = " + finalJ + "");
                    }
                }
            }
        } catch (MockitoException e) {
            throw new RuntimeException(e.getMessage() + ": " + e.getCause().getMessage());
        }

        TutorRegion region = new TutorRegion();
        String name = "TestNode";
        Location locationA = new TutorLocation(0, 0);
        Location locationB = new TutorLocation(1, 1);
        long duration = 2;
        EdgeImpl edge = new EdgeImpl(region, name, locationA, locationB, duration);
        NodeImpl nodeA = new NodeImpl(region, "NodeA", locationA, Set.of(locationB));
        NodeImpl nodeB = new NodeImpl(region, "NodeB", locationB, Set.of(locationA));

        region.putNode(nodeA);
        region.putNode(nodeB);
        region.putEdge(edge);

        assertEquals("EdgeImpl(" +
            "name='" + name + "'"
            + ", locationA='" + locationA + "'"
            + ", locationB='" + locationB + "'"
            + ", duration='" + duration + "'"
            + ')', TutorObjectUnitTests.toString.apply(edge), context,
            TR -> "toString.apply(edge) does not return the correct value.");
    }

    @Test
    public void testObjectTestMethods() throws NoSuchFieldException, IllegalAccessException {
        Context context = contextBuilder().subject("EdgeImplUnitTests#test*()").build();

        EdgeImplUnitTests edgeImplUnitTests = new EdgeImplUnitTests();

        setObjectUnitTests(edgeImplUnitTests, new TutorObjectUnitTests<>(null, null));

        edgeImplUnitTests.testEquals();
        assertEquals(1, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testEquals() did not call ObjectUnitTests.testEquals() once.");
        assertEquals(0, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testEquals() called ObjectUnitTests.testHashCode().");
        assertEquals(0, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testEquals() called ObjectUnitTests.toString().");
        assertEquals(0, TutorComparableUnitTests.testBiggerThenCount, context,
            TR -> "testEquals() called ComparableUnitTests.testBiggerThen().");
        assertEquals(0, TutorComparableUnitTests.testAsBigAsCount, context,
            TR -> "testEquals() called ComparableUnitTests.testAsBigAs().");
        assertEquals(0, TutorComparableUnitTests.testLessThenCount, context,
            TR -> "testEquals() called ComparableUnitTests.testLessThen().");

        TutorObjectUnitTests.reset();

        edgeImplUnitTests.testHashCode();
        assertEquals(0, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testHashCode() called ObjectUnitTests.testEquals().");
        assertEquals(1, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testHashCode() did not call ObjectUnitTests.testHashCode() once.");
        assertEquals(0, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testHashCode() called ObjectUnitTests.toString().");
        assertEquals(0, TutorComparableUnitTests.testBiggerThenCount, context,
            TR -> "testHashCode() called ComparableUnitTests.testBiggerThen().");
        assertEquals(0, TutorComparableUnitTests.testAsBigAsCount, context,
            TR -> "testHashCode() called ComparableUnitTests.testAsBigAs().");
        assertEquals(0, TutorComparableUnitTests.testLessThenCount, context,
            TR -> "testHashCode() called ComparableUnitTests.testLessThen().");

        TutorObjectUnitTests.reset();

        edgeImplUnitTests.testToString();
        assertEquals(0, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testToString() called ObjectUnitTests.testEquals().");
        assertEquals(0, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testToString() called ObjectUnitTests.testHashCode().");
        assertEquals(1, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testToString() did not call ObjectUnitTests.toString() once.");
        assertEquals(0, TutorComparableUnitTests.testBiggerThenCount, context,
            TR -> "testToString() called ComparableUnitTests.testBiggerThen().");
        assertEquals(0, TutorComparableUnitTests.testAsBigAsCount, context,
            TR -> "testToString() called ComparableUnitTests.testAsBigAs().");
        assertEquals(0, TutorComparableUnitTests.testLessThenCount, context,
            TR -> "testToString() called ComparableUnitTests.testLessThen().");

    }

    @Test
    public void testInitializeComparableUnitTests() throws ReflectiveOperationException {

        Context context = contextBuilder().subject("EdgeImplUnitTests#initialize").build();

        try (MockedConstruction<Location> mocked = mockConstruction(Location.class, TutorTests_H12_Private::prepareLocation);
             MockedConstruction<RegionImpl> mocked2 = mockConstruction(RegionImpl.class, TutorTests_H12_Private::prepareRegion);
             MockedConstruction<NodeImpl> mocked3 = mockConstruction(NodeImpl.class, TutorTests_H12_Private::prepareNode);
             MockedConstruction<EdgeImpl> mocked4 = mockConstruction(EdgeImpl.class, TutorTests_H12_Private::prepareEdge)) {

            EdgeImplUnitTests edgeImplUnitTests = new EdgeImplUnitTests();
            EdgeImplUnitTests.initialize();

            assertNotNull(TutorComparableUnitTests.lastInstance, context,
                TR -> "No ObjectUnitTest Instance got created.");

            assertSame(TutorComparableUnitTests.lastInstance, getComparableUnitTests(edgeImplUnitTests), context,
                TR -> "The comparableUnitTests Attribute is not the last create comparableUnitTests instance.");

            assertNotNull(TutorComparableUnitTests.testObjectFactory, context,
                TR -> "The testObjectFactory is null.");

            assertEquals(10, TutorComparableUnitTests.testObjectCount, context,
                TR -> "The testObjectCount is not 10.");

            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    int finalI = i;
                    int finalJ = j;

                    TutorEdge nodeI = TutorTests_H12_Private.edgeMap.get((EdgeImpl) TutorComparableUnitTests.testObjectFactory.apply(i));
                    TutorEdge nodeJ = TutorTests_H12_Private.edgeMap.get((EdgeImpl) TutorComparableUnitTests.testObjectFactory.apply(j));

                    if (i == j) {
                        assertEquals(0, nodeI.compareTo(nodeJ), context,
                            TR -> "testObjectFactory.apply(i).compareTo(testObjectFactory.apply(j)) is not equal to 0 for i = " + finalI + " and j = " + finalJ + "");
                    } else if (i < j) {
                        assertTrue(nodeI.compareTo(nodeJ) < 0, context,
                            TR -> "testObjectFactory.apply(i).compareTo(testObjectFactory.apply(j)) is not less than zero for i = " + finalI + " and j = " + finalJ + "");
                    } else {
                        assertTrue(nodeI.compareTo(nodeJ) > 0, context,
                            TR -> "testObjectFactory.apply(i).compareTo(testObjectFactory.apply(j)) is not greater than zero for i = " + finalI + " and j = " + finalJ + "");
                    }
                }
            }
        } catch (MockitoException e) {
            throw new RuntimeException(e.getMessage() + ": " + e.getCause().getMessage());
        }
    }

    @Test
    public void testComparableTestMethods() throws NoSuchFieldException, IllegalAccessException {
        Context context = contextBuilder().subject("EdgeImplUnitTests#test*()").build();

        EdgeImplUnitTests edgeImplUnitTests = new EdgeImplUnitTests();

        setComparableUnitTests(edgeImplUnitTests, new TutorComparableUnitTests<>(null));

        edgeImplUnitTests.testBiggerThen();
        assertEquals(0, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testBiggerThen() called ObjectUnitTests.testEquals().");
        assertEquals(0, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testBiggerThen() called ObjectUnitTests.testHashCode().");
        assertEquals(0, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testBiggerThen() called ObjectUnitTests.toString().");
        assertEquals(1, TutorComparableUnitTests.testBiggerThenCount, context,
            TR -> "testBiggerThen() did not call ComparableUnitTests.testBiggerThen() once.");
        assertEquals(0, TutorComparableUnitTests.testAsBigAsCount, context,
            TR -> "testBiggerThen() called ComparableUnitTests.testAsBigAs().");
        assertEquals(0, TutorComparableUnitTests.testLessThenCount, context,
            TR -> "testBiggerThen() called ComparableUnitTests.testLessThen().");

        TutorComparableUnitTests.reset();

        edgeImplUnitTests.testAsBigAs();
        assertEquals(0, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testAsBigAs() called ObjectUnitTests.testEquals().");
        assertEquals(0, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testAsBigAs() called ObjectUnitTests.testHashCode().");
        assertEquals(0, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testAsBigAs() called ObjectUnitTests.toString().");
        assertEquals(0, TutorComparableUnitTests.testBiggerThenCount, context,
            TR -> "testAsBigAs() called ComparableUnitTests.testBiggerThen().");
        assertEquals(1, TutorComparableUnitTests.testAsBigAsCount, context,
            TR -> "testAsBigAs() did not call ComparableUnitTests.testAsBigAs() once.");
        assertEquals(0, TutorComparableUnitTests.testLessThenCount, context,
            TR -> "testAsBigAs() called ComparableUnitTests.testLessThen().");

        TutorComparableUnitTests.reset();

        edgeImplUnitTests.testLessThen();
        assertEquals(0, TutorObjectUnitTests.testEqualsCount, context,
            TR -> "testLessThen() called ObjectUnitTests.testEquals().");
        assertEquals(0, TutorObjectUnitTests.testHashCodeCount, context,
            TR -> "testLessThen() called ObjectUnitTests.testHashCode().");
        assertEquals(0, TutorObjectUnitTests.testToStringCount, context,
            TR -> "testLessThen() called ObjectUnitTests.toString().");
        assertEquals(0, TutorComparableUnitTests.testBiggerThenCount, context,
            TR -> "testLessThen() called ComparableUnitTests.testBiggerThen().");
        assertEquals(0, TutorComparableUnitTests.testAsBigAsCount, context,
            TR -> "testLessThen() called ComparableUnitTests.testAsBigAs().");
        assertEquals(1, TutorComparableUnitTests.testLessThenCount, context,
            TR -> "testLessThen() did not call ComparableUnitTests.testLessThen() once.");
    }

    @Test
    public void testGetNodeFailAANodeA() {
        RegionImpl region = new TutorRegion();

        EdgeImplUnitTests.nodeA = new TutorNode(region, "A", new TutorLocation(0, 0), Set.of(new TutorLocation(1, 0), new TutorLocation(-2, 0)));
        EdgeImplUnitTests.nodeB = new TutorNode(region, "B", new TutorLocation(1, 0), Set.of(new TutorLocation(2, 0), new TutorLocation(0, 0)));
        EdgeImplUnitTests.nodeC = new TutorNode(region, "C", new TutorLocation(2, 0), Set.of(new TutorLocation(1, 0)));

        region.putNode(EdgeImplUnitTests.nodeA);
        region.putNode(EdgeImplUnitTests.nodeB);
        region.putNode(EdgeImplUnitTests.nodeC);
        region.putNode(new TutorNode(region, "D", new TutorLocation(-2, 0), Set.of(new TutorLocation(0, 0))));

        EdgeImplUnitTests.edgeAA = spy(new TutorEdge(region, "AA", new TutorLocation(-2, 0), new TutorLocation(0, 0), 1));
        EdgeImplUnitTests.edgeAB = new TutorEdge(region, "AB", new TutorLocation(0, 0), new TutorLocation(1, 0), 1);
        EdgeImplUnitTests.edgeBC = new TutorEdge(region, "BC", new TutorLocation(1, 0), new TutorLocation(2, 0), 1);

        region.putEdge(EdgeImplUnitTests.edgeAA);
        region.putEdge(EdgeImplUnitTests.edgeAB);
        region.putEdge(EdgeImplUnitTests.edgeBC);

        clearInvocations(EdgeImplUnitTests.edgeAA);

        Assertions.assertThrows(AssertionFailedError.class, () -> new EdgeImplUnitTests().testGetNode(),
            "testGetNode() did not throw an AssertionFailedError when edgeAA.getNodeA() returns the wrong node.");

        verify(EdgeImplUnitTests.edgeAA, times(1)).getNodeA();
    }

    @Test
    public void testGetNodeFailAANodeB() {
        RegionImpl region = new TutorRegion();

        EdgeImplUnitTests.nodeA = new TutorNode(region, "A", new TutorLocation(0, 0), Set.of(new TutorLocation(1, 0), new TutorLocation(0, 2)));
        EdgeImplUnitTests.nodeB = new TutorNode(region, "B", new TutorLocation(1, 0), Set.of(new TutorLocation(2, 0), new TutorLocation(0, 0)));
        EdgeImplUnitTests.nodeC = new TutorNode(region, "C", new TutorLocation(2, 0), Set.of(new TutorLocation(1, 0)));

        region.putNode(EdgeImplUnitTests.nodeA);
        region.putNode(EdgeImplUnitTests.nodeB);
        region.putNode(EdgeImplUnitTests.nodeC);
        region.putNode(new TutorNode(region, "D", new TutorLocation(0, 2), Set.of(new TutorLocation(0, 0))));

        EdgeImplUnitTests.edgeAA = spy(new TutorEdge(region, "AA", new TutorLocation(0, 0), new TutorLocation(0, 2), 1));
        EdgeImplUnitTests.edgeAB = new TutorEdge(region, "AB", new TutorLocation(0, 0), new TutorLocation(1, 0), 1);
        EdgeImplUnitTests.edgeBC = new TutorEdge(region, "BC", new TutorLocation(1, 0), new TutorLocation(2, 0), 1);

        region.putEdge(EdgeImplUnitTests.edgeAA);
        region.putEdge(EdgeImplUnitTests.edgeAB);
        region.putEdge(EdgeImplUnitTests.edgeBC);

        clearInvocations(EdgeImplUnitTests.edgeAA);

        Assertions.assertThrows(AssertionFailedError.class, () -> new EdgeImplUnitTests().testGetNode(),
            "testGetNode() did not throw an AssertionFailedError when edgeAA.getNodeB() returns the wrong node.");

        verify(EdgeImplUnitTests.edgeAA, times(1)).getNodeB();
    }

    @Test
    public void testGetNodeFailABNodeA() {
        RegionImpl region = new TutorRegion();

        EdgeImplUnitTests.nodeA = new TutorNode(region, "A", new TutorLocation(0, 0), Set.of(new TutorLocation(0, 0)));
        EdgeImplUnitTests.nodeB = new TutorNode(region, "B", new TutorLocation(1, 0), Set.of(new TutorLocation(2, 0), new TutorLocation(-2, 0)));
        EdgeImplUnitTests.nodeC = new TutorNode(region, "C", new TutorLocation(2, 0), Set.of(new TutorLocation(1, 0)));

        region.putNode(EdgeImplUnitTests.nodeA);
        region.putNode(EdgeImplUnitTests.nodeB);
        region.putNode(EdgeImplUnitTests.nodeC);
        region.putNode(new TutorNode(region, "D", new TutorLocation(-2, 0), Set.of(new TutorLocation(1, 0))));

        EdgeImplUnitTests.edgeAA = new TutorEdge(region, "AA", new TutorLocation(0, 0), new TutorLocation(0, 0), 1);
        EdgeImplUnitTests.edgeAB = spy(new TutorEdge(region, "AB", new TutorLocation(-2, 0), new TutorLocation(1, 0), 1));
        EdgeImplUnitTests.edgeBC = new TutorEdge(region, "BC", new TutorLocation(1, 0), new TutorLocation(2, 0), 1);

        region.putEdge(EdgeImplUnitTests.edgeAA);
        region.putEdge(EdgeImplUnitTests.edgeAB);
        region.putEdge(EdgeImplUnitTests.edgeBC);

        clearInvocations(EdgeImplUnitTests.edgeAB);

        Assertions.assertThrows(AssertionFailedError.class, () -> new EdgeImplUnitTests().testGetNode(),
            "testGetNode() did not throw an AssertionFailedError when edgeAB.getNodeA() returns the wrong node.");

        verify(EdgeImplUnitTests.edgeAB, times(1)).getNodeA();
    }

    @Test
    public void testGetNodeFailABNodeB() {
        RegionImpl region = new TutorRegion();

        EdgeImplUnitTests.nodeA = new TutorNode(region, "A", new TutorLocation(0, 0), Set.of(new TutorLocation(0, 0), new TutorLocation(0, 2)));
        EdgeImplUnitTests.nodeB = new TutorNode(region, "B", new TutorLocation(1, 0), Set.of(new TutorLocation(2, 0)));
        EdgeImplUnitTests.nodeC = new TutorNode(region, "C", new TutorLocation(2, 0), Set.of(new TutorLocation(1, 0)));

        region.putNode(EdgeImplUnitTests.nodeA);
        region.putNode(EdgeImplUnitTests.nodeB);
        region.putNode(EdgeImplUnitTests.nodeC);
        region.putNode(new TutorNode(region, "D", new TutorLocation(0, 2), Set.of(new TutorLocation(0, 0))));

        EdgeImplUnitTests.edgeAA = new TutorEdge(region, "AA", new TutorLocation(0, 0), new TutorLocation(0, 0), 1);
        EdgeImplUnitTests.edgeAB = spy(new TutorEdge(region, "AB", new TutorLocation(0, 0), new TutorLocation(0, 2), 1));
        EdgeImplUnitTests.edgeBC = new TutorEdge(region, "BC", new TutorLocation(1, 0), new TutorLocation(2, 0), 1);

        region.putEdge(EdgeImplUnitTests.edgeAA);
        region.putEdge(EdgeImplUnitTests.edgeAB);
        region.putEdge(EdgeImplUnitTests.edgeBC);

        clearInvocations(EdgeImplUnitTests.edgeAB);

        Assertions.assertThrows(AssertionFailedError.class, () -> new EdgeImplUnitTests().testGetNode(),
            "testGetNode() did not throw an AssertionFailedError when edgeAB.getNodeB() returns the wrong node.");

        verify(EdgeImplUnitTests.edgeAB, times(1)).getNodeB();
    }

    @Test
    public void testGetNodeFailBCNodeA() {
        RegionImpl region = new TutorRegion();

        EdgeImplUnitTests.nodeA = new TutorNode(region, "A", new TutorLocation(0, 0), Set.of(new TutorLocation(0, 0), new TutorLocation(1, 0), new TutorLocation(2, 0)));
        EdgeImplUnitTests.nodeB = new TutorNode(region, "B", new TutorLocation(1, 0), Set.of(new TutorLocation(0, 0)));
        EdgeImplUnitTests.nodeC = new TutorNode(region, "C", new TutorLocation(2, 0), Set.of(new TutorLocation(0, 0)));

        region.putNode(EdgeImplUnitTests.nodeA);
        region.putNode(EdgeImplUnitTests.nodeB);
        region.putNode(EdgeImplUnitTests.nodeC);

        EdgeImplUnitTests.edgeAA = new TutorEdge(region, "AA", new TutorLocation(0, 0), new TutorLocation(0, 0), 1);
        EdgeImplUnitTests.edgeAB = new TutorEdge(region, "AB", new TutorLocation(0, 0), new TutorLocation(1, 0), 1);
        EdgeImplUnitTests.edgeBC = spy(new TutorEdge(region, "BC", new TutorLocation(0, 0), new TutorLocation(2, 0), 1));

        region.putEdge(EdgeImplUnitTests.edgeAA);
        region.putEdge(EdgeImplUnitTests.edgeAB);
        region.putEdge(EdgeImplUnitTests.edgeBC);

        clearInvocations(EdgeImplUnitTests.edgeBC);

        Assertions.assertThrows(AssertionFailedError.class, () -> new EdgeImplUnitTests().testGetNode(),
            "testGetNode() did not throw an AssertionFailedError when edgeBC.getNodeA() returns the wrong node.");

        verify(EdgeImplUnitTests.edgeBC, times(1)).getNodeA();
    }

    @Test
    public void testGetNodeFailBCNodeB() {
        RegionImpl region = new TutorRegion();

        EdgeImplUnitTests.nodeA = new TutorNode(region, "A", new TutorLocation(0, 0), Set.of(new TutorLocation(0, 0), new TutorLocation(1, 0)));
        EdgeImplUnitTests.nodeB = new TutorNode(region, "B", new TutorLocation(1, 0), Set.of(new TutorLocation(2, 2), new TutorLocation(0, 0)));
        EdgeImplUnitTests.nodeC = new TutorNode(region, "C", new TutorLocation(2, 0), Set.of());

        region.putNode(EdgeImplUnitTests.nodeA);
        region.putNode(EdgeImplUnitTests.nodeB);
        region.putNode(EdgeImplUnitTests.nodeC);
        region.putNode(new TutorNode(region, "D", new TutorLocation(2, 2), Set.of(new TutorLocation(1, 0))));

        EdgeImplUnitTests.edgeAA = new TutorEdge(region, "AA", new TutorLocation(0, 0), new TutorLocation(0, 0), 1);
        EdgeImplUnitTests.edgeAB = new TutorEdge(region, "AB", new TutorLocation(0, 0), new TutorLocation(1, 0), 1);
        EdgeImplUnitTests.edgeBC = spy(new TutorEdge(region, "BC", new TutorLocation(1, 0), new TutorLocation(2, 2), 1));

        region.putEdge(EdgeImplUnitTests.edgeAA);
        region.putEdge(EdgeImplUnitTests.edgeAB);
        region.putEdge(EdgeImplUnitTests.edgeBC);

        clearInvocations(EdgeImplUnitTests.edgeBC);

        Assertions.assertThrows(AssertionFailedError.class, () -> new EdgeImplUnitTests().testGetNode(),
            "testGetNode() did not throw an AssertionFailedError when edgeBC.getNodeB() returns the wrong node.");

        verify(EdgeImplUnitTests.edgeBC, times(1)).getNodeB();
    }

    @Test
    public void testGetNodeSuccess() {
        RegionImpl region = new TutorRegion();

        EdgeImplUnitTests.nodeA = new TutorNode(region, "A", new TutorLocation(0, 0), Set.of(new TutorLocation(0, 0), new TutorLocation(1, 0)));
        EdgeImplUnitTests.nodeB = new TutorNode(region, "B", new TutorLocation(1, 0), Set.of(new TutorLocation(2, 0), new TutorLocation(0, 0)));
        EdgeImplUnitTests.nodeC = new TutorNode(region, "C", new TutorLocation(2, 0), Set.of(new TutorLocation(1, 0)));

        region.putNode(EdgeImplUnitTests.nodeA);
        region.putNode(EdgeImplUnitTests.nodeB);
        region.putNode(EdgeImplUnitTests.nodeC);

        EdgeImplUnitTests.edgeAA = spy(new TutorEdge(region, "AA", new TutorLocation(0, 0), new TutorLocation(0, 0), 1));
        EdgeImplUnitTests.edgeAB = spy(new TutorEdge(region, "AB", new TutorLocation(0, 0), new TutorLocation(1, 0), 1));
        EdgeImplUnitTests.edgeBC = spy(new TutorEdge(region, "BC", new TutorLocation(1, 0), new TutorLocation(2, 0), 1));

        region.putEdge(EdgeImplUnitTests.edgeAA);
        region.putEdge(EdgeImplUnitTests.edgeAB);
        region.putEdge(EdgeImplUnitTests.edgeBC);

        clearInvocations(EdgeImplUnitTests.edgeAA);
        clearInvocations(EdgeImplUnitTests.edgeAB);
        clearInvocations(EdgeImplUnitTests.edgeBC);

        Assertions.assertDoesNotThrow(() -> new EdgeImplUnitTests().testGetNode(),
            "testGetNode() threw an Exception when all methods return the expected values.");

        verify(EdgeImplUnitTests.edgeAA, times(1)).getNodeA();
        verify(EdgeImplUnitTests.edgeAA, times(1)).getNodeB();
        verify(EdgeImplUnitTests.edgeAB, times(1)).getNodeA();
        verify(EdgeImplUnitTests.edgeAB, times(1)).getNodeB();
        verify(EdgeImplUnitTests.edgeBC, times(1)).getNodeA();
        verify(EdgeImplUnitTests.edgeBC, times(1)).getNodeB();
    }

    @Test
    public void testAssertionCount() {
        Context context = contextBuilder().subject("EdgeImplUnitTests#testGetNode").build();

        CtMethod<?> method = ((BasicMethodLink) BasicTypeLink.of(EdgeImplUnitTests.class).getMethod(identical("testGetNode"))).getCtElement();


        assertEquals(6, countInvocations(method, (name, target) -> name.startsWith("assert") && target.startsWith("org.junit")), context,
            TR -> "testGetNode() does not contain the correct number of assert*() calls.");
    }

    @SuppressWarnings("unchecked")
    public static ObjectUnitTests<NodeImpl> getObjectUnitTests(EdgeImplUnitTests edgeImplUnitTests) throws ReflectiveOperationException {
        Field field = EdgeImplUnitTests.class.getDeclaredField("objectUnitTests");
        field.setAccessible(true);
        return (ObjectUnitTests<NodeImpl>) field.get(edgeImplUnitTests);
    }

    @SuppressWarnings("unchecked")
    public static ComparableUnitTests<NodeImpl> getComparableUnitTests(EdgeImplUnitTests edgeImplUnitTests) throws ReflectiveOperationException {
        Field field = EdgeImplUnitTests.class.getDeclaredField("comparableUnitTests");
        field.setAccessible(true);
        return (ComparableUnitTests<NodeImpl>) field.get(edgeImplUnitTests);
    }

    public static void setObjectUnitTests(EdgeImplUnitTests edgeImplUnitTests, ObjectUnitTests<Location> objectUnitTests) throws NoSuchFieldException, IllegalAccessException {
        Field objectUnitTestsField = EdgeImplUnitTests.class.getDeclaredField("objectUnitTests");
        objectUnitTestsField.setAccessible(true);
        objectUnitTestsField.set(edgeImplUnitTests, objectUnitTests);
    }

    public static void setComparableUnitTests(EdgeImplUnitTests edgeImplUnitTests, ComparableUnitTests<Location> comparableUnitTests) throws NoSuchFieldException, IllegalAccessException {
        Field objectUnitTestsField = EdgeImplUnitTests.class.getDeclaredField("comparableUnitTests");
        objectUnitTestsField.setAccessible(true);
        objectUnitTestsField.set(edgeImplUnitTests, comparableUnitTests);
    }

}
