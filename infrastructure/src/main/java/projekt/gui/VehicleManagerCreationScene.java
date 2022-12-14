package projekt.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import projekt.delivery.archetype.OrderGenerator;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VehicleManagerCreationScene extends Scene implements ControlledScene<VehicleManagerCreationSceneController> {

    private final BorderPane root;
    private final VehicleManagerCreationSceneController controller;

    private List<ProblemArchetype> problems;

    private VehicleManager vehicleManager;
    private OrderGenerator.Factory orderGeneratorFactory;
    private Map<RatingCriteria, Rater.Factory> raterFactoryMap;

    public VehicleManagerCreationScene() {
        super(new BorderPane());
        controller = new VehicleManagerCreationSceneController();
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
        this.raterFactoryMap = raterFactoryMap;
        initComponents();
    }

    @Override
    public VehicleManagerCreationSceneController getController() {
        return controller;
    }

    private void initComponents() {
        final Label titleLabel = new Label("Create Vehicle Manager");
        titleLabel.setPadding(new Insets(20, 20, 20, 20));
        titleLabel.setId("Title");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        root.setTop(titleLabel);



        //setup return and quit buttons
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        buttons.setPadding(new Insets(20, 20, 20, 20));

        final var returnButton = new Button("Return");
        returnButton.setOnAction(e -> {
            ProblemCreationScene scene = (ProblemCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.PROBLEM_CREATION, getController().getStage());
            scene.init(new ArrayList<>(problems), vehicleManager, orderGeneratorFactory, raterFactoryMap);
        });
        buttons.getChildren().add(returnButton);

        final var quitButton = new Button("Quit");
        quitButton.setOnAction(getController()::quit);
        buttons.getChildren().add(quitButton);

        root.setBottom(buttons);
    }

}
