package projekt.delivery;

import projekt.delivery.rating.Rater;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractDeliveryService implements DeliveryService {
    protected final VehicleManager vehicleManager;
    protected final Rater rater;
    private final Object lock = new Object();

    private List<ConfirmedOrder> unprocessedOrders = new ArrayList<>();


    protected AbstractDeliveryService(VehicleManager vehicleManager, Rater rater) {
        this.vehicleManager = vehicleManager;
        this.rater = rater;
    }

    @Override
    public void deliver(List<ConfirmedOrder> confirmedOrders) {
        synchronized (lock) {
            unprocessedOrders.addAll(confirmedOrders);
        }
    }

    public void tick(long currentTick) {
        // Schedule new orders
        List<ConfirmedOrder> newOrders = Collections.emptyList();
        synchronized (lock) {
            if (!unprocessedOrders.isEmpty()) {
                newOrders = unprocessedOrders;
                unprocessedOrders = new ArrayList<>();
            }
        }

        tick(currentTick, newOrders);
    }

    abstract void tick(long currentTick, List<ConfirmedOrder> newOrders);
}
