package projekt.gui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import projekt.delivery.archetype.OrderGenerator;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.rating.*;
import projekt.delivery.routing.VehicleManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaterFactoryMapCreationScene extends Scene implements ControlledScene<RaterFactoryMapCreationSceneController> {

    private final BorderPane root;
    private final RaterFactoryMapCreationSceneController controller;

    private List<ProblemArchetype> problems;

    private VehicleManager vehicleManager;
    private OrderGenerator.Factory orderGeneratorFactory;
    private Map<RatingCriteria, Rater.Factory> raterFactoryMap;
    private Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryBuilderMap = new HashMap<>();

    private static final Map<String, Class<? extends Rater.FactoryBuilder>> factoryMap = Map.of(
        InTimeRater.class.getSimpleName(), InTimeRater.FactoryBuilder.class,
        AmountDeliveredRater.class.getSimpleName(), AmountDeliveredRater.FactoryBuilder.class,
        TravelDistanceRater.class.getSimpleName(), TravelDistanceRater.FactoryBuilder.class
    );

    public RaterFactoryMapCreationScene() {
        super(new BorderPane());
        controller = new RaterFactoryMapCreationSceneController();
        root = (BorderPane) getRoot();
        root.setPrefSize(500, 500);

        // apply styles
        root.getStylesheets().add("projekt/gui/darkMode.css");
        root.getStylesheets().add("projekt/gui/raterStyle.css");//TODO change
    }

    public void init(List<ProblemArchetype> problems,
                     VehicleManager vehicleManager,
                     OrderGenerator.Factory orderGeneratorFactory,
                     Map<RatingCriteria, Rater.Factory> raterFactoryMap) {
        this.problems = problems;
        this.vehicleManager = vehicleManager;
        this.orderGeneratorFactory = orderGeneratorFactory;
        this.raterFactoryMap = raterFactoryMap != null ? raterFactoryMap: new HashMap<>();
        initComponents();
    }

    @Override
    public RaterFactoryMapCreationSceneController getController() {
        return controller;
    }

    private void initComponents() {
        final Label titleLabel = new Label("Create Raters");
        titleLabel.setPadding(new Insets(20, 20, 20, 20));
        titleLabel.setId("Title");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        root.setTop(titleLabel);

        VBox raterVBox = new VBox();
        raterVBox.setPrefSize(200, 100);
        raterVBox.setAlignment(Pos.CENTER);
        raterVBox.setSpacing(10);
        for (RatingCriteria criteria : RatingCriteria.values()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);

            Label label = new Label(criteria.toString());
            ChoiceBox<String> raterSelector = new ChoiceBox<>(FXCollections.observableArrayList(factoryMap.keySet().toArray(String[]::new)));

            raterSelector.setOnAction(e -> {
                switch (raterSelector.getSelectionModel().getSelectedItem()) {
                    case "InTimeRater":
                        refreshInTimeRater(criteria, hBox, label, raterSelector);

                    case "AmountDeliveredRater":
                        refreshAmountDeliveredRater(criteria, hBox, label, raterSelector);

                    case "TravelDistanceRater":
                        refreshTravelDistanceRater(criteria, hBox, label, raterSelector);

                }
            });

            if (raterFactoryMap.containsKey(criteria)) {
                switch (criteria.toString()) {
                    case "IN_TIME":
                        raterSelector.getSelectionModel().select(InTimeRater.class.getSimpleName());
                        refreshInTimeRater(criteria, hBox, label, raterSelector);
                    case "AMOUNT_DELIVERED":
                        raterSelector.getSelectionModel().select(AmountDeliveredRater.class.getSimpleName());
                        refreshAmountDeliveredRater(criteria, hBox, label, raterSelector);
                    case "TRAVEL_DISTANCE":
                        raterSelector.getSelectionModel().select(TravelDistanceRater.class.getSimpleName());
                        refreshTravelDistanceRater(criteria, hBox, label, raterSelector);
                }
            } else {
                hBox.getChildren().addAll(label, raterSelector);
            }

            raterVBox.getChildren().add(hBox);
        }

        root.setCenter(raterVBox);


        //setup return and quit buttons
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        buttons.setPadding(new Insets(20, 20, 20, 20));

        final var returnButton = new Button("Return");
        returnButton.setOnAction(e -> {
            for (Map.Entry<RatingCriteria, Rater.FactoryBuilder> entry : raterFactoryBuilderMap.entrySet()) {
                raterFactoryMap.put(entry.getKey(), entry.getValue().build());
            }

            ProblemCreationScene scene = (ProblemCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.PROBLEM_CREATION, getController().getStage());
            scene.init(new ArrayList<>(problems), vehicleManager, orderGeneratorFactory, raterFactoryMap);
        });
        buttons.getChildren().add(returnButton);

        final var quitButton = new Button("Quit");
        quitButton.setOnAction(getController()::quit);
        buttons.getChildren().add(quitButton);

        root.setBottom(buttons);
    }

    private void refreshTravelDistanceRater(RatingCriteria criteria, HBox hBox, Label label, ChoiceBox<String> raterSelector) {
        TravelDistanceRater.FactoryBuilder factoryBuilder = new TravelDistanceRater.FactoryBuilder();
        raterFactoryBuilderMap.put(criteria, factoryBuilder);

        if (raterFactoryMap.containsKey(criteria)) {
            factoryBuilder.setFactor(((TravelDistanceRater.Factory) raterFactoryMap.get(criteria)).factor);
        }

        TextField factorFieldTravel = new TextField();
        factorFieldTravel.setText(Double.toString(factoryBuilder.factor));
        factorFieldTravel.setOnAction(e2 -> {
            ((InTimeRater.FactoryBuilder) raterFactoryBuilderMap.get(criteria)).setIgnoredTicksOff(
                Integer.parseInt(factorFieldTravel.getText()));
        });

        hBox.getChildren().setAll(label, raterSelector, factorFieldTravel);
    }

    private void refreshAmountDeliveredRater(RatingCriteria criteria, HBox hBox, Label label, ChoiceBox<String> raterSelector) {
        raterFactoryBuilderMap.put(criteria, new AmountDeliveredRater.FactoryBuilder());

        TextField factorFieldDelivered = new TextField();
        factorFieldDelivered.setOnAction(e2 -> {
            ((InTimeRater.FactoryBuilder) raterFactoryBuilderMap.get(criteria)).setIgnoredTicksOff(
                Integer.parseInt(factorFieldDelivered.getText()));
        });

        hBox.getChildren().setAll(label, raterSelector, factorFieldDelivered);
    }

    private void refreshInTimeRater(RatingCriteria criteria, HBox hBox, Label label, ChoiceBox<String> raterSelector) {
        raterFactoryBuilderMap.put(criteria, new InTimeRater.FactoryBuilder());

        TextField ignoredTicksOffField = new TextField();
        ignoredTicksOffField.setOnAction(e2 -> {
            ((InTimeRater.FactoryBuilder) raterFactoryBuilderMap.get(criteria)).setIgnoredTicksOff(
                Integer.parseInt(ignoredTicksOffField.getText()));
        });

        TextField maxTicksOffField = new TextField();
        maxTicksOffField.setOnAction(e2 -> {
            ((InTimeRater.FactoryBuilder) raterFactoryBuilderMap.get(criteria)).setMaxTicksOff(
                Integer.parseInt(maxTicksOffField.getText()));
        });

        hBox.getChildren().setAll(label, raterSelector, ignoredTicksOffField, maxTicksOffField);
    }
}
