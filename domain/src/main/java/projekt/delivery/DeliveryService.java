package projekt.delivery;

import projekt.delivery.rating.Rater;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;

import java.util.List;

public interface DeliveryService {

    Factory SIMPLE = BasicDeliveryService::new;
    Factory BOGO = BogoDeliveryService::new;

    void deliver(List<ConfirmedOrder> confirmedOrders);

    void tick(long currentTick);

    interface Factory {

        DeliveryService create(VehicleManager vehicleManager, Rater rater);
    }
}
