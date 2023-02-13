package projekt.util;

import projekt.base.Location;
import projekt.delivery.routing.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;

public class Utils {

    public static Region.Edge createEdge(Region region, String name, Location from, Location to, long distance) throws ReflectiveOperationException {
        Constructor<?> constructor = Class.forName("projekt.delivery.routing.EdgeImpl").getDeclaredConstructor(Region.class, String.class, Location.class, Location.class, long.class);
        constructor.setAccessible(true);
        return (Region.Edge) constructor.newInstance(region, name, from, to, distance);
    }

    public static Region.Node createNode(Region region, String name, Location location, Set<Location> connections) throws ReflectiveOperationException {
        Constructor<?> constructor = Class.forName("projekt.delivery.routing.NodeImpl").getDeclaredConstructor(Region.class, String.class, Location.class, Set.class);
        constructor.setAccessible(true);
        return (Region.Node) constructor.newInstance(region, name, location, connections);
    }

    public static Region.Restaurant createRestaurant(Region region, String name, Location location, Set<Location> connections, List<String> foodList) throws ReflectiveOperationException {
        Constructor<?> constructor = Class.forName("projekt.delivery.routing.RestaurantImpl").getDeclaredConstructor(Region.class, String.class, Location.class, Set.class, List.class);
        constructor.setAccessible(true);
        return (Region.Restaurant) constructor.newInstance(region, name, location, connections, foodList);
    }

    public static Region.Neighborhood createNeighborhood(Region region, String name, Location location, Set<Location> connections) throws ReflectiveOperationException {
        Constructor<?> constructor = Class.forName("projekt.delivery.routing.NeighborhoodImpl").getDeclaredConstructor(Region.class, String.class, Location.class, Set.class);
        constructor.setAccessible(true);
        return (Region.Neighborhood) constructor.newInstance(region, name, location, connections);
    }

    public static Region createRegion() throws ReflectiveOperationException {
        Constructor<?> constructor = Class.forName("projekt.delivery.routing.RegionImpl").getDeclaredConstructor();
        constructor.setAccessible(true);
        return (Region) constructor.newInstance();
    }

    @SuppressWarnings("unchecked")
    public static void addNodeToRegion(Region region, Region.Node node) throws ReflectiveOperationException {
        Field nodes = region.getClass().getDeclaredField("nodes");
        nodes.setAccessible(true);
        ((Map<Location, Region.Node>) nodes.get(region)).put(node.getLocation(), node);
    }

    public static void addNodesToRegion(Region region, Region.Node... node) throws ReflectiveOperationException {
        for (Region.Node n : node) {
            addNodeToRegion(region, n);
        }
    }

    @SuppressWarnings("unchecked")
    public static void addEdgeToRegion(Region region, Region.Edge edge) throws ReflectiveOperationException {
        Field allEdges = region.getClass().getDeclaredField("allEdges");
        allEdges.setAccessible(true);
        ((List<Region.Edge>) allEdges.get(region)).add(edge);
    }

    public static void addEdgesToRegion(Region region, Region.Edge... edge) throws ReflectiveOperationException {
        for (Region.Edge e : edge) {
            addEdgeToRegion(region, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static void addEdgesAttributeToRegion(Region region, Location locationA, Map<Location, Region.Edge> nodes) throws ReflectiveOperationException {
        Field edgesField = region.getClass().getDeclaredField("edges");
        edgesField.setAccessible(true);
        ((Map<Location, Map<Location, Region.Edge>>) edgesField.get(region)).put(locationA, new HashMap<>(nodes));
    }

    @SuppressWarnings("JavaReflectionInvocation")
    public static Vehicle createVehicle(int id, double capacity, VehicleManager vehicleManager, VehicleManager.OccupiedRestaurant startingRestaurant) throws ReflectiveOperationException {
        Constructor<?> constructor = Class.forName("projekt.delivery.routing.VehicleImpl").getDeclaredConstructor(int.class, double.class,
            Class.forName("projekt.delivery.routing.VehicleManagerImpl"), VehicleManager.OccupiedRestaurant.class);
        constructor.setAccessible(true);
        return (Vehicle) constructor.newInstance(id, capacity, vehicleManager, startingRestaurant);
    }

    public static VehicleManager createVehicleManager(Region region) throws ReflectiveOperationException {
        Constructor<?> constructor = Class.forName("projekt.delivery.routing.VehicleManagerImpl").getDeclaredConstructor(Region.class, PathCalculator.class);
        constructor.setAccessible(true);
        return (VehicleManager) constructor.newInstance(region, new CachedPathCalculator(new DijkstraPathCalculator()));
    }

    @SuppressWarnings({"unchecked"})
    public static void addVehicleToVehicleManager(VehicleManager vehicleManager, Vehicle vehicle, VehicleManager.Occupied<?> occupied) throws ReflectiveOperationException {
        Field vehicles = vehicleManager.getClass().getDeclaredField("vehicles");
        vehicles.setAccessible(true);
        ((List<Vehicle>) vehicles.get(vehicleManager)).add(vehicle);

        Field vehiclesField = Class.forName("projekt.delivery.routing.AbstractOccupied").getDeclaredField("vehicles");
        vehiclesField.setAccessible(true);
        Map<Vehicle, Object> vehiclesMap = (Map<Vehicle, Object>) vehiclesField.get(occupied);

        Constructor<?> constructor = Class.forName("projekt.delivery.routing.AbstractOccupied$VehicleStats").getDeclaredConstructor(long.class, VehicleManager.Occupied.class);
        constructor.setAccessible(true);
        Object vehicleStats = constructor.newInstance(0L, null);

        vehiclesMap.put(vehicle, vehicleStats);
    }

    public static void setOrdersOfVehicle(Vehicle vehicle, List<ConfirmedOrder> orders) throws ReflectiveOperationException {
        Field ordersField = vehicle.getClass().getDeclaredField("orders");
        ordersField.setAccessible(true);
        ordersField.set(vehicle, orders);
    }

    @SuppressWarnings("JavaReflectionInvocation")
    public static void callPutNode(Region region, Region.Node node) throws ReflectiveOperationException {
        Method method = region.getClass().getDeclaredMethod("putNode", Class.forName("projekt.delivery.routing.NodeImpl"));
        method.setAccessible(true);
        method.invoke(region, node);
    }

    @SuppressWarnings("JavaReflectionInvocation")
    public static void callPutEdge(Region region, Region.Edge edge) throws ReflectiveOperationException {
        Method method = region.getClass().getDeclaredMethod("putEdge", Class.forName("projekt.delivery.routing.EdgeImpl"));
        method.setAccessible(true);
        method.invoke(region, edge);
    }

    public static void callLoadOrder(Vehicle vehicle, ConfirmedOrder order) throws ReflectiveOperationException {
        Method method = vehicle.getClass().getDeclaredMethod("loadOrder", ConfirmedOrder.class);
        method.setAccessible(true);
        method.invoke(vehicle, order);
    }

    public static void callUnloadOrder(Vehicle vehicle, ConfirmedOrder order) throws ReflectiveOperationException {
        Method method = vehicle.getClass().getDeclaredMethod("unloadOrder", ConfirmedOrder.class);
        method.setAccessible(true);
        method.invoke(vehicle, order);
    }

    @SuppressWarnings("unchecked")
    public static List<ConfirmedOrder> getOrdersOfVehicle(Vehicle vehicle) throws ReflectiveOperationException {
        Field ordersField = vehicle.getClass().getDeclaredField("orders");
        ordersField.setAccessible(true);
        return (List<ConfirmedOrder>) ordersField.get(vehicle);
    }

    @SuppressWarnings("unchecked")
    public static Deque<Vehicle.Path> getMoveQueueOfVehicle(Vehicle vehicle) throws ReflectiveOperationException {
        Field moveQueueField = vehicle.getClass().getDeclaredField("moveQueue");
        moveQueueField.setAccessible(true);
        return (Deque<Vehicle.Path>) moveQueueField.get(vehicle);
    }

    public static void mockPathCalculator(VehicleManager vehicleManager) throws ReflectiveOperationException {
        PathCalculator pathCalculator = mock(DijkstraPathCalculator.class);
        Field pathCalculatorField = Class.forName("projekt.delivery.routing.VehicleManagerImpl").getDeclaredField("pathCalculator");
        pathCalculatorField.setAccessible(true);
        pathCalculatorField.set(vehicleManager, pathCalculator);
    }

    public static Vehicle.Path createPath(Deque<Region.Node> nodes, Consumer<? super Vehicle> arrivalAction) throws ReflectiveOperationException {
        Constructor<?> constructor = Class.forName("projekt.delivery.routing.VehicleImpl$PathImpl").getDeclaredConstructor(Deque.class, Consumer.class);
        constructor.setAccessible(true);
        return (Vehicle.Path) constructor.newInstance(nodes, arrivalAction);
    }
}
