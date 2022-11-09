package projekt.delivery.routing;

public class FoodNotSupportedException extends RuntimeException {
    public FoodNotSupportedException(Vehicle vehicle, String food) {
        super(String.format(
            "%s with id %d does not support food %s",
            vehicle.getClass().getSimpleName(),
            vehicle.getId(),
            food
        ));
    }
}
