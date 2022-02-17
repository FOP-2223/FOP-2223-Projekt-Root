package projekt.delivery.vehicle;

import org.jetbrains.annotations.Nullable;
import projekt.base.DistanceCalculator;
import projekt.food.FoodType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class VehicleManagerImpl implements VehicleManager {

    private final Region region;
    private final DistanceCalculator distanceCalculator;
    private final Predicate<? super Occupied<Region.Node>> defaultNodePredicate;
    private final Occupied<Region.Node> defaultNode;
    private final List<VehicleImpl> vehicles = new ArrayList<>();
    private final Collection<Vehicle> unmodifiableVehicles = Collections.unmodifiableCollection(vehicles);
    private final Map<Region.Node, Occupied<Region.Node>> occupiedNodes;
    private final Map<Region.Edge, Occupied<Region.Edge>> occupiedEdges;

    VehicleManagerImpl(
        Region region,
        DistanceCalculator distanceCalculator,
        Predicate<? super Occupied<Region.Node>> defaultNodePredicate
    ) {
        this.region = region;
        this.distanceCalculator = distanceCalculator;
        this.defaultNodePredicate = defaultNodePredicate;
        occupiedNodes = toOccupied(region.getNodes());
        occupiedEdges = toOccupied(region.getEdges());
        defaultNode = findNode(defaultNodePredicate);
    }

    private <C extends Region.Component<C>> Map<C, Occupied<C>> toOccupied(Collection<C> original) {
        return original.stream()
            .map(c -> new OccupiedImpl<>(c, this))
            .collect(Collectors.toUnmodifiableMap(OccupiedImpl::getComponent, Function.identity()));
    }

    private Occupied<Region.Node> findNode(Predicate<? super Occupied<Region.Node>> nodePredicate) {
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
    public Vehicle addVehicle(
        double capacity,
        Iterable<FoodType<?, ?>> compatibleFoodTypes,
        @Nullable Predicate<? super Occupied<Region.Node>> nodePredicate
    ) {
        final Occupied<Region.Node> occupied;
        if (nodePredicate == null) {
            occupied = defaultNode;
        } else {
            occupied = findNode(nodePredicate);
        }
        VehicleImpl vehicle = new VehicleImpl(vehicles.size(), capacity, compatibleFoodTypes, occupied, this);
        vehicles.add(vehicle);
        return vehicle;
    }

    @Override
    public Vehicle addVehicle(double capacity, Iterable<FoodType<?, ?>> compatibleFoodTypes) {
        return addVehicle(capacity, compatibleFoodTypes, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Region.Component<C>> Occupied<C> getOccupied(C component) {
        if (component instanceof Region.Node) {
            final @Nullable Occupied<C> result = (Occupied<C>) occupiedNodes.get(component);
            if (result == null) {
                throw new IllegalArgumentException("Could not find occupied node for " + component.getName());
            }
            return result;
        } else if (component instanceof Region.Edge) {
            final @Nullable Occupied<C> result = (Occupied<C>) occupiedEdges.get(component);
            if (result == null) {
                throw new IllegalArgumentException("Could not find occupied edge for " + component.getName());
            }
            return result;
        }
        throw new IllegalArgumentException("Component is not of recognized subtype: " + component.getClass().getName());
    }

    @Override
    public Collection<Occupied<Region.Node>> getOccupiedNodes() {
        return occupiedNodes.values();
    }

    @Override
    public Collection<Occupied<Region.Edge>> getOccupiedEdges() {
        return occupiedEdges.values();
    }
}
