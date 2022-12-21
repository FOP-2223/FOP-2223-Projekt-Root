package projekt.delivery.simulation;

import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.service.DeliveryService;

public interface Simulation {

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
    void runSimulation(long maxTicks);

    /**
     * Stops the currently running simulation of the food delivery service.
     */
    void endSimulation();

    boolean isRunning();

    double getRatingForCriterion(RatingCriteria criterion);

    SimulationConfig getSimulationConfig();

    DeliveryService getDeliveryService();

    long getCurrentTick();

    void runCurrentTick();

    void addListener(SimulationListener listener);

    boolean removeListener(SimulationListener listener);

    boolean toggleRunning();
}
