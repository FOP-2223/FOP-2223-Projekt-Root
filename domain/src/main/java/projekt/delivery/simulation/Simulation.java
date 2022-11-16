package projekt.delivery.simulation;

public interface Simulation {
    void onStateUpdated();

    /**
     * Start the simulation of the food delivery.
     * This method blocks the current thread and only returns when the simulation is terminated.
     * To terminate the simulation, you need to call {@link #endSimulation()} from a separate thread.
     */
    void runSimulation();

    /**
     * Stops the currently running simulation of the food delivery service.
     */
    void endSimulation();

    SimulationConfig getSimulationConfig();

    long getCurrentTick();

    void runTick();

}
