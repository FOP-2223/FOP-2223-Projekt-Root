package projekt.delivery.deliveryService;

import projekt.delivery.event.Event;
import projekt.delivery.rating.Rater;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.solver.ProblemSolver;

import java.util.*;

public class ProblemSolverDeliveryService extends AbstractDeliveryService {

    private final Map<Vehicle, Map<Long, List<ConfirmedOrder>>> solution;
    protected final List<ConfirmedOrder> pendingOrders = new ArrayList<>();

    public ProblemSolverDeliveryService(
        VehicleManager vehicleManager,
        Rater rater,
        ProblemSolver problemSolver
    ) {
        super(vehicleManager, rater);
        this.solution = problemSolver.solve();
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
                    .filter(order -> !ordersReadyForVehicle.contains(order))
                    .toList();

                ListIterator<ConfirmedOrder> it = ordersPendingForVehicle.listIterator();
                while (it.hasNext()) {
                    final ConfirmedOrder order = it.next();
                    if (order.getTotalWeight() < vehicle.getCapacity() - vehicle.getCurrentWeight()
                        && vehicle.getCompatibleFoodTypes().containsAll(order.getFoodList())) {
                        loadedAtLeastOneOrderOnVehicle = true;
                        vehicleManager.getWarehouse().loadOrder(vehicle, order, currentTick);
                        vehicle.moveQueued(vehicleManager.getRegion().getNode(order.getLocation()), v ->
                            vehicleManager.getOccupiedNeighborhood((Region.Node) v.getOccupied()).deliverOrder(v, order, currentTick));
                        it.remove();
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
