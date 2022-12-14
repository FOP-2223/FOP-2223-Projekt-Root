package projekt.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import projekt.Main;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.archetype.ProblemGroupImpl;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.service.BasicDeliveryService;

import java.util.*;

public class MainMenuScene extends Scene implements ControlledScene<MainMenuSceneController> {

    private final BorderPane root;
    private final TableView<ProblemArchetype> table;
    private final MainMenuSceneController controller;

    private List<ProblemArchetype> problems = new ArrayList<>(Main.problemGroup.problems());

    @SuppressWarnings("unchecked")
    public MainMenuScene() {
        super(new BorderPane());
        // Typesafe reference to the root group of the scene.
        root = (BorderPane) getRoot();
        controller = new MainMenuSceneController();

        root.setPrefSize(600, 600);
        root.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        root.setMinSize(Double.MIN_VALUE, Double.MIN_VALUE);
        // apply styles
        root.getStylesheets().add("projekt/gui/menuStyle.css");
        root.getStylesheets().add("projekt/gui/darkMode.css");

        final Label titleLabel = new Label("Lieferservice Simulation");
        titleLabel.setPadding(new Insets(20, 20, 20, 20));
        titleLabel.setId("Title");

        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        final var buttonsVbox = new VBox();
        buttonsVbox.setPrefSize(200, 100);
        buttonsVbox.setAlignment(Pos.CENTER);
        buttonsVbox.setSpacing(10);
        root.setCenter(buttonsVbox);

        final var startGameButton = new Button("Start Simulation");
        startGameButton.setOnAction((e) -> {
            //Execute the GUIRunner in a separate Thread to prevent it from blocking the GUI
            new Thread(() -> {
                ProblemGroup problemGroup = new ProblemGroupImpl(problems, Arrays.stream(RatingCriteria.values()).toList());
                new GUIRunner(controller.getStage()).run(problemGroup, Main.simulationConfig, 10, BasicDeliveryService::new);
            }).start();
        });
        buttonsVbox.getChildren().add(startGameButton);

        table = new TableView<>();
        table.setMaxWidth(200);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMinHeight(200);

        // Nummer-Spalte
        TableColumn<ProblemArchetype, String> column = new TableColumn<>("ProblemArchetype");
        column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().name()));
        column.setMinWidth(200);

        //Spalten in Tabelle packen:
        table.getColumns().addAll(column);

        buttonsVbox.getChildren().add(table);

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
            scene.init(new ArrayList<>(problems), null, null, null);
        });
        buttonsVbox.getChildren().add(addButton);

        final var aboutButton = new Button("About");
        buttonsVbox.getChildren().add(aboutButton);

        final var quitButton = new Button("Quit");
        buttonsVbox.getChildren().add(quitButton);

        buttonsVbox.getChildren().stream()
            .filter(Button.class::isInstance)
            .map(Button.class::cast)
            .forEach(button -> {
                button.setPrefSize(200, 50);
                //button.setMaxSize(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
            });

    }

    public void init(List<ProblemArchetype> problems) {
        this.problems = problems;
        table.setItems(FXCollections.observableArrayList(problems));
    }

    @Override
    public MainMenuSceneController getController() {
        return controller;
    }
}
