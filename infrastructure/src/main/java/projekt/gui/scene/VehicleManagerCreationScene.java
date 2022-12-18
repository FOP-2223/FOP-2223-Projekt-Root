package projekt.gui.scene;

import projekt.delivery.archetype.OrderGenerator;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;
import projekt.gui.controller.VehicleManagerCreationSceneController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VehicleManagerCreationScene extends MenuScene<VehicleManagerCreationSceneController> {

    private String name;
    private Long simulationLength;
    private VehicleManager vehicleManager;
    private OrderGenerator.FactoryBuilder orderGeneratorFactoryBuilder;
    private Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryBuilderMap;

    public VehicleManagerCreationScene() {
        super(new VehicleManagerCreationSceneController(), "Edit Vehicle Manager");
    }

    public void init(List<ProblemArchetype> problems,
                     String name,
                     Long simulationLength,
                     VehicleManager vehicleManager,
                     OrderGenerator.FactoryBuilder orderGeneratorFactoryBuilder,
                     Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryBuilderMap) {
        this.name = name;
        this.simulationLength = simulationLength;
        this.vehicleManager = vehicleManager;
        this.orderGeneratorFactoryBuilder = orderGeneratorFactoryBuilder;
        this.raterFactoryBuilderMap = raterFactoryBuilderMap;
        super.init(problems);
    }

    @Override
    public void initComponents() {
    }

    @Override
    public void initReturnButton() {
        returnButton.setOnAction(e -> {
            ProblemCreationScene scene = (ProblemCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.PROBLEM_CREATION, getController().getStage());
            scene.init(new ArrayList<>(problems), name, simulationLength, vehicleManager, orderGeneratorFactoryBuilder, raterFactoryBuilderMap);
        });
    }

    @Override
    public VehicleManagerCreationSceneController getController() {
        return controller;
    }

}
