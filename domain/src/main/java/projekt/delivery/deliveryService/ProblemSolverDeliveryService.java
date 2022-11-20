package projekt.delivery.deliveryService;

import projekt.delivery.event.Event;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ProblemSolverDeliveryService extends AbstractDeliveryService {

    private final Map<Vehicle, Map<Long, List<ConfirmedOrder>>> solution;
    protected final List<ConfirmedOrder> pendingOrders = new ArrayList<>();

    public ProblemSolverDeliveryService(
        VehicleManager vehicleManager,
        Map<Vehicle, Map<Long, List<ConfirmedOrder>>> solution
    ) {
        super(vehicleManager);
        this.solution = solution;
    }

    @Override
    List<Event> tick(long currentTick, List<ConfirmedOrder> newOrders) {

        // Move vehicles forward.
        List<Event> events = vehicleManager.tick(currentTick);

        // Add all newly arrived orders to the list of pending orders.
        pendingOrders.addAll(newOrders);

        // Prioritize orders according to their expected delivery times.
        pendingOrders.sort(Comparator.comparing(order -> order.getTimeInterval().getStart()));

        // For each vehicle waiting in the pizzeria, load as many orders as possible on the vehicle and send it out.
        vehicleManager.getWarehouse().getVehicles().stream()
            .filter(vehicle -> vehicle.getOrders().isEmpty()).forEach(vehicle -> {
                boolean loadedAtLeastOneOrderOnVehicle = false;

                List<ConfirmedOrder> ordersReadyForVehicle = solution.get(vehicle).entrySet()
                    .stream()
                    .filter(entry -> entry.getKey() <= currentTick)
                    .map(Map.Entry::getValue)
                    .flatMap(List::stream)
                    .toList();

                List<ConfirmedOrder> ordersPendingForVehicle = pendingOrders
                    .stream()
                    .filter(ordersReadyForVehicle::contains)
                    .toList();

                for (ConfirmedOrder order : ordersPendingForVehicle) {
                    if (order.getTotalWeight() < vehicle.getCapacity() - vehicle.getCurrentWeight()
                        && vehicle.checkCompatibility(order.getFoodList())) {
                        loadedAtLeastOneOrderOnVehicle = true;
                        vehicleManager.getWarehouse().loadOrder(vehicle, order, currentTick);
                        vehicle.moveQueued(vehicleManager.getRegion().getNode(order.getLocation()), v ->
                            vehicleManager.getOccupiedNeighborhood((Region.Node) v.getOccupied().getComponent()).deliverOrder(v, order, currentTick));
                        pendingOrders.remove(order);
                    }
                }

                // If the vehicle leaves the pizzeria, ensure that it returns after delivering the last order.
                if (loadedAtLeastOneOrderOnVehicle) {
                    vehicle.moveQueued(vehicleManager.getWarehouse().getComponent());
                }
            });

        return events;
    }

    @Override
    public List<ConfirmedOrder> getPendingOrders() {
        return pendingOrders;
    }
}
