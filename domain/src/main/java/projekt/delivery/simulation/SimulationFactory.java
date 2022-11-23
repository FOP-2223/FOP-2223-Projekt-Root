package projekt.delivery.simulation;

import projekt.delivery.archetype.OrderGenerator;
import projekt.delivery.deliveryService.DeliveryService;
import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.ConfirmedOrder;

import java.util.List;
import java.util.Map;

public class SimulationFactory {

    public Simulation createSimulation(
        DeliveryService deliveryService,
        OrderGenerator orderGenerator,
        Map<RatingCriteria, Rater.Factory> raterFactoryMap,
        SimulationConfig simulationConfig) {

        BasicDeliverySimulation simulation = new BasicDeliverySimulation(simulationConfig, raterFactoryMap, deliveryService);

        //Listener to add new orders after each tick
        simulation.addListener((events, tick) -> {
            List<ConfirmedOrder> newOrders = orderGenerator.generateOrders(tick);
            simulation.getDeliveryService().deliver(newOrders);
        });

        return simulation;
    }
}
