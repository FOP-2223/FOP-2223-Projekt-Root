package projekt.delivery.simulation;

import projekt.delivery.archetype.OrderGenerator;
import projekt.delivery.event.Event;
import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;

import java.util.*;

public abstract class AbstractSimulation implements Simulation {

    protected final SimulationConfig simulationConfig;
    List<SimulationListener> listeners = new ArrayList<>();
    private volatile boolean terminationRequested = false;
    protected long currentTick = 0;
    private final long simulationLength;
    protected List<Event> lastEvents;
    protected final Map<RatingCriteria, Rater.Factory> raterFactoryMap;
    protected final Map<RatingCriteria, Rater> currentRaterMap = new HashMap<>();
    private final OrderGenerator.Factory orderGeneratorFactory;
    private OrderGenerator currentOrderGenerator;
    protected boolean isRunning = false;

    public AbstractSimulation(SimulationConfig simulationConfig,
                              Map<RatingCriteria, Rater.Factory> raterFactoryMap,
                              OrderGenerator.Factory orderGeneratorFactory,
                              long simulationLength) {
        this.simulationConfig = simulationConfig;
        this.raterFactoryMap = raterFactoryMap;
        this.orderGeneratorFactory = orderGeneratorFactory;
        this.simulationLength = simulationLength;
    }

    @Override
    public void runSimulation() {
        setupNewSimulation();
        isRunning = true;

        while (!terminationRequested && currentTick <= simulationLength) {
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

            runCurrentTick();

            // Wait till next tick is due.
            long executionTime = System.currentTimeMillis() - tickStartTime;
            long millisTillNextTick = simulationConfig.getMillisecondsPerTick() - executionTime;
            if (millisTillNextTick < 0) {
                System.out.println("\033[0;33m"); //make text yellow
                System.out.println("WARNING: Can't keep up! Did the system time change, or is the server overloaded?");
                System.out.println("\033[0m"); // reset text color
            } else {
                try {
                    //noinspection BusyWait
                    Thread.sleep(millisTillNextTick);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        isRunning = false;
    }

    @Override
    public void runSimulation(long simulationLength) {
        addListener((events, tick) -> {
            if (tick == simulationLength) endSimulation();
        });

        runSimulation();
    }

    @Override
    public void endSimulation() {
        terminationRequested = true;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public double getRatingForCriterion(RatingCriteria criterion) {
        return currentRaterMap.get(criterion).getScore();
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
    public void runCurrentTick() {
        getDeliveryService().deliver(currentOrderGenerator.generateOrders(getCurrentTick()));
        lastEvents = Collections.unmodifiableList(tick());

        for (SimulationListener listener : listeners) {
            listener.onTick(lastEvents, getCurrentTick());
        }

        currentTick++;
    }

    abstract List<Event> tick();

    public void addListener(SimulationListener listener) {
        listeners.add(listener);
    }

    public boolean removeListener(SimulationListener listener) {
        return listeners.remove(listener);
    }

    private void setupNewSimulation() {
        currentTick = 0;
        lastEvents = new ArrayList<>();
        getDeliveryService().reset();
        setupRaters();
        setupOrderGenerator();
    }
    private void setupRaters() {
        for (Rater rater : currentRaterMap.values()) {
            removeListener(rater);
        }

        currentRaterMap.clear();

        for (RatingCriteria criterion : raterFactoryMap.keySet()) {
            Rater rater = raterFactoryMap.get(criterion).create();
            addListener(rater);
            currentRaterMap.put(criterion, rater);
        }
    }

    private void setupOrderGenerator() {
        currentOrderGenerator = orderGeneratorFactory.create();
    }

}
