package projekt.delivery;

import projekt.delivery.routing.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

public class AngleDestinationPartitioner implements DestinationPartitioner {

    @Override
    public Map<Vehicle, List<ConfirmedOrder>> partition(List<Vehicle> vehicles, List<ConfirmedOrder> orders) {
        Map<Vehicle, List<ConfirmedOrder>> result = vehicles
            .stream()
            .collect(Collectors.toMap(
                vehicle -> vehicle,
                vehicle -> new ArrayList<>())
            );
        Iterator<Map.Entry<Vehicle, List<ConfirmedOrder>>> entryIterator = result.entrySet().iterator();
        Map.Entry<Vehicle, List<ConfirmedOrder>> currentEntry = entryIterator.next();

        for (ConfirmedOrder order : orders) {
            if (getCurrentVehicleWeight(currentEntry.getValue()) + order.getTotalWeight() > currentEntry.getKey().getCapacity()) {
                currentEntry = entryIterator.next();

//                continue;
            }

            currentEntry.getValue().add(order);
        }

        return result;
    }

    private double getCurrentVehicleWeight(List<ConfirmedOrder> confirmedOrders) {
        return confirmedOrders.stream().mapToDouble(ConfirmedOrder::getTotalWeight).sum();
    }
}
