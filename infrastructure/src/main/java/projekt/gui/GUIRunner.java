package projekt.gui;

import javafx.application.Platform;
import javafx.stage.Stage;
import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.runner.Runner;
import projekt.delivery.service.BasicDeliveryService;
import projekt.delivery.simulation.BasicDeliverySimulation;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class GUIRunner implements Runner {

    private final Stage stage;

    public GUIRunner(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Map<RatingCriteria, Double> run(ProblemGroup problemGroup, SimulationConfig simulationConfig, int simulationRuns) {

        List<Simulation> simulations = createSimulations(problemGroup, simulationConfig);
        Map<RatingCriteria, Double> results = new HashMap<>();

        for (RatingCriteria criteria : problemGroup.ratingCriteria()) {
            results.put(criteria, 0.0);
        }

        for (int i = 0; i < simulationRuns; i++) {
            for (Simulation simulation : simulations) {

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

                simulation.runSimulation();

                simulation.removeListener(simulationScene.get());

                results.replaceAll((criteria, rating) -> results.get(criteria) + simulation.getRatingForCriterion(criteria));
            }
        }

        results.replaceAll((criteria, rating) -> (results.get(criteria) / (simulationRuns * problemGroup.problems().size())));

        //execute the scene switching on the javafx thread
        Platform.runLater(() -> {
            RaterScene raterScene = (RaterScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.RATING, stage);
            raterScene.init(results);
        });

        return results;
    }

    private List<Simulation> createSimulations(
        ProblemGroup problemGroup,
        SimulationConfig simulationConfig) {

        List<Simulation> simulations = new ArrayList<>();

        problemGroup.problems().forEach(problem -> simulations.add(new BasicDeliverySimulation(
            simulationConfig,
            problem.raterFactoryMap(),
            new BasicDeliveryService(problem.vehicleManager()),
            problem.orderGeneratorFactory(),
            problem.simulationLength())));

        return simulations;
    }

}
