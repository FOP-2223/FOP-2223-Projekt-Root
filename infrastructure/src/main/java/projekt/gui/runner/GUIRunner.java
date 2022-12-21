package projekt.gui.runner;

import javafx.application.Platform;
import javafx.stage.Stage;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.runner.AbstractRunner;
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
import java.util.function.Function;

public class GUIRunner extends AbstractRunner {

    private final Stage stage;

    public GUIRunner(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Map<RatingCriteria, Double> run(ProblemGroup problemGroup,
                                           SimulationConfig simulationConfig,
                                           int simulationRuns,
                                           Function<VehicleManager, DeliveryService> deliveryServiceFactory) {

        Map<ProblemArchetype, Simulation> simulations = createSimulations(problemGroup, simulationConfig, deliveryServiceFactory);
        Map<RatingCriteria, Double> results = new HashMap<>();

        for (RatingCriteria criteria : problemGroup.ratingCriteria()) {
            results.put(criteria, 0.0);
        }

        for (int i = 0; i < simulationRuns; i++) {
            for (Map.Entry<ProblemArchetype, Simulation> entry : simulations.entrySet()) {

                Simulation simulation = entry.getValue();

                //store the SimulationScene
                AtomicReference<SimulationScene> simulationScene = new AtomicReference<>();
                //CountDownLatch to check if the SimulationScene got created
                CountDownLatch countDownLatch = new CountDownLatch(1);
                //execute the scene switching on the javafx application thread
                Platform.runLater(() -> {
                    //switch to the SimulationScene and set everything up
                    SimulationScene scene = (SimulationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.SIMULATION, stage);
                    scene.init(simulation);
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

                simulation.runSimulation(entry.getKey().simulationLength());

                simulation.removeListener(simulationScene.get());

                results.replaceAll((criteria, rating) -> results.get(criteria) + entry.getValue().getRatingForCriterion(criteria));
            }
        }

        results.replaceAll((criteria, rating) -> (results.get(criteria) /*/ (simulationRuns * problemGroup.problems().size())*/));

        switchToRaterScene(results, problemGroup);

        return results;
    }

    private void switchToRaterScene(Map<RatingCriteria, Double> results, ProblemGroup problemGroup) {
        //execute the scene switching on the javafx thread
        Platform.runLater(() -> {
            RaterScene raterScene = (RaterScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.RATING, stage);
            raterScene.init(problemGroup.problems(), results);
        });
    }

}
