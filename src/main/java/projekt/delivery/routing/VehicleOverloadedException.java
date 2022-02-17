package projekt.delivery.routing;

public class VehicleOverloadedException extends Exception {
    VehicleOverloadedException(Vehicle vehicle, double necessaryCapacity) {
        super(String.format(
            "Vehicle with id %d is overloaded! Maximum capacity: %f Necessary capacity: %f",
            vehicle.getId(),
            vehicle.getCapacity(),
            necessaryCapacity
        ));
    }
}
