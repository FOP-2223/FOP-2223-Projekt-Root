package projekt.delivery.service;

import projekt.base.TickInterval;
import projekt.delivery.event.ArrivedAtRestaurantEvent;
import projekt.delivery.event.Event;
import projekt.delivery.event.SpawnEvent;
import projekt.delivery.routing.*;

import java.util.*;
import java.util.stream.Collectors;

public class OurDeliveryService extends AbstractDeliveryService {

    private final List<RestaurantManager> managers = new ArrayList<>();

    public OurDeliveryService(VehicleManager vehicleManager) {
        super(vehicleManager);

        for (VehicleManager.OccupiedRestaurant restaurant : vehicleManager.getOccupiedRestaurants()) {
            managers.add(new RestaurantManager(restaurant.getComponent(), new ArrayList<>(restaurant.getVehicles()), vehicleManager.getPathCalculator()));
        }
    }

    @Override
    protected List<Event> tick(long currentTick, List<ConfirmedOrder> newOrders) {

        List<Event> events = vehicleManager.tick(currentTick);

        handleEvents(events);

        Map<RestaurantManager, List<ConfirmedOrder>> ordersForManager = new HashMap<>();

        for (ConfirmedOrder order : newOrders) {
            RestaurantManager manager = getResponsibleManager(order);
            if (!ordersForManager.containsKey(manager)) {
                ordersForManager.put(manager, new ArrayList<>());
            }
            ordersForManager.get(manager).add(order);
        }

        for (RestaurantManager manager : managers) {
            manager.tick(currentTick, ordersForManager.getOrDefault(manager, List.of()));
        }

        distributeVehicles();

        return events;
    }


    private void distributeVehicles() {
        int unusedVehicles = 0;

        for (RestaurantManager manager : managers) {
            unusedVehicles += manager.getUnusedVehicles().size();
        }

        int vehiclesPerManager = unusedVehicles / managers.size();

        for (RestaurantManager manager : managers) {

            int vehicleDiff = vehiclesPerManager - manager.getUnusedVehicles().size();

            if (vehicleDiff <= 0) {
                continue;
            }

            for (RestaurantManager otherManager : managers) {

                if (otherManager == manager) {
                    continue;
                }

                while (otherManager.getTotalAvailableVehicle().size() < vehiclesPerManager && vehicleDiff > 0) {
                    Vehicle vehicle = otherManager.getUnusedVehicles().get(0);
                    vehicle.moveQueued(manager.managed);
                    manager.addQueuedVehicle(vehicle);
                    otherManager.removeVehicle(vehicle);
                    vehicleDiff--;
                }
            }
        }
    }

    private void handleEvents(List<Event> events) {
        events.stream()
            .filter(ArrivedAtRestaurantEvent.class::isInstance)
            .map(ArrivedAtRestaurantEvent.class::cast)
            .forEach(event -> {
                RestaurantManager manager = managers.stream()
                    .filter(m -> m.getManaged().equals(event.getRestaurant().getComponent()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No responsible manager found for restaurant " + event.getRestaurant().getComponent()));
                manager.addVehicle(event.getVehicle());
            });

        events.stream()
            .filter(SpawnEvent.class::isInstance)
            .map(SpawnEvent.class::cast)
            .forEach(event -> {
                RestaurantManager manager = managers.stream()
                    .filter(m -> m.getManaged().equals(event.getNode()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No responsible manager found for restaurant " + event.getNode()));
                manager.addVehicle(event.getVehicle());
            });
    }

    private RestaurantManager getResponsibleManager(ConfirmedOrder order) {
        return managers.stream()
            .filter(manager -> manager.getManaged().equals(order.getRestaurant().getComponent()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No responsible manager found for order " + order));
    }

    private void createManagers() {
        for (VehicleManager.OccupiedRestaurant restaurant : vehicleManager.getOccupiedRestaurants()) {
            managers.add(new RestaurantManager(restaurant.getComponent(), new ArrayList<>(restaurant.getVehicles()), vehicleManager.getPathCalculator()));
        }
    }

    @Override
    public List<ConfirmedOrder> getPendingOrders() {
        List<ConfirmedOrder> pendingOrders = new ArrayList<>();

        for (RestaurantManager manager : managers) {
            pendingOrders.addAll(manager.getPendingOrders());
        }

        return pendingOrders;
    }

    @Override
    public void reset() {
        super.reset();
        managers.clear();
        createManagers();
    }

    public interface Factory extends DeliveryService.Factory {

        OurDeliveryService create(VehicleManager vehicleManager);
    }

    private class RestaurantManager {

        private final Region.Restaurant managed;
        private final Map<Vehicle, List<RouteNode>> planedRoutes = new HashMap<>();
        private final Region region;
        private final PathCalculator pathCalculator;
        private final List<ConfirmedOrder> pendingOrders = new ArrayList<>();
        private final List<Vehicle> queuedVehicles = new ArrayList<>();

        public RestaurantManager(Region.Restaurant managed, List<Vehicle> availableVehicles, PathCalculator pathCalculator) {
            this.managed = managed;
            this.pathCalculator = pathCalculator;

            region = managed.getRegion();

            availableVehicles.forEach(this::addVehicle);
        }

        @SuppressWarnings("DuplicatedCode")
        public void acceptOrder(ConfirmedOrder order, long currentTick) {
            Map<Region.Node, Deque<Region.Node>> paths = pathCalculator.getAllPathsTo(region.getNode(order.getLocation()));

            Vehicle bestVehicle = null;
            List<RouteNode> bestNewRoute = null;

            for (Map.Entry<Vehicle, List<RouteNode>> plannedRoute : planedRoutes.entrySet()) {

                Vehicle responsibleVehicle = plannedRoute.getKey();
                List<RouteNode> route = plannedRoute.getValue();
                List<Region.Node> nodes = route.stream().map(RouteNode::node).toList();

                if (getWeight(route) + order.getWeight() > responsibleVehicle.getCapacity()) {
                    continue;
                }

                if (route.isEmpty()) {
                    List<RouteNode> newRoute = pathCalculator.getPath(getManaged(), region.getNode(order.getLocation())).stream()
                        .map(node -> new RouteNode(node, new ArrayList<>()))
                        .collect(Collectors.toCollection(ArrayList::new));
                    newRoute.get(newRoute.size() - 1).orders.add(order);

                    switch (compareRoute(bestNewRoute, newRoute, order, currentTick)) {
                        case BREAK -> {
                            pendingOrders.add(order);
                            return;
                        }
                        case SWITCH -> {
                            bestVehicle = responsibleVehicle;
                            bestNewRoute = newRoute;
                        }
                    }

                    continue;
                }

                Optional<RouteNode> matchingNode = route.stream().filter(routeNode -> routeNode.node().getLocation().equals(order.getLocation())).findAny();

                if (matchingNode.isPresent()) {
                    List<RouteNode> newRoute = copyRoute(route);
                    newRoute.get(newRoute.indexOf(matchingNode.get())).orders().add(order);

                    switch (compareRoute(bestNewRoute, newRoute, order, currentTick)) {
                        case BREAK -> {
                            pendingOrders.add(order);
                            return;
                        }
                        case SWITCH -> {
                            bestVehicle = responsibleVehicle;
                            bestNewRoute = newRoute;
                        }
                    }

                    continue;
                }

                for (Region.Node possibleAttachment : plannedRoute.getValue().stream().map(RouteNode::node).toList()) {

                    List<RouteNode> newRoute = copyRoute(route);

                    List<RouteNode> routeToOrder = paths.get(possibleAttachment).stream()
                        .map(node -> new RouteNode(node, new ArrayList<>()))
                        .collect(Collectors.toCollection(ArrayList::new));
                    routeToOrder.get(routeToOrder.size() - 1).orders.add(order);


                    List<RouteNode> routeFromOrder = new ArrayList<>();

                    if (nodes.indexOf(possibleAttachment) != nodes.size() - 1) {
                        routeFromOrder = paths.get(nodes.get(nodes.indexOf(possibleAttachment) + 1)).stream()
                            .map(node -> new RouteNode(node, new ArrayList<>()))
                            .collect(Collectors.toCollection(ArrayList::new));
                        routeFromOrder.remove(routeFromOrder.size() - 1); //remove duplicate order delivery node
                        Collections.reverse(routeFromOrder);
                    }

                    routeToOrder.addAll(routeFromOrder);

                    newRoute.addAll(nodes.indexOf(possibleAttachment) + 1, routeToOrder);

                    switch (compareRoute(bestNewRoute, newRoute, order, currentTick)) {
                        case BREAK -> {
                            pendingOrders.add(order);
                            return;
                        }
                        case SWITCH -> {
                            bestVehicle = responsibleVehicle;
                            bestNewRoute = newRoute;
                        }
                    }
                }

            }

            if (bestVehicle == null) {
                pendingOrders.add(order);
                return;
            }

            long deliveryDuration = getDeliveryDuration(order, bestNewRoute);
            if (deliveryDuration + currentTick > order.getDeliveryInterval().start()) {
                planedRoutes.put(bestVehicle, bestNewRoute);
                return;
            }

            pendingOrders.add(order);
        }

        public void tick(long currentTick, List<ConfirmedOrder> newOrders) {

            for (ConfirmedOrder order : new ArrayList<>(pendingOrders)) {
                pendingOrders.remove(order);
                acceptOrder(order, currentTick);
            }

            for (ConfirmedOrder order : newOrders) {
                acceptOrder(order, currentTick);
            }

            for (Map.Entry<Vehicle, List<RouteNode>> plannedRoute : new HashSet<>(planedRoutes.entrySet())) {
                Vehicle responsibleVehicle = plannedRoute.getKey();
                List<RouteNode> route = plannedRoute.getValue();

                if (route.isEmpty()) {
                    continue;
                }

                if (getTicksUntilOff(route, currentTick) < 5 || getWeight(route) >= 0.95 * responsibleVehicle.getCapacity()) {
                    moveVehicle(responsibleVehicle, currentTick);
                }
            }
        }

        public void addVehicle(Vehicle vehicle) {
            planedRoutes.put(vehicle, new ArrayList<>());
            queuedVehicles.remove(vehicle);
        }

        public void removeVehicle(Vehicle vehicle) {
            planedRoutes.remove(vehicle);
        }

        public void addQueuedVehicle(Vehicle vehicle) {
            queuedVehicles.add(vehicle);
        }

        public Region.Restaurant getManaged() {
            return managed;
        }

        public Collection<? extends ConfirmedOrder> getPendingOrders() {
            return pendingOrders;
        }

        public void reset() {
            pendingOrders.clear();
        }

        private void moveVehicle(Vehicle vehicle, long currentTick) {
            List<RouteNode> route = planedRoutes.get(vehicle);

            for (RouteNode routeNode : route) {
                if (routeNode.orders.isEmpty()) {
                    continue;
                }

                for (ConfirmedOrder order : routeNode.orders) {
                    vehicleManager.getOccupiedRestaurant(managed).loadOrder(vehicle, order, currentTick);
                }

                vehicle.moveQueued(routeNode.node(), (v, t) -> {
                    routeNode.orders().forEach(o -> {
                        vehicleManager.getOccupiedNeighborhood((Region.Node) v.getOccupied().getComponent()).deliverOrder(v, o, t);
                    });
                });
            }

            RestaurantManager leastVehiclesManager = null;

            for (RestaurantManager restaurantManager : managers) {
                if (leastVehiclesManager == null
                    || restaurantManager.getTotalAvailableVehicle().size() < leastVehiclesManager.getTotalAvailableVehicle().size()) {
                    leastVehiclesManager = restaurantManager;
                }
            }

            assert leastVehiclesManager != null;
            vehicle.moveQueued(leastVehiclesManager.managed);
            leastVehiclesManager.addQueuedVehicle(vehicle);
            removeVehicle(vehicle);
        }


        private final static int KEEP = 0;
        private final static int SWITCH = 1;
        private final static int BREAK = 2;
        private int compareRoute(List<RouteNode> oldRoute, List<RouteNode> newRoute, ConfirmedOrder order, long currentTick) {

            //if the order would be delivered too early, don't load it
            if (getDeliveryDuration(order, newRoute) + currentTick < order.getDeliveryInterval().start()) {
                return BREAK;
            }

            //if no old route is given choose the new route
            if (oldRoute == null) {
                return SWITCH;
            }

            long oldTicksOff = getTotalTicksOffForRoute(oldRoute, currentTick);
            long newTicksOff = getTotalTicksOffForRoute(newRoute, currentTick);

            //if both routes are on time, choose the one with the least distance
            if (oldTicksOff == 0 && newTicksOff == 0) {
                long oldDistance = getDistance(oldRoute);
                long newDistance = getDistance(newRoute);

                if (newDistance < oldDistance) {
                    return SWITCH;
                }

                return KEEP;
            }

            //if the new route is faster than the old route choose it
            if (newTicksOff < oldTicksOff) {
                return SWITCH;
            }

            //if the old route is faster than the new route keep it
            return KEEP;
        }

        private double getWeight(List<RouteNode> route) {
            double weight = 0;

            for (RouteNode routeNode : route) {
                for (ConfirmedOrder order : routeNode.orders) {
                    weight += order.getWeight();
                }
            }

            return weight;
        }

        private long getDistance(List<RouteNode> route) {
            long distance = 0L;
            Region.Node previous = managed;

            for (RouteNode routeNode : route) {
                distance += Objects.requireNonNull(region.getEdge(previous, routeNode.node)).getDuration();
                previous = routeNode.node;
            }

            return distance;
        }

        private long getDeliveryDuration(ConfirmedOrder order, List<RouteNode> route) {
            long distance = 0L;
            Region.Node previous = managed;

            for (RouteNode routeNode : route) {
                distance += Objects.requireNonNull(region.getEdge(previous, routeNode.node())).getDuration();
                previous = routeNode.node;

                if (routeNode.node().getLocation().equals(order.getLocation())) {
                    return distance;
                }
            }

            throw new IllegalArgumentException("Order not in route");
        }

        private long getTotalTicksOffForRoute(List<RouteNode> route, long currentTick) {

            Region.Node previous = managed;
            long distance = 0L;
            long ticksOff = 0L;

            for (RouteNode routeNode : route) {
                distance += Objects.requireNonNull(region.getEdge(previous, routeNode.node)).getDuration();
                previous = routeNode.node;

                for (ConfirmedOrder order : routeNode.orders) {
                    ticksOff += Math.abs(getTicksOff(order, distance + currentTick));
                }
            }

            return ticksOff;
        }

        private long getTicksUntilOff(List<RouteNode> route, long currentTick) {

            Region.Node previous = managed;
            long distance = 0L;
            long ticksUntilOff = Long.MAX_VALUE;

            for (RouteNode routeNode : route) {
                distance += Objects.requireNonNull(region.getEdge(previous, routeNode.node)).getDuration();
                previous = routeNode.node;

                for (ConfirmedOrder order : routeNode.orders) {
                    if (order.getDeliveryInterval().end() > distance + currentTick) {
                        ticksUntilOff = 0;
                    } else if (order.getDeliveryInterval().start() < distance + currentTick) {
                        ticksUntilOff = Math.min(ticksUntilOff, order.getDeliveryInterval().end() - currentTick - distance);
                    }
                }
            }

            return ticksUntilOff;
        }

        private long getTicksOff(ConfirmedOrder order, long deliveryTime) {
            TickInterval deliveryInterval = order.getDeliveryInterval();

            if (deliveryInterval.start() > deliveryTime) {
                return deliveryInterval.start() - deliveryTime;
            }

            if (deliveryTime > deliveryInterval.end()) {
                return deliveryTime - deliveryInterval.end();
            }

            return 0;

        }

        public List<Vehicle> getUnusedVehicles() {
            return planedRoutes.keySet().stream().filter(v -> planedRoutes.get(v).isEmpty()).collect(Collectors.toList());
        }

        public List<Vehicle> getTotalAvailableVehicle() {
            return queuedVehicles;
        }

    }

    private record RouteNode(Region.Node node, List<ConfirmedOrder> orders) {

        public RouteNode copy() {
            return new RouteNode(node, new ArrayList<>(orders));
        }

    }

    private List<RouteNode> copyRoute(List<RouteNode> route) {
        return route.stream().map(RouteNode::copy).collect(Collectors.toCollection(ArrayList::new));
    }

}
