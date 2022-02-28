package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.DistanceCalculator;
import projekt.delivery.event.Event;
import projekt.delivery.event.EventBus;
import projekt.delivery.event.SpawnEvent;
import projekt.food.FoodType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class VehicleManagerImpl implements VehicleManager {

    private LocalDateTime currentTime;
    private final Region region;
    final Map<Region.Node, OccupiedNodeImpl> occupiedNodes;
    final Map<Region.Edge, OccupiedEdgeImpl> occupiedEdges;
    private final DistanceCalculator distanceCalculator;
    private final PathCalculator pathCalculator;
    private final OccupiedWarehouseImpl warehouse;
    private final List<VehicleImpl> vehicles = new ArrayList<>();
    private final Collection<Vehicle> unmodifiableVehicles = Collections.unmodifiableCollection(vehicles);
    private final Set<AbstractOccupied<?>> allOccupied;
    private final EventBus eventBus = new EventBus();

    VehicleManagerImpl(
        LocalDateTime currentTime,
        Region region,
        DistanceCalculator distanceCalculator,
        PathCalculator pathCalculator,
        Region.Node warehouse
    ) {
        this.currentTime = currentTime;
        this.region = region;
        this.distanceCalculator = distanceCalculator;
        this.pathCalculator = pathCalculator;
        this.warehouse = new OccupiedWarehouseImpl(warehouse, this);
        occupiedNodes = toOccupiedNodes(region.getNodes());
        occupiedEdges = toOccupiedEdges(region.getEdges());
        allOccupied = getAllOccupied();
    }

    private Map<Region.Node, OccupiedNodeImpl> toOccupiedNodes(Collection<Region.Node> nodes) {
        return nodes.stream()
            .map(node -> node.equals(warehouse.getComponent())
                ? warehouse
                : new OccupiedNodeImpl(node, this))
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

    private OccupiedNodeImpl findNode(Predicate<? super Occupied<Region.Node>> nodePredicate) {
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
    public DistanceCalculator getDistanceCalculator() {
        return distanceCalculator;
    }

    @Override
    public Collection<Vehicle> getVehicles() {
        return unmodifiableVehicles;
    }

    @Override
    public OccupiedWarehouseImpl getWarehouse() {
        return warehouse;
    }

    Vehicle addVehicle(
        double capacity,
        Collection<FoodType<?, ?>> compatibleFoodTypes,
        @Nullable Predicate<? super Occupied<Region.Node>> nodePredicate
    ) {
        final OccupiedNodeImpl occupied;
        if (nodePredicate == null) {
            occupied = warehouse;
        } else {
            occupied = findNode(nodePredicate);
        }
        final VehicleImpl vehicle = new VehicleImpl(vehicles.size(), capacity, compatibleFoodTypes, occupied, this);
        vehicles.add(vehicle);
        occupied.vehicles.put(vehicle, new AbstractOccupied.VehicleStats(currentTime, null));
        vehicle.setOccupied(occupied);
        getEventBus().queuePost(SpawnEvent.of(currentTime, vehicle, occupied.getComponent()));
        return vehicle;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Region.Component<C>> AbstractOccupied<C> getOccupied(C component) {
        if (component instanceof Region.Node) {
            final @Nullable AbstractOccupied<C> result = (AbstractOccupied<C>) occupiedNodes.get(component);
            if (result == null) {
                throw new IllegalArgumentException("Could not find occupied node for " + component.getName());
            }
            return result;
        } else if (component instanceof Region.Edge) {
            final @Nullable AbstractOccupied<C> result = (AbstractOccupied<C>) occupiedEdges.get(component);
            if (result == null) {
                throw new IllegalArgumentException("Could not find occupied edge for " + component.getName());
            }
            return result;
        }
        throw new IllegalArgumentException("Component is not of recognized subtype: " + component.getClass().getName());
    }

    @Override
    public Collection<Occupied<Region.Node>> getOccupiedNodes() {
        return Collections.unmodifiableCollection(occupiedNodes.values());
    }

    @Override
    public Collection<Occupied<Region.Edge>> getOccupiedEdges() {
        return Collections.unmodifiableCollection(occupiedEdges.values());
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    @Override
    public void tick() {
        currentTime = currentTime.plusMinutes(1);
        // It is important that nodes are ticked before edges
        // This only works because edge ticking is idempotent
        // Otherwise, there may be two state changes in a single tick.
        // For example, a node tick may move a vehicle onto an edge.
        // Ticking this edge afterwards does not move the vehicle further along the edge
        // compared to a vehicle already on the edge.
        occupiedNodes.values().forEach(AbstractOccupied::tick);
        occupiedEdges.values().forEach(AbstractOccupied::tick);
        final List<Event> events = eventBus.popEvents(currentTime);
    }

    @Override
    public PathCalculator getPathCalculator() {
        return pathCalculator;
    }
}
