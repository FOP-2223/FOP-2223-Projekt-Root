package projekt.delivery.vehicle;

import projekt.delivery.FoodNotSupportedException;
import projekt.food.Food;
import projekt.food.FoodType;

import java.util.Collection;
import java.util.Deque;

public interface Vehicle {

    /**
     * The current {@link Region.Component} that this entity is on.
     *
     * <p>
     * This may be a {@link Region.Edge} or a {@link Region.Node}.
     * </p>
     *
     * @return The current {@link Region.Component} that this entity is on
     */
    VehicleManager.Occupied<?> getOccupied();

    /**
     * Deletes the entire move queue and moves directly to the provided {@link Region.Node}.
     */
    void moveDirect(Region.Node node);

    /**
     * Adds the provided {@link Region.Node} to the move queue.
     */
    void moveQueued(Region.Node node);

    Deque<Region.Node> getMoveQueue();

    int getId();

    /**
     * The maximum acceptable weight of the total cargo in KG.
     */
    double getCapacity();

    /**
     *
     */
    Collection<Food> getFood();

    Collection<FoodType<?, ?>> getCompatibleFoodTypes();

    /**
     * @throws FoodNotSupportedException if the vehicle does not support the provided food
     */
    void addFood(Food food) throws VehicleOverloadedException;

    // TODO: allow appending of own methods?
    default double getCurrentWeight() {
        return getFood().stream().mapToDouble(Food::getWeight).sum();
    }
}
