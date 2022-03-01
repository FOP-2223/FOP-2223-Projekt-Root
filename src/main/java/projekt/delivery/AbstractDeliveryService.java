package projekt.delivery;

import projekt.delivery.rating.Rater;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractDeliveryService implements DeliveryService {
    protected final VehicleManager vehicleManager;
    protected final Rater rater;
    private final Simulation simulation;
    private final SimulationConfig simulationConfig;
    private volatile boolean terminationRequested = false;
    private final Object lock = new Object();
    private List<ConfirmedOrder> unprocessedOrders = new ArrayList<>();

    protected AbstractDeliveryService(VehicleManager vehicleManager, Rater rater, Simulation simulation, SimulationConfig simulationConfig) {
        this.vehicleManager = vehicleManager;
        this.rater = rater;
        this.simulation = simulation;
        this.simulationConfig = simulationConfig;
    }

    @Override
    public void deliver(List<ConfirmedOrder> confirmedOrders) {
        synchronized (lock) {
            unprocessedOrders.addAll(confirmedOrders);
        }
    }

    @Override
    public final void runSimulation() {
        while (!terminationRequested) {
            if (simulationConfig.isPaused()) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            long tickStartTime = System.currentTimeMillis();

            runTick();

            // Wait till next tick is due.
            long executionTime = System.currentTimeMillis() - tickStartTime;
            long millisTillNextTick = simulationConfig.getMillisecondsPerTick() - executionTime;
            if (millisTillNextTick < 0) {
                // TODO: Make text yellow.
                System.out.println("WARNING: Can't keep up! Did the system time change, or is the server overloaded?");
            } else {
                try {
                    //noinspection BusyWait
                    Thread.sleep(millisTillNextTick);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void runTick() {
        // Schedule new orders
        List<ConfirmedOrder> newOrders = Collections.emptyList();
        synchronized (lock) {
            if (!unprocessedOrders.isEmpty()) {
                newOrders = unprocessedOrders;
                unprocessedOrders = new ArrayList<>();
            }
        }

        tick(newOrders);
        simulation.onStateUpdated();
    }

    abstract void tick(List<ConfirmedOrder> newOrders);

    @Override
    public void endSimulation() {
        terminationRequested = true;
    }

    @Override
    public SimulationConfig getSimulationConfig() {
        return simulationConfig;
    }
}
