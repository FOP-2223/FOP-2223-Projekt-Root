package projekt.delivery;

import projekt.delivery.vehicle.Vehicle;

import java.util.List;
import java.util.Map;

public interface DestinationPartitioner {
    Map<Vehicle, List<ConfirmedOrder>> partition(List<Vehicle> vehicles, List<ConfirmedOrder> orders);
}
