package projekt.h12;

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
import projekt.delivery.routing.NodeImpl;
import projekt.delivery.routing.NodeImplUnitTests;
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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
public class TutorTests_H12_NodeImplUnitTests_Private {

    @BeforeEach
    public void setUp() {
        TutorObjectUnitTests.reset();
        TutorComparableUnitTests.reset();
    }

    @Test
    public void testRegion() {
        Context context = contextBuilder().subject("NodeImplUnitTests#initialize").build();

        NodeImplUnitTests.initialize();

        Region region = NodeImplUnitTests.nodeA.region;

        assertSame(region, NodeImplUnitTests.nodeB.region, context,
            TR -> "The region of node B is not the same as the region of node A.");
        assertSame(region, NodeImplUnitTests.nodeC.region, context,
            TR -> "The region of node C is not the same as the region of node A.");
        assertSame(region, NodeImplUnitTests.nodeD.region, context,
            TR -> "The region of node D is not the same as the region of node A.");

        assertEquals(4, region.getNodes().size(), context,
            TR -> "The region does not contain four nodes.");
        assertTrue(region.getNodes().contains(NodeImplUnitTests.nodeA), context,
            TR -> "The region does not contain node A.");
        assertTrue(region.getNodes().contains(NodeImplUnitTests.nodeB), context,
            TR -> "The region does not contain node B.");
        assertTrue(region.getNodes().contains(NodeImplUnitTests.nodeC), context,
            TR -> "The region does not contain node C.");
        assertTrue(region.getNodes().contains(NodeImplUnitTests.nodeD), context,
            TR -> "The region does not contain node D.");

        assertEquals(2, NodeImplUnitTests.nodeA.connections.size(), context,
            TR -> "The node A does not have two connections.");
        assertTrue(NodeImplUnitTests.nodeA.connections.contains(NodeImplUnitTests.nodeA.location), context,
            TR -> "The node A does not have a connection to node A.");
        assertTrue(NodeImplUnitTests.nodeA.connections.contains(NodeImplUnitTests.nodeB.location), context,
            TR -> "The node A does not have a connection to node B.");

        assertEquals(2, NodeImplUnitTests.nodeB.connections.size(), context,
            TR -> "The node B does not have two connections.");
        assertTrue(NodeImplUnitTests.nodeB.connections.contains(NodeImplUnitTests.nodeA.location), context,
            TR -> "The node B does not have a connection to node A.");
        assertTrue(NodeImplUnitTests.nodeB.connections.contains(NodeImplUnitTests.nodeC.location), context,
            TR -> "The node B does not have a connection to node C.");

        assertEquals(1, NodeImplUnitTests.nodeC.connections.size(), context,
            TR -> "The node C does not have one connection.");
        assertTrue(NodeImplUnitTests.nodeC.connections.contains(NodeImplUnitTests.nodeB.location), context,
            TR -> "The node C does not have a connection to node B.");

        assertEquals(0, NodeImplUnitTests.nodeD.connections.size(), context,
            TR -> "The node D does not have zero connections.");

        assertNotNull(NodeImplUnitTests.nodeA.name, context,
            TR -> "The name of node A is null.");
        assertNotNull(NodeImplUnitTests.nodeB.name, context,
            TR -> "The name of node B is null.");
        assertNotNull(NodeImplUnitTests.nodeC.name, context,
            TR -> "The name of node C is null.");
        assertNotNull(NodeImplUnitTests.nodeD.name, context,
            TR -> "The name of node D is null.");

        assertNotNull(NodeImplUnitTests.nodeA.location, context,
            TR -> "The location of node A is null.");
        assertNotNull(NodeImplUnitTests.nodeB.location, context,
            TR -> "The location of node B is null.");
        assertNotNull(NodeImplUnitTests.nodeC.location, context,
            TR -> "The location of node C is null.");
        assertNotNull(NodeImplUnitTests.nodeD.location, context,
            TR -> "The location of node D is null.");

        assertSame(region, NodeImplUnitTests.edgeAA.region, context,
            TR -> "The region of edge AA is not the same as the region of node A.");
        assertSame(region, NodeImplUnitTests.edgeAB.region, context,
            TR -> "The region of edge AB is not the same as the region of node A.");
        assertSame(region, NodeImplUnitTests.edgeBC.region, context,
            TR -> "The region of edge BC is not the same as the region of node B.");

        assertEquals(3, region.getEdges().size(), context,
            TR -> "The region does not contain three edges.");
        assertTrue(region.getEdges().contains(NodeImplUnitTests.edgeAA), context,
            TR -> "The region does not contain edge AA.");
        assertTrue(region.getEdges().contains(NodeImplUnitTests.edgeAB), context,
            TR -> "The region does not contain edge AB.");
        assertTrue(region.getEdges().contains(NodeImplUnitTests.edgeBC), context,
            TR -> "The region does not contain edge BC.");

        assertEquals(NodeImplUnitTests.edgeAA.locationA, NodeImplUnitTests.nodeA.location, context,
            TR -> "The location A of edge AA is not the same as the location of node A.");
        assertEquals(NodeImplUnitTests.edgeAA.locationB, NodeImplUnitTests.nodeA.location, context,
            TR -> "The location B of edge AA is not the same as the location of node A.");
        assertTrue((NodeImplUnitTests.edgeAB.locationA.equals(NodeImplUnitTests.nodeA.location) && NodeImplUnitTests.edgeAB.locationB.equals(NodeImplUnitTests.nodeB.location)) ||
                (NodeImplUnitTests.edgeAB.locationB.equals(NodeImplUnitTests.nodeA.location) && NodeImplUnitTests.edgeAB.locationA.equals(NodeImplUnitTests.nodeB.location)), context,
            TR -> "The location A and B of edge AB are not the same as the location of node A and B.");
        assertTrue((NodeImplUnitTests.edgeBC.locationA.equals(NodeImplUnitTests.nodeB.location) && NodeImplUnitTests.edgeBC.locationB.equals(NodeImplUnitTests.nodeC.location)) ||
                (NodeImplUnitTests.edgeBC.locationB.equals(NodeImplUnitTests.nodeB.location) && NodeImplUnitTests.edgeBC.locationA.equals(NodeImplUnitTests.nodeC.location)), context,
            TR -> "The location A and B of edge BC are not the same as the location of node B and C.");
    }

    @Test
    public void testInitializeObjectUnitTests() throws ReflectiveOperationException {

        Context context = contextBuilder().subject("NodeImplUnitTests#initialize").build();

        try (MockedConstruction<Location> mocked = mockConstruction(Location.class, TutorTests_H12_Private::prepareLocation);
             MockedConstruction<RegionImpl> mocked2 = mockConstruction(RegionImpl.class, TutorTests_H12_Private::prepareRegion);
             MockedConstruction<NodeImpl> mocked3 = mockConstruction(NodeImpl.class, TutorTests_H12_Private::prepareNode);
             MockedConstruction<EdgeImpl> mocked4 = mockConstruction(EdgeImpl.class, TutorTests_H12_Private::prepareEdge)) {

            NodeImplUnitTests nodeImplUnitTests = new NodeImplUnitTests();
            NodeImplUnitTests.initialize();

            assertNotNull(TutorObjectUnitTests.lastInstance, context,
                TR -> "No ObjectUnitTest Instance got created.");

            assertSame(TutorObjectUnitTests.lastInstance, getObjectUnitTests(nodeImplUnitTests), context,
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

                    TutorNode nodeI = TutorTests_H12_Private.nodeMap.get((NodeImpl) TutorObjectUnitTests.testObjectFactory.apply(i));
                    TutorNode nodeJ = TutorTests_H12_Private.nodeMap.get((NodeImpl) TutorObjectUnitTests.testObjectFactory.apply(j));

                    if (i == j) {
                        assertEquals(nodeI, nodeJ, context,
                            TR -> "testObjectFactory.apply(i).equals(testObjectFactory.apply(j)) returned false for i = " + finalI + " and j = " + finalJ + "");
                    } else {
                        assertNotEquals(nodeI, nodeJ, context,
                            TR -> "testObjectFactory.apply(i).equals(testObjectFactory.apply(j)) returned true for i = " + finalI + " and j = " + finalJ + "");
                    }
                }
            }
        }  catch (MockitoException e) {
            throw new RuntimeException(e.getMessage() + ": " + e.getCause().getMessage());
        }

        TutorRegion region = new TutorRegion();
        String name = "TestNode";
        Location locationA = new TutorLocation(0, 0);
        TutorLocation locationB = new TutorLocation(1, 1);
        Set<Location> connections = Set.of(locationA, locationB);
        NodeImpl nodeA = new TutorNode(region, name, locationA, connections);
        NodeImpl nodeB = new TutorNode(region, "nodeB", locationB, Set.of(locationA));
        EdgeImpl edgeAA = new TutorEdge(region, "edgeAA", locationA, locationA, 2);
        EdgeImpl edgeAB = new TutorEdge(region, "edgeAB", locationA, locationB, 2);

        region.putNode(nodeA);
        region.putNode(nodeB);
        region.putEdge(edgeAA);
        region.putEdge(edgeAB);

        assertEquals("NodeImpl(name='" + name + "'"
                + ", location='" + locationA + "'"
                + ", connections='" + connections + "'"
                + ')', TutorObjectUnitTests.toString.apply(nodeA), context,
            TR -> "toString.apply does not return the correct value.");
    }

    @Test
    public void testObjectTestMethods() throws NoSuchFieldException, IllegalAccessException {
        Context context = contextBuilder().subject("NodeImplUnitTests#test*()").build();

        NodeImplUnitTests nodeImplUnitTests = new NodeImplUnitTests();

        setObjectUnitTests(nodeImplUnitTests, new TutorObjectUnitTests<>(null, null));

        nodeImplUnitTests.testEquals();
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

        nodeImplUnitTests.testHashCode();
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

        nodeImplUnitTests.testToString();
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

        Context context = contextBuilder().subject("NodeImplUnitTests#initialize").build();

        try (MockedConstruction<Location> mocked = mockConstruction(Location.class, TutorTests_H12_Private::prepareLocation);
             MockedConstruction<RegionImpl> mocked2 = mockConstruction(RegionImpl.class, TutorTests_H12_Private::prepareRegion);
             MockedConstruction<NodeImpl> mocked3 = mockConstruction(NodeImpl.class, TutorTests_H12_Private::prepareNode);
             MockedConstruction<EdgeImpl> mocked4 = mockConstruction(EdgeImpl.class, TutorTests_H12_Private::prepareEdge)) {

            NodeImplUnitTests nodeImplUnitTests = new NodeImplUnitTests();
            NodeImplUnitTests.initialize();

            assertNotNull(TutorComparableUnitTests.lastInstance, context,
                TR -> "No ObjectUnitTest Instance got created.");

            assertSame(TutorComparableUnitTests.lastInstance, getComparableUnitTests(nodeImplUnitTests), context,
                TR -> "The comparableUnitTests Attribute is not the last create comparableUnitTests instance.");

            assertNotNull(TutorComparableUnitTests.testObjectFactory, context,
                TR -> "The testObjectFactory is null.");

            assertEquals(10, TutorComparableUnitTests.testObjectCount, context,
                TR -> "The testObjectCount is not 10.");

            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    int finalI = i;
                    int finalJ = j;

                    TutorNode nodeI = TutorTests_H12_Private.nodeMap.get((NodeImpl) TutorComparableUnitTests.testObjectFactory.apply(i));
                    TutorNode nodeJ = TutorTests_H12_Private.nodeMap.get((NodeImpl) TutorComparableUnitTests.testObjectFactory.apply(j));

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
        Context context = contextBuilder().subject("NodeImplUnitTests#test*()").build();

        NodeImplUnitTests nodeImplUnitTests = new NodeImplUnitTests();

        setComparableUnitTests(nodeImplUnitTests, new TutorComparableUnitTests<>(null));

        nodeImplUnitTests.testBiggerThen();
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

        nodeImplUnitTests.testAsBigAs();
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

        nodeImplUnitTests.testLessThen();
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

    public void createRegion() {
        RegionImpl region = new RegionImpl();

        NodeImplUnitTests.nodeA = spy(new NodeImpl(region, "A", new Location(0, 0), Set.of(new Location(0, 0), new Location(1, 0))));
        NodeImplUnitTests.nodeB = spy(new NodeImpl(region, "B", new Location(1, 0), Set.of(new Location(2, 0), new Location(0, 0))));
        NodeImplUnitTests.nodeC = spy(new NodeImpl(region, "C", new Location(2, 0), Set.of(new Location(1, 0))));
        NodeImplUnitTests.nodeD = spy(new NodeImpl(region, "D", new Location(3, 0), Set.of()));

        region.putNode(NodeImplUnitTests.nodeA);
        region.putNode(NodeImplUnitTests.nodeB);
        region.putNode(NodeImplUnitTests.nodeC);
        region.putNode(NodeImplUnitTests.nodeD);

        NodeImplUnitTests.edgeAA = new EdgeImpl(region, "AA", new Location(0, 0), new Location(0, 0), 1);
        NodeImplUnitTests.edgeAB = new EdgeImpl(region, "AB", new Location(0, 0), new Location(1, 0), 1);
        NodeImplUnitTests.edgeBC = new EdgeImpl(region, "BC", new Location(1, 0), new Location(2, 0), 1);

        region.putEdge(NodeImplUnitTests.edgeAA);
        region.putEdge(NodeImplUnitTests.edgeAB);
        region.putEdge(NodeImplUnitTests.edgeBC);
    }

    @Test
    public void testGetEdgeFailAA() {
        Context context = contextBuilder().subject("NodeImplUnitTests#testGetEdge").build();

        createRegion();

        AtomicBoolean called = new AtomicBoolean(false);

        when(NodeImplUnitTests.nodeA.getEdge(NodeImplUnitTests.nodeA)).thenAnswer(invocation -> {
            if (invocation.getArgument(0).equals(NodeImplUnitTests.nodeA)) {
                called.set(true);
                return NodeImplUnitTests.edgeAB;
            }
            return NodeImplUnitTests.nodeA.getEdge(invocation.getArgument(0));
        });

        assertThrows(AssertionFailedError.class, () -> new NodeImplUnitTests().testGetEdge(),
            "testGetEdge() did not throw an AssertionFailedError when nodeA.getEdge(nodeA) returned the wrong edge.");

        assertTrue(called.get(), context, TR -> "testGetEdge() did not call nodeA.getEdge(nodeA).");
    }

    @Test
    public void testGetEdgeFailAB() {
        Context context = contextBuilder().subject("NodeImplUnitTests#testGetEdge").build();

        createRegion();

        AtomicBoolean called = new AtomicBoolean(false);

        when(NodeImplUnitTests.nodeA.getEdge(NodeImplUnitTests.nodeB)).thenAnswer(invocation -> {
            if (invocation.getArgument(0).equals(NodeImplUnitTests.nodeB)) {
                called.set(true);
                return NodeImplUnitTests.edgeAA;
            }
            return NodeImplUnitTests.nodeA.getEdge(invocation.getArgument(0));
        });

        assertThrows(AssertionFailedError.class, () -> new NodeImplUnitTests().testGetEdge(),
            "testGetEdge() did not throw an AssertionFailedError when nodeA.getEdge(nodeB) returned the wrong edge.");

        assertTrue(called.get(), context, TR -> "testGetEdge() did not call nodeA.getEdge(nodeB).");
    }

    @Test
    public void testGetEdgeFailAC() {
        Context context = contextBuilder().subject("NodeImplUnitTests#testGetEdge").build();

        createRegion();

        AtomicBoolean called = new AtomicBoolean(false);

        when(NodeImplUnitTests.nodeA.getEdge(NodeImplUnitTests.nodeC)).thenAnswer(invocation -> {
            if (invocation.getArgument(0).equals(NodeImplUnitTests.nodeC)) {
                called.set(true);
                return NodeImplUnitTests.edgeAA;
            }
            return NodeImplUnitTests.nodeA.getEdge(invocation.getArgument(0));
        });

        assertThrows(AssertionFailedError.class, () -> new NodeImplUnitTests().testGetEdge(),
            "testGetEdge() did not throw an AssertionFailedError when nodeA.getEdge(nodeC) returned not null.");

        assertTrue(called.get(), context, TR -> "testGetEdge() did not call nodeA.getEdge(nodeB).");
    }

    @Test
    public void testGetEdgeFailAD() {
        Context context = contextBuilder().subject("NodeImplUnitTests#testGetEdge").build();

        createRegion();

        AtomicBoolean called = new AtomicBoolean(false);

        when(NodeImplUnitTests.nodeA.getEdge(NodeImplUnitTests.nodeD)).thenAnswer(invocation -> {
            if (invocation.getArgument(0).equals(NodeImplUnitTests.nodeD)) {
                called.set(true);
                return NodeImplUnitTests.edgeAA;
            }
            return NodeImplUnitTests.nodeA.getEdge(invocation.getArgument(0));
        });

        assertThrows(AssertionFailedError.class, () -> new NodeImplUnitTests().testGetEdge(),
            "testGetEdge() did not throw an AssertionFailedError when nodeA.getEdge(nodeD) returned not null.");

        assertTrue(called.get(), context, TR -> "testGetEdge() did not call nodeA.getEdge(nodeB).");
    }

    @Test
    public void testGetEdgeSuccess() {
        createRegion();

        clearInvocations(NodeImplUnitTests.nodeA);

        assertDoesNotThrow(() -> new NodeImplUnitTests().testGetEdge(),
            "testGetEdge() threw an exception when nodeA.getEdge always returns the correct value.");

        verify(NodeImplUnitTests.nodeA, times(4)).getEdge(any());
    }

    @Test
    public void testGetEdgeAssertionCount() {
        Context context = contextBuilder().subject("NodeImplUnitTests#testGetEdge").build();

        CtMethod<?> method = ((BasicMethodLink) BasicTypeLink.of(NodeImplUnitTests.class).getMethod(identical("testGetEdge"))).getCtElement();

        assertEquals(4, countInvocations(method, (name, target) -> name.startsWith("assert") && target.startsWith("org.junit")), context,
            TR -> "testGetEdge() does not contain the correct number of assert*() calls.");
    }

    @Test
    public void testAdjacentNodesFailA() {
        createRegion();

        when(NodeImplUnitTests.nodeA.getAdjacentNodes()).thenReturn(Set.of(NodeImplUnitTests.nodeB, NodeImplUnitTests.nodeC));

        assertThrows(AssertionFailedError.class, () -> new NodeImplUnitTests().testAdjacentNodes(),
            "testAdjacentNodes() did not throw an AssertionFailedError when nodeA.testAdjacentNodes() returns an incorrect value.");

        verify(NodeImplUnitTests.nodeA, times(1)).getAdjacentNodes();
    }

    @Test
    public void testAdjacentNodesFailB() {
        createRegion();

        when(NodeImplUnitTests.nodeB.getAdjacentNodes()).thenReturn(Set.of(NodeImplUnitTests.nodeC));

        assertThrows(AssertionFailedError.class, () -> new NodeImplUnitTests().testAdjacentNodes(),
            "testAdjacentNodes() did not throw an AssertionFailedError when nodeB.testAdjacentNodes() returns an incorrect value.");

        verify(NodeImplUnitTests.nodeB, times(1)).getAdjacentNodes();
    }

    @Test
    public void testAdjacentNodesFailC() {
        createRegion();

        when(NodeImplUnitTests.nodeC.getAdjacentNodes()).thenReturn(Set.of(NodeImplUnitTests.nodeB, NodeImplUnitTests.nodeC));

        assertThrows(AssertionFailedError.class, () -> new NodeImplUnitTests().testAdjacentNodes(),
            "testAdjacentNodes() did not throw an AssertionFailedError when nodeC.testAdjacentNodes() returns an incorrect value.");

        verify(NodeImplUnitTests.nodeC, times(1)).getAdjacentNodes();
    }

    @Test
    public void testAdjacentNodesFailD() {
        createRegion();

        when(NodeImplUnitTests.nodeD.getAdjacentNodes()).thenReturn(Set.of(NodeImplUnitTests.nodeB, NodeImplUnitTests.nodeC));

        assertThrows(AssertionFailedError.class, () -> new NodeImplUnitTests().testAdjacentNodes(),
            "testAdjacentNodes() did not throw an AssertionFailedError when nodeD.testAdjacentNodes() returns an incorrect value.");

        verify(NodeImplUnitTests.nodeD, times(1)).getAdjacentNodes();
    }

    @Test
    public void testAdjacentNodesSuccess() {
        createRegion();

        assertDoesNotThrow(() -> new NodeImplUnitTests().testAdjacentNodes(),
            "testAdjacentNodes() threw an exception when node*.getAdjacentNodes() always returns the correct value.");

        verify(NodeImplUnitTests.nodeA, times(1)).getAdjacentNodes();
        verify(NodeImplUnitTests.nodeB, times(1)).getAdjacentNodes();
        verify(NodeImplUnitTests.nodeC, times(1)).getAdjacentNodes();
        verify(NodeImplUnitTests.nodeD, times(1)).getAdjacentNodes();
    }

    @Test
    public void testAdjacentNodesAssertionCount() {
        Context context = contextBuilder().subject("NodeImplUnitTests#testAdjacentNodes").build();

        CtMethod<?> method = ((BasicMethodLink) BasicTypeLink.of(NodeImplUnitTests.class).getMethod(identical("testAdjacentNodes"))).getCtElement();

        assertEquals(4, countInvocations(method, (name, target) -> name.startsWith("assert") && target.startsWith("org.junit")), context,
            TR -> "testAdjacentNodes() does not contain the correct number of assert*() calls.");
    }

    @Test
    public void testAdjacentEdgesFailA() {
        createRegion();

        when(NodeImplUnitTests.nodeA.getAdjacentEdges()).thenReturn(Set.of(NodeImplUnitTests.edgeAA, NodeImplUnitTests.edgeBC));

        clearInvocations(NodeImplUnitTests.nodeA);

        assertThrows(AssertionFailedError.class, () -> new NodeImplUnitTests().testAdjacentEdges(),
            "testAdjacentEdges() did not throw an AssertionFailedError when nodeA.testAdjacentEdges() returns an incorrect value.");

        verify(NodeImplUnitTests.nodeA, times(1)).getAdjacentEdges();
    }

    @Test
    public void testAdjacentEdgesFailB() {
        createRegion();

        when(NodeImplUnitTests.nodeB.getAdjacentEdges()).thenReturn(Set.of(NodeImplUnitTests.edgeBC));

        clearInvocations(NodeImplUnitTests.nodeB);

        assertThrows(AssertionFailedError.class, () -> new NodeImplUnitTests().testAdjacentEdges(),
            "testAdjacentEdges() did not throw an AssertionFailedError when nodeB.testAdjacentEdges() returns an incorrect value.");

        verify(NodeImplUnitTests.nodeB, times(1)).getAdjacentEdges();
    }

    @Test
    public void testAdjacentEdgesFailC() {
        createRegion();

        when(NodeImplUnitTests.nodeC.getAdjacentEdges()).thenReturn(new HashSet<>());

        clearInvocations(NodeImplUnitTests.nodeC);

        assertThrows(AssertionFailedError.class, () -> new NodeImplUnitTests().testAdjacentEdges(),
            "testAdjacentEdges() did not throw an AssertionFailedError when nodeC.testAdjacentEdges() returns an incorrect value.");

        verify(NodeImplUnitTests.nodeC, times(1)).getAdjacentEdges();
    }

    @Test
    public void testAdjacentEdgesFailD() {
        createRegion();

        when(NodeImplUnitTests.nodeD.getAdjacentEdges()).thenReturn(Set.of(NodeImplUnitTests.edgeAA));

        clearInvocations(NodeImplUnitTests.nodeD);

        assertThrows(AssertionFailedError.class, () -> new NodeImplUnitTests().testAdjacentEdges(),
            "testAdjacentEdges() did not throw an AssertionFailedError when nodeD.testAdjacentEdges() returns an incorrect value.");

        verify(NodeImplUnitTests.nodeD, times(1)).getAdjacentEdges();
    }

    @Test
    public void testAdjacentEdgesSuccess() {
        createRegion();

        assertDoesNotThrow(() -> new NodeImplUnitTests().testAdjacentEdges(),
            "testAdjacentEdges() threw an exception when node*.getAdjacentEdges() always returns the correct value.");

        verify(NodeImplUnitTests.nodeA, times(1)).getAdjacentEdges();
        verify(NodeImplUnitTests.nodeB, times(1)).getAdjacentEdges();
        verify(NodeImplUnitTests.nodeC, times(1)).getAdjacentEdges();
        verify(NodeImplUnitTests.nodeD, times(1)).getAdjacentEdges();
    }

    @Test
    public void testAdjacentEdgesAssertionCount() {
        Context context = contextBuilder().subject("NodeImplUnitTests#testAdjacentEdges").build();

        CtMethod<?> method = ((BasicMethodLink) BasicTypeLink.of(NodeImplUnitTests.class).getMethod(identical("testAdjacentEdges"))).getCtElement();

        assertEquals(4, countInvocations(method, (name, target) -> name.startsWith("assert") && target.startsWith("org.junit")), context,
            TR -> "testAdjacentEdges() does not contain the correct number of assert*() calls.");
    }

    @SuppressWarnings("unchecked")
    public static ObjectUnitTests<NodeImpl> getObjectUnitTests(NodeImplUnitTests nodeImplUnitTests) throws ReflectiveOperationException {
        Field field = NodeImplUnitTests.class.getDeclaredField("objectUnitTests");
        field.setAccessible(true);
        return (ObjectUnitTests<NodeImpl>) field.get(nodeImplUnitTests);
    }

    @SuppressWarnings("unchecked")
    public static ComparableUnitTests<NodeImpl> getComparableUnitTests(NodeImplUnitTests nodeImplUnitTests) throws ReflectiveOperationException {
        Field field = NodeImplUnitTests.class.getDeclaredField("comparableUnitTests");
        field.setAccessible(true);
        return (ComparableUnitTests<NodeImpl>) field.get(nodeImplUnitTests);
    }

    public static void setObjectUnitTests(NodeImplUnitTests locationUnitTests, ObjectUnitTests<Location> objectUnitTests) throws NoSuchFieldException, IllegalAccessException {
        Field objectUnitTestsField = NodeImplUnitTests.class.getDeclaredField("objectUnitTests");
        objectUnitTestsField.setAccessible(true);
        objectUnitTestsField.set(locationUnitTests, objectUnitTests);
    }

    public static void setComparableUnitTests(NodeImplUnitTests locationUnitTests, ComparableUnitTests<Location> comparableUnitTests) throws NoSuchFieldException, IllegalAccessException {
        Field objectUnitTestsField = NodeImplUnitTests.class.getDeclaredField("comparableUnitTests");
        objectUnitTestsField.setAccessible(true);
        objectUnitTestsField.set(locationUnitTests, comparableUnitTests);
    }


}
