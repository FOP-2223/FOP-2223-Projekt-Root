package projekt.delivery.deliveryService;

import projekt.delivery.event.Event;
import projekt.delivery.rating.Rater;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * A very simple delivery service that distributes orders to compatible vehicles in a FIFO manner.
 */
public class BasicDeliveryService extends AbstractDeliveryService {

    // List of orders that have not yet been loaded onto delivery vehicles
    protected final List<ConfirmedOrder> pendingOrders = new ArrayList<>();

    protected BasicDeliveryService(
        VehicleManager vehicleManager
    ) {
        super(vehicleManager);
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
                ListIterator<ConfirmedOrder> it = pendingOrders.listIterator();
                while (it.hasNext()) {
                    final ConfirmedOrder order = it.next();
                    if (order.getTotalWeight() < vehicle.getCapacity() - vehicle.getCurrentWeight()
                        && vehicle.checkCompatibility(order.getFoodList())) {
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
