package projekt.delivery;

import projekt.delivery.routing.Vehicle;
import projekt.food.Food;

public class FoodNotSupportedException extends RuntimeException {
    FoodNotSupportedException(Vehicle vehicle, Food food) {
        super(String.format(
            "%s with id %d does not support food %s",
            vehicle.getClass().getSimpleName(),
            vehicle.getId(),
            food.getClass().getSimpleName()
        ));
    }
}
