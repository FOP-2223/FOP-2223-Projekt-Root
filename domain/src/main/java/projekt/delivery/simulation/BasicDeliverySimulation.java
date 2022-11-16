package projekt.delivery.simulation;

import projekt.delivery.deliveryService.DeliveryService;

import java.util.ArrayList;
import java.util.List;

public class BasicDeliverySimulation extends AbstractSimulation {

    private final DeliveryService deliveryService;
    List<Listener> listeners = new ArrayList<>();

    public BasicDeliverySimulation(SimulationConfig simulationConfig, DeliveryService deliveryService) {
        super(simulationConfig);
        this.deliveryService = deliveryService;
    }

    @Override
    void tick() {
        deliveryService.tick(getCurrentTick());
    }

    @Override
    public void onStateUpdated() {
        for (Listener listener : listeners) {
            listener.onStateUpdated();
        }
    }

    public DeliveryService getDeliveryService() {
        return deliveryService;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public boolean removeListener(Listener listener) {
        return listeners.remove(listener);
    }
    public interface Listener {

        void onStateUpdated();
    }
}
