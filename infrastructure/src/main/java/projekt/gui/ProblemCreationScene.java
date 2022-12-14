package projekt.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import projekt.delivery.archetype.OrderGenerator;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;
import projekt.io.OrderGeneratorFactoryIO;
import projekt.io.ProblemArchetypeIO;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ProblemCreationScene extends Scene implements ControlledScene<ProblemCreationSceneController> {

    private final BorderPane root;
    private final ProblemCreationSceneController controller;

    private TableView<ProblemArchetype> table;
    private List<ProblemArchetype> problems;

    private VehicleManager vehicleManager;
    private OrderGenerator.Factory orderGeneratorFactory;
    private Map<RatingCriteria, Rater.Factory> raterFactoryMap;

    public ProblemCreationScene() {
        super(new BorderPane());
        controller = new ProblemCreationSceneController();
        root = (BorderPane) getRoot();
        root.setPrefSize(500, 500);

        // apply styles
        root.getStylesheets().add("projekt/gui/darkMode.css");
        root.getStylesheets().add("projekt/gui/raterStyle.css");
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

    private Map<String, ProblemArchetype> readProblems() {
        Map<String, ProblemArchetype> result = new HashMap<>();
        String dir = "presets";
        Set<File> files = getAllFilesInResourceDir(dir);
        //files.addAll(getAllFilesInBuildDir(dir));

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                result.put(file.getName(), ProblemArchetypeIO.readProblemArchetype(reader));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        final Label titleLabel = new Label("Add ProblemArchetype");
        titleLabel.setPadding(new Insets(20, 20, 20, 20));
        titleLabel.setId("Title");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        root.setTop(titleLabel);

        TabPane tabPane = new TabPane();

        final var buttonsVbox = new VBox();
        buttonsVbox.setPrefSize(200, 100);
        buttonsVbox.setAlignment(Pos.CENTER);
        buttonsVbox.setSpacing(10);
        root.setCenter(buttonsVbox);

        final HBox vehicleManagerHBox = new HBox();
        vehicleManagerHBox.setAlignment(Pos.CENTER);
        vehicleManagerHBox.setSpacing(10);

        Map<String, Region> regions = new HashMap<>();
        final ChoiceBox<String> regionSelector = new ChoiceBox<>(FXCollections.observableArrayList(regions.keySet()));
        vehicleManagerHBox.getChildren().add(regionSelector);

        final var newVehicleManagerButton = new Button("Create Vehicle Manager");
        newVehicleManagerButton.setOnAction(e -> {
            VehicleManagerCreationScene scene = (VehicleManagerCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.VEHICLE_MANAGER_CREATION, getController().getStage());
            scene.init(problems, vehicleManager, orderGeneratorFactory, raterFactoryMap);
        });
        vehicleManagerHBox.getChildren().add(newVehicleManagerButton);

        buttonsVbox.getChildren().add(vehicleManagerHBox);

        final HBox orderGeneratorHBox = new HBox();
        orderGeneratorHBox.setAlignment(Pos.CENTER);
        orderGeneratorHBox.setSpacing(10);

        final ChoiceBox<String> vehicleManagerSelector = new ChoiceBox<>(FXCollections.observableArrayList("item1", "item2"));
        orderGeneratorHBox.getChildren().add(vehicleManagerSelector);

        final var newOrderGeneratorButton = new Button("Create Order Generator");
        newOrderGeneratorButton.setOnAction(e -> {
            OrderGeneratorFactoryCreationScene scene = (OrderGeneratorFactoryCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.ORDER_CREATION, getController().getStage());
            scene.init(problems, vehicleManager, orderGeneratorFactory, raterFactoryMap);
        });
        orderGeneratorHBox.getChildren().add(newOrderGeneratorButton);

        buttonsVbox.getChildren().add(orderGeneratorHBox);

        final HBox RaterHBox = new HBox();
        RaterHBox.setAlignment(Pos.CENTER);
        RaterHBox.setSpacing(10);

        final ChoiceBox<String> OrderGeneratorSelector = new ChoiceBox<>(FXCollections.observableArrayList("item1", "item2"));
        RaterHBox.getChildren().add(OrderGeneratorSelector);

        final var newRaterButton = new Button("Add Rater");
        newRaterButton.setOnAction(e -> {
            RaterFactoryMapCreationScene scene = (RaterFactoryMapCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.RATER_CREATION, getController().getStage());
            scene.init(problems, vehicleManager, orderGeneratorFactory, raterFactoryMap);
        });
        RaterHBox.getChildren().add(newRaterButton);

        buttonsVbox.getChildren().add(RaterHBox);

        final var createAndAddButton = new Button("Create and Add");
        createAndAddButton.setOnAction(e -> {
            Region region = regions.get(regionSelector.getValue());

        });
        buttonsVbox.getChildren().add(createAndAddButton);

        Tab tab1 = new Tab("Create new ProblemArchetype", buttonsVbox);

        BorderPane tableBorderPane = new BorderPane();

        table = new TableView<>();
        table.setMaxWidth(200);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMinHeight(200);

        // Nummer-Spalte
        TableColumn<ProblemArchetype, String> column = new TableColumn<>("ProblemArcheType");
        column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().name()));
        column.setMinWidth(200);

        //Spalten in Tabelle packen:
        table.getColumns().addAll(column);

        Map<String, ProblemArchetype> readProblems = readProblems();

        table.getItems().setAll(readProblems.values());

        tableBorderPane.setCenter(table);

        final var addButton = new Button("Add");
        addButton.setOnAction(e -> {
            problems.add(table.getSelectionModel().getSelectedItem());
            MainMenuScene scene = (MainMenuScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.MAIN_MENU, getController().getStage());
            scene.init(problems);
        });

        HBox addButtonHBox = new HBox();
        addButtonHBox.setAlignment(Pos.CENTER);
        addButtonHBox.getChildren().add(addButton);

        tableBorderPane.setBottom(addButtonHBox);

        Tab tab2 = new Tab("Select existing Problem", tableBorderPane);

        tab1.setClosable(false);
        tab2.setClosable(false);

        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);

        root.setCenter(tabPane);

        //setup return and quit buttons
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        buttons.setPadding(new Insets(20, 20, 20, 20));

        final var returnButton = new Button("Return");
        returnButton.setOnAction(e -> {
            MainMenuScene scene = (MainMenuScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.MAIN_MENU, getController().getStage());
            scene.init(new ArrayList<>(problems));
        });
        buttons.getChildren().add(returnButton);

        final var quitButton = new Button("Quit");
        quitButton.setOnAction(getController()::quit);
        buttons.getChildren().add(quitButton);

        root.setBottom(buttons);
    }

    private Set<File> getAllFilesInResourceDir(String dir) {
        URL url = Objects.requireNonNull(getClass().getResource(dir));
        File folder = new File(url.getPath());
        return new HashSet<>(Arrays.asList(Objects.requireNonNull(folder.listFiles())));
    }

    private Set<File> getAllFilesInBuildDir(String dir) {
        Path folder = Objects.requireNonNull(Paths.get(System.getProperty("user.dir"), dir));
        return new HashSet<>(Arrays.asList(Objects.requireNonNull(folder.toFile().listFiles())));
    }

    @Override
    public ProblemCreationSceneController getController() {
        return controller;
    }




}
