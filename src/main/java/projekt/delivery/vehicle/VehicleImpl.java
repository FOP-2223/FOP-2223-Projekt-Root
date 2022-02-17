package projekt.delivery.vehicle;

import projekt.food.Food;
import projekt.food.FoodType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

class VehicleImpl implements Vehicle {

    private final int id;
    private final double capacity;
    private final List<Food> foods = new ArrayList<>();
    private final Iterable<FoodType<?, ?>> compatibleFoodTypes;
    private VehicleManager.Occupied<?> occupied;
    private final VehicleManager vehicleManager;

    public VehicleImpl(
        int id,
        double capacity,
        Iterable<FoodType<?, ?>> compatibleFoodTypes,
        VehicleManager.Occupied<?> occupied,
        VehicleManager vehicleManager
    ) {
        this.id = id;
        this.capacity = capacity;
        this.compatibleFoodTypes = compatibleFoodTypes;
        this.occupied = occupied;
        this.vehicleManager = vehicleManager;
    }

    @Override
    public VehicleManager.Occupied<?> getOccupied() {
        return occupied;
    }

    @Override
    public void moveDirect(Region.Node node) {
        if (occupied instanceof Region.Node currentNode) {

        }
    }

    @Override
    public void moveQueued(Region.Node node) {

    }

    @Override
    public Deque<Region.Node> getMoveQueue() {
        return null;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public double getCapacity() {
        return capacity;
    }

    public List<Food> getFoods() {
        return foods;
    }

    @Override
    public Collection<Food> getFood() {
        return foods;
    }

    @Override
    public Collection<FoodType<?, ?>> getCompatibleFoodTypes() {
        return compatibleFoodTypes;
    }

    @Override
    public void addFood(Food food) throws VehicleOverloadedException {
        double capacityNeeded = getCurrentWeight() + food.getWeight();

        if (capacityNeeded > capacity) {
            throw new VehicleOverloadedException(this, capacityNeeded);
        }

        foods.add(food);
    }
}
