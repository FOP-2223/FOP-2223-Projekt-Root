package projekt.delivery.simulation;

import projekt.delivery.deliveryService.DeliveryService;
import projekt.delivery.event.Event;

import java.util.List;

public class BasicDeliverySimulation extends AbstractSimulation {

    private final DeliveryService deliveryService;

    public BasicDeliverySimulation(SimulationConfig simulationConfig, DeliveryService deliveryService) {
        super(simulationConfig);
        this.deliveryService = deliveryService;
    }

    @Override
    List<Event> tick() {
        return deliveryService.tick(getCurrentTick());
    }

    public DeliveryService getDeliveryService() {
        return deliveryService;
    }
}
