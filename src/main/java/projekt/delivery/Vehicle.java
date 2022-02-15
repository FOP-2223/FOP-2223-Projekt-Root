package projekt.delivery;

import projekt.food.Food;

import java.util.stream.Stream;

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
    Region.Component<?> getComponent();

    int getId();

    /**
     * The maximum acceptable weight of the total cargo in KG.
     */
    double getCapacity();

    /**
     *
     */
    Stream<Food> streamFood();

    /**
     *
     * @param food
     * @throws FoodNotSupportedException if the vehicle does not support the provided food
     */
    void addFood(Food food) throws VehicleOverloadedException;

    // TODO: allow appending of own methods?
    default double getCurrentWeight() {
        return streamFood().mapToDouble(Food::getWeight).sum();
    }
}
