package projekt.delivery.simulation;

import projekt.delivery.deliveryService.DeliveryService;
import projekt.delivery.event.Event;
import projekt.delivery.rating.Rater;

import java.util.List;

public class BasicDeliverySimulation extends AbstractSimulation {

    private final DeliveryService deliveryService;
    private final Rater rater;

    public BasicDeliverySimulation(SimulationConfig simulationConfig, Rater rater, DeliveryService deliveryService) {
        super(simulationConfig);
        this.deliveryService = deliveryService;
        this.rater = rater;
    }

    @Override
    List<Event> tick() {
        return deliveryService.tick(getCurrentTick());
    }

    @Override
    public double getRating() {
        return rater.rate(null);
    }

    public DeliveryService getDeliveryService() {
        return deliveryService;
    }
}
