package projekt.delivery;

import projekt.delivery.event.Event;
import projekt.delivery.rating.Rater;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;

import java.util.List;

public class FopEx extends AbstractDeliveryService {

    public FopEx(
        VehicleManager vehicleManager,
        Rater rater,
        Simulation simulation,
        SimulationConfig simulationConfig
    ) {
        super(vehicleManager, rater, simulation, simulationConfig);
    }

    @Override
    void tick(List<ConfirmedOrder> newOrders) {
        List<Event> events = vehicleManager.tick();
        // TODO: H9
    }
}
