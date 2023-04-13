package projekt.solution;

import org.jetbrains.annotations.Nullable;
import projekt.base.Location;
import projekt.delivery.event.Event;
import projekt.delivery.event.EventBus;
import projekt.delivery.event.SpawnEvent;
import projekt.delivery.routing.AbstractOccupied;
import projekt.delivery.routing.OccupiedEdgeImpl;
import projekt.delivery.routing.OccupiedNeighborhoodImpl;
import projekt.delivery.routing.OccupiedNodeImpl;
import projekt.delivery.routing.OccupiedRestaurantImpl;
import projekt.delivery.routing.PathCalculator;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleImpl;
import projekt.delivery.routing.VehicleManagerImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("DuplicatedCode")
public class TutorVehicleManager extends VehicleManagerImpl {

    private final Collection<Vehicle> unmodifiableVehicles = Collections.unmodifiableCollection(vehicles);

    public TutorVehicleManager(
        Region region,
        PathCalculator pathCalculator
    ) {
        super(region, pathCalculator);
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
            throw new IllegalArgumentException("Node " + node + " is not a restaurant");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Region.Component<C>> AbstractOccupied<C> getOccupied(C component) {
        Objects.requireNonNull(component, "Component is null!");
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
            throw new IllegalArgumentException("Node " + node + " is not a neighborhood");
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