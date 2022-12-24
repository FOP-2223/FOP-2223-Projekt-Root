package projekt.gui.scene;


import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.generator.EmptyOrderGenerator;
import projekt.delivery.generator.FridayOrderGenerator;
import projekt.delivery.generator.OrderGenerator;
import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;
import projekt.gui.controller.OrderGeneratorFactoryCreationSceneController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderGeneratorFactoryCreationScene extends MenuScene<OrderGeneratorFactoryCreationSceneController> {

    private final List<HBox> options = new ArrayList<>();
    private final Map<String, Number> values = new HashMap<>();
    private final CheckBox seedCheckBox = new CheckBox();
    private final Button applyButton = new Button("Apply");
    private final ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private String name;
    private Long simulationLength;
    private VehicleManager vehicleManager;
    private OrderGenerator.FactoryBuilder orderGeneratorFactoryBuilder;
    private Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryBuilderMap;
    private VBox vBox = new VBox();
    private TextField seedTextField;

    public OrderGeneratorFactoryCreationScene() {
        super(new OrderGeneratorFactoryCreationSceneController(), "Edit OrderGenerator");
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
    public OrderGeneratorFactoryCreationSceneController getController() {
        return controller;
    }

    @Override
    public void initComponents() {
        vBox = new VBox();
        vBox.setMaxWidth(300);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);

        createChoiceBox();

        if (orderGeneratorFactoryBuilder != null) {
            if (orderGeneratorFactoryBuilder instanceof FridayOrderGenerator.FactoryBuilder) {
                setupFridayOptions();
                choiceBox.setValue("Friday Order Generator");
            } else {
                setupEmptyOptions();
                choiceBox.setValue("Empty Order Generator");
            }
        }

        applyButton.setOnAction(e -> {
            OrderGenerator.FactoryBuilder newOrderGenerator;
            if (choiceBox.getValue().equals("Friday Order Generator")) {
                newOrderGenerator = FridayOrderGenerator.Factory.builder()
                    .setOrderCount((Integer) values.get("orderCount"))
                    .setDeliveryInterval((Integer) values.get("deliveryInterval"))
                    .setMaxWeight((Double) values.get("maxWeight"))
                    .setVariance((Double) values.get("variance"))
                    .setLastTick((Long) values.get("lastTick"))
                    .setVehicleManager(vehicleManager)
                    .setSeed(seedCheckBox.isSelected() ? (Integer) values.get("seed") : -1);
            } else if (choiceBox.getValue().equals("Empty Order Generator")) {
                newOrderGenerator = new EmptyOrderGenerator.FactoryBuilder();
            } else {
                throw new IllegalArgumentException();
            }

            ProblemCreationScene scene = (ProblemCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.PROBLEM_CREATION, getController().getStage());
            scene.init(new ArrayList<>(problems), name, simulationLength, vehicleManager, newOrderGenerator, raterFactoryBuilderMap);
        });

        seedCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> seedTextField.setDisable(!newValue));

        root.setCenter(vBox);
    }

    @Override
    public void initReturnButton() {
        returnButton.setOnAction(e -> {
            ProblemCreationScene scene = (ProblemCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.PROBLEM_CREATION, getController().getStage());
            scene.init(new ArrayList<>(problems), name, simulationLength, vehicleManager, orderGeneratorFactoryBuilder, raterFactoryBuilderMap);
        });
    }

    private void createChoiceBox() {

        HBox box = new HBox();

        Label label = new Label("OrderGenerator:");

        choiceBox.getItems().setAll("Friday Order Generator", "Empty Order Generator");
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            switch (choiceBox.getItems().get((Integer) newValue)) {
                case "Friday Order Generator" -> setupFridayOptions();
                case "Empty Order Generator" -> setupEmptyOptions();
            }
        });

        box.getChildren().addAll(label, createIntermediateRegion(0), choiceBox);
        vBox.getChildren().add(box);
    }

    private void setupFridayOptions() {

        vBox.getChildren().removeAll(options);
        vBox.getChildren().remove(applyButton);

        options.clear();
        values.clear();

        if (orderGeneratorFactoryBuilder == null) {
            values.put("orderCount", 0);
            values.put("deliveryInterval", 0);
            values.put("maxWeight", 0.0);
            values.put("variance", 0.0);
            values.put("lastTick", 0L);
            values.put("seed", 0);
        } else {
            FridayOrderGenerator.FactoryBuilder fridayBuilder = (FridayOrderGenerator.FactoryBuilder) orderGeneratorFactoryBuilder;
            values.put("orderCount", fridayBuilder.orderCount);
            values.put("deliveryInterval", fridayBuilder.deliveryInterval);
            values.put("maxWeight", fridayBuilder.maxWeight);
            values.put("variance", fridayBuilder.variance);
            values.put("lastTick", fridayBuilder.lastTick);
            values.put("seed", fridayBuilder.seed == -1 ? 0 : fridayBuilder.seed);
        }

        HBox orderCountHBox = new HBox();
        Label orderCountLabel = createIndentedLabel("Order Count");
        TextField orderCountTextField = createPositiveIntegerTextField(value -> values.put("orderCount", value), (Integer) values.get("orderCount"));
        orderCountHBox.getChildren().addAll(orderCountLabel, createIntermediateRegion(0), orderCountTextField);

        HBox deliveryIntervalHBox = new HBox();
        Label deliveryIntervalLabel = createIndentedLabel("Delivery Interval");
        TextField deliveryIntervalTextField = createPositiveIntegerTextField(value -> values.put("deliveryInterval", value), (Integer) values.get("deliveryInterval"));
        deliveryIntervalHBox.getChildren().addAll(deliveryIntervalLabel, createIntermediateRegion(0), deliveryIntervalTextField);

        HBox maxWeightHBox = new HBox();
        Label maxWeightLabel = createIndentedLabel("Max Weight");
        TextField maxWeightTextField = createPositiveDoubleTextField(value -> values.put("maxWeight", value), (Double) values.get("maxWeight"));
        maxWeightHBox.getChildren().addAll(maxWeightLabel, createIntermediateRegion(0), maxWeightTextField);

        HBox varianceHBox = new HBox();
        Label varianceLabel = createIndentedLabel("Variance");
        TextField varianceTextField = createPositiveDoubleTextField(value -> values.put("variance", value), (Double) values.get("variance"));
        varianceHBox.getChildren().addAll(varianceLabel, createIntermediateRegion(0), varianceTextField);

        HBox lastTickHBox = new HBox();
        Label lastTickLabel = createIndentedLabel("Last Tick");
        TextField lastTickTextField = createLongTextField(value -> values.put("lastTick", value), (Long) values.get("lastTick"));
        lastTickHBox.getChildren().addAll(lastTickLabel, createIntermediateRegion(0), lastTickTextField);

        HBox seedHBox = new HBox();
        Label seedLabel = createIndentedLabel("Seed");
        TextField seedTextField = createPositiveIntegerTextField(value -> values.put("seed", value), (Integer) values.get("seed") == -1 ? 0 : (Integer) values.get("seed"));
        this.seedTextField = seedTextField;
        seedHBox.getChildren().addAll(seedCheckBox, seedLabel, createIntermediateRegion(0), seedTextField);

        if (orderGeneratorFactoryBuilder == null || ((FridayOrderGenerator.FactoryBuilder) orderGeneratorFactoryBuilder).seed == -1)
            seedTextField.setDisable(true);

        options.addAll(List.of(orderCountHBox, deliveryIntervalHBox, maxWeightHBox, varianceHBox, lastTickHBox, seedHBox));

        vBox.getChildren().addAll(options);
        vBox.getChildren().add(applyButton);
    }

    private void setupEmptyOptions() {
        vBox.getChildren().removeAll(options);
        options.clear();
        values.clear();
    }

}
