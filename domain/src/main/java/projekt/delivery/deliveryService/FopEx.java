package projekt.delivery.deliveryService;

import projekt.delivery.event.Event;
import projekt.delivery.rating.Rater;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;

import java.util.ArrayList;
import java.util.List;

public class FopEx extends AbstractDeliveryService {

    // List of orders that have not yet been loaded onto delivery vehicles
    protected final List<ConfirmedOrder> pendingOrders = new ArrayList<>();

    public FopEx(
        VehicleManager vehicleManager
    ) {
        super(vehicleManager);
    }

    @Override
    List<Event> tick(long currentTick, List<ConfirmedOrder> newOrders) {
        List<Event> events = vehicleManager.tick(currentTick);
        // TODO: H9
        return events;
    }

    @Override
    public List<ConfirmedOrder> getPendingOrders() {
        return null;
    }
}
