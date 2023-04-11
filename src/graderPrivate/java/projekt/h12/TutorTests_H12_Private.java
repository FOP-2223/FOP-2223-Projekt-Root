package projekt.h12;

import org.jetbrains.annotations.NotNull;
import org.mockito.MockedConstruction;
import projekt.base.Location;
import projekt.delivery.routing.EdgeImpl;
import projekt.delivery.routing.NodeImpl;
import projekt.delivery.routing.RegionImpl;
import projekt.solution.TutorEdge;
import projekt.solution.TutorLocation;
import projekt.solution.TutorNode;
import projekt.solution.TutorRegion;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtElement;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TutorTests_H12_Private {

    public static Map<Location, TutorLocation> locationMap = new HashMap<>();

    public static void prepareLocation(Location location, MockedConstruction.Context context) {

        for (int i = 0; i < 2; i++) {
            if (context.arguments().get(i) == null)
                fail("Location constructor argument " + i + " is null");
        }

        int x = (int) context.arguments().get(0);
        int y = (int) context.arguments().get(1);

        TutorLocation tutorLocation = new TutorLocation(x, y);

        locationMap.put(location, tutorLocation);

        when(location.getX()).thenAnswer(invocation -> x);
        when(location.getY()).thenAnswer(invocation -> y);

        when(location.compareTo(any(Location.class))).thenAnswer(invocation ->
            tutorLocation.compareTo(new TutorLocation(
                ((Location) invocation.getArgument(0)).getX(),
                ((Location) invocation.getArgument(0)).getY())));

        try {
            Field xField = Location.class.getDeclaredField("x");
            Field yField = Location.class.getDeclaredField("y");
            xField.setAccessible(true);
            yField.setAccessible(true);
            xField.set(location, x);
            yField.set(location, y);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Map<RegionImpl, TutorRegion> regionMap = new HashMap<>();

    public static void prepareRegion(RegionImpl region, MockedConstruction.Context context) {

        TutorRegion tutorRegion = new TutorRegion();

        regionMap.put(region, tutorRegion);

        doAnswer(invocation -> {
            tutorRegion.putNode(nodeMap.get((NodeImpl) invocation.getArgument(0)));

            try {
                Field nodesField = RegionImpl.class.getDeclaredField("nodes");
                nodesField.setAccessible(true);
                nodesField.set(region, tutorRegion.nodes);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return null;
        }).when(region).putNode(any(NodeImpl.class));

        doAnswer(invocation -> {
            tutorRegion.putEdge(edgeMap.get((EdgeImpl) invocation.getArgument(0)));

            try {
                Field edgesField = RegionImpl.class.getDeclaredField("edges");
                Field allEdgesField = RegionImpl.class.getDeclaredField("allEdges");

                edgesField.setAccessible(true);
                allEdgesField.setAccessible(true);

                edgesField.set(region, tutorRegion.edges);
                allEdgesField.set(region, tutorRegion.allEdges);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return null;
        }).when(region).putEdge(any(EdgeImpl.class));

        when(region.getNode(any(Location.class))).thenAnswer(invocation -> tutorRegion.getNode(invocation.getArgument(0)));
        when(region.getEdge(any(Location.class), any(Location.class))).thenAnswer(invocation -> tutorRegion.getEdge((Location) invocation.getArgument(0), invocation.getArgument(1)));
        when(region.getNodes()).thenAnswer(invocation -> tutorRegion.getNodes());
        when(region.getEdges()).thenAnswer(invocation -> tutorRegion.getEdges());

        try {
            Field nodesField = RegionImpl.class.getDeclaredField("nodes");
            Field edgesField = RegionImpl.class.getDeclaredField("edges");
            Field allEdgesField = RegionImpl.class.getDeclaredField("allEdges");
            Field unmodifiableNodesField = RegionImpl.class.getDeclaredField("unmodifiableNodes");
            Field unmodifiableEdgesField = RegionImpl.class.getDeclaredField("unmodifiableEdges");

            nodesField.setAccessible(true);
            edgesField.setAccessible(true);
            allEdgesField.setAccessible(true);
            unmodifiableNodesField.setAccessible(true);
            unmodifiableEdgesField.setAccessible(true);

            nodesField.set(region, tutorRegion.nodes);
            edgesField.set(region, tutorRegion.edges);
            allEdgesField.set(region, tutorRegion.allEdges);
            unmodifiableNodesField.set(region, tutorRegion.unmodifiableNodes);
            unmodifiableEdgesField.set(region, tutorRegion.unmodifiableEdges);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Map<NodeImpl, TutorNode> nodeMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void prepareNode(NodeImpl node, MockedConstruction.Context context) {

        for (int i = 0; i < 4; i++) {
            if (context.arguments().get(i) == null)
                fail("NodeImpl constructor argument " + i + " is null");
        }

        RegionImpl region = (RegionImpl) context.arguments().get(0);
        String name = (String) context.arguments().get(1);
        Location location = (Location) context.arguments().get(2);
        Set<Location> connections = (Set<Location>) context.arguments().get(3);


        if (!(region instanceof TutorRegion)) {
            if (!regionMap.containsKey(region)) {
                fail("Invalid region in node constructor: " + region);
            }
            region = regionMap.get(region);
        }

        if (!(location instanceof TutorLocation)) {
            if (!locationMap.containsKey(location)) {
                fail("Invalid location in node constructor: " + location);
            }
            location = locationMap.get(location);
        }

        HashSet<Location> connectionsCopy = new HashSet<>(connections);
        connections = new HashSet<>();

        for (Location o : connectionsCopy) {
            if (!(o instanceof TutorLocation)) {
                if (!locationMap.containsKey(o)) {
                    fail("Invalid connection location in node constructor: " + o);
                }
                connections.add(locationMap.get(o));
            } else {
                connections.add(o);
            }
        }


        TutorNode tutorNode = new TutorNode(region, name, location, connections);

        nodeMap.put(node, tutorNode);

        when(node.getRegion()).thenAnswer(invocation -> tutorNode.getRegion());
        when(node.getName()).thenAnswer(invocation -> tutorNode.getName());
        when(node.getLocation()).thenAnswer(invocation -> tutorNode.getLocation());
        when(node.getEdge(any())).thenAnswer(invocation -> tutorNode.getEdge(invocation.getArgument(0)));
        when(node.getAdjacentNodes()).thenAnswer(invocation -> tutorNode.getAdjacentNodes());
        when(node.getAdjacentEdges()).thenAnswer(invocation -> tutorNode.getAdjacentEdges());
        when(node.getConnections()).thenAnswer(invocation -> tutorNode.getConnections());
        when(node.compareTo(any(NodeImpl.class))).thenAnswer(invocation ->
            tutorNode.compareTo(invocation.getArgument(0)));

        try {
            Field nameField = NodeImpl.class.getDeclaredField("name");
            Field regionField = NodeImpl.class.getDeclaredField("region");
            Field locationField = NodeImpl.class.getDeclaredField("location");
            Field ConnectionsField = NodeImpl.class.getDeclaredField("connections");

            nameField.setAccessible(true);
            regionField.setAccessible(true);
            locationField.setAccessible(true);
            ConnectionsField.setAccessible(true);

            nameField.set(node, tutorNode.name);
            regionField.set(node, tutorNode.region);
            locationField.set(node, tutorNode.location);
            ConnectionsField.set(node, tutorNode.connections);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Map<EdgeImpl, TutorEdge> edgeMap = new HashMap<>();

    public static void prepareEdge(EdgeImpl edge, MockedConstruction.Context context) {

        for (int i = 0; i < 5; i++) {
            if (context.arguments().get(i) == null)
                fail("EdgeImpl constructor argument " + i + " is null");
        }

        RegionImpl region = (RegionImpl) context.arguments().get(0);
        String name = (String) context.arguments().get(1);
        Location locationA = (Location) context.arguments().get(2);
        Location locationB = (Location) context.arguments().get(3);
        long duration = (long) context.arguments().get(4);

        if (!(region instanceof TutorRegion)) {
            if (!regionMap.containsKey(region)) {
                fail("Invalid region in edge constructor: " + region);
            }
            region = regionMap.get(region);
        }
        if (!(locationA instanceof TutorLocation)) {
            if (!locationMap.containsKey(locationA)) {
                fail("Invalid location in edge constructor: " + locationA);
            }
            locationA = locationMap.get(locationA);
        }
        if (!(locationB instanceof TutorLocation)) {
            if (!locationMap.containsKey(locationB)) {
                fail("Invalid location in edge constructor: " + locationB);
            }
            locationB = locationMap.get(locationB);
        }
        TutorEdge tutorEdge = new TutorEdge(region, name,
            locationA, locationB, duration);

        edgeMap.put(edge, tutorEdge);

        when(edge.getRegion()).thenAnswer(invocation -> tutorEdge.getRegion());
        when(edge.getName()).thenAnswer(invocation -> tutorEdge.getName());
        when(edge.getLocationA()).thenAnswer(invocation -> tutorEdge.getLocationA());
        when(edge.getLocationB()).thenAnswer(invocation -> tutorEdge.getLocationB());
        when(edge.getDuration()).thenAnswer(invocation -> tutorEdge.getDuration());
        when(edge.getNodeA()).thenAnswer(invocation -> tutorEdge.getNodeA());
        when(edge.getNodeB()).thenAnswer(invocation -> tutorEdge.getNodeB());
        when(edge.compareTo(any(EdgeImpl.class))).thenAnswer(invocation ->
            tutorEdge.compareTo(invocation.getArgument(0)));

        try {
            Field nameField = EdgeImpl.class.getDeclaredField("name");
            Field regionField = EdgeImpl.class.getDeclaredField("region");
            Field locationAField = EdgeImpl.class.getDeclaredField("locationA");
            Field locationBField = EdgeImpl.class.getDeclaredField("locationB");
            Field durationField = EdgeImpl.class.getDeclaredField("duration");

            nameField.setAccessible(true);
            regionField.setAccessible(true);
            locationAField.setAccessible(true);
            locationBField.setAccessible(true);
            durationField.setAccessible(true);

            nameField.set(edge, tutorEdge.name);
            regionField.set(edge, tutorEdge.region);
            locationAField.set(edge, tutorEdge.locationA);
            locationBField.set(edge, tutorEdge.locationB);
            durationField.set(edge, tutorEdge.duration);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static int countInvocations(CtElement element, BiPredicate<String, String> methodTester) {
        return countInvocations(element, methodTester, new HashSet<>());
    }

    public static int countInvocations(CtElement
                                           element, BiPredicate<String, String> methodTester, Set<String> visited) {
        int count = 0;

        for (CtElement child : element.getElements(ctElement -> ctElement instanceof CtInvocation<?>)) {
            CtInvocation<?> invocation = (CtInvocation<?>) child;

            if (methodTester.test(invocation.getExecutable().getSimpleName(), invocation.getTarget().toString())) {
                count++;
            }

            //don't recursively check the same method twice
            if (visited.contains(invocation.getExecutable().toString())) {
                continue;
            }

            visited.add(invocation.getExecutable().toString());
            String target = invocation.getTarget().toString();
            if (!(target.startsWith("org.junit") || target.startsWith("java"))) {
                count += countInvocations(invocation.getExecutable().getExecutableDeclaration(), methodTester, visited);
            }

        }

        return count;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static <T> boolean checkCompares(T[] expectedExpected, T[] expectedActual, T[] actualExpected, T[]
        actualActual) {
        for (int i = 0; i < expectedExpected.length; i++) {
            if (checkCompares(expectedExpected[i], expectedActual[i], actualExpected[i], actualActual[i])) {
                continue;
            }
            return false;
        }
        return true;
    }

    public static <T> boolean checkCompares(T expectedExpected, T expectedActual, T actualExpected, T actualActual) {
        return (Objects.equals(expectedExpected, actualExpected) && Objects.equals(expectedActual, actualActual)) ||
            (Objects.equals(expectedExpected, actualActual) && Objects.equals(expectedActual, actualExpected));

    }

    public static IntegerWrapper[] createTestObjectSpies(int count) {
        IntegerWrapper[] testObjects = new IntegerWrapper[count];
        for (int i = 0; i < count; i++) {
            testObjects[i] = spy(new IntegerWrapper(i));
        }
        return testObjects;
    }

    public static IntegerWrapper[] createTestObjects(int count) {
        IntegerWrapper[] testObjects = new IntegerWrapper[count];
        for (int i = 0; i < count; i++) {
            testObjects[i] = new IntegerWrapper(i);
        }
        return testObjects;
    }

    public static class TestObjectFactory implements Function<Integer, IntegerWrapper> {

        public int applyCount = 0;

        @Override
        public IntegerWrapper apply(Integer integer) {
            applyCount++;
            return new IntegerWrapper(integer);
        }

    }

    public static class IntegerWrapper implements Comparable<IntegerWrapper> {

        public int value;

        public static int nextID = 0;
        public int ID = nextID++;

        public IntegerWrapper(int value) {
            this.value = value;
        }

        public static void reset() {
            nextID = 0;
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        public int compareTo(@NotNull IntegerWrapper o) {
            if (o == null) { //Avoid NPE when setting up ArgumentCaptors
                return 1;
            }
            return Integer.compare(value, o.value);
        }

        @Override
        public int hashCode() {
            return ID;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof IntegerWrapper && ((IntegerWrapper) obj).value == value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

}
