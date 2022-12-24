package projekt.gui.runner;

import javafx.application.Platform;
import javafx.stage.Stage;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.runner.AbstractRunner;
import projekt.delivery.runner.Runner;
import projekt.delivery.service.DeliveryService;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;
import projekt.gui.scene.RaterScene;
import projekt.gui.scene.SceneSwitcher;
import projekt.gui.scene.SimulationScene;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A {@link Runner} that executes a {@link Simulation} and visualises it and its result with a gui.
 */
public class GUIRunner extends AbstractRunner {

    private final Stage stage;
    private volatile boolean terminationRequested = false;

    public GUIRunner(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Map<RatingCriteria, Double> run(ProblemGroup problemGroup,
                                           SimulationConfig simulationConfig,
                                           int simulationRuns,
                                           DeliveryService.Factory deliveryServiceFactory) {

        Map<ProblemArchetype, Simulation> simulations = createSimulations(problemGroup, simulationConfig, deliveryServiceFactory);
        Map<RatingCriteria, Double> results = new HashMap<>();

        for (RatingCriteria criteria : problemGroup.ratingCriteria()) {
            results.put(criteria, 0.0);
        }

        for (int i = 0; i < simulationRuns; i++) {

            for (Map.Entry<ProblemArchetype, Simulation> entry : simulations.entrySet()) {
                Simulation simulation = entry.getValue();
                ProblemArchetype problem = entry.getKey();

                //store the SimulationScene
                AtomicReference<SimulationScene> simulationScene = new AtomicReference<>();
                //CountDownLatch to check if the SimulationScene got created
                CountDownLatch countDownLatch = new CountDownLatch(1);
                //execute the scene switching on the javafx application thread
                int finalI = i;
                Platform.runLater(() -> {
                    //switch to the SimulationScene and set everything up
                    SimulationScene scene = (SimulationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.SIMULATION, stage);
                    scene.init(simulation, problem, finalI, simulationRuns, this);
                    simulation.addListener(scene);
                    simulationScene.set(scene);
                    countDownLatch.countDown();
                });

                try {
                    //wait for the SimulationScene to be set
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //run the simulation
                simulation.runSimulation(problem.simulationLength());

                //remove the scene from the list of listeners
                simulation.removeListener(simulationScene.get());

                //check if gui got closed
                if (terminationRequested) {
                    return null;
                }

                results.replaceAll((criteria, rating) -> results.get(criteria) + simulation.getRatingForCriterion(criteria));
            }
        }

        results.replaceAll((criteria, rating) -> (results.get(criteria) / (simulationRuns * problemGroup.problems().size())));

        //execute the scene switching on the javafx thread
        Platform.runLater(() -> {
            RaterScene raterScene = (RaterScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.RATING, stage);
            raterScene.init(problemGroup.problems(), results);
        });

        return null;
    }

    public void terminate() {
        terminationRequested = true;
    }
}
