package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;
import projekt.delivery.event.Event;
import projekt.delivery.event.EventBus;
import projekt.delivery.event.SpawnEvent;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class VehicleManagerImpl implements VehicleManager {

    final Map<Region.Node, OccupiedNodeImpl<? extends Region.Node>> occupiedNodes;
    final Map<Region.Edge, OccupiedEdgeImpl> occupiedEdges;
    private final Region region;
    private final PathCalculator pathCalculator;
    private final List<VehicleImpl> vehiclesToSpawn = new ArrayList<>();
    private final List<VehicleImpl> vehicles = new ArrayList<>();
    private final Collection<Vehicle> unmodifiableVehicles = Collections.unmodifiableCollection(vehicles);
    private final EventBus eventBus = new EventBus();

    VehicleManagerImpl(
        Region region,
        PathCalculator pathCalculator
    ) {
        this.region = region;
        this.pathCalculator = pathCalculator;
        occupiedNodes = toOccupiedNodes(region.getNodes());
        occupiedEdges = toOccupiedEdges(region.getEdges());

        if (getOccupiedRestaurants().size() == 0) {
            throw new IllegalArgumentException("At least one restaurant is required to create a VehicleManager");
        }
    }

    private OccupiedNodeImpl<? extends Region.Node> toOccupied(Region.Node node) {

        if (node instanceof Region.Restaurant restaurant) return new OccupiedRestaurantImpl(restaurant, this);
        if (node instanceof Region.Neighborhood neighborhood) return new OccupiedNeighborhoodImpl(neighborhood, this);
        return new OccupiedNodeImpl<>(node, this);
    }

    private Map<Region.Node, OccupiedNodeImpl<? extends Region.Node>> toOccupiedNodes(Collection<Region.Node> nodes) {
        return nodes.stream()
            .map(this::toOccupied)
            .collect(Collectors.toUnmodifiableMap(Occupied::getComponent, Function.identity()));
    }

    private Map<Region.Edge, OccupiedEdgeImpl> toOccupiedEdges(Collection<Region.Edge> edges) {
        return edges.stream()
            .map(edge -> new OccupiedEdgeImpl(edge, this))
            .collect(Collectors.toUnmodifiableMap(Occupied::getComponent, Function.identity()));
    }

    private Set<AbstractOccupied<?>> getAllOccupied() {
        final Set<AbstractOccupied<?>> result = new HashSet<>();
        result.addAll(occupiedNodes.values());
        result.addAll(occupiedEdges.values());
        return Collections.unmodifiableSet(result);
    }

    private OccupiedNodeImpl<? extends Region.Node> getOccupiedNode(Location location) {
        return occupiedNodes.values().stream()
            .filter(node -> node.getComponent().getLocation().equals(location))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Could not find node with given predicate"));
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public PathCalculator getPathCalculator() {
        return pathCalculator;
    }

    @Override
    public Collection<Vehicle> getVehicles() {
        return unmodifiableVehicles;
    }

    @Override
    public Collection<Vehicle> getAllVehicles() {
        Collection<Vehicle> allVehicles = new ArrayList<>(getVehicles());
        allVehicles.addAll(vehiclesToSpawn);
        return allVehicles;
    }

    @Override
    public List<OccupiedRestaurant> getOccupiedRestaurants() {
        return occupiedNodes.values().stream()
            .filter(OccupiedRestaurant.class::isInstance)
            .map(OccupiedRestaurant.class::cast)
            .toList();
    }

    @Override
    public OccupiedRestaurant getOccupiedRestaurant(Region.Node node) {
        Objects.requireNonNull(node, "Node is null!");
        final @Nullable OccupiedNodeImpl<? extends Region.Node> occupiedNode = occupiedNodes.get(node);
        if (occupiedNode instanceof OccupiedRestaurant) {
            return (OccupiedRestaurant) occupiedNode;
        } else {
            throw new IllegalArgumentException("Node " + occupiedNode + " is not a restaurant");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Region.Component<C>> AbstractOccupied<C> getOccupied(C component) {
        Objects.requireNonNull(component, "component is null!");
        if (component instanceof Region.Node) {
            final @Nullable AbstractOccupied<C> result = (AbstractOccupied<C>) occupiedNodes.get(component);
            if (result == null) {
                throw new IllegalArgumentException("Could not find occupied node for " + component);
            }
            return result;
        } else if (component instanceof Region.Edge) {
            final @Nullable AbstractOccupied<C> result = (AbstractOccupied<C>) occupiedEdges.get(component);
            if (result == null) {
                throw new IllegalArgumentException("Could not find occupied edge for " + component);
            }
            return result;
        }
        throw new IllegalArgumentException("Component is not of recognized subtype: " + component.getClass().getName());
    }

    @Override
    public Collection<OccupiedNeighborhood> getOccupiedNeighborhoods() {
        return occupiedNodes.values().stream()
            .filter(OccupiedNeighborhood.class::isInstance)
            .map(OccupiedNeighborhood.class::cast)
            .toList();
    }

    @Override
    public OccupiedNeighborhood getOccupiedNeighborhood(Region.Node node) {
        Objects.requireNonNull(node, "Node is null!");
        final @Nullable OccupiedNodeImpl<?> occupiedNode = occupiedNodes.get(node);
        if (occupiedNode instanceof OccupiedNeighborhood) {
            return (OccupiedNeighborhood) occupiedNode;
        } else {
            throw new IllegalArgumentException("Node " + occupiedNode + " is not a neighborhood");
        }
    }

    @Override
    public Collection<Occupied<? extends Region.Node>> getOccupiedNodes() {
        return Collections.unmodifiableCollection(occupiedNodes.values());
    }

    @Override
    public Collection<Occupied<? extends Region.Edge>> getOccupiedEdges() {
        return Collections.unmodifiableCollection(occupiedEdges.values());
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public List<Event> tick(long currentTick) {
        for (VehicleImpl vehicle : vehiclesToSpawn) {
            spawnVehicle(vehicle, currentTick);
        }
        vehiclesToSpawn.clear();
        // It is important that nodes are ticked before edges
        // This only works because edge ticking is idempotent
        // Otherwise, there may be two state changes in a single tick.
        // For example, a node tick may move a vehicle onto an edge.
        // Ticking this edge afterwards does not move the vehicle further along the edge
        // compared to a vehicle already on the edge.
        occupiedNodes.values().forEach(occupiedNode -> occupiedNode.tick(currentTick));
        occupiedEdges.values().forEach(occupiedEdge -> occupiedEdge.tick(currentTick));
        return eventBus.popEvents(currentTick);
    }

    public void reset() {
        for (AbstractOccupied<?> occupied : getAllOccupied()) {
            occupied.reset();
        }

        for (Vehicle vehicle : getAllVehicles()) {
            vehicle.reset();
        }

        vehiclesToSpawn.addAll(getVehicles().stream()
            .map(VehicleImpl.class::cast)
            .toList());

        vehicles.clear();
    }

    @SuppressWarnings("UnusedReturnValue")
    Vehicle addVehicle(
        Location startingLocation,
        double capacity
    ) {
        OccupiedNodeImpl<? extends Region.Node> occupied = getOccupiedNode(startingLocation);

        if (!(occupied instanceof OccupiedRestaurant)) {
            throw new IllegalArgumentException("Vehicles can only spawn at restaurants!");
        }

        final VehicleImpl vehicle = new VehicleImpl(
            vehicles.size() + vehiclesToSpawn.size(),
            capacity,
            this,
            (OccupiedRestaurant) occupied);
        vehiclesToSpawn.add(vehicle);
        vehicle.setOccupied(occupied);
        return vehicle;
    }

    private void spawnVehicle(VehicleImpl vehicle, long currentTick) {
        vehicles.add(vehicle);
        OccupiedRestaurantImpl warehouse = (OccupiedRestaurantImpl) vehicle.getOccupied();
        warehouse.vehicles.put(vehicle, new AbstractOccupied.VehicleStats(currentTick, null));
        getEventBus().queuePost(SpawnEvent.of(currentTick, vehicle, warehouse.getComponent()));
    }
}
