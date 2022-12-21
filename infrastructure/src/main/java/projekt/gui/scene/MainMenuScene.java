package projekt.gui.scene;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import projekt.Main;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.archetype.ProblemGroupImpl;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.service.BasicDeliveryService;
import projekt.gui.ProblemArchetypeOverviewPane;
import projekt.gui.controller.MainMenuSceneController;
import projekt.gui.runner.GUIRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMenuScene extends MenuScene<MainMenuSceneController> {

    private TableView<ProblemArchetype> table;
    public MainMenuScene() {
        super(new MainMenuSceneController(), "Delivery Service Simulation");
    }

    public void init(List<ProblemArchetype> problems) {
        super.init(problems);
        table.setItems(FXCollections.observableArrayList(problems));
    }

    @Override
    public void initComponents() {
        root.setRight(new ProblemArchetypeOverviewPane(problems.get(0)));
        VBox buttonsVbox = new VBox();
        buttonsVbox.setPrefSize(200, 100);
        buttonsVbox.setAlignment(Pos.CENTER);
        buttonsVbox.setSpacing(10);
        root.setCenter(buttonsVbox);

        Button startGameButton = new Button("Start Simulation");
        startGameButton.setOnAction((e) -> {
            //Execute the GUIRunner in a separate Thread to prevent it from blocking the GUI
            new Thread(() -> {
                ProblemGroup problemGroup = new ProblemGroupImpl(problems, Arrays.stream(RatingCriteria.values()).toList());
                new GUIRunner(controller.getStage()).run(problemGroup, Main.simulationConfig, 10, BasicDeliveryService::new);
            }).start();
        });
        buttonsVbox.getChildren().add(startGameButton);

        table = createProblemArchetypeTableView();

        buttonsVbox.getChildren().add(table);

        table.requestFocus();
        table.getSelectionModel().select(0);
        table.getFocusModel().focus(0);

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
            root.setRight(new ProblemArchetypeOverviewPane(newValue))
        );

        Button removeButton = new Button("Remove ProblemArchetype");
        removeButton.setOnAction(e -> {
            ProblemArchetype selectedItem = table.getSelectionModel().getSelectedItem();
            table.getItems().remove(selectedItem);
            problems.remove(selectedItem);
        });
        buttonsVbox.getChildren().add(removeButton);

        Button addButton = new Button("Add Problem Archetype");
        addButton.setOnAction(e -> {
            ProblemCreationScene scene = (ProblemCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.PROBLEM_CREATION, getController().getStage());
            scene.init(new ArrayList<>(problems), null, null, null, null, null);
        });
        buttonsVbox.getChildren().add(addButton);

        buttonsVbox.getChildren().stream()
            .filter(Button.class::isInstance)
            .map(Button.class::cast)
            .forEach(button -> button.setPrefSize(200, 50));
    }

    @Override
    public void initReturnButton() {
        ((HBox) root.getBottom()).getChildren().remove(returnButton);
    }

    @Override
    public MainMenuSceneController getController() {
        return controller;
    }
}
