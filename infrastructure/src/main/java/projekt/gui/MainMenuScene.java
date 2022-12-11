package projekt.gui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import projekt.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainMenuScene extends Scene implements ControlledScene<MainMenuSceneController> {

    private final BorderPane root;
    private final MainMenuSceneController controller;

    public MainMenuScene() {
        super(new BorderPane());
        // Typesafe reference to the root group of the scene.
        root = (BorderPane) getRoot();
        controller = new MainMenuSceneController();

        root.setPrefSize(600, 460);
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
                new GUIRunner(controller.getStage()).run(Main.problemGroup, Main.simulationConfig, 10);
            }).start();
        });
        buttonsVbox.getChildren().add(startGameButton);

        final HBox regionHBox = new HBox();
        regionHBox.setAlignment(Pos.CENTER);
        regionHBox.setSpacing(10);

        final ChoiceBox<String> regionSelector = new ChoiceBox<>(FXCollections.observableArrayList(readRegions().keySet()));
        regionHBox.getChildren().add(regionSelector);

        final var newRegionButton = new Button("Add Region");
        regionHBox.getChildren().add(newRegionButton);

        buttonsVbox.getChildren().add(regionHBox);

        final HBox vehicleManagerHBox = new HBox();
        vehicleManagerHBox.setAlignment(Pos.CENTER);
        vehicleManagerHBox.setSpacing(10);

        final ChoiceBox<String> vehicleManagerSelector = new ChoiceBox<>(FXCollections.observableArrayList("item1", "item2"));
        vehicleManagerHBox.getChildren().add(vehicleManagerSelector);

        final var newVehicleManagerButton = new Button("Add VehicleManager");
        vehicleManagerHBox.getChildren().add(newVehicleManagerButton);

        buttonsVbox.getChildren().add(vehicleManagerHBox);

        final HBox OrderGeneratorHBox = new HBox();
        OrderGeneratorHBox.setAlignment(Pos.CENTER);
        OrderGeneratorHBox.setSpacing(10);

        final ChoiceBox<String> OrderGeneratorSelector = new ChoiceBox<>(FXCollections.observableArrayList("item1", "item2"));
        OrderGeneratorHBox.getChildren().add(OrderGeneratorSelector);

        final var newOrderGeneratorButton = new Button("Add Ordergenerator");
        OrderGeneratorHBox.getChildren().add(newOrderGeneratorButton);

        buttonsVbox.getChildren().add(OrderGeneratorHBox);

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

    private Map<String, Region> readRegions() {
        Map<String, Region> result = new HashMap<>();


        URL url = getClass().getResource("presets/region");

        assert url != null;
        System.out.println(url);
        File regionsFolder = new File(url.getPath());
        for (File file : Objects.requireNonNull(regionsFolder.listFiles())) {
            result.put(file.getName(), null);
        }

        File file = new File(url.getPath(), "region2.txt");
        System.out.println(file.getAbsolutePath());
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public MainMenuSceneController getController() {
        return controller;
    }
}
