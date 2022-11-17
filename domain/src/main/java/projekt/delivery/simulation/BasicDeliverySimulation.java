package projekt.delivery.simulation;

import projekt.delivery.deliveryService.DeliveryService;
import projekt.delivery.event.DeliverOrderEvent;
import projekt.delivery.event.Event;
import projekt.delivery.rating.Rater;
import projekt.delivery.routing.ConfirmedOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasicDeliverySimulation extends AbstractSimulation {

    private final DeliveryService deliveryService;
    private final Rater rater;
    private final List<List<ConfirmedOrder>> deliveredOrders = new ArrayList<>();

    public BasicDeliverySimulation(SimulationConfig simulationConfig, Rater rater, DeliveryService deliveryService) {
        super(simulationConfig);
        this.deliveryService = deliveryService;
        this.rater = rater;

        //Listener to save the current state of the simulation after each tick
        addListener(() -> {
            if (deliveredOrders.size() == Integer.MAX_VALUE) {
                System.err.println("Maximum length of stored event timeline reached.");
                return;
            }
            deliveredOrders.add(getCurrentEvents().stream()
                .filter(DeliverOrderEvent.class::isInstance)
                .map(DeliverOrderEvent.class::cast)
                .map(DeliverOrderEvent::getOrder)
                .toList());
        });
    }

    @Override
    List<Event> tick() {
        return deliveryService.tick(getCurrentTick());
    }

    @Override
    public double getRating() {
        return rater.rate(null);
    }

    public List<List<ConfirmedOrder>> getAllOrders() {
        return Collections.unmodifiableList(deliveredOrders);
    }

    public List<ConfirmedOrder> getOrdersForTick(long tick) {

        if (tick > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("requested tick too large");
        }
        if (tick < 0) {
            throw new IllegalArgumentException("negative tick requested");
        }

        return deliveredOrders.get((int) tick);
    }

    public DeliveryService getDeliveryService() {
        return deliveryService;
    }
}
