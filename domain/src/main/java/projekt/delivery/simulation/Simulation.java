package projekt.delivery.simulation;

import projekt.delivery.event.Event;

import java.util.List;

public interface Simulation {
    void onStateUpdated();

    /**
     * Start the simulation of the food delivery.
     * This method blocks the current thread and only returns when the simulation is terminated.
     * To terminate the simulation, you need to call {@link #endSimulation()} from a separate thread.
     */
    void runSimulation();

    /**
     * Start the simulation of the food delivery.
     * This method blocks the current thread and only returns when the simulation is terminated.
     * The simulation will automatically be terminated after maxTicks ticks.
     * To terminate the simulation, you can also call {@link #endSimulation()} from a separate thread.
     *
     * @param maxTicks The maximum amount of ticks the simulation will run.
     *                 When the maximum amount of ticks is reached the simulation will be stopped automatically.
     */
    void runSimulation(int maxTicks);

    /**
     * Stops the currently running simulation of the food delivery service.
     */
    void endSimulation();

    SimulationConfig getSimulationConfig();

    long getCurrentTick();

    List<Event> getCurrentEvents();

    void runTick();

    double getRating();

    void addListener(Listener listener);

    boolean removeListener(Listener listener);

    interface Listener {

        void onStateUpdated();
    }

}
