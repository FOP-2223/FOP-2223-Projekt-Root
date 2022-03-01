package projekt.delivery;

import projekt.delivery.rating.Rater;
import projekt.delivery.routing.VehicleManager;

import java.util.List;

public class FOPex extends AbstractDeliveryService {

    public FOPex(
        VehicleManager vehicleManager,
        Rater rater,
        Simulation simulation,
        SimulationConfig simulationConfig
    ) {
        super(vehicleManager, rater, simulation, simulationConfig);
    }

    @Override
    void tick(List<ConfirmedOrder> newOrders) {
        // TODO: H9
    }
}
