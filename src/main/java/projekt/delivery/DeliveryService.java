package projekt.delivery;

import projekt.delivery.routing.VehicleManager;
import projekt.rating.Rater;

import java.util.List;

public interface DeliveryService {

    void deliver(List<ConfirmedOrder> confirmedOrders);

    /**
     * Start the simulation of the food delivery.
     * This method blocks the current thread and only returns when the simulation is terminated.
     * To terminate the simulation, you need to call {@link #endSimulation()} from a separate thread.
     */
    void runSimulation();

    /**
     * Stops the currently running simulation of the food delivery service.
     */
    void endSimulation();

    interface Factory {

        DeliveryService create(VehicleManager vehicleManager, Rater rater, Simulation simulation, SimulationConfig simulationConfig);
    }

    Factory SIMPLE = BasicDeliveryService::new;
}
