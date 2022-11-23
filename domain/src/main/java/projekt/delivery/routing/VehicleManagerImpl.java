package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.delivery.event.Event;
import projekt.delivery.event.EventBus;
import projekt.delivery.event.SpawnEvent;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class VehicleManagerImpl implements VehicleManager {

    final Map<Region.Node, OccupiedNodeImpl<? extends Region.Node>> occupiedNodes;
    final Map<Region.Edge, OccupiedEdgeImpl> occupiedEdges;
    private final Region region;
    private final PathCalculator pathCalculator;
    private final OccupiedWarehouseImpl warehouse;
    private final List<VehicleImpl> vehiclesToSpawn = new ArrayList<>();
    private final List<VehicleImpl> vehicles = new ArrayList<>();
    private final Collection<Vehicle> unmodifiableVehicles = Collections.unmodifiableCollection(vehicles);
    private final Set<AbstractOccupied<?>> allOccupied;
    private final EventBus eventBus = new EventBus();
    private long currentTick;

    VehicleManagerImpl(
        long currentTick,
        Region region,
        PathCalculator pathCalculator,
        Region.Node warehouse
    ) {
        this.currentTick = currentTick;
        this.region = region;
        this.pathCalculator = pathCalculator;
        this.warehouse = new OccupiedWarehouseImpl(warehouse, this);
        occupiedNodes = toOccupiedNodes(region.getNodes());
        occupiedEdges = toOccupiedEdges(region.getEdges());
        allOccupied = getAllOccupied();
    }

    private OccupiedNodeImpl<? extends Region.Node> toOccupied(Region.Node node) {
        return node.equals(warehouse.getComponent())
            ? warehouse
            : node instanceof Region.Neighborhood
                ? new OccupiedNeighborhoodImpl((Region.Neighborhood) node, this)
                : new OccupiedNodeImpl<>(node, this);
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

    private OccupiedNodeImpl<?> findNode(Predicate<? super Occupied<? extends Region.Node>> nodePredicate) {
        return occupiedNodes.values().stream()
            .filter(nodePredicate)
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
    public OccupiedWarehouseImpl getWarehouse() {
        return warehouse;
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
    public OccupiedNeighborhood getOccupiedNeighborhood(Region.Node component) {
        Objects.requireNonNull(component, "component is null!");
        final @Nullable OccupiedNodeImpl<?> node = occupiedNodes.get(component);
        if (node instanceof OccupiedNeighborhood) {
            return (OccupiedNeighborhood) node;
        } else {
            throw new IllegalArgumentException("Component " + component + " is not a neighborhood");
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

    /*@Override
    public long getCurrentTick() {
        return currentTick;
    }*/

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

    Vehicle addVehicle(
        double capacity,
        Collection<String> compatibleFoodTypes,
        @Nullable Predicate<? super Occupied<? extends Region.Node>> nodePredicate
    ) {
        final OccupiedNodeImpl<?> occupied;
        if (nodePredicate == null) {
            occupied = warehouse;
        } else {
            occupied = findNode(nodePredicate);
        }
        final VehicleImpl vehicle = new VehicleImpl(
            vehicles.size() + vehiclesToSpawn.size(),
            capacity,
            compatibleFoodTypes,
            occupied,
            this
        );
        vehiclesToSpawn.add(vehicle);
        vehicle.setOccupied(occupied);
        return vehicle;
    }

    private void spawnVehicle(VehicleImpl vehicle, long currentTick) {
        vehicles.add(vehicle);
        OccupiedWarehouseImpl warehouse = (OccupiedWarehouseImpl) vehicle.getOccupied();
        warehouse.vehicles.put(vehicle, new AbstractOccupied.VehicleStats(currentTick, null));
        getEventBus().queuePost(SpawnEvent.of(currentTick, vehicle, warehouse.getComponent()));
    }
}
