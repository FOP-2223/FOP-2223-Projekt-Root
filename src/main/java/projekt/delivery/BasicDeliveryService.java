package projekt.delivery;

import projekt.base.Location;
import projekt.delivery.event.Event;
import projekt.delivery.rating.Rater;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;
import projekt.food.Food;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * A very simple delivery service that distributes orders to compatible vehicles in a FIFO manner.
 */
public class BasicDeliveryService extends AbstractDeliveryService {
    // List of orders that have not yet been loaded onto delivery vehicles
    private final List<ConfirmedOrder> pendingOrders = new ArrayList<>();
    private final Region.Node pizzeriaNode;

    protected BasicDeliveryService(VehicleManager vehicleManager, Rater rater, Simulation simulation, SimulationConfig simulationConfig) {
        super(vehicleManager, rater, simulation, simulationConfig);
        pizzeriaNode = vehicleManager.getRegion().getNode(new Location(0, 0));
    }

    @Override
    void tick(List<ConfirmedOrder> newOrders) {
        // Move vehicles forward.
        List<Event> events = vehicleManager.tick();

        // Add all newly arrived orders to the list of pending orders.
        pendingOrders.addAll(newOrders);

        // Prioritize orders according to their expected delivery times.
        pendingOrders.sort(Comparator.comparing(order -> order.getTimeInterval().getStart()));

        // For each vehicle waiting in the pizzeria, load as many orders as possible on the vehicle and send it out.
        vehicleManager.getOccupied(pizzeriaNode).getVehicles().stream()
            .filter(vehicle -> vehicle.getFood().isEmpty()).forEach(vehicle -> {
                boolean loadedAtLeastOneOrderOnVehicle = false;
                ListIterator<ConfirmedOrder> it = pendingOrders.listIterator();
                while (it.hasNext()) {
                    ConfirmedOrder order = it.next();
                    if (order.getTotalWeight() < vehicle.getCapacity() - vehicle.getCurrentWeight()
                        && order.getFoodList().stream()
                        .map(Food::getFoodVariant)
                        .map(Food.Variant::getFoodType)
                        .allMatch(vehicle.getCompatibleFoodTypes()::contains)) {
                        loadedAtLeastOneOrderOnVehicle = true;
                        order.getFoodList().forEach(vehicle::addFood);
                        vehicle.moveQueued(vehicleManager.getRegion().getNode(order.getLocation()), v ->
                            order.getFoodList().forEach(v::unloadFood));
                        it.remove();
                    }
                }

                // If the vehicle leaves the pizzeria, ensure that it returns after delivering the last order.
                if (loadedAtLeastOneOrderOnVehicle) {
                    vehicle.moveQueued(pizzeriaNode);
                }
            });
    }
}
