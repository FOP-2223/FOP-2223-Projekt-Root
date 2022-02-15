package projekt.delivery;

import projekt.food.Food;

import java.util.List;
import java.util.stream.Stream;

public class VehicleImpl implements Vehicle {

    private final int id;
    private final double capacity;
    private final List<Food> foods;

    public VehicleImpl(int id, double capacity, List<Food> foods) {
        this.id = id;
        this.capacity = capacity;
        this.foods = foods;
    }

    @Override
    public Region.Component<?> getComponent() {
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
    public Stream<Food> streamFood() {
        return foods.stream();
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
