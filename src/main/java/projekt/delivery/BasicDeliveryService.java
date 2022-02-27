package projekt.delivery;

import projekt.delivery.routing.VehicleManager;
import projekt.rating.Rater;

import java.util.List;

public class BasicDeliveryService extends AbstractDeliveryService {
    protected BasicDeliveryService(VehicleManager vehicleManager, Rater rater, Simulation simulation, SimulationConfig simulationConfig) {
        super(vehicleManager, rater, simulation, simulationConfig);
    }

    @Override
    void tick(List<ConfirmedOrder> newOrders) {

    }
}
