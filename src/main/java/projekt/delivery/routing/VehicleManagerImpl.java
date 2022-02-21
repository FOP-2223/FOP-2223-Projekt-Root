package projekt.delivery.routing;

import org.jetbrains.annotations.Nullable;
import projekt.base.DistanceCalculator;
import projekt.delivery.event.EventBus;
import projekt.food.FoodType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class VehicleManagerImpl implements VehicleManager {

    private final Region region;
    final Map<Region.Node, OccupiedNodeImpl> occupiedNodes;
    final Map<Region.Edge, OccupiedEdgeImpl> occupiedEdges;
    private final DistanceCalculator distanceCalculator;
    private final Predicate<? super Occupied<Region.Node>> defaultNodePredicate;
    private final OccupiedNodeImpl defaultNode;
    private final List<VehicleImpl> vehicles = new ArrayList<>();
    private final Collection<Vehicle> unmodifiableVehicles = Collections.unmodifiableCollection(vehicles);
    private final SortedSet<AbstractOccupied<?>> sortedOccupied;
    private final EventBus eventBus = new EventBus();

    private LocalDateTime currentTime = LocalDateTime.now(); // TODO: Initialize properly

    VehicleManagerImpl(
        Region region,
        DistanceCalculator distanceCalculator,
        Predicate<? super Occupied<Region.Node>> defaultNodePredicate
    ) {
        this.region = region;
        this.distanceCalculator = distanceCalculator;
        this.defaultNodePredicate = defaultNodePredicate;
        defaultNode = findNode(defaultNodePredicate);
        occupiedNodes = toOccupied(region.getNodes());
        occupiedEdges = toOccupied(region.getEdges());
        sortedOccupied = getAllOccupied();
    }

    @SuppressWarnings("unchecked")
    private <C extends Region.Component<C>, O extends AbstractOccupied<C>> Map<C, O> toOccupied(Collection<C> original) {
        return original.stream()
            .map(c -> {
                if (c instanceof Region.Node) {
                    return (O) new OccupiedNodeImpl((Region.Node) c, this);
                } else if (c instanceof Region.Edge) {
                    return (O) new OccupiedEdgeImpl((Region.Edge) c, this);
                } else {
                    throw new AssertionError("Component must be either node or edge");
                }
            })
            .collect(Collectors.toUnmodifiableMap(Occupied::getComponent, Function.identity()));
    }

    private SortedSet<AbstractOccupied<?>> getAllOccupied() {
        final SortedSet<AbstractOccupied<?>> result = new TreeSet<>(Comparator.comparing(Occupied::getComponent));
        result.addAll(occupiedNodes.values());
        result.addAll(occupiedEdges.values());
        return Collections.unmodifiableSortedSet(result);
    }

    private OccupiedNodeImpl findNode(Predicate<? super Occupied<Region.Node>> nodePredicate) {
        return occupiedNodes.values().stream()
            .filter(nodePredicate)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Could not find node with given predicate"));
    }

    private void tick() {
        tickOccupied(occupiedEdges, false);
        tickOccupied(occupiedNodes, false);
    }

    private <C extends Region.Component<C>, O extends AbstractOccupied<C>> void tickOccupied(Map<C, O> occupied,
                                                                                             boolean onlyIfDirty) {
        boolean ticked = false;
        for (Map.Entry<C, O> entry : occupied.entrySet()) {
            if (!onlyIfDirty || entry.getValue().dirty) {
                entry.getValue().dirty = false;
                entry.getValue().tick();
                ticked = true;
            }
        }
        if (ticked) {
            tickOccupied(occupied, true);
        }
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
    public Vehicle addVehicle(
        double capacity,
        Collection<FoodType<?, ?>> compatibleFoodTypes,
        @Nullable Predicate<? super Occupied<Region.Node>> nodePredicate
    ) {
        final OccupiedNodeImpl occupied;
        if (nodePredicate == null) {
            occupied = defaultNode;
        } else {
            occupied = findNode(nodePredicate);
        }
        final VehicleImpl vehicle = new VehicleImpl(vehicles.size(), capacity, compatibleFoodTypes, occupied, this);
        vehicles.add(vehicle);
        return vehicle;
    }

    @Override
    public Vehicle addVehicle(double capacity, Collection<FoodType<?, ?>> compatibleFoodTypes) {
        return addVehicle(capacity, compatibleFoodTypes, null);
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
    public void update() {
    }
}
