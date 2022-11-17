package projekt.delivery.simulation;

import projekt.delivery.event.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractSimulation implements Simulation {

    private final SimulationConfig simulationConfig;
    List<Listener> listeners = new ArrayList<>();
    private volatile boolean terminationRequested = false;

    private long currentTick = 0;
    private List<Event> currentEvents;

    public AbstractSimulation(SimulationConfig simulationConfig) {
        this.simulationConfig = simulationConfig;
    }

    @Override
    public void runSimulation() {
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

    @Override
    public void runSimulation(int maxTicks) {
        addListener(() -> {
            if (getCurrentTick() == maxTicks) endSimulation();
        });

        runSimulation();
    }

    @Override
    public void endSimulation() {
        terminationRequested = true;
    }

    @Override
    public SimulationConfig getSimulationConfig() {
        return simulationConfig;
    }

    @Override
    public long getCurrentTick() {
        return currentTick;
    }

    @Override
    public List<Event> getCurrentEvents() {
        return currentEvents;
    }

    @Override
    public void runTick() {
        currentTick++;
        currentEvents = Collections.unmodifiableList(tick());
        onStateUpdated();
    }

    abstract List<Event> tick();

    @Override
    public void onStateUpdated() {
        for (Listener listener : listeners) {
            listener.onStateUpdated();
        }
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public boolean removeListener(Listener listener) {
        return listeners.remove(listener);
    }

}
