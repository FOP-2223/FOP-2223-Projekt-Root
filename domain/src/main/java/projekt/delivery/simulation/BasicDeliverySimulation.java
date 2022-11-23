package projekt.delivery.simulation;

import projekt.delivery.deliveryService.DeliveryService;
import projekt.delivery.event.Event;
import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasicDeliverySimulation extends AbstractSimulation {

    private final DeliveryService deliveryService;

    public BasicDeliverySimulation(SimulationConfig simulationConfig, Map<RatingCriteria, Rater.Factory> raterFactoryMap, DeliveryService deliveryService) {
        super(simulationConfig, raterFactoryMap);
        this.deliveryService = deliveryService;
    }

    @Override
    List<Event> tick() {
        return deliveryService.tick(getCurrentTick());
    }

    @Override
    public DeliveryService getDeliveryService() {
        return deliveryService;
    }
}
