package projekt.delivery.deliveryService;

import projekt.delivery.event.Event;
import projekt.delivery.rating.Rater;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;

import java.util.List;

public class FopEx extends AbstractDeliveryService {

    public FopEx(
        VehicleManager vehicleManager,
        Rater rater,
        Simulation simulation,
        SimulationConfig simulationConfig
    ) {
        super(vehicleManager, rater);
    }

    @Override
    List<Event> tick(long currentTick, List<ConfirmedOrder> newOrders) {
        List<Event> events = vehicleManager.tick(currentTick);
        // TODO: H9
        return events;
    }
}
