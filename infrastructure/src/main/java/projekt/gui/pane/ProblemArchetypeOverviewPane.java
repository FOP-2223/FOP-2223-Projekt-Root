package projekt.gui.pane;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.generator.EmptyOrderGenerator;
import projekt.delivery.generator.FridayOrderGenerator;
import projekt.delivery.generator.OrderGenerator;
import projekt.delivery.rating.*;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;
import projekt.gui.scene.MenuScene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProblemArchetypeOverviewPane extends Pane {

    public static final int WIDTH_BETWEEN_VALUE = 50;

    public ProblemArchetypeOverviewPane(ProblemArchetype problem) {
        this(problem.name(),
            problem.simulationLength(),
            orderGeneratorFactoryToFactoryBuilder(problem.orderGeneratorFactory()),
            raterFactoryMapToFactoryBuilder(problem.raterFactoryMap()),
            problem.vehicleManager());
    }

    public ProblemArchetypeOverviewPane(String name,
                                        Long simulationLength,
                                        OrderGenerator.FactoryBuilder orderGenerator,
                                        Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryMap,
                                        VehicleManager vehicleManager) {
        setPadding(new Insets(20, 20, 20, 20));

        VBox box = new VBox();

        box.getChildren().addAll(createNameProperties(name));
        box.getChildren().addAll(createProperty("SimulationLength", simulationLength, 0));
        box.getChildren().addAll(createOrderGeneratorProperties(orderGenerator));
        box.getChildren().addAll(createRaterFactoryMapProperties(raterFactoryMap));
        box.getChildren().addAll(createVehicleManagerProperties(vehicleManager));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(box);
        scrollPane.setPrefSize(320, 350);
        scrollPane.prefHeightProperty().bind(heightProperty());
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        getChildren().add(scrollPane);
    }

    private static OrderGenerator.FactoryBuilder orderGeneratorFactoryToFactoryBuilder(OrderGenerator.Factory factory) {
        if (factory instanceof FridayOrderGenerator.Factory fridayOrderGenerator) {
            return FridayOrderGenerator.Factory.builder()
                .setOrderCount(fridayOrderGenerator.orderCount)
                .setDeliveryInterval(fridayOrderGenerator.deliveryInterval)
                .setMaxWeight(fridayOrderGenerator.maxWeight)
                .setVariance(fridayOrderGenerator.variance)
                .setLastTick(fridayOrderGenerator.lastTick)
                .setSeed(fridayOrderGenerator.seed);
        } else {
            return new EmptyOrderGenerator.FactoryBuilder();
        }
    }

    private static Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryMapToFactoryBuilder(Map<RatingCriteria, Rater.Factory> raterFactoryMap) {
        Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryBuilderMap = new HashMap<>();

        for (Map.Entry<RatingCriteria, Rater.Factory> entry : raterFactoryMap.entrySet()) {
            if (entry.getValue() instanceof InTimeRater.Factory inTimeRater) {
                raterFactoryBuilderMap.put(entry.getKey(), InTimeRater.Factory.builder()
                    .setIgnoredTicksOff(inTimeRater.ignoredTicksOff)
                    .setMaxTicksOff(inTimeRater.maxTicksOff));
            } else if (entry.getValue() instanceof AmountDeliveredRater.Factory amountDeliveredFactory) {
                raterFactoryBuilderMap.put(entry.getKey(), AmountDeliveredRater.Factory.builder()
                    .setFactor(amountDeliveredFactory.factor));
            } else if (entry.getValue() instanceof TravelDistanceRater.Factory travelDistanceFactory) {
                raterFactoryBuilderMap.put(entry.getKey(), TravelDistanceRater.Factory.builder()
                    .setFactor(travelDistanceFactory.factor));
            }
        }

        return raterFactoryBuilderMap;
    }

    private List<HBox> createNameProperties(String name) {
        List<HBox> properties = new ArrayList<>();

        if (name == null || name.trim().equals("")) {
            properties.add(createProperty("Name", "Not set", 0));
        } else {
            properties.add(createProperty("Name", name, 0));
        }

        return properties;
    }

    private List<HBox> createVehicleManagerProperties(VehicleManager vehicleManager) {
        List<HBox> properties = new ArrayList<>();

        if (vehicleManager == null) {
            properties.add(createProperty("VehicleManager", "Not set", 0));
            return properties;
        }

        properties.add(createProperty("VehicleManager", "", 0));

        properties.add(createProperty("Nodes", vehicleManager.getRegion().getNodes().size(), 1));
        for (projekt.delivery.routing.Region.Node node : vehicleManager.getRegion().getNodes()) {
            properties.add(createProperty("Node " + node.getName(), node.getLocation(), 2));
        }

        properties.add(createProperty("Edges", vehicleManager.getRegion().getNodes().size(), 1));
        for (projekt.delivery.routing.Region.Edge edge : vehicleManager.getRegion().getEdges()) {
            properties.add(createProperty("Edge " + edge.getName(), edge.getNodeA().getLocation() + " - " + edge
                .getNodeB().getLocation(), 2));
        }

        properties.add(createProperty("Vehicles", vehicleManager.getAllVehicles().size(), 1));
        for (Vehicle vehicle : vehicleManager.getAllVehicles()) {
            properties.add(createProperty("Vehicle", "", 2));

            properties.add(createProperty("Starting Node", vehicle.getStartingNode().getComponent().getLocation(), 3));
            properties.add(createProperty("Capacity", vehicle.getCapacity(), 3));
        }

        return properties;
    }

    private List<HBox> createOrderGeneratorProperties(OrderGenerator.FactoryBuilder builder) {
        List<HBox> properties = new ArrayList<>();

        if (builder == null) {
            properties.add(createProperty("OrderGenerator", "Not set", 0));
            return properties;
        }

        if (builder instanceof FridayOrderGenerator.FactoryBuilder fridayFactory) {
            properties.add(createProperty("OrderGenerator", "FridayOrderGenerator", 0));
            properties.add(createProperty("OrderCount", fridayFactory.orderCount, 1));
            properties.add(createProperty("deliveryInterval", fridayFactory.deliveryInterval, 1));
            properties.add(createProperty("maxWeight", fridayFactory.maxWeight, 1));
            properties.add(createProperty("variance", fridayFactory.variance, 1));
            properties.add(createProperty("lastTick", fridayFactory.lastTick, 1));
            properties.add(createProperty("seed", fridayFactory.seed == -1 ? "Random" : fridayFactory.seed, 1));
        } else if (builder instanceof EmptyOrderGenerator.FactoryBuilder) {
            properties.add(createProperty("OrderGenerator", "EmptyOrderGenerator", 0));
        }

        return properties;
    }

    private List<HBox> createRaterFactoryMapProperties(Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryMap) {
        List<HBox> properties = new ArrayList<>();

        if (raterFactoryMap == null) {
            properties.add(createProperty("Rater", "Not set", 0));
            return properties;
        }

        properties.add(createProperty("Rater", "", 0));

        for (RatingCriteria criteria : RatingCriteria.values()) {

            if (raterFactoryMap.containsKey(criteria)) {

                Rater.FactoryBuilder builder = raterFactoryMap.get(criteria);

                if (builder instanceof InTimeRater.FactoryBuilder inTimeFactory) {
                    properties.add(createProperty(criteria.toString(), "InTimeRater", 1));

                    properties.add(createProperty("IgnoredTicksOff", inTimeFactory.ignoredTicksOff, 2));
                    properties.add(createProperty("MaxTicksOff", inTimeFactory.maxTicksOff, 2));
                } else if (builder instanceof AmountDeliveredRater.FactoryBuilder amountDeliveredRater) {
                    properties.add(createProperty(criteria.toString(), "AmountDeliveredRater", 1));

                    properties.add(createProperty("Factor", amountDeliveredRater.factor, 2));
                } else if (builder instanceof TravelDistanceRater.FactoryBuilder travelDistanceRater) {
                    properties.add(createProperty(criteria.toString(), "TravelDistanceRater", 1));

                    properties.add(createProperty("Factor", travelDistanceRater.factor, 2));
                }

            } else {
                properties.add(createProperty(criteria.toString(), "Not set", 1));
            }

        }

        return properties;
    }

    private HBox createProperty(String name, Object value, int indentationCount) {
        HBox box = new HBox();

        Label nameLabel = new Label("    ".repeat(Math.max(0, indentationCount)) + name + ":");
        Label valueLabel = new Label(value.toString());

        box.getChildren().addAll(nameLabel, MenuScene.createIntermediateRegion(WIDTH_BETWEEN_VALUE), valueLabel);

        return box;
    }
}
