package projekt.gui.scene;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import projekt.delivery.archetype.OrderGenerator;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.rating.*;
import projekt.delivery.routing.VehicleManager;
import projekt.gui.controller.RaterFactoryMapCreationSceneController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaterFactoryMapCreationScene extends MenuScene<RaterFactoryMapCreationSceneController> {

    private String name;
    private Long simulationLength;
    private VehicleManager vehicleManager;
    private OrderGenerator.FactoryBuilder orderGeneratorFactoryBuilder;
    private Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryBuilderMap;

    private final CheckBox inTimeCheckBox = new CheckBox();
    private final CheckBox amountDeliveredCheckBox = new CheckBox();
    private final CheckBox travelDistanceCheckBox = new CheckBox();

    private long ignoredTicksOff;
    private long maxTicksOff;
    private double amountDeliveredFactor;
    private double travelDistanceFactor;

    public RaterFactoryMapCreationScene() {
        super(new RaterFactoryMapCreationSceneController(), "Edit Raters");
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
        this.raterFactoryBuilderMap = raterFactoryBuilderMap != null ? raterFactoryBuilderMap : new HashMap<>();
        super.init(problems);
    }

    @Override
    public void initComponents() {
        VBox raterVBox = new VBox();
        raterVBox.setMaxWidth(300);
        raterVBox.setAlignment(Pos.CENTER);
        raterVBox.setSpacing(10);

        raterVBox.getChildren().addAll(createInTimeHBoxes());
        raterVBox.getChildren().addAll(createAmountDeliveredHBoxes());
        raterVBox.getChildren().addAll(createTravelDistanceHBoxes());

        Button applyButton = new Button("Apply");
        applyButton.setOnAction((e) -> {
            Map<RatingCriteria, Rater.FactoryBuilder> newRaterFactoryMap = new HashMap<>();

            if (inTimeCheckBox.isSelected()) {
                newRaterFactoryMap.put(RatingCriteria.IN_TIME,
                    new InTimeRater.FactoryBuilder().setIgnoredTicksOff(ignoredTicksOff).setMaxTicksOff(maxTicksOff));
            }
            if (amountDeliveredCheckBox.isSelected()) {
                newRaterFactoryMap.put(RatingCriteria.AMOUNT_DELIVERED,
                    new AmountDeliveredRater.FactoryBuilder().setFactor(amountDeliveredFactor));
            }
            if (travelDistanceCheckBox.isSelected()) {
                newRaterFactoryMap.put(RatingCriteria.TRAVEL_DISTANCE,
                    new TravelDistanceRater.FactoryBuilder().setFactor(travelDistanceFactor));
            }

            ProblemCreationScene scene = (ProblemCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.PROBLEM_CREATION, getController().getStage());
            scene.init(new ArrayList<>(problems), name, simulationLength, vehicleManager, orderGeneratorFactoryBuilder, newRaterFactoryMap);
        });

        raterVBox.getChildren().add(applyButton);

        root.setCenter(raterVBox);
    }

    @Override
    public void initReturnButton() {
        returnButton.setOnAction(e -> {
            ProblemCreationScene scene = (ProblemCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.PROBLEM_CREATION, getController().getStage());
            scene.init(new ArrayList<>(problems), name, simulationLength, vehicleManager, orderGeneratorFactoryBuilder, raterFactoryBuilderMap);
        });
    }

    private List<HBox> createInTimeHBoxes() {
        List<HBox> HBoxes = new ArrayList<>();

        HBox inTimeHBox = new HBox();
        inTimeHBox.setSpacing(10);
        inTimeHBox.getChildren().setAll(inTimeCheckBox, new Label("In Time:"));
        HBoxes.add(inTimeHBox);

        HBox ignoredTicksOffHBOX = new HBox();
        ignoredTicksOffHBOX.setSpacing(10);
        TextField ignoredTicksOffTextField = createLongTextField(value -> ignoredTicksOff = value, 0L);
        ignoredTicksOffHBOX.getChildren().addAll(createIndentedLabel("Ignored Ticks Off"), createIntermediateRegion(0), ignoredTicksOffTextField);
        HBoxes.add(ignoredTicksOffHBOX);

        HBox maxTicksOffHBOX = new HBox();
        maxTicksOffHBOX.setSpacing(10);
        TextField maxTicksOffTextField = createLongTextField(value -> maxTicksOff = value, 0L);
        maxTicksOffHBOX.getChildren().addAll(createIndentedLabel("Max Ticks Off"),createIntermediateRegion(0), maxTicksOffTextField);
        HBoxes.add(maxTicksOffHBOX);

        return HBoxes;
    }

    private List<HBox> createAmountDeliveredHBoxes() {
        List<HBox> HBoxes = new ArrayList<>();

        HBox amountDeliveredHBox = new HBox();
        amountDeliveredHBox.setSpacing(10);
        amountDeliveredHBox.getChildren().setAll(amountDeliveredCheckBox, new Label("Amount Delivered:"));
        HBoxes.add(amountDeliveredHBox);

        HBox amountDeliveredFactorHBox = new HBox();
        amountDeliveredFactorHBox.setSpacing(10);
        TextField amountDeliveredFactorTextField = createDoubleTextField(value -> amountDeliveredFactor = value, 0.0);
        amountDeliveredFactorHBox.getChildren().addAll(createIndentedLabel("factor"), createIntermediateRegion(0), amountDeliveredFactorTextField);
        HBoxes.add(amountDeliveredFactorHBox);

        return HBoxes;
    }

    private List<HBox> createTravelDistanceHBoxes() {
        List<HBox> HBoxes = new ArrayList<>();

        HBox travelDistanceHBox = new HBox();
        travelDistanceHBox.setSpacing(10);
        travelDistanceHBox.getChildren().setAll(travelDistanceCheckBox, new Label("Traveled Distance:"));
        HBoxes.add(travelDistanceHBox);

        HBox travelDistanceFactorHBox = new HBox();
        travelDistanceFactorHBox.setSpacing(10);
        TextField travelDistanceFactorTextField = createDoubleTextField(value -> travelDistanceFactor = value, 0.0);
        travelDistanceFactorHBox.getChildren().addAll(createIndentedLabel("factor"), createIntermediateRegion(0), travelDistanceFactorTextField);
        HBoxes.add(travelDistanceFactorHBox);

        return HBoxes;
    }

    @Override
    public RaterFactoryMapCreationSceneController getController() {
        return controller;
    }

}
