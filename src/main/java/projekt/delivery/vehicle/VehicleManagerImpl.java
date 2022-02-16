package projekt.delivery.vehicle;

import projekt.base.DistanceCalculator;
import projekt.food.FoodType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class VehicleManagerImpl implements VehicleManager {

    private final Region region;
    private final DistanceCalculator distanceCalculator;
    private final List<VehicleImpl> vehicles = new ArrayList<>();
    private final Collection<Vehicle> unmodifiableVehicles = Collections.unmodifiableCollection(vehicles);
    private final Map<Region.Component<?>, Occupied<?>> occupied;

    VehicleManagerImpl(Region region, DistanceCalculator distanceCalculator) {
        this.region = region;
        this.distanceCalculator = distanceCalculator;
        Map<Region.Component<?>, Occupied<?>> occupied = new HashMap<>();
        region.getNodes().forEach(c -> addOccupied(c, occupied));
        region.getEdges().forEach(c -> addOccupied(c, occupied));
        this.occupied = Collections.unmodifiableMap(occupied);
    }

    private <C extends Region.Component<C>> void addOccupied(C node, Map<? super C, ? super Occupied<C>> occupied) {
        occupied.put(node, new OccupiedImpl<>(node, this));
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
    public Vehicle addVehicle(double capacity, Iterable<FoodType<?, ?>> compatibleFoodTypes) {

    }

    @Override
    public <C extends Region.Component<C>> Occupied<C> getOccupied(C component) {
        return null;
    }

    @Override
    public Collection<Occupied<Region.Node>> getOccupiedNodes() {
        return null;
    }

    @Override
    public Collection<Occupied<Region.Edge>> getOccupiedEdges() {
        return null;
    }
}
